package br.eng.alvloureiro.heroes.network.model

import br.eng.alvloureiro.heroes.network.api.MarvelApi
import javax.inject.Inject


class ApiModel @Inject constructor(private val mApi: MarvelApi) {

    fun charactereList(offset: Int) = mApi.getCharacters(offset).execute().body()

    fun entityDetails(url: String) = mApi.getEntityDetail(url).execute().body()

    fun searchSuperHeroByName(name: String) = mApi.searchCharacterByName(name).execute().body()
}
