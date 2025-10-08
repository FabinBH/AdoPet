package com.example.projetopi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projetopi.databinding.FragmentChooseBinding
import com.example.projetopi.ui.adapter.ViewPagerAdapter
import com.example.projetopi.R
import com.google.android.material.tabs.TabLayoutMediator

class ChooseFragment : Fragment() {
    private var _binding: FragmentChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initTabs()
    }

    /*private fun initTabs() {
        val pageAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = pageAdapter

        pageAdapter.addFragment(OngFragment(), R.string.ong_page)
        pageAdapter.addFragment(AdoptionFragment(), R.string.adoption_page)

        binding.viewPager.offscreenPageLimit = pageAdapter.itemCount

        TabLayoutMediator(binding.tabs, binding.viewPager) {
            tab, position -> tab.text = getString(pageAdapter.getTitle(position))
        }.attach()
    }*/
}