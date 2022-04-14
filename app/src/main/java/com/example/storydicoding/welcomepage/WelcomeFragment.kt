package com.example.storydicoding.welcomepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.storydicoding.R
import com.example.storydicoding.databinding.FragmentWelcomeBinding
import com.example.storydicoding.welcomepage.login.LoginFragment
import com.example.storydicoding.welcomepage.signup.SignupFragment

class WelcomeFragment : Fragment() {
    private var _binding:FragmentWelcomeBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentWelcomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
    }

    private fun setupAction(){
        val mFragmentManager=parentFragmentManager
        binding.apply {
            btnLogin.setOnClickListener {
                mFragmentManager.commit {
                    replace(R.id.fragment_welcome_container, LoginFragment(),LoginFragment::class.java.simpleName)
                    addToBackStack(null)
                }
            }
            btnSignup.setOnClickListener {
                mFragmentManager.commit {
                    replace(R.id.fragment_welcome_container, SignupFragment(),SignupFragment::class.java.simpleName)
                    addToBackStack(null)
                }
            }
        }
    }
}