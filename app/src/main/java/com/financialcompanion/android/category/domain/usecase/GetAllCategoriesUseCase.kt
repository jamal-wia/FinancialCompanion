package com.financialcompanion.android.category.domain.usecase

import com.financialcompanion.android.category.domain.model.CategoryModel
import com.financialcompanion.android.category.domain.repository.CategoryRepository

class GetAllCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<CategoryModel> {
        return categoryRepository.getAllCategories()
    }
}
