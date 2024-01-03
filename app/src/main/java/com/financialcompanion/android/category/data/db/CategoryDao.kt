package com.financialcompanion.android.category.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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

    @Query("SELECT * FROM subcategories WHERE parent_id == :parentId")
    suspend fun getSubcategoriesBy(parentId: Long): List<SubcategoryEntity>

    @Insert
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    @Insert
    suspend fun insertSubcategory(subcategoryEntity: SubcategoryEntity)

    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity)

    @Update
    suspend fun updateSubcategory(subcategoryEntity: SubcategoryEntity)

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)

    @Delete
    suspend fun deleteSubcategory(subcategoryEntity: SubcategoryEntity)

}