package com.skh.reviewme.Setting


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Setting.Error.SettingErrorFragment
import com.skh.reviewme.Setting.Notification.SettingNotificationFragment
import com.skh.reviewme.Setting.Photo.SettingPhotoActivity
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.FragmentSettingMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * A simple [Fragment] subclass.
 */
class SettingMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentSettingMainBinding
    lateinit var pref: SharedPreferences
    private var isCalled: Boolean = false
    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_main, container, false)
        binding.onClickListener = this
        pref = activity!!.getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        getUserProfileApi()
        return binding.root
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_btn_out -> {
                signOut()
            }
            R.id.setting_btn_changeimage -> {
                beginNewActivity(Intent(context!!, SettingPhotoActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
            R.id.setting_btn_errors -> {
                val frag = SettingErrorFragment()
                addFragment(activity, R.id.frame_layout, frag, true, true, "SettingErrorFragment")
            }
            R.id.setting_btn_notification -> {
                val frag = SettingNotificationFragment()
                addFragment(activity, R.id.frame_layout, frag, true, true, "SettingNotificationFragment")
            }
        }
    }

    private fun signOut() {
        if (ApplicationClass.getIsKakao())
            kakaoLogout()
        else
            googleLogout()
    }

    private fun kakaoLogout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                activity?.finishAffinity()
                redirectLoginActivity()

            }
        })
    }

    private fun googleLogout() {
        GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
                .let {
                    GoogleSignIn.getClient(context!!, it)
                            .let {
                                it.signOut().addOnCompleteListener {
                                    activity?.finishAffinity()
                                    redirectLoginActivity()
                                }
                            }
                }
    }



    private fun getUserProfileApi() {

        val userid = pref.getString("userLoginId", "")

        disposable = client.GetSettingUserProfileRx(userid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    binding.item = result
                    binding.executePendingBindings()
                    binding.settingAgeTxt.append("살")
                    isCalled = true

                }, { error ->
                    DLog.e("t : ${error?.message.toString()}")
                    isCalled = true
                })
    }

    override fun onResume() {
        super.onResume()
        if (isCalled)
            getUserProfileApi()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

//    private open inner class kakaoTalkResponseCallback<T> : TalkResponseCallback<T>() {
//        override fun onNotKakaoTalkUser() {
//
//        }
//
//        override fun onSessionClosed(errorResult: ErrorResult?) {
//
//        }
//
//        override fun onSuccess(result: T) {
//
//        }
//
//        override fun onNotSignedUp() {
//
//        }
//
//    }


}// Required empty public constructor
