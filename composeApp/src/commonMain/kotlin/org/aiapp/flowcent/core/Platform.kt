package org.aiapp.flowcent.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform