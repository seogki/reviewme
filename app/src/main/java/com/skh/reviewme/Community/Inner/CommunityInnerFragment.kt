package com.skh.reviewme.Community.Inner


import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Community.model.CommunityInnerCommentModel
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.FragmentCommunityInnerBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * A simple [Fragment] subclass.
 */
class CommunityInnerFragment : BaseFragment(), View.OnClickListener {

    lateinit var id: String

    lateinit var binding: FragmentCommunityInnerBinding
    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var communityInnerAdapter: CommunityInnerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_inner, container, false)
        binding.onClickListener = this
        id = arguments?.getString("communityid").toString()
        setView(id)
        setRv()
        return binding.root
    }

    private fun setRv() {

        communityInnerAdapter = CommunityInnerAdapter(context!!, ArrayList<CommunityInnerCommentModel>())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        (layoutManager as LinearLayoutManager).initialPrefetchItemCount = 5
        binding.innerRvComment.layoutManager = layoutManager
        binding.innerRvComment.adapter = communityInnerAdapter
        binding.innerRvComment.setItemViewCacheSize(20)
        binding.innerRvComment.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_AUTO
        binding.innerRvComment.isNestedScrollingEnabled = false

        view?.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
        )

        setSpotDialog()
        getCommunityCommentFromServer()
    }

    private fun setView(id: String?) {

        disposable = id?.let {
            client.GetInnerCommunityItemRx(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        binding.item = result
                        binding.executePendingBindings()
                    }, { error ->
                        DLog.e("t : ${error?.message.toString()}")
                    })
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun getCommunityCommentFromServer() {

        disposable = id.let {
            client.GetInnerCommunityCommentRx(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        communityInnerAdapter.addItems(result?.CommunityInnerCommentModel as MutableList<CommunityInnerCommentModel>)
                        dismissSpotDialog()
                    }, { error ->
                        DLog.e("t : ${error?.message.toString()}"); dismissSpotDialog()
                    })
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.inner_txt_comment -> {
                addComment()
            }
        }
    }

    private fun addComment() {
        if (!binding.innerEditComment.text.toString().isEmpty()) {
            sendCommunityCommentToServer()
            closeKeyboard()
        }

    }

    private fun sendCommunityCommentToServer() {
        val pref = activity?.getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        val userid = pref?.getString("userLoginId", "")
        val comment = binding.innerEditComment.text.toString()
        val username = pref?.getString("UserNick", "")
        if (comment.length < 0) {

        } else {
            disposable = client.SetInnerCommunityCommentRx(userid!!, id, username!!, comment, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        onRefresh()
                    }, { error ->
                        DLog.e("t : ${error?.message.toString()}")
                    })
        }
    }

    private fun onRefresh() {
        communityInnerAdapter.clearItems()
        binding.innerEditComment.text.clear()
        getCommunityCommentFromServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}// Required empty public constructor


