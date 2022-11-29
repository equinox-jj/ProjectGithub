package com.projectgithub.presentation.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.projectgithub.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val USERNAME_INTENT = "username_i"
        const val NAME_INTENT = "name_i"
        const val LOCATION_INTENT = "location_i"
        const val REPOSITORY_INTENT = "repository_i"
        const val COMPANY_INTENT = "company_i"
        const val FOLLOWERS_INTENT = "followers_i"
        const val FOLLOWING_INTENT = "following_i"
        const val AVATAR_INTENT = "avatar_i"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.apply {
            ivAvatarDet.load(intent.getIntExtra(AVATAR_INTENT, 0)) {
                crossfade(800)
            }
            ivAvatarSmallDet.load(intent.getIntExtra(AVATAR_INTENT, 0)) {
                crossfade(800)
                transformations(CircleCropTransformation())
            }
            ctlDet.title = intent.getStringExtra(NAME_INTENT)
            tvUsernameDet.text = intent.getStringExtra(USERNAME_INTENT)
            tvCompanyDet.text = intent.getStringExtra(COMPANY_INTENT)
            tvLocationDet.text = intent.getStringExtra(LOCATION_INTENT)
            tvRepository.text = intent.getStringExtra(REPOSITORY_INTENT)
            tvFollowers.text = intent.getStringExtra(FOLLOWERS_INTENT)
            tvFollowing.text = intent.getStringExtra(FOLLOWING_INTENT)
        }
    }
}