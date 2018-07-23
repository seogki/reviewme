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
import com.kakao.kakaotalk.callback.TalkResponseCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Network.ApiCilent
import com.skh.reviewme.R
import com.skh.reviewme.Setting.Model.SettingUserProfileModel
import com.skh.reviewme.Setting.Photo.SettingPhotoActivity
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.FragmentSettingMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class SettingMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentSettingMainBinding
    lateinit var pref: SharedPreferences
    private var isCalled: Boolean = false

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
                beginNewActivity(Intent(context!!, SettingPhotoActivity::class.java))
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

    private open inner class kakaoTalkResponseCallback<T> : TalkResponseCallback<T>() {
        override fun onNotKakaoTalkUser() {

        }

        override fun onSessionClosed(errorResult: ErrorResult?) {

        }

        override fun onSuccess(result: T) {

        }

        override fun onNotSignedUp() {

        }

    }

    private fun getUserProfileApi() {

        val userid = pref.getString("userLoginId", "")
        val call = ApiCilent.getInstance().getService().GetSettingUserProfile(userid)

        call.enqueue(object : Callback<SettingUserProfileModel> {
            override fun onFailure(call: Call<SettingUserProfileModel>?, t: Throwable?) {
                DLog.e(t?.message.toString())
                isCalled = true
            }

            override fun onResponse(call: Call<SettingUserProfileModel>?, response: Response<SettingUserProfileModel>?) {
                binding.item = response?.body()
                binding.executePendingBindings()
                binding.settingAgeTxt.append("살")

//                binding.settingNameTxt.text = "닉네임 : ${response?.body()?.UserNick}"
//                binding.settingEmailTxt.text = "이메일 : ${response?.body()?.UserEmail}"
//                binding.settingAgeTxt.text = "나이 : ${response?.body()?.UserAge}"
//                binding.settingGenderTxt.text = "성별 : ${response?.body()?.UserGender}"

                isCalled = true
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if (isCalled)
            getUserProfileApi()
    }


}// Required empty public constructor
