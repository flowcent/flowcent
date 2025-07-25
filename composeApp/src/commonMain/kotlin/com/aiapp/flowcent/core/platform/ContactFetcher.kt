package com.aiapp.flowcent.core.platform


data class DeviceContact(val name: String?, val phoneNumber: String)
expect class ContactFetcher {
    suspend fun fetchContacts(): List<DeviceContact>
}