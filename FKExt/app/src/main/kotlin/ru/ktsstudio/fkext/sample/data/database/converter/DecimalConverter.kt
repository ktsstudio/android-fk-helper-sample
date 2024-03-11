package ru.ktsstudio.fkext.sample.data.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class DecimalConverter {

    @TypeConverter
    fun getDecimal(decimal: String): BigDecimal = BigDecimal(decimal)

    @TypeConverter
    fun getDecimalString(decimal: BigDecimal): String = decimal.toString()
}

