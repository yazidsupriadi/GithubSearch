package com.example.userconsumerapp.ui.favorite

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userconsumerapp.R
import com.example.userconsumerapp.databinding.ActivityFavoriteBinding
import com.example.userconsumerapp.adapter.GithubAdapter
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.userconsumerapp.data.Github
import com.example.userconsumerapp.ui.detail.DetailActivity
import com.example.userconsumerapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: GithubAdapter
    private var listGithubFavorite: ArrayList<Github> = arrayListOf()
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"
        binding.recycleViewFavorite.layoutManager = LinearLayoutManager(this)
        binding.recycleViewFavorite.setHasFixedSize(true)
        adapter = GithubAdapter(listGithubFavorite)
        binding.recycleViewFavorite.adapter = adapter
        adapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Github) {
                val moveIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_DATA,data)
                startActivity(moveIntent)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBarFavorite.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBarFavorite.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            adapter.setListGithub(favorites)

        }
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }

}