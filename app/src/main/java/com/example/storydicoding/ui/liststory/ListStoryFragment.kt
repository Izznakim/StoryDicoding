package com.example.storydicoding.ui.liststory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storydicoding.R
import com.example.storydicoding.databinding.FragmentListStoryBinding

class ListStoryFragment : Fragment() {
    private var _binding:FragmentListStoryBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentListStoryBinding.inflate(inflater,container,false)
        return binding.root
    }
}