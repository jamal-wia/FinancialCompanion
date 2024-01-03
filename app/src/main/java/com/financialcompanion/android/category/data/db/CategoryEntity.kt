package com.financialcompanion.android.category.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.financialcompanion.android.category.domain.model.CategoryModel

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
)

@Entity(
    tableName = "subcategories",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["parent_id"]
        )
    ]
)
data class SubcategoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "parent_id") val parentId: Long,
    @ColumnInfo(name = "name") val name: String,
)

fun List<CategoryEntity>.toDomain(
    subcategories: List<SubcategoryEntity>? = null
) = map { it.toDomain(subcategories) }

fun CategoryEntity.toDomain(
    subcategories: List<SubcategoryEntity>? = null
) = CategoryModel(
    id = this.id,
    name = this.name,
    subcategories = subcategories?.toDomain()
)

fun List<SubcategoryEntity>.toDomain() = map { it.toDomain() }

fun SubcategoryEntity.toDomain() = CategoryModel(
    id = this.id,
    name = this.name
)