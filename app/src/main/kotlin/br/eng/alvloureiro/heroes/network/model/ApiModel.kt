package br.eng.alvloureiro.heroes.network.model

import br.eng.alvloureiro.heroes.network.api.MarvelApi
import javax.inject.Inject


class ApiModel @Inject constructor(private val mApi: MarvelApi) {

    fun charactereList(offset: Int) = mApi.getCharacters(offset).execute().body()

    fun comicDetails(url: String) = mApi.getComicDetail2(url).execute().body()
}
