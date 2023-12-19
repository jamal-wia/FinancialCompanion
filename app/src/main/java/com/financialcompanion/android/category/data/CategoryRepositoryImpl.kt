package com.financialcompanion.android.category.data

import com.financialcompanion.android.core.domain.extension.genInject

class CategoryRepositoryImpl {

    val categoryDao by genInject<CategoryDao>()


}