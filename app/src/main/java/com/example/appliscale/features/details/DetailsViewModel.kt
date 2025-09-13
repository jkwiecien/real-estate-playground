package com.example.appliscale.features.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appliscale.AppliscaleRepository
import com.example.appliscale.api.model.Listing
import com.example.appliscale.features.list.ListUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.delayFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DetailsViewModel(
    private val listingId: Int,
    private val repository: AppliscaleRepository
) : ViewModel() {

    private val listingIdMutableFlow = MutableStateFlow(0)
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiStateFlow: StateFlow<DetailsUiState> = listingIdMutableFlow
        .filter { it != 0 }
        .flatMapLatest { id ->
            repository.getListingDetails(id)
                .map { listing ->
                    DetailsUiState.Success(listing) as DetailsUiState
                }
                .onStart { emit(DetailsUiState.Loading) }
                .catch { error ->
                    emit(DetailsUiState.Error(error.message ?: "An unknown error occurred"))
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = DetailsUiState.Loading
        )

    init {
        listingIdMutableFlow.update { listingId }
    }

}