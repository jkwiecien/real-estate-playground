package com.example.appliscale.api

import com.example.appliscale.api.model.ListResponse
import com.example.appliscale.api.model.Listing
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface AppliscaleApiService {

    @GET("listings.json")
    suspend fun getListings(): ListResponse

    @GET("listings/{listingId}.json")
    suspend fun getListingDetails(@Path("listingId") listingId: Int): Listing
}