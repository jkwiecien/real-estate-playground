package com.example.appliscale.features.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appliscale.AppliscaleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ListViewModel(
    private val repository: AppliscaleRepository
) : ViewModel() {

    val listUiStateFlow = repository.getListings()
        .map { ListUiState.Success(it) as ListUiState }
        .catch { error ->
            Log.e("error on list", null, error)
            emit(ListUiState.Error(error.message ?: "An unknown error occurred")) }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ListUiState.Loading
        )
}