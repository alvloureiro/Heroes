package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class Summary(
    @Expose
    var resourceURI: String? = null,

    @Expose
    var name: String? = null,

    @Expose
    var type: String? = null
): Serializable
