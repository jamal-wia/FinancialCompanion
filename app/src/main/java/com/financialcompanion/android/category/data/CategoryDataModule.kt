package com.financialcompanion.android.category.data

import androidx.room.Room
import com.financialcompanion.android.app.data.AppDatabase
import com.financialcompanion.android.category.data.repository.CategoryRepositoryImpl
import com.financialcompanion.android.category.domain.repository.CategoryRepository
import org.koin.dsl.bind
import org.koin.dsl.module

fun categoryDataModule() = module {

    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = AppDatabase.NAME
        ).build()
    }

    single {
        get<AppDatabase>()
            .categoryDao()
    }

    single { CategoryRepositoryImpl() } bind CategoryRepository::class

}
