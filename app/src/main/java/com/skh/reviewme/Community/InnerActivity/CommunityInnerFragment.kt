package com.skh.reviewme.Community.InnerActivity


import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.R
import com.skh.reviewme.databinding.FragmentCommunityInnerBinding


/**
 * A simple [Fragment] subclass.
 */
class CommunityInnerFragment : BaseFragment() {

    lateinit var binding: FragmentCommunityInnerBinding
    lateinit var bitmap : Bitmap


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


//        bitmap = arguments?.getParcelable("IMAGE")!!


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_inner, container, false)

//        binding.comInnerImg.setImageBitmap(bitmap)

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)


        }
        super.onCreate(savedInstanceState)
    }

}// Required empty public constructor


