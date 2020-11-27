package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.api.ProfileApi
import by.zenkevich_churun.findcell.prisoner.api.ram.RamProfileApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
interface ApplicationModule {

    @Singleton
    @Binds
    fun prisonerApi(
        impl: RamProfileApi
    ): ProfileApi
}