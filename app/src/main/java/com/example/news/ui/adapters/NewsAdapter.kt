package com.example.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.data.model.ArticleEntity
import com.example.news.databinding.ItemArticlePreviewBinding

class NewsAdapter(private val listener: ((item: ArticleEntity) -> Unit)? = null) :
    ListAdapter<ArticleEntity, RecyclerView.ViewHolder>(DiffCallback()) {

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
                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener?.invoke(getItem(adapterPosition))
                    }
                }
            }
        }

        fun bind(item: ArticleEntity) {
            binding.article = item
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