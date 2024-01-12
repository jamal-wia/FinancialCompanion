package com.financialcompanion.android.category.presentation.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.financialcompanion.android.R
import com.financialcompanion.android.category.domain.model.CategoryModel
import com.financialcompanion.android.category.presentation.categories.CategoriesViewState.DataState
import com.financialcompanion.android.category.presentation.categories.CategoriesViewState.EmptyState
import com.financialcompanion.android.category.presentation.categories.CategoriesViewState.LoadingState
import com.financialcompanion.android.core.presentation.mvvm.BaseMvvmFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : BaseMvvmFragment() {

    override val viewModel by viewModel<CategoriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(ctx).apply {
        setContent {
            val collectedState = viewModel.state.collectAsState()
            when (val state = collectedState.value) {
                LoadingState -> LoadingState()
                is DataState -> DataState(state)
                EmptyState -> EmptyState()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun LoadingState() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DataState(state: DataState = DataState.MockState) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.categories.size) { index ->
                    val category = state.categories[index]
                    Column {
                        CategoryItem(category = category)
                        if (index + 1 < state.categories.size) Divider()
                    }
                }
            }
        }
    }

    @Composable
    fun CategoryItem(category: CategoryModel) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(size = 46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = kotlin.run {
                            val imageId = (category.image as CategoryModel.Image.ImageId).value
                            painterResource(id = imageId)
                        },
                        contentDescription = "",
                        modifier = Modifier.size(size = 24.dp)
                    )
                }
                Text(text = category.name)
            }
        }
    }

    @Composable
    fun EmptyState() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_home_black),
                contentDescription = ""
            )
        }
    }
}
