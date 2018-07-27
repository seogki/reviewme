package com.skh.reviewme.Review.Photos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.skh.reviewme.Review.Interface.HashMapListener
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.databinding.ActivityReviewPhotoBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ReviewPhotoActivity : AppCompatActivity(), View.OnClickListener, HashMapListener {

    private lateinit var binding: ActivityReviewPhotoBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var galleryAdapter: GalleryAdapter
    var mCurrentPhotoPath: String? = null
    private var requestFileCode: Int = 4798
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_photo)
        binding.onClickListener = this
        binding.reviewPhotoImgBack.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        binding.btnFloataction.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        setView()
    }

    private fun setView() {

        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.reviewPhotoRv.layoutManager = layoutManager
        galleryAdapter = GalleryAdapter(this, ImageFile().fetchAllImages(this))
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


    private fun savePhotoInPref(filename: String) {
        val pref = getSharedPreferences("fileName", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("filestring", filename)
        editor.apply()
        finish()
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
                    val photoURI = FileProvider.getUriForFile(this@ReviewPhotoActivity
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
        savePhotoInPref(mCurrentPhotoPath as String)
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


    @SuppressLint("ApplySharedPref")
    override fun onHash(pos: Int, filename: String) {
        DLog.e("int : String : $pos : $filename")
        savePhotoInPref(filename)
    }
}
