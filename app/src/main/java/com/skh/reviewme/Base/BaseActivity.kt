package com.skh.reviewme.Base

import android.annotation.SuppressLint
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.skh.reviewme.Main.ReviewMainActivity

/**
 * Created by Seogki on 2018. 6. 7..
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var toast: Toast

    fun AppCompatActivity.addFragment(@IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean) {
        if (AllowStateloss)
            supportFragmentManager.beginTransaction().addToBackStack(fragment.tag).add(frameId, fragment, fragment.tag)?.commitAllowingStateLoss()
        else
            supportFragmentManager.beginTransaction().addToBackStack(fragment.tag).add(frameId, fragment, fragment.tag)?.commit()
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
    fun AppCompatActivity.LoginActivity() {
        startActivity(Intent(this, LoginActivity()::class.java))
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
        overridePendingTransition(0,0)
    }

}