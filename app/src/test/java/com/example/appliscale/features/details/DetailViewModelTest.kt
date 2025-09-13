package com.example.appliscale.features.details

import app.cash.turbine.test
import com.example.appliscale.AppliscaleRepository
import com.example.appliscale.api.model.Listing
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AppliscaleRepository
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when ViewModel is created, uiStateFlow emits Loading then Success`() = runTest {
        // Arrange
        val listingId = 123
        val mockListing = Listing( // Corrected constructor with all required fields
            id = listingId,
            city = "Test City",
            price = 100000,
            professional = "Real Estate Pro",
            propertyType = "Apartment",
            bedrooms = 2,
            area = 80,
            offerType = 1,
            rooms = 3
        )
        every { repository.getListingDetails(listingId) } returns flowOf(mockListing)

        viewModel = DetailsViewModel(listingId, repository)

        // Assert
        viewModel.uiStateFlow.test {
            assertEquals(DetailsUiState.Loading, awaitItem())

            val successState = awaitItem()
            assertTrue(successState is DetailsUiState.Success)
            assertEquals(mockListing, (successState as DetailsUiState.Success).listing)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when repository throws an error, uiStateFlow emits Loading then Error`() = runTest {
        // Arrange
        val listingId = 456
        val errorMessage = "Network failed"
        every { repository.getListingDetails(listingId) } returns flow { throw RuntimeException(errorMessage) }

        viewModel = DetailsViewModel(listingId, repository)

        advanceUntilIdle()

        // Assert
        viewModel.uiStateFlow.test {
            // consume Loading
            assertEquals(DetailsUiState.Loading, awaitItem())

            val errorState = awaitItem()
            assertTrue(errorState is DetailsUiState.Error)
            assertEquals(errorMessage, (errorState as DetailsUiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

}