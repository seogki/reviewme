package com.skh.reviewme.Base

import android.annotation.SuppressLint
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.skh.reviewme.Home.HomeMainActivity
import com.skh.reviewme.Login.LoginActivity
import com.skh.reviewme.Login.ReviewRegister.RegisterActivity
import com.skh.reviewme.R
import com.skh.reviewme.Review.ReviewMainActivity

/**
 * Created by Seogki on 2018. 6. 7..
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var toast: Toast

    fun AppCompatActivity.addFragment(@IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean, backstack: Boolean, tag: String) {
        val transaction = supportFragmentManager?.beginTransaction()?.add(frameId, fragment, tag)

        if (backstack) {
            transaction?.addToBackStack(fragment.tag)
        }

        if (AllowStateloss)
            transaction?.commitAllowingStateLoss()
        else
            transaction?.commit()
    }

    fun AppCompatActivity.replaceFragment(@IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean) {


        if (AllowStateloss)
            supportFragmentManager.beginTransaction().replace(frameId, fragment, fragment.tag)?.commitAllowingStateLoss()
        else
            supportFragmentManager.beginTransaction().replace(frameId, fragment, fragment.tag)?.commit()

    }

    fun AppCompatActivity.beginActivity(intent: Intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    fun AppCompatActivity.redirectReviewMainActivity() {
        startActivity(Intent(this, ReviewMainActivity::class.java))
    }

    fun AppCompatActivity.redirectLoginActivity() {
        startActivity(Intent(this, LoginActivity()::class.java))
    }

    fun AppCompatActivity.redirectRegisterActivity() {
        startActivity(Intent(this, RegisterActivity()::class.java))
    }

    fun AppCompatActivity.redirectHomeMainActivity() {
        startActivity(Intent(this, HomeMainActivity()::class.java))
    }


    fun showGuide() {
        toast = Toast.makeText(this,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
        toast.show()
    }

    fun finishToast() {
        toast.cancel()
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun alertAndFinishDialog() {
        AlertDialog.Builder(this@BaseActivity, R.style.MyDialogTheme)
                .setMessage("앱 종료 후 다시 시도해주시기 바랍니다.")
                .setPositiveButton("확인", { dialog, _ ->
                    dialog.dismiss()
                    finishAffinity()
                }).setNegativeButton(null, null)
                .show()
    }

    fun alertDialog(msg: String) {
        AlertDialog.Builder(this@BaseActivity, R.style.MyDialogTheme)
                .setMessage("나중에 다시 시도해주시기 바랍니다.")
                .setPositiveButton("확인", { dialog, _ ->
                    dialog.dismiss()
                }).setNegativeButton(null, null)
                .show()
    }

}