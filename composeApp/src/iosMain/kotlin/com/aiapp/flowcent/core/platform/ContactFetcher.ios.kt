package com.aiapp.flowcent.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Contacts.CNContactFetchRequest
import platform.Contacts.CNContactStore
import platform.Contacts.CNLabeledValue
import platform.Contacts.CNPhoneNumber

actual class ContactFetcher {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun fetchContacts(): List<DeviceContact> {
        val store = CNContactStore()
        val keysToFetch = listOf("givenName", "familyName", "phoneNumbers")
        val request = CNContactFetchRequest(keysToFetch)
        val contacts = mutableListOf<DeviceContact>()

        store.enumerateContactsWithFetchRequest(request, error = null) { contact, _ ->
            val name = "${contact?.givenName} ${contact?.familyName}".trim()
            contact?.phoneNumbers?.forEach { labeledValue ->
                val phoneNumber = (labeledValue as? CNLabeledValue)?.value as? CNPhoneNumber
                phoneNumber?.stringValue?.let {
                    // IMPORTANT: Normalize the phone number here!
                    contacts.add(DeviceContact(name, normalizePhoneNumber(it)))
                }
            }
        }
        return contacts
    }

    private fun normalizePhoneNumber(number: String): String {
        return number.replace(Regex("[^+\\d]"), "")
    }
}