package com.example.appliscale.features.details

import com.example.appliscale.api.model.Listing

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(val listing: Listing) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}