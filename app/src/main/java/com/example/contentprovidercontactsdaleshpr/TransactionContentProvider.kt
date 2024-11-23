package com.example.contentprovidercontactsdaleshpr


import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri


import android.content.ContentUris
import android.content.UriMatcher

import android.database.sqlite.SQLiteDatabase

class TransactionContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.contentprovidercontactsdaleshpr.TransactionContentProvider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/${CustomDatabaseHelper.TABLE_NAME}")
        const val TRANSACTIONS = 1
        const val TRANSACTION_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, CustomDatabaseHelper.TABLE_NAME, TRANSACTIONS)
            addURI(AUTHORITY, "${CustomDatabaseHelper.TABLE_NAME}/#", TRANSACTION_ID)
        }
    }

    private lateinit var database: SQLiteDatabase

    override fun onCreate(): Boolean {
        val dbHelper = CustomDatabaseHelper(context!!)
        database = dbHelper.writableDatabase
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = database.insert(CustomDatabaseHelper.TABLE_NAME, null, values)
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TRANSACTIONS -> database.query(CustomDatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            TRANSACTION_ID -> {
                val id = ContentUris.parseId(uri)
                database.query(CustomDatabaseHelper.TABLE_NAME, projection, "${CustomDatabaseHelper.COLUMN_ID}=?", arrayOf(id.toString()), null, null, sortOrder)
            }
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return when (uriMatcher.match(uri)) {
            TRANSACTION_ID -> {
                val id = ContentUris.parseId(uri)
                database.update(CustomDatabaseHelper.TABLE_NAME, values, "${CustomDatabaseHelper.COLUMN_ID}=?", arrayOf(id.toString()))
            }
            else -> 0
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (uriMatcher.match(uri)) {
            TRANSACTION_ID -> {
                val id = ContentUris.parseId(uri)
                database.delete(CustomDatabaseHelper.TABLE_NAME, "${CustomDatabaseHelper.COLUMN_ID}=?", arrayOf(id.toString()))
            }
            else -> 0
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            TRANSACTIONS -> "vnd.android.cursor.dir/$AUTHORITY.${CustomDatabaseHelper.TABLE_NAME}"
            TRANSACTION_ID -> "vnd.android.cursor.item/$AUTHORITY.${CustomDatabaseHelper.TABLE_NAME}"
            else -> null
        }
    }
}