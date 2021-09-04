package com.mumjolandia.android.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager


class AndroidUtils{
    companion object{
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager: InputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    0
                )
            }
        }
    }
}
