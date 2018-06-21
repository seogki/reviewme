package com.skh.reviewme.Community.Question

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ActivityCommunityQuestionBinding

class CommunityQuestionActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityCommunityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_community_question)

        binding.onClickListener = this


    }

    override fun onClick(v: View?) {
        when(v?.id){
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
                            startActivity(Intent(this@CommunityQuestionActivity,CommunityQuestionPhotoActivity::class.java))
                        }, 100)
                    }

                    override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>) {

                    }
                }).check()

    }
}
