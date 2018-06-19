package com.skh.reviewme.Base

import android.content.Intent
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
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

    fun addFragment(activity: FragmentActivity?,@IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean) {
        if (AllowStateloss)
            activity?.supportFragmentManager?.beginTransaction()?.add(frameId, fragment, fragment.tag)?.commitAllowingStateLoss()
        else
            activity?.supportFragmentManager?.beginTransaction()?.add(frameId, fragment, fragment.tag)?.commit()
    }
}