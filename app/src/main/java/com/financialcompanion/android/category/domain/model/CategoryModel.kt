package com.financialcompanion.android.category.domain.model

data class CategoryModel(
    val id: Long,
    val name: String,
    val subcategories: List<CategoryModel>? = null,
)