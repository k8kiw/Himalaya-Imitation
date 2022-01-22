package com.kotori.home.di

import com.kotori.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * koin的依赖注入，需要注入的参数都在此处实现
 */
val moduleHome = module {

    // database注入repository


    // repository注入view model
    viewModel { HomeViewModel() }
}