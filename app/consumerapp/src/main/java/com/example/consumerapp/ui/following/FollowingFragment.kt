package com.example.consumerapp.ui.following

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.FollowingAdapter
import com.example.consumerapp.ui.detail.DetailActivity
import com.example.consumerapp.ui.follower.FollowerFragment
import com.example.consumerapp.data.Following
import com.example.consumerapp.data.Github
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import com.example.consumerapp.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    companion object{

        private  val TAG = FollowerFragment::class.java.simpleName
    }


    private lateinit var binding: FragmentFollowingBinding

    private val listGithubFollowing: ArrayList<Following> = arrayListOf()
    private lateinit var adapter : FollowingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowingAdapter(listGithubFollowing)
        val dataUser = activity?.intent?.getParcelableExtra<Github>(DetailActivity.EXTRA_DATA) as Github
        listGithubFollowing.clear()
        getGithubFollower(dataUser.username.toString())
    }

    private fun getGithubFollower(id: String) {
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {

                binding.progressBarFollowing.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val following = Following()
                        following.username = username
                        following.avatar = avatar
                        listGithubFollowing.add(following)

                    }
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBarFollowing.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
    private fun showRecyclerList() {
        binding.rvGithubFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvGithubFollowing.adapter = adapter


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentFollowingBinding.inflate(inflater,container,false)
        binding.rvGithubFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvGithubFollowing.adapter = FollowingAdapter(listGithubFollowing)
        return binding.root
    }



}