package br.eng.alvloureiro.heroes.network.interceptors

import br.eng.alvloureiro.heroes.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response


class HttpQueryInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url: HttpUrl = originalHttpUrl.newBuilder() //TODO put ts and hash parameter
            .addQueryParameter("ts", BuildConfig.TS)
            .addQueryParameter("hash", BuildConfig.HASH)
            .addQueryParameter("apikey", BuildConfig.API_KEY)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
