package com.aiapp.flowcent.core.platform

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dev.gitlive.firebase.auth.PhoneVerificationProvider
import kotlinx.coroutines.CompletableDeferred
import java.util.concurrent.TimeUnit

class AndroidPhoneVerificationProvider(
    override val activity: Activity
) : PhoneVerificationProvider {

    private var verificationId: String? = null
    private val codeDeferred = CompletableDeferred<String>()

    override val timeout: Long = 60
    override val unit: TimeUnit = TimeUnit.SECONDS

    private var triggerResendCallback: ((Unit) -> Unit)? = null

    override fun codeSent(triggerResend: (Unit) -> Unit) {
        triggerResendCallback = triggerResend
    }

    override suspend fun getVerificationCode(): String {
        return codeDeferred.await()
    }

    fun verifyPhoneNumber(
        phoneNumber: String,
        firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeout, unit)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(vid: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = vid
                    triggerResendCallback?.invoke(Unit)
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    val code = credential.smsCode
                    if (!code.isNullOrEmpty() && !codeDeferred.isCompleted) {
                        codeDeferred.complete(code)
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    if (!codeDeferred.isCompleted) {
                        codeDeferred.completeExceptionally(e)
                    }
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}