package io.lowapple.sparta.git.app.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.lowapple.sparta.git.app.db.dao.RepoDao

@Entity(tableName = RepoDao.TABLE_NAME)
data class RepoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val full_name: String,
    val description: String
)