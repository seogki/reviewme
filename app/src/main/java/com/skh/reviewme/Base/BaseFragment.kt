package com.skh.reviewme.Base

import android.content.Intent
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.View
import com.skh.reviewme.Login.LoginActivity
import com.skh.reviewme.R


/**
 * Created by Seogki on 2018. 6. 7..
 */
open class BaseFragment : Fragment() {

    lateinit var dialog: AlertDialog

    fun Fragment.beginNewActivity(intent: Intent) {
        startActivity(intent)
    }

    fun getURLForResource(resourceId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.`package`.name + "/" + resourceId).toString()
    }


    fun addFragmentWithSharedElement(activity: FragmentActivity?
                                     , @IdRes frameId: Int
                                     , fragment: Fragment
                                     , AllowStateloss: Boolean
                                     , sharedView: View?
                                     , transitionName: String) {

        if (AllowStateloss) {
            activity?.supportFragmentManager
                    ?.beginTransaction()
//                    ?.setReorderingAllowed(true)
                    ?.addSharedElement(sharedView, transitionName)
                    ?.add(frameId, fragment, fragment.tag)
                    ?.addToBackStack(fragment.tag)
                    ?.commitAllowingStateLoss()
        } else {
            activity?.supportFragmentManager
                    ?.beginTransaction()
//                    ?.setReorderingAllowed(true)
                    ?.addSharedElement(sharedView, transitionName)
                    ?.add(frameId, fragment, fragment.tag)
                    ?.addToBackStack(fragment.tag)
                    ?.commit()
        }
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


}