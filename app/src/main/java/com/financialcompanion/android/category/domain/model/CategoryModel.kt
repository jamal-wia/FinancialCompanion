package com.financialcompanion.android.category.domain.model

import com.financialcompanion.android.category.data.db.CategoryEntity
import com.financialcompanion.android.category.data.db.SubcategoryEntity

data class CategoryModel(
    val id: Long,
    val name: String,
    val subcategories: List<CategoryModel>? = null,
)

fun CategoryModel.toEntity() = CategoryEntity(
    id = this.id,
    name = this.name
)

fun CategoryModel.toSubEntity(
    parentEntity: CategoryEntity
) = SubcategoryEntity(
    id = this.id,
    name = this.name,
    parentId = parentEntity.id
)