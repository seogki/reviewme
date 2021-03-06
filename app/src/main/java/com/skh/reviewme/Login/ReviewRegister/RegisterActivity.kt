package com.skh.reviewme.Login.ReviewRegister

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.crashlytics.android.Crashlytics
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.skh.reviewme.ApplicationClass
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Review.ReviewMainActivity
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.UtilMethod
import com.skh.reviewme.databinding.ActivityRegisterBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class RegisterActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityRegisterBinding
    lateinit var id: String
    lateinit var pref: SharedPreferences
    lateinit var name: String
    private val client by lazy {
        ApiCilentRx.create()
    }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkRegistration()
        bindview()
    }

    private fun bindview(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.registBtnRegister.visibility = View.GONE
        binding.onClickListener = this
    }


    private fun checkRegistration() {
        if (ApplicationClass.getIsKakao()) {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    id = "Kakao_" + result?.id.toString()
                    isUserOnRx(id)
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    redirectLoginActivity()
                    finish()
                }

            })
        } else {
            id = requestGoogleId(); isUserOnRx(id)
        }

    }

    private fun isUserOnRx(id: String) {
        saveUserId(id)
        disposable = client.isUserOnRx(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { result -> isUserAvailable(result?.get("result").toString(), result?.get("name").toString()) }
                , { error -> DLog.e("error ${error?.message.toString()}") })

    }

    private fun isUserAvailable(isAvailable: String, username: String) {
        when {
            isAvailable.contains("200") -> {
                saveUserNick(username)
                crashlyticsSetData(username)
                redirectHomeMainActivity()
                finish()
            }
            isAvailable.contains("에러") -> alertAndFinishDialog()
            else -> {
                setViewVisibility()
            }
        }
    }
    private fun crashlyticsSetData(username: String){
        Crashlytics.setUserIdentifier(id)
        Crashlytics.setUserName(username)
    }


    private fun requestGoogleId(): String {
        val acct = GoogleSignIn.getLastSignedInAccount(this)

        return "Google_" + acct?.id.toString()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.regist_btn_register -> {

                if (nullCheck())
                    setImageProfileRx()
                else
                    Toast.makeText(this@RegisterActivity, "모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

            R.id.regist_img_profileimage -> {
                beginActivity(Intent(this@RegisterActivity, RegisterProfileImageActivity::class.java))
            }
            R.id.regist_btn_clearImage -> {
                binding.registImgProfileimage.setImageDrawable(null)
            }
        }
    }

    private fun setImageProfileRx() {
        if (binding.registImgProfileimage.drawable != null) {
            val file = UtilMethod.getCompressed(this@RegisterActivity, File(name).toString(), "profile")
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file).let { MultipartBody.Part.createFormData("userProfile", file.name, it) }


            val userid = id.trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
            val age = binding.registEditAge.text.toString().trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
            val email = binding.registEditEmail.text.toString().trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
            val nickname = binding.registEditNickname.text.toString().trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
            val gender = binding.registRadiogroupAge.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }.trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
            val isKakao = ApplicationClass.getIsKakao().toString().trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }


            disposable = client.registerAccountImageRx(userid, nickname, email, age, gender, isKakao, requestFile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                DLog.e("message :$result")
                                beginActivity(Intent(this@RegisterActivity, ReviewMainActivity::class.java))
                                finish()
                            }, { error -> DLog.e("error ${error?.message.toString()}") })

        } else {
            val age = binding.registEditAge.text.toString()
            val email = binding.registEditEmail.text.toString()
            val nickname = binding.registEditNickname.text.toString()
            val gender = binding.registRadiogroupAge.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }
            val isKakao = ApplicationClass.getIsKakao().toString()

            disposable = client.registerAccountRx(id, nickname, email, age, gender, isKakao)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                DLog.e("message :$result")
                                beginActivity(Intent(this@RegisterActivity, ReviewMainActivity::class.java))
                                finish()
                            }, { error -> DLog.e("error ${error?.message.toString()}") })

        }

    }


    override fun onResume() {
        super.onResume()
        val pref = this@RegisterActivity.getSharedPreferences("fileName", Context.MODE_PRIVATE)
        this.name = pref?.getString("filestring", "empty")!!
        if (name != "empty") {
            Uri.parse("file://$name")
                    .let {
                        Glide.with(this@RegisterActivity)
                                .load(it)
                                .apply(RequestOptions()
                                        .centerCrop()
                                        .circleCrop()
                                        .override(300, 300))
                                .into(object : SimpleTarget<Drawable>() {
                                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                        binding.registImgProfileimage.setImageDrawable(resource)
                                    }

                                })
                    }
        }
        pref.edit().remove("fileName").apply()
    }

    private fun nullCheck(): Boolean {

        return (binding.registEditNickname.text.toString().isNotEmpty()
                && binding.registEditEmail.text.toString().isNotEmpty()
                && binding.registEditAge.text.toString().isNotEmpty())
    }

    private fun setViewVisibility() {

        binding.registBtnClearImage.visibility = View.VISIBLE
        binding.registEmptyBackground.visibility = View.GONE
        binding.registBtnRegister.visibility = View.VISIBLE
    }

    private fun saveUserNick(username: String) {
        val editor = pref.edit()
        editor.putString("UserNick", username.replace("\"", ""))
        editor.apply()
    }

    private fun saveUserId(id: String) {
        pref = getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("userLoginId", id)
        editor.apply()
    }

//    override fun onPause() {
//        super.onPause()
//        disposable?.dispose()
//    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
