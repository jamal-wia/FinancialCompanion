package com.financialcompanion.android.category.data

import androidx.room.Room
import org.koin.dsl.module

fun categoryDataModule() = module {

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .build()
    }

    single {
        get<AppDatabase>().categoryDao()
    }

    single { CategoryRepositoryImpl() }

}
