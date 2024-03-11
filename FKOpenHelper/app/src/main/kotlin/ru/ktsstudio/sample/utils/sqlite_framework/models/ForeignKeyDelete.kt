package ru.ktsstudio.sample.utils.sqlite_framework.models

internal data class ForeignKeyDelete(
    val foreignKey: ForeignKey?,
    val primaryKeyName: String,
    val primaryKeyValue: String?,
    val useTable: String
)


internal data class ForeignKeyInsertUpdate(
    val foreignKey: ForeignKey,
    val value: String
)
