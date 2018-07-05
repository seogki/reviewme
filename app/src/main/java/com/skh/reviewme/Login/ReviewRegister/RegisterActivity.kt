package com.skh.reviewme.Login.ReviewRegister

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.JsonObject
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Main.ReviewMainActivity
import com.skh.reviewme.Network.ApiCilent
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityRegisterBinding
    lateinit var id: String
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.registBtnRegister.visibility = View.GONE
        binding.registBtnRegister.setOnClickListener(this)
        checkRegistration()


    }


    private fun checkRegistration() {


        if (ApplicationClass.getIsKakao()) {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    id = "Kakao_" + result?.id.toString()
                    isUserOn(id)
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    redirectLoginActivity()
                    finish()
                }

            })
        } else {
            id = requestGoogleId()
            isUserOn(id)
        }

    }

    private fun isUserOn(id: String) {
        pref = getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("userLoginId", id)
        editor.apply()
        val call = ApiCilent.getInstance().getService().isUserOn(id)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                DLog.e("msg" + t?.message)
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                DLog.e("" + response?.body()?.get("result").toString())
                DLog.e("" + response?.body()?.toString())
                isUserAvailable(response?.body()?.get("result").toString(), response?.body()?.get("name").toString())
            }

        })
    }

    private fun isUserAvailable(isAvailable: String, username: String) {
        if (isAvailable.contains("200")) {
            val editor = pref.edit()
            editor.putString("UserNick", username.replace("\"",""))
            editor.apply()
            redirectReviewMainActivity()
            finish()
        } else {
            binding.registEmptyBackground.visibility = View.GONE
            binding.registBtnRegister.visibility = View.VISIBLE

        }
    }

    private fun requestGoogleId(): String {
        val acct = GoogleSignIn.getLastSignedInAccount(this)

        return "Google_" + acct?.id.toString()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.regist_btn_register -> {

                if (nullCheck()) {
                    val age = binding.registEditAge.text.toString()
                    val email = binding.registEditEmail.text.toString()
                    val nickname = binding.registEditNickname.text.toString()
                    val gender = binding.registRadiogroupAge.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }
                    val isKakao = ApplicationClass.getIsKakao().toString()


                    val call = ApiCilent.getInstance().getService().registerAccount(id, nickname, email, age, gender, isKakao)

                    call.enqueue(object : Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                            DLog.e("t data : " + t?.message)
                            DLog.e("통신 실패")
                        }

                        override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                            DLog.e("통신 성공")
                            beginActivity(Intent(this@RegisterActivity, ReviewMainActivity::class.java))
                            finish()
                        }

                    })

                } else {
                    Toast.makeText(this@RegisterActivity, "모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun nullCheck(): Boolean {
        var checks: Boolean?
        checks = binding.registEditNickname.text.toString().isNotEmpty()

        checks = binding.registEditEmail.text.toString().isNotEmpty()

        checks = binding.registEditAge.text.toString().isNotEmpty()

        return checks
    }
}
