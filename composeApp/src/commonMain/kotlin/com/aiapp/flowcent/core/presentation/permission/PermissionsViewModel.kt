package com.aiapp.flowcent.core.presentation.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.contacts.CONTACTS
import dev.icerock.moko.permissions.microphone.RECORD_AUDIO
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PermissionsViewModel(
    private val controller: PermissionsController
) : ViewModel() {

    private val _state = MutableStateFlow(FCPermissionState())
    val state: StateFlow<FCPermissionState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    contactPermissionState = controller.getPermissionState(Permission.CONTACTS),
                    audioPermissionState = controller.getPermissionState(Permission.RECORD_AUDIO),
                )
            }
        }
    }

    fun provideOrRequestRecordAudioPermission() {
        Napier.e("Sohan provideOrRequestRecordAudioPermission")
        viewModelScope.launch {
            try {
                controller.providePermission(Permission.RECORD_AUDIO)
                _state.update {
                    it.copy(
                        audioPermissionState = PermissionState.Granted
                    )
                }
            } catch (e: DeniedAlwaysException) {
                _state.update {
                    it.copy(
                        audioPermissionState = PermissionState.DeniedAlways
                    )
                }
            } catch (e: DeniedException) {
                _state.update {
                    it.copy(
                        audioPermissionState = PermissionState.Denied
                    )
                }
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
                _state.update {
                    it.copy(
                        audioPermissionState = PermissionState.NotDetermined
                    )
                }
            }
        }
    }

    fun provideOrRequestContactPermission() {
        viewModelScope.launch {
            try {
                controller.providePermission(Permission.CONTACTS)
                _state.update {
                    it.copy(
                        contactPermissionState = PermissionState.Granted
                    )
                }
            } catch (e: DeniedAlwaysException) {
                _state.update {
                    it.copy(
                        contactPermissionState = PermissionState.DeniedAlways
                    )
                }
            } catch (e: DeniedException) {
                _state.update {
                    it.copy(
                        contactPermissionState = PermissionState.Denied
                    )
                }
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
                _state.update {
                    it.copy(
                        contactPermissionState = PermissionState.NotDetermined
                    )
                }
            }
        }
    }
}