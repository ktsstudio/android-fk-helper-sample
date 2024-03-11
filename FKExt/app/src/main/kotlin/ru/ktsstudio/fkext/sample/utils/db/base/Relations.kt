package ru.ktsstudio.fkext.sample.utils.db.base

interface Relations<T> {
    val embeddedValue: T

    fun embeddedConstraintsEmpty(): Boolean
    fun getEmbeddedConstraints(): List<BaseEntity<*>>
}
