package com.financialcompanion.android.category.data.repository

import com.financialcompanion.android.category.data.db.CategoryDao
import com.financialcompanion.android.category.data.db.SubcategoryEntity
import com.financialcompanion.android.category.data.db.toDomain
import com.financialcompanion.android.category.data.pref.CategoryPref
import com.financialcompanion.android.category.data.util.CategoriesHolder
import com.financialcompanion.android.category.domain.model.CategoryModel
import com.financialcompanion.android.category.domain.model.toEntity
import com.financialcompanion.android.category.domain.model.toSubEntity
import com.financialcompanion.android.category.domain.repository.CategoryRepository
import com.financialcompanion.android.core.domain.di.injectApplicationScope
import com.financialcompanion.android.core.domain.di.injectIoDispatcher
import com.financialcompanion.android.core.domain.extension.genInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl : CategoryRepository {

    private val appScope by injectApplicationScope()
    private val ioDispatcher by injectIoDispatcher()

    private val categoryDao by genInject<CategoryDao>()

    init {
        if (!CategoryPref.created) {
            initDatabase()
        }
    }

    override suspend fun getAllCategories(): List<CategoryModel> = withContext(ioDispatcher) {
        initDatabaseJob?.join()

        val categories = categoryDao.getCategories()
        val result: List<CategoryModel> =
            if (categories.isNotEmpty()) ArrayList(categories.size)
            else emptyList()

        categories.forEach { parentCategory ->
            val subcategories = categoryDao.getSubcategoriesBy(parentCategory.id)
            (result as ArrayList).add(parentCategory.toDomain(subcategories.ifEmpty { null }))
        }

        return@withContext result
    }

    private var initDatabaseJob: Job? = null

    private fun initDatabase() {
        initDatabaseJob = appScope.launch(ioDispatcher) {
            delay(timeMillis = 1L)

            CategoriesHolder.categories.forEach { category ->
                val entity = category.toEntity()
                val subEntities: ArrayList<SubcategoryEntity>? =
                    if (!category.subcategories.isNullOrEmpty()) {
                        ArrayList<SubcategoryEntity>(category.subcategories.size)
                            .apply {
                                category.subcategories.forEach {
                                    add(it.toSubEntity(entity))
                                }
                            }
                    } else {
                        null
                    }

                categoryDao.insertCategory(entity)
                subEntities?.forEach { categoryDao.insertSubcategory(it) }
            }

            CategoryPref.created = true
            initDatabaseJob = null
        }
    }
}
