package com.skh.reviewme.Login.ReviewRegister

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Main.ReviewMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.registBtnRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.regist_btn_register -> {
                beginActivity(Intent(this@RegisterActivity, ReviewMainActivity::class.java))
                finish()
            }
        }
    }
}
