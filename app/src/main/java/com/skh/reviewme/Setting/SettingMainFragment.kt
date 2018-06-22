package com.skh.reviewme.Setting


import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.kakaotalk.callback.TalkResponseCallback
import com.kakao.kakaotalk.response.KakaoTalkProfile
import com.kakao.kakaotalk.v2.KakaoTalkService
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.R
import com.skh.reviewme.databinding.FragmentSettingMainBinding


/**
 * A simple [Fragment] subclass.
 */
class SettingMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentSettingMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_main, container, false)
        binding.settingBtnOut.setOnClickListener(this)
        getProfile()
        return binding.root
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_btn_out -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        if (ApplicationClass.getIsKakao())
            kakaoLogout()
        else
            googleLogout()
    }

    private fun getProfile() {
        if (ApplicationClass.getIsKakao()) {
            // 카카오로 로그인했을경우
            requestKakaoProfile()
        } else {
            requestGoogleProfile()
        }
    }

    private fun requestKakaoProfile() {
        KakaoTalkService.getInstance().requestProfile(object : kakaoTalkResponseCallback<KakaoTalkProfile>() {
            override fun onSuccess(talkProfile: KakaoTalkProfile) {
                val nickName = talkProfile.nickName;
                val profileImageURL = talkProfile.profileImageUrl
//                val thumbnailURL = talkProfile.thumbnailUrl
//                val countryISO = talkProfile.countryISO;

                val uri = Uri.parse(profileImageURL)
                setProfileData(nickName, null, uri)
            }
        })
    }

    private fun requestGoogleProfile() {
        val acct = GoogleSignIn.getLastSignedInAccount(activity!!)
        if (acct != null) {

//            val personGivenName = acct.givenName
//            val personFamilyName = acct.familyName
//            val personId = acct.id

            val personName = acct.displayName
            val personPhoto = acct.photoUrl
            val personEmail = acct.email
            setProfileData(personName, personEmail, personPhoto)

        }
    }

    private fun setProfileData(title: String?, text: String?, uri: Uri?) {
        binding.settingNameTxt.text = title
        if (text == null) {
            binding.settingEmailTxt.text ="없음"
        } else {
            binding.settingEmailTxt.text = text
        }

        Glide.with(context!!)
                .load(uri)
                .apply(RequestOptions()
                        .centerCrop()
                        .circleCrop()
                        .override(200, 200)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.1f)
                .into(binding.settingImg)
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


}// Required empty public constructor
