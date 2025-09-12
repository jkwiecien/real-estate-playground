package com.example.appliscale

import com.example.appliscale.api.AppliscaleApiService
import com.example.appliscale.api.model.Listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppliscaleRepository(
    private val apiService: AppliscaleApiService
) {
    fun getListings(): Flow<List<Listing>> = flow {
        emit(apiService.getListings().items)
    }

    fun getListingDetails(id: Int): Flow<Listing> = flow {
        emit(apiService.getListingDetails(id))
    }
}