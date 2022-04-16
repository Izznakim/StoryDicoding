package com.example.storydicoding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storydicoding.Helper.Companion.dateFormat
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.databinding.StoryItemBinding
import com.google.android.material.snackbar.Snackbar

class StoryAdapter(private val listStory:List<ListStoryItem>):RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    class StoryViewHolder(private val binding: StoryItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(story:ListStoryItem){
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .apply(RequestOptions())
                    .into(ivPhoto)

                tvName.text=story.name
                tvCreatedat.text=story.createdAt.dateFormat()

                itemView.setOnClickListener {
                    Snackbar.make(itemView.rootView,story.name,Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding=StoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) =holder.bind(listStory[position])

    override fun getItemCount(): Int =listStory.size
}