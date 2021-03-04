package by.sviazen.app.di

import by.sviazen.core.injected.common.Hasher
import by.sviazen.domain.util.Base64Coder
import by.sviazen.remote.retrofit.common.Base64AndroidCoder
import by.sviazen.remote.retrofit.common.Sha512Hasher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
interface CommonModule {

    @Binds
    fun hasher(
        impl: Sha512Hasher
    ): Hasher

    @Binds
    fun base64Coder(
        impl: Base64AndroidCoder
    ): Base64Coder
}