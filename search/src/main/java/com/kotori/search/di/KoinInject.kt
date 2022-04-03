package com.kotori.search.di

import com.kotori.common.database.AppDatabase
import com.kotori.search.repository.SearchRepository
import com.kotori.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.property.inject


val moduleSearch = module {

    single { AppDatabase.instance }

    single { SearchRepository(get()) }

    viewModel { SearchViewModel(get()) }
}