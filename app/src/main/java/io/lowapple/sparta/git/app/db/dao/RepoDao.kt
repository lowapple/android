package io.lowapple.sparta.git.app.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.lowapple.sparta.git.app.db.entity.RepoEntity

@Dao
interface RepoDao {
    companion object {
        const val TABLE_NAME = "repos"
    }

    @Query("SELECT * FROM $TABLE_NAME")
    fun repositories(): LiveData<List<RepoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: List<RepoEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    fun clear()
}