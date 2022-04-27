package com.example.storydicoding.ui.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storydicoding.Helper.Companion.dateFormat
import com.example.storydicoding.R
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBinding

    private var story: ListStoryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        story=intent.getParcelableExtra(STORY)

        setupView()
    }

    private fun setupView() {
        com.example.storydicoding.setupView(window)

        binding.apply {
            layoutDetail.apply {
                Glide.with(this@DetailActivity)
                    .load(story?.photoUrl)
                    .into(ivPhoto)

                tvName.text = story?.name
                tvCreatedat.text = story?.createdAt?.dateFormat()
            }
            tvDesc.text = story?.description
        }
    }

    companion object{
        const val STORY="story"
    }
}