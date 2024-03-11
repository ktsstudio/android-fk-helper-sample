package ru.ktsstudio.sample.utils.sqlite_framework.models

internal data class ForeignKey(
    val foreignTable: String,
    val localTable: String,
    val foreignColumn: String,
    val localColumn: String,
)
