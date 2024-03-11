package ru.ktsstudio.sample.utils.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T : Any> rememberVisibleStateParams(
    visible: Boolean = false,
    params: T? = null
): VisibleStateParams<T> = remember {
    VisibleStateParamsImpl(
        mutableState = mutableStateOf(
            value = VisibleStateWithParams(
                visible = visible,
                params = params
            )
        ),
    )
}

interface VisibleStateParams<T : Any> {

    val shouldShow: Boolean

    fun show(params: T)

    fun hide()

    fun getParams(): T

    fun getNullableParams(): T?
}

private class VisibleStateParamsImpl<T : Any>(
    private val mutableState: MutableState<VisibleStateWithParams<T>>,
) : VisibleStateParams<T>, MutableState<VisibleStateWithParams<T>> by mutableState {

    override val shouldShow: Boolean
        get() = value.visible

    override fun show(params: T) {
        value = value.copy(visible = true, params = params)
    }

    override fun hide() {
        value = value.copy(visible = false)
    }

    override fun getParams(): T = value.params.requireNotNull("params is null")

    override fun getNullableParams(): T? = value.params
}

private data class VisibleStateWithParams<T>(
    val visible: Boolean,
    val params: T?,
)
