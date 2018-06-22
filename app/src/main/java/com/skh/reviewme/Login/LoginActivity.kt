package com.skh.reviewme.Login

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.Utility
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityLoginBinding
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN: Int = 4798
    lateinit var callback: ISessionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DLog.e("key hsh" + Utility.getKeyHash(this))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.signInButton.setOnClickListener(this)
        binding.comLogin.setOnClickListener(this)
        DLog.e("onSessionInitiated")

        initiateKakaoSign()
        initiateGoogleSign()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sign_in_button -> {
                signIn()
            }
            R.id.com_login -> {
                binding.comKakaoLogin.performClick()
            }
        }
    }


    // 구글 계정 연동을 위해 필요함
    override fun onStart() {
        super.onStart()
        GoogleSignIn.getLastSignedInAccount(this).let { updataUI(it) }
    }
    private fun initiateKakaoSign(){
        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()

    }

    private fun initiateGoogleSign() {
        gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    private fun signIn() {
        mGoogleSignInClient.signInIntent.let { startActivityForResult(it, RC_SIGN_IN) }
    }

    private fun updataUI(account: GoogleSignInAccount?) {
        DLog.e("account : " + account.toString())
        if (account != null) {
            ApplicationClass.setIsKakao(false)
            redirectReviewMainActivity()
            finish()
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            task.getResult(ApiException::class.java).let { updataUI(it) }

        } catch (e: ApiException) {
            updataUI(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) { //구글일때
            GoogleSignIn.getSignedInAccountFromIntent(data).let { handleSignInResult(it) }
        }

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            // 카카오 일경우
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //카카오톡 계정 연동을 위해 필요

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            DLog.e("onSessionOpened")
            ApplicationClass.setIsKakao(true)
            redirectReviewMainActivity()
            finish()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                DLog.e("onSessionOpenFailed")
                DLog.e(exception.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback) //destroy 될 경우 콜백 삭제
    }

}
