package com.skh.reviewme.Base

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
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
}