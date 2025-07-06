/*
 * Created by Saeedus Salehin on 24/5/25, 8:40 PM.
 */

package com.aiapp.flowcent

import android.app.Application
import com.aiapp.flowcent.core.auth.FirebaseAuthInitializer
import com.aiapp.flowcent.di.initKoin
import org.koin.android.ext.koin.androidContext

class FlowCentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FlowCentApplication)
        }
        FirebaseAuthInitializer.onApplicationStart()
    }
}