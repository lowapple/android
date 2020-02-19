package io.lowapple.sparta.git.app

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.lowapple.sparta.git.app.db.entity.RepoEntity

@BindingAdapter(value = ["items", "search"])
fun setRepositories(view: RecyclerView, items: List<RepoEntity>?, search: String) {
    view.adapter?.run {
        if (this is RepoAdapter) {
            this.items = items?.filter {
                search.toLowerCase().run {
                    it.name.toLowerCase().contains(this) ||
                            it.description.toLowerCase().contains(this) ||
                            it.full_name.toLowerCase().contains(this)
                }
            } ?: emptyList()

            notifyDataSetChanged()
        }
    } ?: run {
        RepoAdapter(items ?: arrayListOf()).apply {
            // 중복 데이터 제거
            setHasStableIds(true)
            // Adapter 설정
            view.adapter = this
        }
    }
}