package ru.ktsstudio.fkext.sample.utils.db

import androidx.room.withTransaction
import ru.ktsstudio.fkext.sample.data.database.FinanceDB

const val MAX_SQLITE_PARAMETER_BIND_COUNT = 999

inline fun FinanceDB.inTransaction(block: FinanceDB.() -> Unit) {
    beginTransaction()
    try {
        block()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}

suspend inline fun FinanceDB.withTransaction(
    crossinline block: suspend FinanceDB.() -> Unit
) {
    withTransaction { block() }
}

inline fun <T> FinanceDB.batchedQueryInTransaction(
    list: List<T>,
    batchSize: Int = MAX_SQLITE_PARAMETER_BIND_COUNT,
    query: (List<T>) -> Unit
) {
    if (inTransaction()) {
        list.chunked(batchSize).forEach(query)
    } else {
        inTransaction {
            list.chunked(batchSize).forEach(query)
        }
    }
}

inline fun <T, R> List<T>.queryWithParameterCountCheck(query: (list: List<T>) -> List<R>): List<R> {
    return chunked(MAX_SQLITE_PARAMETER_BIND_COUNT)
        .flatMap { query(it) }
}
