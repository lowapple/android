package io.lowapple.sparta.git.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.lowapple.sparta.git.app.api.model.Repo
import kotlinx.android.synthetic.main.row_repo.view.*

class RepoAdapter : RecyclerView.Adapter<RepoAdapter.RepoHolder>() {

    private val items = ArrayList<Repo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder =
        RepoHolder.createFromViewGroup(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.bind(
            items[position]
        )
    }

    fun update(items: List<Repo>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    class RepoHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        fun bind(repo: Repo) {
            view.repo_model_full_name.text = repo.full_name
            view.repo_model_description.text = repo.description
        }

        companion object {
            fun createFromViewGroup(parent: ViewGroup): RepoHolder {
                return RepoHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_repo, parent, false))
            }
        }
    }
}