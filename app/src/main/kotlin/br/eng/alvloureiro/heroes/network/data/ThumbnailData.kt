package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class ThumbnailData(
    @Expose
    var path: String? = null,

    @Expose
    var extension: String? = null
): Serializable
