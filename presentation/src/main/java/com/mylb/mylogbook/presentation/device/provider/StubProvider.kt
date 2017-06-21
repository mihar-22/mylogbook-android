package com.mylb.mylogbook.presentation.device.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class StubProvider : ContentProvider() {

    override fun onCreate() = true

    override fun getType(uri: Uri?) = null

    override fun query(
            uri: Uri?,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
    ) = null

    override fun insert(uri: Uri?, values: ContentValues?) = null

    override fun update(
            uri: Uri?,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
    ) = 0

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?) = 0

}
