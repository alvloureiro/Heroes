package br.eng.alvloureiro.heroes.di.components

import br.eng.alvloureiro.heroes.HeroesApplication
import br.eng.alvloureiro.heroes.di.modules.ApplicationModule
import br.eng.alvloureiro.heroes.di.modules.NetworkModule
import br.eng.alvloureiro.heroes.di.scopes.ApplicationScope
import br.eng.alvloureiro.heroes.ui.activity.MainActivity
import com.google.gson.Gson
import dagger.Component
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit


@ApplicationScope
@Component(modules = [(ApplicationModule::class), (NetworkModule::class)])
interface ApplicationComponent {
    fun inject(app: HeroesApplication)
    fun inject(mainActivity: MainActivity)

    fun retrofit(): Retrofit
    fun okhttp(): OkHttpClient
    fun gson(): Gson
    fun cache(): Cache
}
