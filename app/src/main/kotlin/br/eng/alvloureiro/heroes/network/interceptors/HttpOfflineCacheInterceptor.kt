package br.eng.alvloureiro.heroes.network.interceptors

import br.eng.alvloureiro.heroes.HeroesApplication
import br.eng.alvloureiro.heroes.extensions.isNetworkAvailable
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit


class HttpOfflineCacheInterceptor(val app: HeroesApplication): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!app.baseContext.isNetworkAvailable()) {
            val cacheControl = CacheControl.Builder().maxStale(MAX_STALE, TimeUnit.HOURS).build()
            request = request.newBuilder().cacheControl(cacheControl).build()
        }

        return chain.proceed(request)
    }

    private companion object {
        const val MAX_STALE = 3
    }
}
