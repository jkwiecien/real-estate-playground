package com.example.appliscale.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.appliscale.api.model.Listing
import com.example.appliscale.ui.theme.AppliscaleTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Property Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is DetailsUiState.Loading -> CircularProgressIndicator()
                is DetailsUiState.Error -> Text(text = state.message)
                is DetailsUiState.Success -> ListingDetails(listing = state.listing)
            }
        }
    }
}

@Composable
fun ListingDetails(listing: Listing) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = listing.imageUrl,
            contentDescription = listing.propertyType,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = formatter.format(listing.price),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${listing.propertyType} in ${listing.city}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            DetailRow("Area:", "${listing.area} m²")
            listing.rooms?.let { DetailRow("Rooms:", it.toString()) }
            listing.bedrooms?.let { DetailRow("Bedrooms:", it.toString()) }
            DetailRow("City:", listing.city)
            DetailRow("Property Type:", listing.propertyType)
            DetailRow("Agent:", listing.professional)
        }
    }
}

@Preview
@Composable
private fun ListingDetailsPreview() {
    val listing = Listing(
        id = 1,
        bedrooms = 3,
        city = "Paris",
        area = 120,
        imageUrl = "https://v.seloger.com/s/crop/590x330/visuels/1/7/t/w/17twup9gg17p8p6k4gth0y196vj9d413g9s2z84sw.jpg",
        price = 1500000,
        professional = "John Doe",
        propertyType = "Apartment",
        offerType = 1,
        rooms = 5
    )
    ListingDetails(listing)
}

@Composable
fun DetailRow(label: String, value: String) {
    AppliscaleTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
