package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.injected.common.Hasher
import by.zenkevich_churun.findcell.remote.retrofit.common.Base64AndroidCoder
import by.zenkevich_churun.findcell.remote.retrofit.common.Sha512Hasher
import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
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