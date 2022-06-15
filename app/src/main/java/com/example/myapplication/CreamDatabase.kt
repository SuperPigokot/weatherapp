package com.example.myapplication

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "cream")
data class Cream (
    @PrimaryKey @ColumnInfo("cream_id") val id: Int,
    @ColumnInfo("uf_index_id") val uf_index_id: Int,
    @ColumnInfo("name") val name: String?
)

@Dao
public interface CreamDao {
    @Query("SELECT * FROM cream")
    fun getAllCreams(): List<Cream>

    @Query("SELECT * FROM cream WHERE uf_index_id = :creamId")
    fun getCream(creamId: Int): List<Cream>

    @Insert(entity = Cream::class)
    fun insertCream(creamDatabase: List<Cream>)

    @Update
    fun updateCream(creamDatabase: List<Cream>)

    @Query("DELETE FROM cream WHERE cream_id like :creamId")
    fun deleteCream(creamId: Int)

}

@Database(entities = arrayOf(Cream::class), version = 1)
abstract class CreamDatabase : RoomDatabase() {

    abstract fun dataDao(): CreamDao
    companion object {

        @Volatile
        private var INSTANCE: CreamDatabase? = null
        fun getInstance(context: Context): CreamDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        fun getDatabase(context: Context): CreamDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CreamDatabase::class.java, "cream")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                            getInstance(context).dataDao().insertCream(PREPOPULATE_DATA)
                        }
                    }
                })
                .fallbackToDestructiveMigration().allowMainThreadQueries().build()
        val PREPOPULATE_DATA = listOf(Cream(1, 1, "Kristi Home Acnet Крем-гель дневной SPF10"), Cream(2, 1, "Боро плюс Perfect Derma Rich Moisturising Day Face Cream SPF10"),
        Cream(3, 1, "The U Натуральный увлажняющий крем для лица Your megapolis cream SPF10"), Cream(4, 1, "Clarins creme Solaire Confort SPF20"),
            Cream(5, 1, "Avene Hydrance OPTIMALE UV Legere SPF20"), Cream(6, 1, "Shiseido Essential Energy SPF20"),
            Cream(7, 2, "Крем Соллио солнцезащитный SPF30"), Cream(8, 2, "La Roche Posay Anthelios SPF30"),Cream(9, 2, "Cetaphil Dermacontrol Себорегулирующий увлажняющий крем SPF30"),
            Cream(10, 2, "Garnier Ambre Solaire для лица SPF30"), Cream(11, 2, "ARAVIA MULTI PROTECTION SUN CREAM SPF30"),
            Cream(12, 3, "Floralis Здоровье Солнце SPF50"), Cream(13, 3, "Bioderma Photoderm MAX SPF50+ Aquafluide tres haute protection"),Cream(14, 3, "Clarins Creme Solaire Toucher Sec Visage SPF50"),
            Cream(15, 4, "Солнцезащитный крем Биокон Sun Marina Kids для особо чувствительных участков лица и тела SPF70"),Cream(16, 4, "Kora Усиленная защита SPF50+ для лица и тела"), Cream(17, 4, "Eucerin солнцезащитный гель-крем для проблемной кожи лица SPF50"),
            Cream(18, 5, "Солнцезащитный крем Floresan 'Полный блок' SPF100"))
    }
}