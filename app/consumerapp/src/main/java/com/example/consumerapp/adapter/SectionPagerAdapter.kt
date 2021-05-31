package com.example.consumerapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.consumerapp.ui.follower.FollowerFragment
import com.example.consumerapp.ui.following.FollowingFragment


class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity)  {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()

        }
        return fragment as Fragment
    }



}