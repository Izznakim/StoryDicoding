package com.example.storydicoding.ui.liststory.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.storydicoding.Helper.Companion.dateFormat
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.databinding.FragmentDetailStoryBinding

class DetailStoryFragment : DialogFragment() {
    private var _binding: FragmentDetailStoryBinding?=null
    private var story: ListStoryItem?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentDetailStoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArguments()
        setupView()
        Log.d(TAG, "onViewCreated: ${story?.name}")
    }

    private fun setupArguments(){
        if (arguments!=null) {
            story = arguments?.getParcelable(STORY)
        }
    }

    private fun setupView(){
        binding.apply {
            layoutDetail.apply {
                Glide.with(requireContext())
                    .load(story?.photoUrl)
                    .into(ivPhoto)

                tvName.text = story?.name
                tvCreatedat.text = story?.createdAt?.dateFormat()
            }
            tvDesc.text=story?.description
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog=dialog
        if (dialog!=null){
            val width=ViewGroup.LayoutParams.MATCH_PARENT
            val height=ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    companion object{
        const val STORY="story"
        private const val TAG = "DetailStoryFragment"
    }
}