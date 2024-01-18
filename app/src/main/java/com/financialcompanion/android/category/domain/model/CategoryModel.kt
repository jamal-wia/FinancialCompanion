package com.financialcompanion.android.category.domain.model

import com.financialcompanion.android.category.data.db.CategoryEntity
import com.financialcompanion.android.category.data.db.SubcategoryEntity

data class CategoryModel(
    val id: Long,
    val name: String,
    val image: Image? = null,
    val subcategories: List<CategoryModel>? = null,
) {
    sealed interface Image {
        @JvmInline
        value class ImageId(val value: Int) : Image

        @JvmInline
        value class ImageLink(val value: String) : Image

        companion object {
            operator fun invoke(imageId: Int) = ImageId(imageId)

            operator fun invoke(imageLink: String) = ImageLink(imageLink)
        }
    }
}

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
