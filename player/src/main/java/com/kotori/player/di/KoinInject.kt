package com.kotori.player.di

import com.kotori.player.viewmodel.PlayerViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val modulePlayer = module {

    viewModel { PlayerViewModel() }
}