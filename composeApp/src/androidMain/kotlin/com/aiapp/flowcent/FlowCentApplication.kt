/*
 * Created by Saeedus Salehin on 24/5/25, 8:40 PM.
 */

package com.aiapp.flowcent

import android.app.Application
import com.aiapp.flowcent.auth.data.FirebaseAuthInitializer
import com.aiapp.flowcent.core.presentation.platform.initializeRevenueCat
import com.aiapp.flowcent.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext

class FlowCentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        initKoin {
            androidContext(this@FlowCentApplication)
        }
        FirebaseAuthInitializer.onApplicationStart()
        initializeRevenueCat(this)
    }
}