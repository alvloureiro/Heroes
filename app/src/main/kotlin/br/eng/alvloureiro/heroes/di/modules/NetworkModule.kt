package br.eng.alvloureiro.heroes.di.modules

import br.eng.alvloureiro.heroes.BuildConfig
import br.eng.alvloureiro.heroes.BuildConfig.BASE_ENDPOINT
import br.eng.alvloureiro.heroes.HeroesApplication
import br.eng.alvloureiro.heroes.di.scopes.ApplicationScope
import br.eng.alvloureiro.heroes.network.api.MarvelApi
import br.eng.alvloureiro.heroes.network.interceptors.HttpCacheInterceptor
import br.eng.alvloureiro.heroes.network.interceptors.HttpOfflineCacheInterceptor
import br.eng.alvloureiro.heroes.network.interceptors.HttpQueryInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

@Module
class NetworkModule(private val app: HeroesApplication) {
    @Provides
    @ApplicationScope
    internal fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @ApplicationScope
    internal fun providesCache(): Cache {
        val cacheDir = File(app.cacheDir, "http-cache")
        return Cache(cacheDir, CACHE_SIZE.toLong())
    }

    @Provides
    @ApplicationScope
    internal fun providesOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpOfflineCacheInterceptor(app))
            .addInterceptor(HttpQueryInterceptor())
            .addInterceptor(HttpLoggingInterceptor()
                .setLevel(
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                ))
            .addNetworkInterceptor(HttpCacheInterceptor())
            .readTimeout(OKHTTP_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }

    @Provides
    @ApplicationScope
    internal fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_ENDPOINT)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @ApplicationScope
    internal fun providesMarvelApi(retrofit: Retrofit) = retrofit.create(MarvelApi::class.java)

    private companion object {
        const val CACHE_SIZE = 10 * 1024 * 1024 //10MB
        const val OKHTTP_READ_TIMEOUT = 40
        const val OKHTTP_WRITE_TIMEOUT = 40
    }
}
