package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class ResultData<T>(
    @Expose
    var offset: Int? = null,

    @Expose
    var limit: Int? = null,

    @Expose
    var total: Int? = null,

    @Expose
    var count: Int? = null,

    @Expose
    var results: List<T> = mutableListOf()
): Serializable

