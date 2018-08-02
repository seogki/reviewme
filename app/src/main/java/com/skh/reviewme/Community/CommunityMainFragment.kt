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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Community.Inner.CommunityInnerFragment
import com.skh.reviewme.Community.Question.CommunityQuestionActivity
import com.skh.reviewme.Community.model.CommunityModel
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.GridSpacingItemDecoration
import com.skh.reviewme.Util.UtilMethod
import com.skh.reviewme.databinding.FragmentCommunityMainBinding
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class CommunityMainFragment : BaseFragment()
        , View.OnClickListener
        , BaseRecyclerViewAdapter.OnItemClickListener
        , SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener {


    lateinit var binding: FragmentCommunityMainBinding
    private lateinit var communityMainAdapter: CommunityMainAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var dialog: android.app.AlertDialog
    private var isLoading: Boolean = false
    private var isSearch: Boolean = false
    private var searchText: String = ""
    private var pref: SharedPreferences? = null
    private val client by lazy {
        ApiCilentRx.create()
    }
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_main, container, false)
        binding.onClickListener = this
        binding.mainSearchEdit.setOnEditorActionListener(this)
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
        if (communityMainAdapter.itemCount < 1) {
            dialog.dismiss()
            isLoading = false
        } else {
            disposable = communityMainAdapter.getItem(communityMainAdapter.itemCount - 1)?.communityid?.let {
                client.ScrollGetCommunityItemRx(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ result ->
                            isLoading = when {
                                result?.CommunityModel?.isNotEmpty() == true -> {
                                    communityMainAdapter.addItems(result.CommunityModel as MutableList<CommunityModel>)
                                    dialog.dismiss()
                                    DLog.e("memory: " + UtilMethod.getMemoryUsage(communityMainAdapter.itemCount))
                                    false
                                }
                                else -> {
                                    dialog.dismiss()
                                    false
                                }
                            }
                        }, { error ->
                            DLog.e("t : ${error?.message.toString()}")
                            dialog.dismiss()
                            isLoading = false
                        })
            }
        }

    }

    private fun getCommunityItemFromServer() {

        disposable = client.GetCommunityItemRx().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    communityMainAdapter.addItems(result?.CommunityModel as MutableList<CommunityModel>)
                    dialog.dismiss()
                    setScrollbar()
                }, { error ->
                    DLog.e("t : ${error?.message.toString()}")
                    dialog.dismiss()
                })
    }


    override fun onItemClick(view: View, position: Int) {
        DLog.e("item $position")

        val id = communityMainAdapter.getItem(position)?.communityid
        val bundle = Bundle()
        bundle.putString("communityid", id)
        val frag = CommunityInnerFragment()
        frag.arguments = bundle
        addFragment(activity, R.id.frame_layout, frag, false, true, "CommunityInnerFragment")
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
            disposable = client.GetSearchedCommunityItemRx(userid, searchText).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        communityMainAdapter.addItems(result?.CommunityModel as MutableList<CommunityModel>)
                        clearAndClose(binding.mainSearchEdit)
                        dialog.dismiss()

                    }, { error ->
                        DLog.e("t : ${error?.message.toString()}")
                        dialog.dismiss()
                    })

        } else {
            Toast.makeText(context, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }


    }

    private fun getSearchScrollApi() {

        if (communityMainAdapter.itemCount < 1) {
            dialog.dismiss()
            isLoading = false
        } else {
            val userid = pref?.getString("userLoginId", "") ?: return
            disposable = communityMainAdapter.getItem(communityMainAdapter.itemCount - 1)?.communityid?.let {
                client.GetScrollSearchedCommunityItemRx(userid, searchText, it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ result ->
                            isLoading = when {
                                result?.CommunityModel?.isNotEmpty() == true -> {
                                    communityMainAdapter.addItems(result.CommunityModel as MutableList<CommunityModel>)
                                    dialog.dismiss()
                                    DLog.e("memory: " + UtilMethod.getMemoryUsage(communityMainAdapter.itemCount))
                                    false
                                }
                                else -> {
                                    dialog.dismiss()
                                    false
                                }
                            }

                        }, { error ->
                            DLog.e("t : ${error?.message.toString()}")
                            dialog.dismiss()
                            isLoading = false
                        })
            }
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

    private fun setScrollbar() {
        Handler().postDelayed({
            setRecyclerViewScrollbar()
        }, 100)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                getSearchedApi()
            }
        }
        return true
    }

    private fun setSpotDialogs() {
        dialog = SpotsDialog
                .Builder()
                .setContext(context!!)
                .setMessage("데이터를 불러오는 중...")
                .setCancelable(false)
                .build().apply { show() }
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

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


}// Required empty public constructor
