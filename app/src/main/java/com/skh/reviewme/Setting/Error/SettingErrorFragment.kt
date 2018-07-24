package com.skh.reviewme.Setting.Error


import android.app.Activity
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.google.gson.JsonObject
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Network.ApiCilent
import com.skh.reviewme.R
import com.skh.reviewme.Setting.Model.SettingErrorModel
import com.skh.reviewme.Setting.Model.SettingErrorModels
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.GridSpacingItemDecoration
import com.skh.reviewme.databinding.FragmentSettingErrorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class SettingErrorFragment : BaseFragment(), View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener {


    private lateinit var binding: FragmentSettingErrorBinding
    private lateinit var settingErrorAdapter: SettingErrorAdapter
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var pref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_error, container, false)
        binding.onClickListener = this
        pref = activity!!.getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        setRv()
        return binding.root

    }

    private fun setRv() {
        settingErrorAdapter = SettingErrorAdapter(context!!, ArrayList<SettingErrorModel>())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.errorRv.layoutManager = layoutManager

        binding.errorRv.itemAnimator = null
        binding.errorRv.setHasFixedSize(true)
        settingErrorAdapter.setHasStableIds(true)
        binding.errorRv.addItemDecoration(GridSpacingItemDecoration(1, 20, false, 0))
        settingErrorAdapter.setOnItemClickListener(this)

        binding.errorRv.adapter = settingErrorAdapter
        getErrorApi()


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.error_btn_register -> {
                binding.errorBtnRegister.isEnabled = false
                registerErrorApi()
            }
        }
    }

    private fun registerErrorApi() {
        if (binding.errorEditName.text.toString().isNotEmpty() && binding.errorEditContent.text.toString().isNotEmpty()) {
            val userid = pref.getString("userLoginId", "")
            val title = binding.errorEditName.text.toString().trim()
            val tabs = binding.errorRadiogroupSetting.checkedRadioButtonId.let { activity!!.findViewById<RadioButton>(it).text.toString() }.trim()
            val content = binding.errorEditContent.text.toString().trim()
            val call = ApiCilent.getInstance().getService().SetSettingErrorItem(userid, title, content, tabs)
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                    DLog.e(t?.message.toString())
                    binding.errorBtnRegister.isEnabled = true
                }

                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {

                    AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                            .setMessage("접수 되었습니다.")
                            .setPositiveButton("확인", { dialog, _ ->
                                dialog.dismiss()
                                binding.errorBtnRegister.isEnabled = true
                                binding.errorEditContent.text.clear()
                                binding.errorEditName.text.clear()
                                refreshRv()
                            }).setNegativeButton(null, null)
                            .show()

                }

            })
        } else {
            binding.errorBtnRegister.isEnabled = true
            Toast.makeText(context, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getErrorApi() {
        val call = ApiCilent.getInstance().getService().GetSettingErrorItem()
        call.enqueue(object : Callback<SettingErrorModels> {
            override fun onFailure(call: Call<SettingErrorModels>?, t: Throwable?) {
                DLog.e(t?.message.toString())
            }

            override fun onResponse(call: Call<SettingErrorModels>?, response: Response<SettingErrorModels>?) {

//                    DLog.e("res : ${response.body()!!}")
                if (response?.body() != null) {
                    settingErrorAdapter.addItems(response.body()?.settingErrorModel as MutableList<SettingErrorModel>)
                    settingErrorAdapter.notifyDataSetChanged()

                }
            }

        })
    }

    private fun refreshRv() {
        settingErrorAdapter.clearItems()
        getErrorApi()
    }

    override fun onItemClick(view: View, position: Int) {

    }

}// Required empty public constructor
