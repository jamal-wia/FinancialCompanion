package com.financialcompanion.android.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.financialcompanion.android.category.data.db.CategoryDao
import com.financialcompanion.android.category.data.db.CategoryEntity
import com.financialcompanion.android.category.data.db.SubcategoryEntity

@Database(entities = [CategoryEntity::class, SubcategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val NAME = "app_database"
    }
}
