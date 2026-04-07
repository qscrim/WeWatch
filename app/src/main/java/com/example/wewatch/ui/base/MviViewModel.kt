package com.example.wewatch.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class MviViewModel<Intent : MviIntent, UiState : Any>(
    initialState: UiState
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        observeIntents()
    }

    fun processIntent(newIntent: Intent) {
        viewModelScope.launch {
            handleIntent(_state.value, newIntent).let { newState ->
                _state.value = newState
            }
        }
    }

    protected abstract suspend fun handleIntent(currentState: UiState, intent: Intent): UiState
    protected abstract fun observeIntents()
}