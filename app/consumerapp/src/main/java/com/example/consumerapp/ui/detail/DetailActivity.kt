package com.example.consumerapp.ui.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.consumerapp.ui.setting.SettingActivity
import com.example.consumerapp.adapter.SectionPagerAdapter
import com.example.consumerapp.data.Github
import com.example.consumerapp.ui.favorite.FavoriteActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.consumerapp.R
import com.example.consumerapp.data.Favorite
import com.example.consumerapp.database.DatabaseContract
import com.example.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import android.net.Uri


class DetailActivity : AppCompatActivity() {
    private var favorite: Favorite? = null
    private lateinit var uriWithId: Uri
    companion object {
        const val EXTRA_DATA = "extra data"
        const val EXTRA_FAVORITE = "extra favorite"
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }



    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        viewPager()

        favorite = intent.getParcelableExtra(EXTRA_FAVORITE)

        if (favorite != null) {
            val tvName: TextView = findViewById(R.id.name)
            val tvUsername: TextView = findViewById(R.id.username)
            val tvCompany: TextView = findViewById(R.id.company)
            val tvLocation: TextView = findViewById(R.id.location)
            val ivAvatar: ImageView = findViewById(R.id.img_detail_profile)
            val tvFollowing: TextView = findViewById(R.id.following_count)
            val tvFollowers: TextView = findViewById(R.id.followers_count)
            val tvRepository: TextView = findViewById(R.id.repository_count)
            val detailFavoriteUser = intent.getParcelableExtra<Favorite>(EXTRA_DATA) as Favorite

            supportActionBar!!.title = detailFavoriteUser.name.toString()
            tvName.text = detailFavoriteUser.name.toString()
            tvUsername.text =  "@${detailFavoriteUser.username.toString()}"
            Glide.with(this)
                .load(detailFavoriteUser.avatar.toString())
                .fitCenter()
                .into(ivAvatar)
            tvCompany.text = detailFavoriteUser.company.toString()
            tvLocation.text = detailFavoriteUser.location.toString()
            tvFollowers.text = detailFavoriteUser.followers.toString()
            tvFollowing.text = detailFavoriteUser.following.toString()
            tvRepository.text = detailFavoriteUser.repository.toString()
        } else {

            val tvName: TextView = findViewById(R.id.name)
            val tvUsername: TextView = findViewById(R.id.username)
            val tvCompany: TextView = findViewById(R.id.company)
            val tvLocation: TextView = findViewById(R.id.location)
            val ivAvatar: ImageView = findViewById(R.id.img_detail_profile)
            val tvFollowing: TextView = findViewById(R.id.following_count)
            val tvFollowers: TextView = findViewById(R.id.followers_count)
            val tvRepository: TextView = findViewById(R.id.repository_count)
            val detailUser = intent.getParcelableExtra<Github>(EXTRA_DATA) as Github

            supportActionBar!!.title = detailUser.name.toString()
            tvName.text = detailUser.name.toString()
            tvUsername.text =  detailUser.username.toString()
            Glide.with(this)
                .load(detailUser.avatar.toString())
                .fitCenter()
                .into(ivAvatar)
            tvCompany.text = detailUser.company.toString()
            tvLocation.text = detailUser.location.toString()
            tvFollowers.text = detailUser.followers.toString()
            tvFollowing.text = detailUser.following.toString()
            tvRepository.text = detailUser.repository.toString()
        }


        val fab: ImageView = findViewById(R.id.fab)

        val isFavorite: Int = R.drawable.ic_baseline_favorite_24
        val isUnFavorite : Int = R.drawable.ic_baseline_favorite_border_24
        var favoriteStatus = false


        fab.setOnClickListener { view ->
            if (!favoriteStatus){

                val tvName: TextView = findViewById(R.id.name)
                val tvUsername: TextView = findViewById(R.id.username)
                val tvCompany: TextView = findViewById(R.id.company)
                val tvLocation: TextView = findViewById(R.id.location)
                val tvFollowing: TextView = findViewById(R.id.following_count)
                val tvFollowers: TextView = findViewById(R.id.followers_count)
                val tvRepository: TextView = findViewById(R.id.repository_count)

                val detailUser = intent.getParcelableExtra<Github>(EXTRA_DATA) as Github
                val favoriteUsername = tvUsername.text.toString()
                val favoriteName = tvName.text.toString()
                val favoriteCompany = tvCompany.text.toString()
                val favoriteLocation = tvLocation.text.toString()
                val favoriteAvatar = detailUser.avatar.toString()
                val favoriteFollowing = tvFollowing.text.toString()
                val favoriteFollowers = tvFollowers.text.toString()
                val favoriteRepository = tvRepository.text.toString()
                val favoriteData = "1"

                val values = ContentValues()
                values.put(DatabaseContract.FavoriteColumns.USERNAME,favoriteUsername)
                values.put(DatabaseContract.FavoriteColumns.NAME,favoriteName)
                values.put(DatabaseContract.FavoriteColumns.COMPANY,favoriteCompany)
                values.put(DatabaseContract.FavoriteColumns.LOCATION,favoriteLocation)
                values.put(DatabaseContract.FavoriteColumns.AVATAR,favoriteAvatar)
                values.put(DatabaseContract.FavoriteColumns.FOLLOWING,favoriteFollowing)
                values.put(DatabaseContract.FavoriteColumns.FOLLOWERS,favoriteFollowers)
                values.put(DatabaseContract.FavoriteColumns.REPOSITORY,favoriteRepository)
                values.put(DatabaseContract.FavoriteColumns.FAVORITE,favoriteData)
                contentResolver.insert(CONTENT_URI, values)

                favoriteStatus = true

                Snackbar.make(view, "The user has been added to the favorite list", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
                fab.setImageResource(isFavorite)

            }
            else{


                contentResolver.delete(uriWithId, null, null)
                favoriteStatus = false

                Snackbar.make(view, "The user has been deleted to the favorite list", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
                fab.setImageResource(isUnFavorite)


            }
        }

    }



    private fun viewPager() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val item = menu.findItem(R.id.search)
        item.isVisible = false
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            R.id.setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }


}