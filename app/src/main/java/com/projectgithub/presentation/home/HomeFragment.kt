package com.projectgithub.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.projectgithub.R
import com.projectgithub.data.model.UserData
import com.projectgithub.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecycler()
    }

    private fun initRecycler() {
        binding.apply {
            homeAdapter = HomeAdapter(userDataList)
            rvUserList.adapter = homeAdapter
            rvUserList.setHasFixedSize(true)
            userData.addAll(userDataList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}