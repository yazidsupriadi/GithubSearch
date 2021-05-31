package com.example.userconsumerapp.helper

import android.database.Cursor
import com.example.userconsumerapp.database.DatabaseContract
import com.example.userconsumerapp.data.Github

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Github> {
        val favoriteList = ArrayList<Github>()
        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))


                favoriteList.add(Github(username, name, avatar, company, location, followers, following, repository))
            }
        }
        return favoriteList
    }
}