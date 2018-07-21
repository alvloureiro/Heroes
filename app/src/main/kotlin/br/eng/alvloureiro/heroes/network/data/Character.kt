package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class Character (
    @Expose
    var id: Int? = null,

    @Expose
    var name: String? = null,

    @Expose
    var description: String? = null,

    @Expose
    var resourceURI: String? = null,

    @Expose
    var thumbnail: ThumbnailData? = null,

    @Expose
    var comics: ResourceSummary? = null,

    @Expose
    var stories: ResourceSummary? = null,

    @Expose
    var events: ResourceSummary? = null,

    @Expose
    var series: ResourceSummary? = null
): Serializable
