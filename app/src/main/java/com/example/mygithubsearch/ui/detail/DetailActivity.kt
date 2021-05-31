package com.example.mygithubsearch.ui.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubsearch.ui.setting.SettingActivity
import com.example.mygithubsearch.adapter.SectionPagerAdapter
import com.example.mygithubsearch.data.Github
import com.example.mygithubsearch.ui.favorite.FavoriteActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.mygithubsearch.R
import com.example.mygithubsearch.database.FavoriteHelper
import com.example.mygithubsearch.database.DatabaseContract
import com.example.mygithubsearch.helper.MappingHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {
    private var favorite: Github? = null
    private lateinit var favoriteHelper: FavoriteHelper
    private var favoriteStatus = false

    companion object {
        const val EXTRA_DATA = "extra data"
        private val TAB_TITLES = intArrayOf(
                R.string.follower,
                R.string.following
        )

        private const val TAG = "UserDetailActivity Status"
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        viewPager()

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        favorite = intent.getParcelableExtra<Github>(EXTRA_DATA) as Github

        val fab: ImageView = findViewById(R.id.fab)
        val isFavorite: Int = R.drawable.ic_baseline_favorite_24
        val isUnFavorite: Int = R.drawable.ic_baseline_favorite_border_24

        setDetailData(favorite!!.username.toString())

        val checkfavorite = checkFavoriteUser(favorite!!.username.toString())

        fab.setOnClickListener { view ->
            if (checkfavorite) {

                val tvUsername: TextView = findViewById(R.id.username)
                val favoriteUsername = tvUsername.text.toString()
                favoriteHelper.deleteById(favoriteUsername)

                favoriteStatus = false
                Snackbar.make(view, "The user has been deleted to the favorite list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                fab.setImageResource(isUnFavorite)

            } else {
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

                favoriteHelper.open()
                val values = ContentValues()
                values.put(DatabaseContract.FavoriteColumns.USERNAME, favoriteUsername)
                values.put(DatabaseContract.FavoriteColumns.NAME, favoriteName)
                values.put(DatabaseContract.FavoriteColumns.COMPANY, favoriteCompany)
                values.put(DatabaseContract.FavoriteColumns.LOCATION, favoriteLocation)
                values.put(DatabaseContract.FavoriteColumns.AVATAR, favoriteAvatar)
                values.put(DatabaseContract.FavoriteColumns.FOLLOWING, favoriteFollowing)
                values.put(DatabaseContract.FavoriteColumns.FOLLOWERS, favoriteFollowers)
                values.put(DatabaseContract.FavoriteColumns.REPOSITORY, favoriteRepository)
                values.put(DatabaseContract.FavoriteColumns.FAVORITE, favoriteData)
                favoriteHelper.insert(values)

                favoriteStatus = true
                Snackbar.make(view, "The user has been added to the favorite list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                fab.setImageResource(isFavorite)


            }
        }
        if (checkfavorite){
            fab.setImageResource(isFavorite)
        }else{
            fab.setImageResource(isUnFavorite)
        }
    }

    private fun checkFavoriteUser(username: String): Boolean {
        return runBlocking {
            val deferred = async {
                val result = favoriteHelper.queryById(username)
                MappingHelper.mapCursorToArrayList(result)
            }
            deferred.await().isNotEmpty()
        }
    }


    private fun setDetailData(id: String) {
        val tvName: TextView = findViewById(R.id.name)
        val tvUsername: TextView = findViewById(R.id.username)
        val tvCompany: TextView = findViewById(R.id.company)
        val tvLocation: TextView = findViewById(R.id.location)
        val ivAvatar: ImageView = findViewById(R.id.img_detail_profile)
        val tvFollowing: TextView = findViewById(R.id.following_count)
        val tvFollowers: TextView = findViewById(R.id.followers_count)
        val tvRepository: TextView = findViewById(R.id.repository_count)

        val client = AsyncHttpClient()
        val apiKey = "token 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"
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username: String = jsonObject.getString("login").toString()
                    val name: String = jsonObject.getString("name").toString()
                    val avatar: String = jsonObject.getString("avatar_url").toString()
                    val company: String = jsonObject.getString("company").toString()
                    val location: String = jsonObject.getString("location").toString()
                    val following: String = jsonObject.getString("following").toString()
                    val followers: String = jsonObject.getString("followers").toString()
                    val repository: String = jsonObject.getString("public_repos").toString()
                    tvName.text = name
                    tvUsername.text = username
                    Glide.with(this@DetailActivity)
                            .load(avatar)
                            .fitCenter()
                            .into(ivAvatar)
                    tvCompany.text = company
                    tvLocation.text = location
                    tvFollowers.text = following
                    tvFollowing.text = followers
                    tvRepository.text = repository

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " DETAIL"}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
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