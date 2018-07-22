package br.eng.alvloureiro.heroes.ui.viewmodel

import br.eng.alvloureiro.heroes.network.data.Character
import br.eng.alvloureiro.heroes.network.data.ModelCommonData
import br.eng.alvloureiro.heroes.network.data.ResultData
import br.eng.alvloureiro.heroes.network.model.ApiModel
import javax.inject.Inject


class DetailViewModel @Inject constructor(private val model: ApiModel): BaseViewModel() {
    fun runGetCharacterEvents(character: Character,
                              success: (ResultData<ModelCommonData>?) -> Unit,
                              fail: (Throwable) -> Unit) {
        executeOnUITryCatch(
            {
                val events = asyncAwait {
                    model.entityDetails(character.events?.collectionURI ?: "")
                }

                success(events?.data)
            },
            { throwable -> fail(throwable) }
        )
    }

    fun runGetCharacterComics(character: Character,
                              success: (ResultData<ModelCommonData>?)-> Unit,
                              fail: (Throwable) -> Unit) {
        executeOnUITryCatch(
            {
                val comics = asyncAwait{
                    model.entityDetails(character.comics?.collectionURI ?: "")
                }

                success(comics?.data)
            },
            { throwable -> fail(throwable) }
        )
    }

    fun runGetCharacterStories(character: Character,
                               success: (ResultData<ModelCommonData>?) -> Unit,
                               fail: (Throwable) -> Unit) {
        executeOnUITryCatch(
            {
                val stories = asyncAwait {
                    model.entityDetails(character.stories?.collectionURI ?: "")
                }

                success(stories?.data)
            },
            { throwable -> fail(throwable) }
        )
    }

    fun runGetCharacterSeries(character: Character,
                              success: (ResultData<ModelCommonData>?) -> Unit,
                              fail: (Throwable) -> Unit) {
        executeOnUITryCatch(
            {
                val series = asyncAwait {
                    model.entityDetails(character.series?.collectionURI ?: "")
                }

                success(series?.data)
            },
            { throwable -> fail(throwable) }
        )
    }
}
