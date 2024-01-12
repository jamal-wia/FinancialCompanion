package com.financialcompanion.android.category.presentation.categories

import com.financialcompanion.android.R
import com.financialcompanion.android.category.domain.model.CategoryModel

sealed class CategoriesViewState {

    object LoadingState : CategoriesViewState()

    class DataState(val categories: List<CategoryModel>) : CategoriesViewState() {

        companion object {
            val MockState = DataState(
                categories = listOf(
                    CategoryModel(
                        id = 1L,
                        name = "Продукты",
                        image = CategoryModel.Image(R.drawable.ic_dinner)
                    ),
                    CategoryModel(
                        id = 2L,
                        name = "Транспорт",
                        image = CategoryModel.Image(R.drawable.ic_car)
                    ),
                    CategoryModel(
                        id = 3L,
                        name = "Жилье",
                        image = CategoryModel.Image(R.drawable.ic_home)
                    ),
                )
            )
        }
    }

    object EmptyState : CategoriesViewState()

}
