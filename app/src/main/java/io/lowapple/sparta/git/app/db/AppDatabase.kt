package io.lowapple.sparta.git.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.lowapple.sparta.git.app.BuildConfig
import io.lowapple.sparta.git.app.db.dao.RepoDao
import io.lowapple.sparta.git.app.db.entity.RepoEntity

@Database(
    entities = [RepoEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRepoDao(): RepoDao

    companion object {
        private var instance: AppDatabase? = null
        fun getAppDatabase(context: Context, inMemoryDatabase: Boolean = false): AppDatabase {
            if (instance != null)
                return instance!!

            instance = if (inMemoryDatabase) {
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            } else {
                Room.databaseBuilder(context, AppDatabase::class.java, BuildConfig.APPLICATION_ID)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}