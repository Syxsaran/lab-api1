package com.example.labpokemon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface PokemonApi {
    @Headers(
        "Accept: application/json"
    )
    @GET("pokemon")
    abstract fun getPokemonList(): Call<PokemonList>

    @GET("pokemon/{id}")
    abstract fun getPokemonDetail(@Path("id") id: String): Call<PokemonDetail>
}