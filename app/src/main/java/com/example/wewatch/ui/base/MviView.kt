package com.example.wewatch.ui.base

interface MviView<UiState> {
    fun render(state: UiState)
}