package ru.ktsstudio.sample.utils.eventbus

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object EventBus {

    private val mutableEvent = Channel<String>(Channel.BUFFERED)
    val event: Flow<String>
        get() = mutableEvent.receiveAsFlow()

    fun send(clients: String) = mutableEvent.trySend(clients)
}