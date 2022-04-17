package com.example.storydicoding.ui.liststory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storydicoding.data.model.User
import com.example.storydicoding.data.response.ListStoryItem
import com.example.storydicoding.databinding.FragmentListStoryBinding
import com.example.storydicoding.ui.adapter.StoryAdapter
import com.example.storydicoding.ui.addstory.AddStoryFragment
import com.example.storydicoding.ui.detailstory.DetailStoryFragment

class ListStoryFragment : Fragment() {
    private var _binding: FragmentListStoryBinding?=null
    private val binding get() = _binding!!
    private val listStoryViewModel by viewModels<ListStoryViewModel>()

    private var user:User?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentListStoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArguments()
        setupAdapter()
        setupRecycleView()
        setupActionView()
    }

    private fun setupArguments(){
        if (arguments!=null) {
            user = arguments?.getParcelable(USER)
        }
    }

    private fun setupAdapter(){
        binding.apply {
            rvStory.layoutManager=LinearLayoutManager(context)
            rvStory.setHasFixedSize(true)
        }
    }

    private fun setupRecycleView(){
        listStoryViewModel.getListStories("Bearer ${user?.token}").observe(viewLifecycleOwner){
            binding.rvStory.adapter=setStories(it)
        }
    }

    private fun setupActionView(){
        binding.fabCreateStory.setOnClickListener {
            val fragmentManager=parentFragmentManager
            val addStoryFragment=AddStoryFragment()
            val bundle= Bundle()

            bundle.putString(AddStoryFragment.TOKEN,user?.token)
            addStoryFragment.arguments=bundle
            addStoryFragment.show(fragmentManager, AddStoryFragment::class.java.simpleName)
        }
    }

    private fun setStories(stories:List<ListStoryItem>):StoryAdapter{
        val listStory=ArrayList<ListStoryItem>()
        if (stories.isNotEmpty()){
            stories.forEach{
                listStory.add(it)
            }
        }else{
            Log.d(TAG, "setStories: Maaf Data Kosong!")
        }
        return StoryAdapter(listStory)
    }

    companion object{
        const val USER="user"
        private const val TAG = "ListStoryFragment"
    }
}