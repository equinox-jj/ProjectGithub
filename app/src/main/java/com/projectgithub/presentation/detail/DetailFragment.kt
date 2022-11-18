package com.projectgithub.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.projectgithub.R
import com.projectgithub.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        initView()

    }

    private fun initView() {
        binding.apply {
            ivAvatarDet.load(args.userData.avatar) {
                crossfade(800)
                transformations(CircleCropTransformation())
            }
            tvUsernameDet.text = args.userData.username
            tvCompanyDet.text = args.userData.company
            tvLocationDet.text = args.userData.location
            tvRepository.text = args.userData.repository
            tvFollowers.text = args.userData.followers
            tvFollowing.text = args.userData.following
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}