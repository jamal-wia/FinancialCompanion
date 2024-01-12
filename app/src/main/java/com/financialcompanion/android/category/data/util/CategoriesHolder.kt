package com.financialcompanion.android.category.data.util

import com.financialcompanion.android.R
import com.financialcompanion.android.category.domain.model.CategoryModel

object CategoriesHolder {
    val categories = listOf(
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
        CategoryModel(
            id = 4L,
            name = "Коммунальные платежи",
            image = CategoryModel.Image(R.drawable.ic_water_tap)
        ),
        CategoryModel(
            id = 5L,
            name = "Развлечения",
            image = CategoryModel.Image(R.drawable.ic_popper)
        ),
        CategoryModel(
            id = 6L,
            name = "Здоровье",
            image = CategoryModel.Image(R.drawable.ic_cardiogram)
        ),
        CategoryModel(
            id = 7L, name = "Спорт",
            image = CategoryModel.Image(R.drawable.ic_dumbbell)
        ),
        CategoryModel(
            id = 8L,
            name = "Образование",
            image = CategoryModel.Image(R.drawable.ic_graduation)
        ),
        CategoryModel(
            id = 9L,
            name = "Одежда",
            image = CategoryModel.Image(R.drawable.ic_tshirt)
        ),
        CategoryModel(
            id = 10L,
            name = "Красота",
            image = CategoryModel.Image(R.drawable.ic_woman)
        ),
        CategoryModel(
            id = 11L,
            name = "Путешествия",
            image = CategoryModel.Image(R.drawable.ic_travel)
        ),
        CategoryModel(
            id = 12L,
            name = "Подарки",
            image = CategoryModel.Image(R.drawable.ic_gift)
        ),
        CategoryModel(
            id = 13L,
            name = "Рестораны",
            image = CategoryModel.Image(R.drawable.ic_restaurant)
        ),
        CategoryModel(
            id = 18L,
            name = "Домашние животные",
            image = CategoryModel.Image(R.drawable.ic_pawprint)
        ),
        CategoryModel(
            id = 19L,
            name = "Дети",
            image = CategoryModel.Image(R.drawable.ic_kids)
        ),
        CategoryModel(
            id = 22L,
            name = "Налоги",
            image = CategoryModel.Image(R.drawable.ic_percentage)
        ),
        CategoryModel(
            id = 24L,
            name = "Благотворительность",
            image = CategoryModel.Image(R.drawable.ic_charity)
        ),
        CategoryModel(
            id = 25L,
            name = "Другое",
            image = CategoryModel.Image(R.drawable.ic_restaurant)
        )
    )
}