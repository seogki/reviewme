package com.skh.reviewme.Login.ReviewRegister

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Main.ReviewMainActivity
import com.skh.reviewme.Network.ApiCilent
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivityRegisterBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.registBtnRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.regist_btn_register -> {

                val age = binding.registEditAge.text.toString()
                val email = binding.registEditEmail.text.toString()
                val nickname = binding.registEditNickname.text.toString()
                val gender = binding.registRadiogroupAge.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }


                val call = ApiCilent.getInstance().getService().registerAccount(nickname, email, age, gender, ApplicationClass.getIsKakao().toString())

                call.enqueue(object : Callback<JSONObject> {
                    override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
                        DLog.e("t data : " + t?.message)
                        DLog.e("통신 실패")
                    }

                    override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>?) {
                        DLog.e("통신 성공")
                        beginActivity(Intent(this@RegisterActivity, ReviewMainActivity::class.java))
                        finish()
                    }

                })

            }
        }
    }
}
