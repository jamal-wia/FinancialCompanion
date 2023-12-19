package com.financialcompanion.android.category.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM subcategories")
    suspend fun getSubcategories(): List<SubcategoryEntity>

    @Query("SELECT * FROM categories WHERE id == :categoryId")
    suspend fun getCategory(categoryId: Long): CategoryEntity

    @Query("SELECT * FROM subcategories WHERE id == :subcategoryId")
    suspend fun getSubcategory(subcategoryId: Long): SubcategoryEntity

}