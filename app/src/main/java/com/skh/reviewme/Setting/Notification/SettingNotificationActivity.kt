package com.skh.reviewme.Setting.Notification

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ActivitySettingNotificationBinding

class SettingNotificationActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySettingNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SettingNotificationActivity, R.layout.activity_setting_notification)

        val intent = intent
        binding.notificationItemTxtTitle.text = intent.getStringExtra("tempTitle")
        binding.notificationItemTxtText.text = intent.getStringExtra("tempText")
        binding.notificationItemTxtTime.text = intent.getStringExtra("tempTime")

    }



}
