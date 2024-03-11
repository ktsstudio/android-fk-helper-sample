package ru.ktsstudio.sample.utils.common

fun <T : Any> T?.requireNotNull(
    message: String? = null,
): T = message?.let {
    requireNotNull(this) { it }
} ?: requireNotNull(this)