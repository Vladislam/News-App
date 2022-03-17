package com.example.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.data.model.ArticleEntity
import com.example.news.databinding.ItemArticlePreviewBinding
import com.example.news.util.const.BindingConstant
import com.example.news.util.extensions.throttleFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class NewsAdapter(private val listener: ((item: ArticleEntity) -> Unit)? = null) :
    ListAdapter<ArticleEntity, RecyclerView.ViewHolder>(DiffCallback()) {

    private val recyclerJob = Job()
    private val recyclerScope = CoroutineScope(Dispatchers.Main + recyclerJob)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        return NewsViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class NewsViewHolder constructor(
        private val binding: ItemArticlePreviewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.clicks()
                    .throttleFirst(BindingConstant.SMALL_THROTTLE)
                    .onEach {
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            listener?.invoke(getItem(adapterPosition))
                        }
                    }
                    .launchIn(recyclerScope)
            }
        }

        fun bind(item: ArticleEntity) = binding.apply {
            article = item
        }
    }


    override fun submitList(list: List<ArticleEntity>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleEntity>() {
        override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity) =
            oldItem == newItem
    }
}