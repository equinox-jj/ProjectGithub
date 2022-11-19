package com.projectgithub.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.network.ApiConfig
import com.projectgithub.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel
    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)
        binding.toolbarDet.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
            setOnClickListener { findNavController().popBackStack() }
        }

        initObserver()
    }

    private fun initObserver() {
        val repository = Repository(ApiConfig.apiServices)
        val username = args.username

        detailViewModel = DetailViewModel(repository)
        detailViewModel.getUserByName(username)
        detailViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {

                }
                is Resources.Success -> {
                    response.data?.let { initView(it) }
                }
                is Resources.Error -> {

                }
            }
        }
    }

    private fun initView(data: DetailResponse) {
        binding.apply {
            ivAvatarDet.load(data.avatarUrl) {
                crossfade(800)
            }
            ivAvatarSmallDet.load(data.avatarUrl) {
                crossfade(800)
                transformations(CircleCropTransformation())
            }
            ctlDet.title = data.name
            tvUsernameDet.text = data.login
            tvCompanyDet.text = data.company.toString()
            tvLocationDet.text = data.location
            tvRepository.text = data.publicRepos.toString()
            tvFollowers.text = data.followers.toString()
            tvFollowers.text = data.following.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}