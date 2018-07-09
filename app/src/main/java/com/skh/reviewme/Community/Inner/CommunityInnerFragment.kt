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
import com.google.gson.JsonObject
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Community.model.CommunityInnerCommentModel
import com.skh.reviewme.Community.model.CommunityInnerCommentModels
import com.skh.reviewme.Community.model.CommunityInnerModel
import com.skh.reviewme.Network.ApiCilent
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.FragmentCommunityInnerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class CommunityInnerFragment : BaseFragment(), View.OnClickListener {

    lateinit var id: String

    lateinit var binding: FragmentCommunityInnerBinding

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

    private fun setView(id: String?) {
        val call = id?.let { ApiCilent.getInstance().getService().GetInnerCommunityItem(it) }
        call?.enqueue(object : Callback<CommunityInnerModel> {
            override fun onFailure(call: Call<CommunityInnerModel>?, t: Throwable?) {
                DLog.e("t message ${t?.message.toString()}")
            }

            override fun onResponse(call: Call<CommunityInnerModel>?, response: Response<CommunityInnerModel>?) {
                binding.item = response?.body()

//                setNullImageVisibility()
            }

        })
    }

    private fun setNullImageVisibility() {
        if (binding.item.image1.isNullOrEmpty())
            binding.innerImgFirst.visibility = View.GONE
        if (binding.item.image2.isNullOrEmpty())
            binding.innerImgSecond.visibility = View.GONE
        if (binding.item.image3.isNullOrEmpty())
            binding.innerImgThird.visibility = View.GONE
        if (binding.item.image4.isNullOrEmpty())
            binding.innerImgFourth.visibility = View.GONE

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
        binding.innerRvComment.setHasFixedSize(false)
        binding.innerRvComment.isNestedScrollingEnabled = false

        view?.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
        )


        setSpotDialog()
        getCommunityCommentFromServer()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getCommunityCommentFromServer() {
        val call = ApiCilent.getInstance().getService().GetInnerCommunityComment(id)

        call.enqueue(object : Callback<CommunityInnerCommentModels> {
            override fun onFailure(call: Call<CommunityInnerCommentModels>?, t: Throwable?) {
                dismissSpotDialog()
            }

            override fun onResponse(call: Call<CommunityInnerCommentModels>?, response: Response<CommunityInnerCommentModels>?) {
                DLog.e("response.body : ${response?.body()?.CommunityInnerCommentModel.toString()}")
                communityInnerAdapter.addItems(response?.body()?.CommunityInnerCommentModel as MutableList<CommunityInnerCommentModel>)
                communityInnerAdapter.notifyDataSetChanged()
                dismissSpotDialog()
            }

        })
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
        val userid = pref?.getString("userLoginId","")
        val comment = binding.innerEditComment.text.toString()
        val username = pref?.getString("UserNick","")
        if (comment.length < 0) {

        } else {
            val call = ApiCilent.getInstance().getService().SetInnerCommunityComment(userid!!,id, username!!,comment,"")
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                    DLog.e("t message : ${t?.message.toString()}")
                }

                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                    onRefresh()
                }

            })
        }
    }

    private fun onRefresh(){
        communityInnerAdapter.clearItems()
        binding.innerEditComment.text.clear()
        getCommunityCommentFromServer()
    }


    override fun onPause() {
        binding.innerConstLayout.visibility = View.GONE
        super.onPause()

    }

    override fun onResume() {
        binding.innerConstLayout.visibility = View.VISIBLE
        super.onResume()

    }

}// Required empty public constructor


