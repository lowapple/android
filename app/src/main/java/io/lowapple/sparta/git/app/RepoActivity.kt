package io.lowapple.sparta.git.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import io.lowapple.sparta.git.app.databinding.ActivityRepoBinding
import org.koin.android.ext.android.inject

class RepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepoBinding
    private val viewModel: RepoViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "레포 선택"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        viewModel.updateRepositories()
        /*
        //
        if (FirebaseAuth.getInstance().currentUser != null) {
            val token = intent?.extras?.getString("token")
            if (token != null) {
                // Repository 정보 불러오기
                CoroutineScope(Dispatchers.Main).launch {
                    repository.repositories(token).apply {
                        Log.d(TAG, this.toString())
                    }
                }
            }
        } else {
            // Auth Activity 실행
            startActivity(
                Intent(applicationContext, AuthActivity::class.java)
            )
            finish()
        }

        repoAdapter = RepoAdapter()

        repo_recyclerview.apply {
            this.adapter = repoAdapter
            this.layoutManager = LinearLayoutManager(applicationContext)
        }
        */
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            // 검색 기능 추가
            val searchView = item.actionView as SearchView
            // 검색
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // viewModel.setSearchingText(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.setSearchingText(newText)
                    return true
                }
            })
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = RepoActivity::class.java.simpleName
    }
}
