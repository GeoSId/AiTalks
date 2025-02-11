package com.geosid.aitalks.di

import com.geosid.aitalks.data.remote.ChatRepository
import com.geosid.aitalks.data.remote.ChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun openAIRepository(
        repo: ChatRepositoryImpl
    ): ChatRepository
}
