package com.example.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.ItemArticlePreviewBinding
import com.example.news.ui.model.Article

class NewsAdapter(private val listener: ((item: Article) -> Unit)? = null) :
    ListAdapter<Article, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {

        return NewsViewHolder(ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
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

        fun bind(item: Article) = with(binding) {
            item.apply {
                Glide.with(binding.root)
                    .load(urlToImage)
                    .placeholder(R.drawable.no_image_placeholder)
                    .into(ivArticleImage)
                tvSource.text = source?.name ?: ""
                tvTitle.text = title
                tvDescription.text = description
                tvPublishedAt.text = publishedAt.replace('T', ' ').substring(0, publishedAt.length - 1)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem.uuid == newItem.uuid

        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
    }
}