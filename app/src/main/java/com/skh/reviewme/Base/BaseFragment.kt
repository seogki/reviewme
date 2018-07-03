package com.skh.reviewme.Base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.inputmethod.InputMethodManager
import com.skh.reviewme.Login.LoginActivity
import com.skh.reviewme.R


/**
 * Created by Seogki on 2018. 6. 7..
 */
open class BaseFragment : Fragment() {


    fun Fragment.beginNewActivity(intent: Intent) {
        startActivity(intent)
    }

    fun getURLForResource(resourceId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.`package`.name + "/" + resourceId).toString()
    }


    fun addFragment(activity: FragmentActivity?, @IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean, backstack: Boolean) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(frameId, fragment, fragment.tag)

        if (backstack) {
            transaction?.addToBackStack(fragment.tag)
        }

        if (AllowStateloss)
            transaction?.commitAllowingStateLoss()
        else
            transaction?.commit()
    }

    fun redirectLoginActivity() {
        startActivity(Intent(context, LoginActivity::class.java))
    }

    fun closeKeyboard() {
        val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

    }

    fun openKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }




}