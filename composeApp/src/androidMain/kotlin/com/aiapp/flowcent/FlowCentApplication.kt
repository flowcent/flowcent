/*
 * Created by Saeedus Salehin on 24/5/25, 8:40 PM.
 */

package com.aiapp.flowcent

import android.app.Application
import com.aiapp.flowcent.core.auth.FirebaseAuthInitializer
import com.aiapp.flowcent.di.initKoin
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaClient
import com.google.android.recaptcha.RecaptchaException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext

class FlowCentApplication : Application() {
    private lateinit var recaptchaClient: RecaptchaClient
    private val recaptchaScope = CoroutineScope(Dispatchers.IO)
    override fun onCreate() {
        super.onCreate()
        initializeRecaptchaClient()
        initKoin {
            androidContext(this@FlowCentApplication)
        }
        FirebaseAuthInitializer.onApplicationStart()
    }


    private fun initializeRecaptchaClient() {
        recaptchaScope.launch {
            try {
                recaptchaClient = Recaptcha.fetchClient(this@FlowCentApplication, "6LcAc4wrAAAAACDkgzMc0iC8fL7qBAyvGhh_cx7O")
            } catch(e: RecaptchaException) {
                // Handle errors ...
                // See "Handle errors" section
                println("Sohan RecaptchaException Error: ${e.errorMessage}")
            }
        }
    }
}