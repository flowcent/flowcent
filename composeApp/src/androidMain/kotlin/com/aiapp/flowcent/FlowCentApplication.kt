/*
 * Created by Saeedus Salehin on 24/5/25, 8:40 PM.
 */

package com.aiapp.flowcent

import android.app.Application
import com.aiapp.flowcent.di.initKoin
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import org.koin.android.ext.koin.androidContext

class FlowCentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FlowCentApplication)
        }
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = "161046411397-71cjns5cfjge3inb8gu9rep0b0nc2d8e.apps.googleusercontent.com"
            )
        )
    }
}