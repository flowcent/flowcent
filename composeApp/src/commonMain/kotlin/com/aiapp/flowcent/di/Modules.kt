/*
 * Created by Saeedus Salehin on 24/5/25, 8:33 PM.
 */

package com.aiapp.flowcent.di

import com.aiapp.flowcent.accounts.domain.repository.AccountRepositoryImpl
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.auth.domain.repository.AuthRepositoryImpl
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.core.domain.repository.ExpenseRepositoryImpl
import com.aiapp.flowcent.core.domain.repository.PrefRepositoryImpl
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.splash.SplashViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module
private val firestore: FirebaseFirestore = Firebase.firestore
private val auth: FirebaseAuth = Firebase.auth

val sharedModule = module {
    //ViewModels
    viewModel {
        SplashViewModel(
            prefRepository = PrefRepositoryImpl(get()),
        )
    }

    viewModel {
        HomeViewModel(
            expenseRepository = ExpenseRepositoryImpl(firestore),
            prefRepository = PrefRepositoryImpl(get()),
            authRepository = AuthRepositoryImpl(firestore)
        )
    }
    viewModel {
        ChatViewModel(
            flowCentAi = get(),
            expenseRepository = ExpenseRepositoryImpl(
                firestore
            ),
            prefRepository = PrefRepositoryImpl(get()),
            accountRepository = AccountRepositoryImpl(firestore)
        )
    }
    viewModel {
        AuthViewModel(
            authRepository = AuthRepositoryImpl(firestore, auth),
            prefRepository = PrefRepositoryImpl(get()),
        )
    }

    viewModel {
        AccountViewModel(
            accountRepository = AccountRepositoryImpl(firestore),
            prefRepository = PrefRepositoryImpl(get()),
            authRepository = AuthRepositoryImpl(firestore),
            contactFetcher = get()
        )
    }
}