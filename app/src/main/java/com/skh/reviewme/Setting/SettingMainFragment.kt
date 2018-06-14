package com.skh.reviewme.Setting


import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Login.SplashActivity
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
        // Inflate the layout for this fragment
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
        GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
                .let {
                    GoogleSignIn
                            .getClient(context!!, it)
                            .let {
                                it.signOut().addOnCompleteListener {
                                    activity?.finishAffinity()
                                    beginNewActivity(Intent(context, SplashActivity::class.java))

                                }
                            }
                }

    }
    private fun getProfile(){
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
}// Required empty public constructor
