package io.lowapple.sparta.git.app

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import io.lowapple.sparta.git.app.repository.GithubClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val repository: GithubClientRepository,
    private val preference: Preference
) : ViewModel() {
    private val _isProgress = MutableLiveData<Boolean>().apply { value = false }
    val isProgress: LiveData<Boolean>
        get() {
            return _isProgress
        }

    private val _isLoginned = MutableLiveData<Boolean>()
    val isLoggined: LiveData<Boolean>
        get() {
            return _isLoginned
        }

    suspend fun getAccessToken(code: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _isProgress.value = true
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val response = repository.getAccessToken(code)
                //
                preference.token = response.access_token
                //
                repository.getUser(response.access_token).apply {
                    Log.d(TAG, toString())
                }

                FirebaseAuth.getInstance()
                    .signInWithCredential(GithubAuthProvider.getCredential(response.access_token))
                    .addOnCompleteListener { task ->
                        CoroutineScope(Dispatchers.Main).launch {
                            _isProgress.value = false
                            _isLoginned.value = task.isSuccessful
                        }
                    }
            }
        }
    }

    companion object {
        val TAG = AuthViewModel::class.java.simpleName
    }
}