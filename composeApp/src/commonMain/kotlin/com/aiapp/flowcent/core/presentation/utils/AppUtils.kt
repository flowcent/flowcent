/*
 * Created by Saeedus Salehin on 4/8/25, 10:55 PM.
 */

package com.aiapp.flowcent.core.presentation.utils

fun Float.asCurrency(): String {
    val amount = (this * 100).toInt() / 100f  // Ensures 2 decimal precision
    val parts = amount.toString().split('.')
    val taka = parts[0]
    val poisha = parts.getOrNull(1)?.padEnd(2, '0')?.take(2) ?: "00"
    return "৳$taka.$poisha"
}
