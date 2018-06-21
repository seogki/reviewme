package com.skh.reviewme.Setting


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
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
                showDialog("로그아웃 하시겠습니까?")
                signOut()
            }
        }
    }
    private fun showDialog(msg: String){
        val dialog = AlertDialog.Builder(context!!)
                .setMessage(msg)
                .setPositiveButton("확인") { dialog, which ->
                   signOut()
                }
                .setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                }

        dialog.show()
    }

    private fun signOut() {
        if (ApplicationClass.getIsKakao())
            kakaoLogout()
        else
            googleLogout()
    }

    private fun getProfile() {
        val acct = GoogleSignIn.getLastSignedInAccount(activity!!)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto = acct.photoUrl


            DLog.e("profiles: $personEmail \n$personName,$personGivenName,$personFamilyName : $personId : $personPhoto")
        }
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
}// Required empty public constructor
