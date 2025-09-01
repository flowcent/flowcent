package com.aiapp.flowcent.accounts.domain.model

enum class MemberRole(val displayName: String) {
    ADMIN("Admin"),
    MEMBER("Member");

    override fun toString(): String = displayName

    companion object {
        fun fromDisplayName(displayName: String): MemberRole {
            return entries.find { it.displayName.equals(displayName, ignoreCase = true) }
                ?: MEMBER
        }
    }
}