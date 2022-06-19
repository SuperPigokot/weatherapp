package com.example.myapplication

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "uvi")
data class Uvi (
    @PrimaryKey @ColumnInfo("uvi_id") val id: Int,
    @ColumnInfo("diapason_from") val diapason_from: Int?,
    @ColumnInfo("diapason_to") val diapason_to: Int?
)

@Dao
public interface uviDao {
    @Query("SELECT * FROM uvi")
    fun getAllUvi(): List<Uvi>

    @Query("SELECT * FROM uvi WHERE uvi_id like :uviId")
    fun getUvi(uviId: Int): List<Uvi>

    @Query("SELECT * FROM uvi WHERE :diapason BETWEEN diapason_from AND diapason_to")
    fun getUviByDiapason(diapason: Float): Int

    @Insert(entity = Uvi::class)
    fun insertUvi(ufIndex: List<Uvi>)

    @Update
    fun updateUvi(ufIndex: List<Uvi>)

    @Query("DELETE FROM uvi WHERE uvi_id like :uviId")
    fun deleteUvi(uviId: Int)
}
@Database(entities = arrayOf(Uvi::class), version = 2)
abstract class uviDatabase : RoomDatabase() {

    abstract fun ufIndexDao(): uviDao

    companion object {

        @Volatile private var INSTANCE: uviDatabase? = null

        fun getInstance(context: Context): uviDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        fun getDatabase(context: Context): uviDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                uviDatabase::class.java, "uvi")
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // insert the data on the IO Thread
                        ioThread {
                            getInstance(context).ufIndexDao().insertUvi(PREPOPULATE_DATA)
                        }
                    }
                })
                .fallbackToDestructiveMigration().allowMainThreadQueries().build()

        val PREPOPULATE_DATA = listOf(Uvi(1, 1, 2), Uvi(2, 2, 5), Uvi(3, 5, 7), Uvi(4, 7, 10), Uvi(5, 10, 99))
    }
}