package com.aiapp.flowcent.core.presentation.platform

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract

actual class ContactFetcher(private val contentResolver: ContentResolver) {
    @SuppressLint("Range")
    actual suspend fun fetchContacts(): List<DeviceContact> {
        val contacts = mutableListOf<DeviceContact>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                // IMPORTANT: Normalize the phone number here!
                contacts.add(DeviceContact(name, normalizePhoneNumber(number)))
            }
        }
        return contacts
    }
}

fun normalizePhoneNumber(number: String): String {
    return number.replace(Regex("[^+\\d]"), "") // Removes spaces, dashes, etc.
}