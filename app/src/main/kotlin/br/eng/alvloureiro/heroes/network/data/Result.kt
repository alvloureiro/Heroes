package br.eng.alvloureiro.heroes.network.data

import com.google.gson.annotations.Expose
import java.io.Serializable


data class Result<T>(
    @Expose
    var code: Int? = null,

    @Expose
    var status: String? = null,

    @Expose
    var data: ResultData<T>? = null
): Serializable
