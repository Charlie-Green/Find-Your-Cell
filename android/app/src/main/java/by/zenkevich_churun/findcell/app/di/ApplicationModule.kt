package by.zenkevich_churun.findcell.app.di

import by.zenkevich_churun.findcell.core.api.PrisonerApi
import by.zenkevich_churun.findcell.prisoner.api.ram.RamPrisonerApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
interface ApplicationModule {

    @Binds
    fun prisonerApi(
        impl: RamPrisonerApi
    ): PrisonerApi
}