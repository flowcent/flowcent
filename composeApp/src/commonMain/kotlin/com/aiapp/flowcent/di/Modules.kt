/*
 * Created by Saeedus Salehin on 24/5/25, 8:33 PM.
 */

package com.aiapp.flowcent.di

import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.home.presentation.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    //ViewModels
    viewModel { HomeViewModel() }
    viewModel { ChatViewModel(flowCentAi = get()) }
}