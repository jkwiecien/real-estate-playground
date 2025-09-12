package com.example.appliscale.di

import com.example.appliscale.AppliscaleRepository
import com.example.appliscale.api.AppliscaleApiService
import com.example.appliscale.features.details.DetailsViewModel
import com.example.appliscale.features.list.ListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    single<AppliscaleApiService> {
        val logging = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://gsl-apps-technical-test.dignp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AppliscaleApiService::class.java)
    }

    single {
        AppliscaleRepository(get())
    }

    viewModel {
        ListViewModel(get())
    }

    viewModel { parameters ->
        DetailsViewModel(listingId = parameters.get(), repository = get())
    }
}

