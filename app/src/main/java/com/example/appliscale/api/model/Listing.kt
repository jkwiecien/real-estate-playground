package com.example.appliscale.api.model

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName

@Stable
data class Listing(
    @SerializedName("id") val id: Int,
    @SerializedName("bedrooms") val bedrooms: Int?,
    @SerializedName("city") val city: String,
    @SerializedName("area") val area: Int?,
    @SerializedName("url") val imageUrl: String? = null,
    @SerializedName("price") val price: Int,
    @SerializedName("professional") val professional: String,
    @SerializedName("propertyType") val propertyType: String,
    @SerializedName("offerType") val offerType: Int?,
    @SerializedName("rooms") val rooms: Int?
)
