package com.example.labpokemon.api

import com.example.labpokemon.PokemonDetail
import com.example.labpokemon.model.PokemonList
import com.example.labpokemon.viewmodel.PokemonDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface PokemonApi {
    @Headers(
        "Accept: application/json"
    )
    @GET("pokemon")
    abstract fun getPokemonList() : Call<PokemonList>
    @GET("pokemon/{id}")
    abstract fun getPokemonDetail(@Path("id") id: String): Call<PokemonDetail>
}