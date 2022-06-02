package academy.bangkit.kerani.ui.fragments.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text
}