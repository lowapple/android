package io.lowapple.sparta.git.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.lowapple.sparta.git.app.db.dao.RepoDao
import io.lowapple.sparta.git.app.db.entity.RepoEntity
import io.lowapple.sparta.git.app.repository.GithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoViewModel(
    private val repository: GithubRepository,
    private val dao: RepoDao,
    private val preference: Preference
) : ViewModel() {

    private val _searchingText = MutableLiveData<String>().apply { value = "" }

    fun setSearchingText(value: String) {
        _searchingText.value = value
    }

    val searchingText: LiveData<String>
        get() {
            return _searchingText
        }

    //
    val items = dao.repositories()

    //
    fun updateRepositories() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val repos = repository.repositories(preference.token!!)
                repos
                    .map {
                        RepoEntity(
                            id = it.id,
                            name = it.name,
                            full_name = it.full_name,
                            description = it.description
                        )
                    }
                    .apply {
                        dao.insert(this)
                    }
            }
        }
    }
}