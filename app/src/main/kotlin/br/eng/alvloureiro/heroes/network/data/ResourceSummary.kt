package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class ResourceSummary(
    @Expose
    var available: Int? = null,

    @Expose
    var returned: Int? = null,

    @Expose
    var collectionURI: String? = null,

    @Expose
    var items: List<Summary>? = null
): Serializable
