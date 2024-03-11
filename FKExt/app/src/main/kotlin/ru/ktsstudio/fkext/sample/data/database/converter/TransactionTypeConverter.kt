package ru.ktsstudio.fkext.sample.data.database.converter

import androidx.room.TypeConverter
import ru.ktsstudio.fkext.sample.data.database.models.TransactionType

class TransactionTypeConverter {

    @TypeConverter
    fun getTransactionType(ordinal: Int): TransactionType = TransactionType.values()[ordinal]

    @TypeConverter
    fun getTransactionTypeInt(type: TransactionType): Int = type.ordinal
}