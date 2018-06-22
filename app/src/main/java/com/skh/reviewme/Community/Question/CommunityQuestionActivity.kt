package com.skh.reviewme.Community.Question

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivityCommunityQuestionBinding

class CommunityQuestionActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityCommunityQuestionBinding
    private var ImageCode: Int = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_question)

        binding.onClickListener = this


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.question_img -> {
                setPhoto()
            }
            R.id.question_btn_register -> {

            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ImageCode) {
            if (resultCode == Activity.RESULT_OK) {
                @Suppress("UNCHECKED_CAST")
                setImage(data?.getSerializableExtra("PhotoFromQuestion") as ArrayList<Bitmap>)
            }
        }
    }

    private fun setImage(receivedDrawable: ArrayList<Bitmap>) {
        DLog.e("setImage : $receivedDrawable")

        var bitmap1: Bitmap? = null
        var bitmap2: Bitmap? = null
        var bitmap3: Bitmap? = null
        var bitmap4: Bitmap? = null

        setImageNull()

        when {
            receivedDrawable.size == 0 -> {
                return
            }
            receivedDrawable.size == 1 -> {
                bitmap1 = receivedDrawable[0]

            }
            receivedDrawable.size == 2 -> {
                bitmap1 = receivedDrawable[0]
                bitmap2 = receivedDrawable[1]

            }
            receivedDrawable.size == 3 -> {
                bitmap1 = receivedDrawable[0]
                bitmap2 = receivedDrawable[1]
                bitmap3 = receivedDrawable[2]


            }
            receivedDrawable.size == 4 -> {
                bitmap1 = receivedDrawable[0]
                bitmap2 = receivedDrawable[1]
                bitmap3 = receivedDrawable[2]
                bitmap4 = receivedDrawable[3]


            }
        }


        if (bitmap1 != null)
            binding.questionImg1.setImageBitmap(bitmap1)

        if (bitmap2 != null)
            binding.questionImg2.setImageBitmap(bitmap2)

        if (bitmap3 != null)
            binding.questionImg3.setImageBitmap(bitmap3)

        if (bitmap4 != null)
            binding.questionImg4.setImageBitmap(bitmap4)

    }

    private fun setImageNull(){
        binding.questionImg1.setImageDrawable(null)
        binding.questionImg2.setImageDrawable(null)
        binding.questionImg3.setImageDrawable(null)
        binding.questionImg4.setImageDrawable(null)
    }
}
