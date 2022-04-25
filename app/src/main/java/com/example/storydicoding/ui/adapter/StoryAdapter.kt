package com.example.storydicoding.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storydicoding.Helper.Companion.dateFormat
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.databinding.StoryItemBinding
import com.example.storydicoding.ui.detailstory.DetailStoryFragment

class StoryAdapter :
    PagingDataAdapter<ListStoryItem,StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    class StoryViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .apply(RequestOptions())
                    .into(ivPhoto)

                tvName.text = story.name
                tvCreatedat.text = story.createdAt.dateFormat()

                itemView.setOnClickListener {
                    val fragmentManager =
                        (itemView.context as AppCompatActivity).supportFragmentManager
                    val detailStoryFragment = DetailStoryFragment()
                    val bundle = Bundle()

                    bundle.putParcelable(DetailStoryFragment.STORY, story)
                    detailStoryFragment.show(
                        fragmentManager,
                        DetailStoryFragment::class.java.simpleName
                    )
                    detailStoryFragment.arguments = bundle
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}