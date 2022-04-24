package com.offline.continentalrecipesusingnavgraph.di

import android.content.Context
import androidx.room.Room
import com.offline.continentalrecipesusingnavgraph.BuildConfig
import com.offline.continentalrecipesusingnavgraph.data.local.MealDao
import com.offline.continentalrecipesusingnavgraph.data.local.MealDatabase
import com.offline.continentalrecipesusingnavgraph.data.remote.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    /**
     * Retrofit providers
     */
    @Provides
    fun provideBaseUrl(): String = "https://www.themealdb.com/api/json/v1/1/"

    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        } else {
            OkHttpClient.Builder().build()
        }

    @Provides
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideCategoryApi(retrofit: Retrofit): ApiClient = retrofit.create(ApiClient::class.java)

    /**
     * Dao Providers
     */
    @Provides
    fun provideMealDatabase(@ApplicationContext context: Context): MealDatabase =
        Room.databaseBuilder(context, MealDatabase::class.java, "MealDatabase").build()

    @Provides
    fun provideMealDao(mealDatabase: MealDatabase): MealDao =
        mealDatabase.mealDao()
}