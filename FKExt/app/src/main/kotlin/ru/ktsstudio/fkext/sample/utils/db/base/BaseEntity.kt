package ru.ktsstudio.fkext.sample.utils.db.base

interface BaseEntity<T> {
    val tableName: String
    val remotePrimaryKeyName: String
    val remotePrimaryItem: T?
}
