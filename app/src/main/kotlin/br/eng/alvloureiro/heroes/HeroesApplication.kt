package br.eng.alvloureiro.heroes

import android.app.Application
import br.eng.alvloureiro.heroes.di.components.ApplicationComponent
import br.eng.alvloureiro.heroes.di.components.DaggerApplicationComponent
import br.eng.alvloureiro.heroes.di.modules.ApplicationModule
import br.eng.alvloureiro.heroes.di.modules.NetworkModule


class HeroesApplication: Application() {

    private val component: ApplicationComponent? by lazy {
        DaggerApplicationComponent.builder().networkModule(NetworkModule(this))
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component?.inject(this)
    }

    fun component() = component

}
