package com.example.appliscale.features.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appliscale.AppliscaleRepository
import com.example.appliscale.features.list.ListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DetailsViewModel(
    private val listingId: Int,
    private val repository: AppliscaleRepository
) : ViewModel() {

    private val listingIdMutableFlow = MutableStateFlow(0)
    val uiStateFlow: StateFlow<DetailsUiState> = listingIdMutableFlow
        .map { id ->
            if (id == 0) DetailsUiState.Loading
            else repository.getListingDetails(id) as DetailsUiState
        }
        .catch { error ->
            Log.e("error on details", null, error)
            emit(DetailsUiState.Error(error.message ?: "An unknown error occurred"))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            DetailsUiState.Loading
        )

    init {
        listingIdMutableFlow.update { listingId }
    }

}