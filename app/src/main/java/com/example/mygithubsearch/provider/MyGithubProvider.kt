package com.example.mygithubsearch.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.mygithubsearch.database.DatabaseContract
import com.example.mygithubsearch.database.FavoriteHelper

class MyGithubProvider : ContentProvider() {

    companion object {
        private const val GITHUB = 1
        private const val GITHUB_ID = 2
        private lateinit var favoriteHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(
                    DatabaseContract.AUTHORITY,
                    DatabaseContract.FavoriteColumns.TABLE_NAME, GITHUB)
            sUriMatcher.addURI(DatabaseContract.AUTHORITY, "${DatabaseContract.FavoriteColumns.TABLE_NAME}/#", GITHUB_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            GITHUB -> favoriteHelper.queryAll()
            GITHUB_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (GITHUB) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)

        return Uri.parse("${DatabaseContract.FavoriteColumns.CONTENT_URI}/$added")
    }


    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (GITHUB_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)

        return deleted
    }

    override fun update(
            uri: Uri,
            contentValues: ContentValues?,
            selection: String?,
            selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (GITHUB_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(),contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)

        return updated
    }
}