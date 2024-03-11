package ru.ktsstudio.sample.data.database.converter

import androidx.room.TypeConverter
import ru.ktsstudio.sample.data.database.models.TransactionType

class TransactionTypeConverter {

    @TypeConverter
    fun getTransactionType(ordinal: Int): TransactionType = TransactionType.values()[ordinal]

    @TypeConverter
    fun getTransactionTypeInt(type: TransactionType): Int = type.ordinal
}