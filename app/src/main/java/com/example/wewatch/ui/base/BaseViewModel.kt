package com.example.wewatch.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Базовый класс для всех ViewModel
abstract class BaseViewModel : ViewModel() {

    protected fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch {
            block()
        }
    }

    // Удобный метод для создания StateFlow
    protected fun <T> mutableStateFlow(initial: T): MutableStateFlow<T> {
        return MutableStateFlow(initial)
    }
}