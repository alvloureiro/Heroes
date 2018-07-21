package br.eng.alvloureiro.heroes.di.modules

import br.eng.alvloureiro.heroes.HeroesApplication
import br.eng.alvloureiro.heroes.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides


@Module
class ApplicationModule(private val app: HeroesApplication) {
    @Provides
    @ApplicationScope
    internal fun providesApplication() = app
}
