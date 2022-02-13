package com.kotori.player.di

import com.kotori.player.viewmodel.PlayerViewModel
import com.kotori.player.viewmodel.SmallPlayerViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val modulePlayer = module {

    viewModel { PlayerViewModel() }

    viewModel { SmallPlayerViewModel() }
}