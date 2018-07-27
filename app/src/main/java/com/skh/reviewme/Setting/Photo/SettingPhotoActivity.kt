package com.skh.reviewme.Setting.Photo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Review.Interface.HashMapListener
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.Util.UtilMethod
import com.skh.reviewme.databinding.ActivityReviewPhotoBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SettingPhotoActivity : BaseActivity(), View.OnClickListener, HashMapListener {

    private lateinit var binding: ActivityReviewPhotoBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var galleryAdapter: SettingGalleryAdapter
    private var requestFileCode: Int = 4798
    private lateinit var pref: SharedPreferences
    var mCurrentPhotoPath: String? = null
    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_photo)
        pref = getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        binding.reviewPhotoImgBack.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        binding.btnFloataction.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        binding.onClickListener = this
        setView()
    }

    private fun setView() {

        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.reviewPhotoRv.layoutManager = layoutManager
        galleryAdapter = SettingGalleryAdapter(this, ImageFile().fetchAllImages(this))
        galleryAdapter.sethash(this)
        binding.reviewPhotoRv.setHasFixedSize(false)

        Handler().postDelayed({
            binding.reviewPhotoRv.adapter = galleryAdapter
        }, 100)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_floataction -> {
                startCamera()
            }
            R.id.review_photo_img_back -> {
                finish()
            }
        }
    }


    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                DLog.e("created camera")
            } catch (e: Exception) {
                // Error occurred while creating the File
                e.printStackTrace()
            }
            // Continue only if the File was successfully created

            if (photoFile != null) {
                if (Build.VERSION_CODES.N <= android.os.Build.VERSION.SDK_INT) {
                    val photoURI = FileProvider.getUriForFile(this@SettingPhotoActivity
                            , "com.example.android.fileprovider"
                            , photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    DLog.e("call FileProvider")
                    startActivityForResult(takePictureIntent, requestFileCode)
                } else {
                    val uri = Uri.fromFile(photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takePictureIntent, requestFileCode)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestFileCode && resultCode == RESULT_OK) {
            DLog.e("activity done : $mCurrentPhotoPath")
            if (mCurrentPhotoPath != null)
                galleryAddPic()
        }
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
        if (mCurrentPhotoPath != null)
            changeProfile(mCurrentPhotoPath as String)
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onHash(pos: Int, filename: String) {
        DLog.e("int : String : $pos : $filename")
        binding.reviewPhotoRv.isEnabled = false
        changeProfile(filename)

    }

    private fun changeProfile(name: String) {
        val file = UtilMethod.getCompressed(this@SettingPhotoActivity, File(name).toString(), "drawable1")
        val userid = pref.getString("userLoginId", "").trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file).let { MultipartBody.Part.createFormData("images", file.name, it) }

        disposable = client.SetSettingProfileImageRx(userid, requestFile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    successDialog()
                }, { error ->
                    DLog.e("t : ${error?.message.toString()}")
                    alertDialog(error?.message!!)
                    binding.reviewPhotoRv.isEnabled = true
                })
    }

    private fun successDialog() {
        AlertDialog.Builder(this@SettingPhotoActivity, R.style.MyDialogTheme)
                .setMessage("프로필 사진이 변경 되었습니다")
                .setPositiveButton("확인", { dialog, _ ->
                    dialog.dismiss()
                    finish()
                    binding.reviewPhotoRv.isEnabled = true
                }).setNegativeButton(null, null)
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
