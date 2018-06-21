package com.skh.reviewme.Login

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ActivitySplashBinding


class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN: Int = 4798

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)


        Handler().postDelayed({

            beginActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1300)


    }


}
