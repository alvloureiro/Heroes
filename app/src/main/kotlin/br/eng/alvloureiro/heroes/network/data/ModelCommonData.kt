package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class ModelCommonData(
    @Expose
    var id: Int? = null,

    @Expose
    var title: String? = null,

    @Expose
    var description: String? = null
) : Serializable
