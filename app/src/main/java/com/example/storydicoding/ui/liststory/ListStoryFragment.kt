package com.example.storydicoding.ui.liststory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storydicoding.R
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.databinding.FragmentListStoryBinding
import com.example.storydicoding.ui.adapter.StoryAdapter
import com.example.storydicoding.ui.addstory.AddStoryFragment
import com.google.android.material.snackbar.Snackbar

class ListStoryFragment : Fragment() {
    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private val listStoryViewModel by viewModels<ListStoryViewModel>()

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArguments()
        setupAdapter()
        setupViewModel()
        setupActionView()
    }

    private fun setupArguments() {
        if (arguments != null) {
            user = arguments?.getParcelable(USER)
        }
    }

    private fun setupAdapter() {
        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(context)
            rvStory.setHasFixedSize(true)
        }
    }

    private fun setupViewModel() {
        listStoryViewModel.getListStories("Bearer ${user?.token}").observe(viewLifecycleOwner) {
            binding.rvStory.adapter = setStories(it)
        }

        listStoryViewModel.error.observe(viewLifecycleOwner) {
            val message = if (!it) {
                getString(R.string.message_success_load_list)
            } else {
                getString(R.string.message_fail_load_list)
            }
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }

        listStoryViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setupActionView() {
        binding.fabCreateStory.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val addStoryFragment = AddStoryFragment()
            val bundle = Bundle()

            bundle.putString(AddStoryFragment.TOKEN, user?.token)
            addStoryFragment.arguments = bundle
            addStoryFragment.show(fragmentManager, AddStoryFragment::class.java.simpleName)
        }
    }

    private fun setStories(stories: List<ListStoryItem>): StoryAdapter {
        val listStory = ArrayList<ListStoryItem>()
        if (stories.isNotEmpty()) {
            stories.forEach {
                listStory.add(it)
            }
        } else {
            Snackbar.make(requireView(),getString(R.string.empty_data),Snackbar.LENGTH_LONG).show()
        }
        return StoryAdapter(listStory)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val USER = "user"
    }
}