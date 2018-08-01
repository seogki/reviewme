package com.skh.reviewme.Community.Question

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.UtilMethodComunnity
import com.skh.reviewme.databinding.ActivityCommunityQuestionBinding
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CommunityQuestionActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityCommunityQuestionBinding
    private var ImageCode: Int = 1234
    private lateinit var imagePath: ArrayList<String>
    private lateinit var dialog: android.app.AlertDialog
    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_question)
        binding.questionImgBack.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        binding.onClickListener = this
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.question_img -> {
                setPhoto()
            }
            R.id.question_btn_register -> {
                setSpotDialogs()
                binding.questionBtnRegister.isEnabled = false
                setRegisterCommunityToSever()
            }
            R.id.question_img_back -> {
                finish()
            }
        }
    }

    private fun setRegisterCommunityToSever() {
        if (binding.questionTextTitle.text.toString().isNotEmpty() && binding.questionTxtQuestion.text.toString().isNotEmpty()) {
            setRequestServer()
        } else {
            Toast.makeText(this@CommunityQuestionActivity, "모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
            binding.questionBtnRegister.isEnabled = true
        }
    }

    private fun setRequestServer() {
        val multiPartImages = ArrayList<MultipartBody.Part>()
        val pref = getSharedPreferences("UserId", Activity.MODE_PRIVATE)

        val file1: File?
        val file2: File?
        val file3: File?
        val file4: File?

        val requestFile1: MultipartBody.Part?
        val requestFile2: MultipartBody.Part?
        val requestFile3: MultipartBody.Part?
        val requestFile4: MultipartBody.Part?

        val title = binding.questionTextTitle.text.toString().trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val text = binding.questionTxtQuestion.text.toString().let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val userid = pref.getString("userLoginId", "").trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val userNick = pref.getString("UserNick", "").trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }

        if (binding.questionImg1.drawable != null) {

            file1 = UtilMethodComunnity.getCompressed(this@CommunityQuestionActivity, File(imagePath[0]).toString(), "drawable1")
            requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1).let { MultipartBody.Part.createFormData("images", file1?.name, it) }

            multiPartImages.add(requestFile1)
        }
        if (binding.questionImg2.drawable != null) {
            file2 = UtilMethodComunnity.getCompressed(this@CommunityQuestionActivity, File(imagePath[1]).toString(), "drawable2")
            requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2).let { MultipartBody.Part.createFormData("images", file2?.name, it) }
            multiPartImages.add(requestFile2)
        }
        if (binding.questionImg3.drawable != null) {
            file3 = UtilMethodComunnity.getCompressed(this@CommunityQuestionActivity, File(imagePath[2]).toString(), "drawable3")
            requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file3).let { MultipartBody.Part.createFormData("images", file3?.name, it) }
            multiPartImages.add(requestFile3)
        }
        if (binding.questionImg4.drawable != null) {
            file4 = UtilMethodComunnity.getCompressed(this@CommunityQuestionActivity, File(imagePath[3]).toString(), "drawable4")
            requestFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), file4).let { MultipartBody.Part.createFormData("images", file4?.name, it) }
            multiPartImages.add(requestFile4)
        }

        DLog.e("multipart: $multiPartImages")

        disposable = client.setCommunityPhotosRx(userid, userNick, title, text, multiPartImages).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dialog.dismiss()
                    setCommunityDone()
                }, { error ->
                    DLog.e("t : ${error?.message.toString()}")
                    dialog.dismiss()
                    binding.questionBtnRegister.isEnabled = true
                    alertDialog(error?.message!!)

                })

    }

    private fun setCommunityDone() {
        val pref = getSharedPreferences("CommunityDone", Activity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isDone", true)
        editor.apply()

        AlertDialog.Builder(this@CommunityQuestionActivity, R.style.MyDialogTheme)
                .setMessage("등록 되었습니다")
                .setPositiveButton("확인", { dialog, _ ->
                    dialog.dismiss()
                    finish()
                    binding.questionBtnRegister.isEnabled = true
                }).setNegativeButton(null, null)
                .show()
    }

    private fun setPhoto() {
        TedPermission.with(this)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        Handler().postDelayed({


                            startActivityForResult(Intent(this@CommunityQuestionActivity, CommunityQuestionPhotoActivity::class.java), ImageCode)
                        }, 100)
                    }

                    override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>) {

                    }
                }).check()

    }

    @Suppress("UNCHECKED_CAST")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ImageCode) {
            if (resultCode == Activity.RESULT_OK) {

                imagePath = data?.getStringArrayListExtra("imagePath") as ArrayList<String>
                DLog.e("path " + imagePath.toString())
                setImage(imagePath)
            }
        }
    }

    private fun setImage(receivedDrawable: ArrayList<String>) {
        DLog.e("setImage : $imagePath")


        setImageNull()

        when {
            receivedDrawable.size == 0 -> {
                return
            }
            receivedDrawable.size == 1 -> {
                val uri = Uri.parse("file://" + receivedDrawable[0])
                glideDrawable(uri, binding.questionImg1)
            }
            receivedDrawable.size == 2 -> {
                val uri1 = Uri.parse("file://" + receivedDrawable[0])
                glideDrawable(uri1, binding.questionImg1)
                val uri2 = Uri.parse("file://" + receivedDrawable[1])
                glideDrawable(uri2, binding.questionImg2)

            }
            receivedDrawable.size == 3 -> {
                val uri1 = Uri.parse("file://" + receivedDrawable[0])
                glideDrawable(uri1, binding.questionImg1)
                val uri2 = Uri.parse("file://" + receivedDrawable[1])
                glideDrawable(uri2, binding.questionImg2)
                val uri3 = Uri.parse("file://" + receivedDrawable[2])
                glideDrawable(uri3, binding.questionImg3)

            }
            receivedDrawable.size == 4 -> {
                val uri1 = Uri.parse("file://" + receivedDrawable[0])
                glideDrawable(uri1, binding.questionImg1)
                val uri2 = Uri.parse("file://" + receivedDrawable[1])
                glideDrawable(uri2, binding.questionImg2)
                val uri3 = Uri.parse("file://" + receivedDrawable[2])
                glideDrawable(uri3, binding.questionImg3)
                val uri4 = Uri.parse("file://" + receivedDrawable[3])
                glideDrawable(uri4, binding.questionImg4)
            }
        }
    }

    private fun setSpotDialogs() {
        dialog = SpotsDialog
                .Builder()
                .setContext(this@CommunityQuestionActivity)
                .setMessage("업로드 중...")
                .setCancelable(false)
                .build().apply { show() }
    }

    private fun glideDrawable(uri: Uri, imageView: ImageView) {
        DLog.e("uri + $uri")
        Glide.with(this).load(uri)
                .apply(RequestOptions()
                        .centerCrop()
                        .override(200, 200)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.1f)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        DLog.e("setImage")
                        imageView.setImageDrawable(resource)
                    }

                })
    }

    private fun setImageNull() {
        binding.questionImg1.setImageDrawable(null)
        binding.questionImg2.setImageDrawable(null)
        binding.questionImg3.setImageDrawable(null)
        binding.questionImg4.setImageDrawable(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
