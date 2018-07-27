package com.skh.reviewme.Home

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Community.CommunityMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.Review.ReviewMainActivity
import com.skh.reviewme.Setting.SettingMainActivity
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivityHomeMainBinding

class HomeMainActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityHomeMainBinding
    private var backKeyPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@HomeMainActivity, R.layout.activity_home_main)
        binding.layoutBottomTab.onClickListener = this
        setCurrentTab()
        addFragment(R.id.frame_layout, HomeMainFragment(), false, false, "HomeMainFragment")

    }

    private fun setCurrentTab() {
        binding.layoutBottomTab.bottomLayoutBtn0Txt.setImageDrawable(ContextCompat.getDrawable(this@HomeMainActivity, R.drawable.icons8_home_24_fill))
        binding.layoutBottomTab.bottomLayoutBtn0Txt.drawable.setColorFilter(Color.parseColor("#13A9AA"), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab.bottomLayoutText0.setTextColor(Color.parseColor("#13A9AA"))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this@HomeMainActivity
                        , ReviewMainActivity::class.java)
                )
            }

            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this@HomeMainActivity
                        , CommunityMainActivity::class.java)
                )
            }
            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this@HomeMainActivity
                        , SettingMainActivity::class.java)
                )
            }

        }
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
        return curFragment.tag == "HomeMainFragment"
    }
}
