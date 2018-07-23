package com.skh.reviewme.Community

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Community.Inner.CommunityInnerFragment
import com.skh.reviewme.Community.Question.CommunityQuestionActivity
import com.skh.reviewme.Community.model.CommunityModel
import com.skh.reviewme.Community.model.CommunityModels
import com.skh.reviewme.Network.ApiCilent
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.GridSpacingItemDecoration
import com.skh.reviewme.Util.UtilMethod
import com.skh.reviewme.databinding.FragmentCommunityMainBinding
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityMainFragment : BaseFragment(), View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    lateinit var binding: FragmentCommunityMainBinding
    private lateinit var communityMainAdapter: CommunityMainAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var dialog: android.app.AlertDialog
    private var isLoading: Boolean = false
    private var isSearch: Boolean = false
    private var searchText: String = ""
    private var pref: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_main, container, false)
        binding.onClickListener = this
        pref = activity?.getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        setView()
        return binding.root
    }

    private fun setView() {

        communityMainAdapter = CommunityMainAdapter(context!!, ArrayList<CommunityModel>())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.mainGridRv.layoutManager = layoutManager

        binding.mainGridRv.itemAnimator = null
        binding.mainGridRv.setHasFixedSize(true)
        binding.mainGridRv.isDrawingCacheEnabled = true
        binding.mainGridRv.setItemViewCacheSize(20)
        binding.mainGridRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        communityMainAdapter.setHasStableIds(true)
        binding.mainGridRv.addItemDecoration(GridSpacingItemDecoration(1, 50, false, 0))
        communityMainAdapter.setOnItemClickListener(this)


        binding.swipeLayout.setDistanceToTriggerSync(350)
        binding.swipeLayout.setOnRefreshListener(this)
        setSpotDialogs()
        getCommunityItemFromServer()

        Handler().postDelayed({
            binding.mainGridRv.adapter = communityMainAdapter
        }, 100)

    }

    private fun setSpotDialogs() {
        dialog = SpotsDialog
                .Builder()
                .setContext(context!!)
                .setMessage("데이터를 불러오는 중...")
                .setCancelable(false)
                .build().apply { show() }
    }


    private fun setRecyclerViewScrollbar() {
        binding.mainGridRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView!!.canScrollVertically(1)) {
                    if (!dialog.isShowing && !isLoading) {
                        if (!isSearch) {
                            isLoading = true
                            dialog.show()
                            scrollToEnd()
                        } else {
                            isLoading = true
                            dialog.show()
                            getSearchScrollApi()
                        }
                    }
                }
            }
        })
    }

    private fun scrollToEnd() {
        DLog.e("scroll end Called")
        if (communityMainAdapter.itemCount > 0) {
            val call = communityMainAdapter
                    .getItem(communityMainAdapter.itemCount - 1)
                    ?.communityid
                    ?.let { ApiCilent.getInstance().getService().ScrollGetCommunityItem(it) }

            DLog.e("community id :" + communityMainAdapter.getItem(communityMainAdapter.itemCount - 1)?.communityid)



            call?.enqueue(object : Callback<CommunityModels> {
                override fun onFailure(call: Call<CommunityModels>?, t: Throwable?) {
                    DLog.e("t ${t?.message.toString()}")
                    dialog.dismiss()
                    isLoading = false
                }

                override fun onResponse(call: Call<CommunityModels>?, response: Response<CommunityModels>?) {
                    isLoading = when {
                        response?.body()?.CommunityModel?.isNotEmpty() == true -> {
                            communityMainAdapter.addItems(response.body()!!.CommunityModel as MutableList<CommunityModel>)
                            //                            communityMainAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                            DLog.e("memory: " + UtilMethod.getMemoryUsage(communityMainAdapter.itemCount))
                            false
                        }
                        else -> {
                            dialog.dismiss()
                            false
                        }
                    }
                }

            })
        } else {
            dialog.dismiss()
            isLoading = false
        }

    }

    private fun getCommunityItemFromServer() {
        val call = ApiCilent.getInstance().getService().GetCommunityItem()
        call.enqueue(object : Callback<CommunityModels> {
            override fun onFailure(call: Call<CommunityModels>?, t: Throwable?) {
                DLog.e("t : ${t?.message.toString()}")
                dialog.dismiss()
            }

            override fun onResponse(call: Call<CommunityModels>?, response: Response<CommunityModels>?) {
                communityMainAdapter.addItems(response?.body()?.CommunityModel as MutableList<CommunityModel>)
                dialog.dismiss()
                Handler().postDelayed({
                    setRecyclerViewScrollbar()
                }, 100)
            }

        })
    }

    override fun onItemClick(view: View, position: Int) {
        DLog.e("item $position")

        val id = communityMainAdapter.getItem(position)?.communityid
        val bundle = Bundle()
        bundle.putString("communityid", id)
        val frag = CommunityInnerFragment()
        frag.arguments = bundle
        addFragment(activity, R.id.frame_layout, frag, false, true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.community_btn_cat -> {
                beginNewActivity(Intent(context, CommunityQuestionActivity::class.java))
            }
            R.id.community_search_img -> {
                getSearchedApi()
            }
        }
    }

    private fun getSearchedApi() {
        searchText = binding.mainSearchEdit.text.toString()
        if (searchText.isNotEmpty()) {
            onRefreshWithoutApi()
            val userid = pref?.getString("userLoginId", "") ?: return
            val call = ApiCilent.getInstance().getService().GetSearchedCommunityItem(userid, searchText)
            call.enqueue(object : Callback<CommunityModels> {
                override fun onFailure(call: Call<CommunityModels>?, t: Throwable?) {
                    DLog.e(t?.message.toString())
                }

                override fun onResponse(call: Call<CommunityModels>?, response: Response<CommunityModels>?) {
                    binding.mainSearchEdit.text.clear()
                    closeKeyboard()
                    communityMainAdapter.addItems(response?.body()?.CommunityModel as MutableList<CommunityModel>)

                }

            })
        } else {
            Toast.makeText(context, "뭐라도 치세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSearchScrollApi() {
        if (communityMainAdapter.itemCount > 0) {
            val userid = pref?.getString("userLoginId", "") ?: return
            val call = communityMainAdapter
                    .getItem(communityMainAdapter.itemCount - 1)
                    ?.communityid
                    ?.let { ApiCilent.getInstance().getService().GetScrollSearchedCommunityItem(userid, searchText, it) }

            call?.enqueue(object : Callback<CommunityModels> {
                override fun onFailure(call: Call<CommunityModels>?, t: Throwable?) {
                    DLog.e("t ${t?.message.toString()}")
                    dialog.dismiss()
                    isLoading = false
                }

                override fun onResponse(call: Call<CommunityModels>?, response: Response<CommunityModels>?) {
                    isLoading = when {
                        response?.body()?.CommunityModel?.isNotEmpty() == true -> {
                            communityMainAdapter.addItems(response.body()!!.CommunityModel as MutableList<CommunityModel>)
                            //                            communityMainAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                            DLog.e("memory: " + UtilMethod.getMemoryUsage(communityMainAdapter.itemCount))
                            false
                        }
                        else -> {
                            dialog.dismiss()
                            false
                        }
                    }
                }

            })
        } else {
            dialog.dismiss()
            isLoading = false
        }
    }

    override fun onRefresh() {
        communityMainAdapter.clearItems()
        communityMainAdapter.notifyDataSetChanged()
        binding.mainGridRv.removeOnScrollListener(null)
        dialog.show()
        isSearch = false
        getCommunityItemFromServer()
        binding.swipeLayout.isRefreshing = false
    }

    private fun onRefreshWithoutApi() {
        communityMainAdapter.clearItems()
        communityMainAdapter.notifyDataSetChanged()
        isSearch = true
        binding.mainGridRv.removeOnScrollListener(null)
    }

    override fun onResume() {

        val pref = activity?.getSharedPreferences("CommunityDone", Activity.MODE_PRIVATE)
        val data = pref!!.getBoolean("isDone", false)
        if (data) {
            DLog.e("data : $data")
            pref.edit().remove("isDone").apply()
            onRefresh()
        }

        super.onResume()
    }


}// Required empty public constructor
