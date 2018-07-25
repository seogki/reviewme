package com.skh.reviewme.Setting

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Community.CommunityMainActivity
import com.skh.reviewme.Main.ReviewMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivitySettingMainBinding

class SettingMainActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivitySettingMainBinding
    private var backKeyPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_main)
        binding.layoutBottomTab.onClickListener = this
        addFragment(R.id.frame_layout, SettingMainFragment(), false, false, "SettingMainFragment")
        setCurrentTab()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this@SettingMainActivity
                        , ReviewMainActivity::class.java)
                )

            }
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this@SettingMainActivity
                        , CommunityMainActivity::class.java)
                )
            }
        }
    }

    private fun setCurrentTab() {
        binding.layoutBottomTab.bottomLayoutBtn3Txt.setImageDrawable(ContextCompat.getDrawable(this@SettingMainActivity, R.drawable.icons8_user_24_fill))
        binding.layoutBottomTab.bottomLayoutBtn3Txt.drawable.setColorFilter(Color.parseColor("#13A9AA"), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab.bottomLayoutText3.setTextColor(Color.parseColor("#000000"))
    }


    override fun onBackPressed() {
        DLog.e("onBack Pressed" + isFirstFragment())

        if (isFirstFragment()) {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                showGuide()
                return
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                this.finishAffinity()
                finishToast()
            }
        } else {
            super.onBackPressed()
        }

    }

    private fun isFirstFragment(): Boolean {
        val curFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        DLog.e("current Fragment ${curFragment.tag}")
        return curFragment.tag == "SettingMainFragment"
    }
}
