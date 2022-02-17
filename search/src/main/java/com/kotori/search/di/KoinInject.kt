package com.kotori.search.di

import com.kotori.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val moduleSearch = module {

    viewModel { SearchViewModel() }
}