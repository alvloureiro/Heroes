package br.eng.alvloureiro.heroes.ui.viewmodel

import android.util.Log
import br.eng.alvloureiro.heroes.network.data.Character
import br.eng.alvloureiro.heroes.network.model.ApiModel
import javax.inject.Inject


class MainViewModel @Inject constructor(private val model: ApiModel): BaseViewModel() {

    fun runGetHeroesList(success: (List<Character>) -> Unit, fail: (Throwable) -> Unit) {
        executeOnUITryCatch(
            {
                val heroes = asyncAwait {
                    model.charactereList(0)?.data?.results
                }
                heroes?.forEach {
                    hero -> Log.d("MainViewModel", hero.name)
                }
            },
            {
                fail(it)
            }
        )
    }
}
