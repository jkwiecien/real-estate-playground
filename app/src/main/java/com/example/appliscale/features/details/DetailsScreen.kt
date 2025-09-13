package com.example.appliscale.features.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.appliscale.R
import com.example.appliscale.api.model.Listing
import com.example.appliscale.ui.CircularProgressBox
import com.example.appliscale.ui.ErrorBox
import com.example.appliscale.ui.theme.AppliscaleTheme
import com.example.appliscale.ui.theme.CardBackground
import com.example.appliscale.ui.theme.White
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle(DetailsUiState.Loading)

    Surface(Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is DetailsUiState.Loading -> CircularProgressBox(Modifier.fillMaxSize())
            is DetailsUiState.Error -> ErrorBox(state.message, Modifier.fillMaxSize())
            is DetailsUiState.Success -> ListingDetails(
                listing = state.listing,
                onUpPressed = onBackClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetails(
    listing: Listing,
    preview: Boolean = false,
    onUpPressed: () -> Unit = {}
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 0

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (listing.imageUrl != null) {
                    AsyncImage(
                        model = listing.imageUrl,
                        contentDescription = listing.propertyType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                } else if (preview) {
                    Image(
                        painter = painterResource(R.drawable.preview_willa),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                } else if (preview) {
                    Image(
                        painter = painterResource(R.drawable.preview_willa),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Black.copy(alpha = 0.5f),
                                    Transparent
                                )
                            )
                        )
                ) {
                    TopAppBar(
                        title = { },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Transparent,
                            titleContentColor = Transparent
                        ),
                        navigationIcon = {
                            IconButton(
                                onClick = onUpPressed,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Go up",
                                    tint = White
                                )
                            }
                        },
                        modifier = Modifier.statusBarsPadding()
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = listing.propertyType,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = listing.city,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = formatter.format(listing.price),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listing.rooms?.let {
                        InfoCard(
                            iconRes = R.drawable.ic_door_24dp,
                            label = stringResource(R.string.label_rooms),
                            value = it.toString()
                        )
                    }
                    listing.bedrooms?.let {
                        InfoCard(
                            iconRes = R.drawable.ic_bed_24dp,
                            label = stringResource(R.string.label_bedrooms),
                            value = it.toString()
                        )
                    }

                    listing.area?.let {
                        InfoCard(
                            iconRes = R.drawable.ic_straighten_24dp,
                            label = stringResource(R.string.label_area),
                            value = "$it m²"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Property Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                DetailRow("Property Type", listing.propertyType)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                DetailRow("City", listing.city)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                listing.area?.let {
                    DetailRow("Area", "$it m²")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
                listing.bedrooms?.let {
                    DetailRow("Bedrooms", it.toString())
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
                listing.rooms?.let {
                    DetailRow("Rooms", it.toString())
                }
            }
            Spacer(modifier = Modifier.height(100.dp)) // For bottom bar
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.label_price),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = formatter.format(listing.price),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun InfoCard(iconRes: Int, label: String, value: String) {
    Card(
        modifier = Modifier.size(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ListingDetailsPreview() {
    val listing = Listing(
        id = 1,
        bedrooms = 3,
        city = "Paris",
        area = 120,
        price = 1500000,
        professional = "John Doe",
        propertyType = "Apartment",
        offerType = 1,
        rooms = 5
    )
    AppliscaleTheme {
        ListingDetails(listing, true)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
