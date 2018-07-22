package br.eng.alvloureiro.heroes.ui.viewmodel

import br.eng.alvloureiro.heroes.network.data.Character
import br.eng.alvloureiro.heroes.network.data.ResultData
import br.eng.alvloureiro.heroes.network.model.ApiModel
import javax.inject.Inject


class MainViewModel @Inject constructor(private val model: ApiModel): BaseViewModel() {

    fun runGetHeroesList(success: (ResultData<Character>) -> Unit, fail: (Throwable) -> Unit, offset: Int) {
        executeOnUITryCatch(
            {
                val data = asyncAwait {
                    model.charactereList(offset)?.data
                } ?: ResultData()
                success(data)
            },
            {
                fail(it)
            }
        )
    }
}
