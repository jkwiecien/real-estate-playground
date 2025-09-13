package com.example.appliscale.features.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appliscale.api.model.Listing
import com.example.appliscale.features.details.DetailsUiState
import com.example.appliscale.ui.CircularProgressBox
import com.example.appliscale.ui.ErrorBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel,
    onItemClick: (Int) -> Unit
) {
    val uiState by viewModel.listUiStateFlow.collectAsStateWithLifecycle(ListUiState.Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Real Estate Listings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
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
                is ListUiState.Loading -> CircularProgressBox(Modifier.fillMaxSize())
                is ListUiState.Error -> ErrorBox(state.message, Modifier.fillMaxSize())
                is ListUiState.Success -> {
                    ListingList(listings = state.listings, onItemClick = onItemClick)
                }
            }
        }
    }
}

@Composable
fun ListingList(listings: List<Listing>, onItemClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(listings) { listing ->
            ListingItem(listing = listing, onClick = { onItemClick(listing.id) })
        }
    }
}

