package io.lowapple.sparta.git.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import io.lowapple.sparta.git.app.databinding.ActivityAuthBinding
import io.lowapple.sparta.git.app.repository.GithubClientRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject

class AuthActivity : AppCompatActivity() {

    private lateinit var uri: String
    private lateinit var binding: ActivityAuthBinding
    private val preference: Preference by inject()
    private val viewModel: AuthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "로그인화면"

        uri =
            "${AUTH_URL}client_id=${getString(R.string.sparta_github_client_id)}&redirect_uri=$REDIRECT_URI"

        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
                val code = uri.getQueryParameter("code")
                if (code != null) {
                    getAccessToken(code)
                }
            }
        } else {
            if (preference.token != null) {
                main()
            } else {
                getUserCode()
            }
        }

        // 로그인
        viewModel.isLoggined.observe(this, Observer {
            if (it) {
                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                //
                main()
            } else {
                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 메인화면으로 이동
    private fun main() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getUserCode() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    uri
                )
            )
        )
    }

    private fun getAccessToken(code: String) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAccessToken(code)
        }
    }

    companion object {
        private const val TAG = "AuthActivity"
        private const val AUTH_URL = "https://github.com/login/oauth/authorize?"
        private const val REDIRECT_URI = "spartagit://authorize"
        public const val REQUEST = 10
        public const val SUCCESS = 1
        public const val FAILURE = 0
    }
}
