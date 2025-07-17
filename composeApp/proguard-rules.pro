# proguard-rules.pro

# =================================================================================
# --- General Kotlin & Coroutines ---
# =================================================================================
# Keep suspend functions and their continuations
-keepclasseswithmembernames class * {
    @kotlin.jvm.JvmName <methods>;
}
-keepclassmembers class kotlin.coroutines.Continuation {
    <fields>;
    <methods>;
}
-keep class kotlinx.coroutines.internal.** { *; }
-dontwarn kotlin.Unit

# =================================================================================
# --- Jetpack Compose ---
# =================================================================================
# Keep all Composable functions and their related runtime classes.
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class androidx.compose.runtime.** { *; }
-keepclassmembers class **.R$* {
    public static <fields>;
}

# =================================================================================
# --- Ktor Client ---
# =================================================================================
# Ktor uses reflection extensively for its engines and plugins.
# These rules are broad to ensure functionality.
-dontwarn io.ktor.**
-keep class io.ktor.** { *; }
-keepnames class io.ktor.**

# Explicitly keep default engines if you use them
-keep class io.ktor.client.engine.cio.** { *; }
-keep class io.ktor.client.engine.okhttp.** { *; }

# Keep all client plugins and features
-keep class io.ktor.client.plugins.** { *; }
-keep class io.ktor.client.features.** { *; } # For older Ktor versions

# =================================================================================
# --- Kotlinx Serialization ---
# =================================================================================
# Keep classes annotated with @Serializable and their generated serializer() methods.
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keepclassmembers class * {
    public static ** serializer(...);
}

# Keep the companion objects and generated serializers
-keep class *$$serializer { *; }
-keep class *$$Companion { *; }

# Keep the core serialization library classes
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.internal.* {
    *;
}

# =================================================================================
# --- Data/Model Classes ---
# =================================================================================
# It's a good practice to keep all your data/model classes.
# Replace 'com.yourcompany.project.shared.data' with the actual package name
# where your data models are located in your shared module.
-keep class com.yourcompany.project.shared.data.** { *; }