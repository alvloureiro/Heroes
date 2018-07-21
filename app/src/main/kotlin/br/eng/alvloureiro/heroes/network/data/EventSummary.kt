package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class EventSummary(
    @Expose
    var resourceURI: String? = null,

    @Expose
    var name: String? = null
): Serializable
