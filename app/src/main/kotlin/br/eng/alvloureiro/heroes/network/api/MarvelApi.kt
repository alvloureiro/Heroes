package br.eng.alvloureiro.heroes.network.api

import br.eng.alvloureiro.heroes.network.data.Character
import br.eng.alvloureiro.heroes.network.data.ModelCommonData
import br.eng.alvloureiro.heroes.network.data.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface MarvelApi {
    @GET(GET_CHARACTERS)
    fun getCharacters(@Query("offset") offset: Int): Call<Result<Character>>

    @GET(GET_CHARACTERS)
    fun searchCharacterByName(@Query("name") name: String): Call<Result<Character>>

    @GET
    fun getEntityDetail(@Url comicUrl: String): Call<Result<ModelCommonData>>

    companion object {
        const val GET_CHARACTERS = "/v1/public/characters"
    }
}
