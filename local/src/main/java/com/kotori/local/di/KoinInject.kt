package com.kotori.local.di

import org.koin.androidx.viewmodel.dsl.viewModel
import com.kotori.local.repository.LocalRepository
import com.kotori.local.viewmodel.LocalViewModel
import org.koin.dsl.module

val moduleLocal = module {

    single { LocalRepository(get()) }

    viewModel { LocalViewModel(get()) }
}