package com.example.appliscale.features.list

import com.example.appliscale.api.model.Listing

sealed class ListUiState {
    object Loading : ListUiState()
    data class Success(val listings: List<Listing>) : ListUiState()
    data class Error(val message: String) : ListUiState()
}