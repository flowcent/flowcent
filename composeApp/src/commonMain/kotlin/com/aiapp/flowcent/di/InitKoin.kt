/*
 * Created by Saeedus Salehin on 24/5/25, 8:34 PM.
 */

package com.aiapp.flowcent.di

import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin{
        config?.invoke(this)
        printLogger(Level.DEBUG)
        modules(sharedModule, platformModule)
    }
}