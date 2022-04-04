package com.kotori.home.di

import com.kotori.common.database.AppDatabase
import com.kotori.home.repository.HomeRepository
import com.kotori.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * koin的依赖注入，需要注入的参数都在此处实现
 */
val moduleHome = module {

    // koin的single是跨组件的，只用在一个地方声明
    //single { AppDatabase.instance }

    single { HomeRepository(get()) }

    // repository注入view model
    viewModel { HomeViewModel(get()) }
}