package com.skh.reviewme.Community.customAnimation

import android.os.Build
import android.support.annotation.RequiresApi
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.ChangeBounds
import android.transition.TransitionSet

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
/**
 * Created by Seogki on 2018. 6. 19..
 */
class DetailsTransition : TransitionSet() {

    init {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(ChangeImageTransform())
    }
}