package org.aiapp.flowcent

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform