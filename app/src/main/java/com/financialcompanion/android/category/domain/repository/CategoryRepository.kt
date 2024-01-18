package com.financialcompanion.android.category.domain.repository

import com.financialcompanion.android.category.domain.model.CategoryModel

interface CategoryRepository {

    suspend fun getAllCategories(): List<CategoryModel>
}
