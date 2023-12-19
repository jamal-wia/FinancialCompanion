package com.financialcompanion.android.category.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class, SubcategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}