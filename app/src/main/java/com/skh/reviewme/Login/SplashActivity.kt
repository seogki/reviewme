package com.skh.reviewme.Login

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.skh.reviewme.Main.ReviewMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivitySplashBinding
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN: Int = 4798

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.signInButton.setOnClickListener(this)
    }

    private fun updataUI(account: GoogleSignInAccount?) {
        DLog.e("account : " + account.toString())
        if (account != null) {

            startActivity(Intent(this, ReviewMainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sign_in_button -> {
                signIn()
            }
        }
    }

    private fun signIn() {
        mGoogleSignInClient.signInIntent.let { startActivityForResult(it, RC_SIGN_IN) }
    }

    override fun onStart() {
        super.onStart()
        GoogleSignIn.getLastSignedInAccount(this).let { updataUI(it) }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignIn.getSignedInAccountFromIntent(data).let { handleSignInResult(it) }

        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            task.getResult(ApiException::class.java).let { updataUI(it) }

        } catch (e: ApiException) {
            updataUI(null)
        }
    }

}
