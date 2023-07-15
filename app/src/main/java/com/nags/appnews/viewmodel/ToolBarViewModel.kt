package com.nags.appnews.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nags.appnews.utils.Event

/**
 * ViewModel for the Toolbar.
 */
class ToolBarViewModel : ViewModel() {

    val title = MutableLiveData<String>()
    val isCloseButtonVisible = MutableLiveData(View.VISIBLE)

    private val _closeButtonClicked = MutableLiveData<Event<Boolean>>()
    val closeButtonClicked: LiveData<Event<Boolean>>
        get() = _closeButtonClicked

    /**
     * Handles the click event of the close button.
     */
    fun onCloseButtonClick() {
        _closeButtonClicked.value = Event(true)
    }
}
