package com.example.myapplication

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity(tableName = "uf_index")
data class ufIndex (
    @PrimaryKey @ColumnInfo("uf_index_id") val id: Int,
    @ColumnInfo("diapason") val diapason: String?
)