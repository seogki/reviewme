package com.skh.reviewme.Setting.Notification


import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Network.ApiCilentRx

import com.skh.reviewme.R
import com.skh.reviewme.Setting.Model.SettingNotificationModel
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.GridSpacingItemDecoration
import com.skh.reviewme.databinding.FragmentSettingNotificationBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * A simple [Fragment] subclass.
 */
class SettingNotificationFragment : Fragment(), View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener {


    private lateinit var binding: FragmentSettingNotificationBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var settingNotificationAdapter: SettingNotificationAdapter
    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_notification, container, false)
        binding.notificationImgBack.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        binding.onClickListener = this
        setRv()
        return binding.root
    }

    private fun setRv() {
        settingNotificationAdapter = SettingNotificationAdapter(context!!, ArrayList<SettingNotificationModel>())
        linearLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        binding.notificationRv.layoutManager = linearLayoutManager
        binding.notificationRv.itemAnimator = null
        binding.notificationRv.addItemDecoration(GridSpacingItemDecoration(1, 5, false, 0))
        settingNotificationAdapter.setOnItemClickListener(this)
        binding.notificationRv.adapter = settingNotificationAdapter
        callNotificationApi()

    }

    private fun callNotificationApi() {
        disposable = client.GetSettingNotificationItemRx().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    settingNotificationAdapter.addItems(result?.settingNotificationModel as MutableList<SettingNotificationModel>)
                    DLog.e("text : ${result.settingNotificationModel}")
                }, { error ->
                    DLog.e("t : ${error?.message.toString()}")
                })
    }

    override fun onItemClick(view: View, position: Int) {

        val tempTitle = settingNotificationAdapter.getItem(position)?.NotificationTitle
        val tempText = settingNotificationAdapter.getItem(position)?.NotificationText
        val tempTime = settingNotificationAdapter.getItem(position)?.NotificationTime
        startActivity(Intent(context!!, SettingNotificationActivity::class.java)
                .putExtra("tempTitle", tempTitle)
                .putExtra("tempText", tempText)
                .putExtra("tempTime", tempTime))

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.notification_img_back -> {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}// Required empty public constructor
