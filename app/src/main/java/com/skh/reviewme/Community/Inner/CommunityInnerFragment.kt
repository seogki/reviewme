package com.skh.reviewme.Community.Inner


import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Community.model.CommunityInnerModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.FragmentCommunityInnerBinding


/**
 * A simple [Fragment] subclass.
 */
class CommunityInnerFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentCommunityInnerBinding

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var communityInnerAdapter: CommunityInnerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_inner, container, false)
        binding.onClickListener = this
        val bitmap = arguments?.getParcelable("IMAGE") as? Bitmap
        binding.innerInnerImg1Content.setImageBitmap(bitmap)
        arguments?.getString("text").let { binding.innerTxtTitle.text = it }
        arguments?.getString("title").let { binding.innerTxtRegiName.text = it }
        binding.innerInnerImg2Content.setImageBitmap(bitmap)
        binding.innerInnerImg3Content.setImageBitmap(bitmap)
        binding.innerInnerImg4Content.setImageBitmap(bitmap)
        setRv()

        if (bitmap == null) {
            binding.innerInnerImg1Content.visibility = View.GONE
            binding.innerInnerImg2Content.visibility = View.GONE
            binding.innerInnerImg3Content.visibility = View.GONE
            binding.innerInnerImg4Content.visibility = View.GONE
        }



        return binding.root
    }

    private fun setRv() {


        communityInnerAdapter = CommunityInnerAdapter(context!!, setdata())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        (layoutManager as LinearLayoutManager).initialPrefetchItemCount = 2
        binding.innerRvComment.layoutManager = layoutManager
        binding.innerRvComment.adapter = communityInnerAdapter
        binding.innerRvComment.setItemViewCacheSize(20)
        binding.innerRvComment.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_AUTO
        binding.innerRvComment.setHasFixedSize(false)
        binding.innerRvComment.isNestedScrollingEnabled = false
    }

    private fun setdata(): ArrayList<CommunityInnerModel> {
        val list = ArrayList<CommunityInnerModel>()

        list.add(CommunityInnerModel("skh", "ㅋㅋㅋ"))
        list.add(CommunityInnerModel("skh", "ㅎㅎ"))
        list.add(CommunityInnerModel("skh", "ㅋ515"))
        list.add(CommunityInnerModel("skh", "ㅋ12123ㅋㅋ"))
        list.add(CommunityInnerModel("skh", "1231111ㅋㅋ"))
        list.add(CommunityInnerModel("skh", "ㅋㅂㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㅋㅋ"))
        list.add(CommunityInnerModel("skh", "ㅂㅆㅆㅈㅂㅆㅂㅈㅆㅈㅂㅋㅋㅋ"))

        return list
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
            communityInnerAdapter.addItem(CommunityInnerModel("SEOGKI", binding.innerEditComment.text.toString()))
            closeKeyboard()
            binding.innerEditComment.setText("")
            Handler().postDelayed({
                layoutManager.smoothScrollToPosition(binding.innerRvComment, null, communityInnerAdapter.itemCount - 1)

            }, 100)
        }

    }


}// Required empty public constructor


