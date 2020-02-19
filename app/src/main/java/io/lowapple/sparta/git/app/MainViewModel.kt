package io.lowapple.sparta.git.app

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import io.lowapple.sparta.git.app.api.model.User
import io.lowapple.sparta.git.app.repository.GithubClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: GithubClientRepository,
    private val preference: Preference
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() {
            return _user
        }

    fun updateUserProfile() {
        CoroutineScope(Dispatchers.Main).launch {
            if (preference.token == null) {
                return@launch
            }
            val user = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                repository.getUser(preference.token!!)
            }
            _user.value = user
        }
    }
}