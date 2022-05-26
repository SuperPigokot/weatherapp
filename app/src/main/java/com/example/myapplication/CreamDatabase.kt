package com.example.myapplication

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity(tableName = "cream")
data class CreamDatabase (
    @PrimaryKey @ColumnInfo("cream_id") val id: Int,
    @ColumnInfo("uf_index_id") val uf_index_id: Int,
    @ColumnInfo("name") val name: String?
)