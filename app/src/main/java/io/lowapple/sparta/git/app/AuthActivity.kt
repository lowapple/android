package io.lowapple.sparta.git.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "linkWithCredential:success")
                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.exception)

                    // Redirect
                }
            }
    }

    companion object {
        private const val TAG = "AuthActivity"
    }
}
