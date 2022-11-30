package com.projectgithub.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.projectgithub.R
import com.projectgithub.data.model.UserData
import com.projectgithub.databinding.ActivityHomeBinding
import com.projectgithub.presentation.detail.DetailActivity
import com.projectgithub.presentation.detail.DetailActivity.Companion.AVATAR_INTENT
import com.projectgithub.presentation.detail.DetailActivity.Companion.COMPANY_INTENT
import com.projectgithub.presentation.detail.DetailActivity.Companion.FOLLOWERS_INTENT
import com.projectgithub.presentation.detail.DetailActivity.Companion.FOLLOWING_INTENT
import com.projectgithub.presentation.detail.DetailActivity.Companion.LOCATION_INTENT
import com.projectgithub.presentation.detail.DetailActivity.Companion.NAME_INTENT
import com.projectgithub.presentation.detail.DetailActivity.Companion.REPOSITORY_INTENT
import com.projectgithub.presentation.detail.DetailActivity.Companion.USERNAME_INTENT

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var homeAdapter: HomeAdapter
    private val userData = ArrayList<UserData>()

    private val userDataList: ArrayList<UserData>
        get() {
            val username = resources.getStringArray(R.array.username)
            val name = resources.getStringArray(R.array.name)
            val location = resources.getStringArray(R.array.location)
            val repository = resources.getStringArray(R.array.repository)
            val company = resources.getStringArray(R.array.company)
            val followers = resources.getStringArray(R.array.followers)
            val following = resources.getStringArray(R.array.following)
            val avatar = resources.obtainTypedArray(R.array.avatar)

            val userData = ArrayList<UserData>()
            username.forEachIndexed { index, _ ->
                val dataResult = UserData(
                    username[index],
                    name[index],
                    location[index],
                    repository[index],
                    company[index],
                    followers[index],
                    following[index],
                    avatar.getResourceId(index, 0),
                )
                userData.add(dataResult)
            }
            return userData
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()
        setupListener()
    }

    private fun setupListener() {
        homeAdapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClick(data: UserData) {
                val intent = Intent(this@HomeActivity, DetailActivity::class.java).apply {
                    putExtra(USERNAME_INTENT, data.username)
                    putExtra(NAME_INTENT, data.name)
                    putExtra(LOCATION_INTENT, data.location)
                    putExtra(REPOSITORY_INTENT, data.repository)
                    putExtra(COMPANY_INTENT, data.company)
                    putExtra(FOLLOWERS_INTENT, data.followers)
                    putExtra(FOLLOWING_INTENT, data.following)
                    putExtra(AVATAR_INTENT, data.avatar)
                }
                startActivity(intent)
            }
        })
    }

    private fun initRecycler() {
        binding.apply {
            homeAdapter = HomeAdapter(userDataList)
            rvUserList.adapter = homeAdapter
            rvUserList.setHasFixedSize(true)
            userData.addAll(userDataList)
        }
    }
}