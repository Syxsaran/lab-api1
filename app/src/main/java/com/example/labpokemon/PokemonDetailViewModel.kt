package com.example.labpokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetailViewModel : ViewModel() {
    private val _pokemonDetail: MutableLiveData<PokemonDetail> = MutableLiveData()
    val pokemonDetail: LiveData<PokemonDetail> = _pokemonDetail

    fun fetchPokemonDetail(pokemonId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(PokemonApi::class.java)
        val call: Call<PokemonDetail> = api.getPokemonDetail(pokemonId)

        call.enqueue(object : Callback<PokemonDetail?> {
            override fun onResponse(call: Call<PokemonDetail?>, response: Response<PokemonDetail?>) {
                if (response.isSuccessful) {
                    val detail = response.body()
                    if (detail != null) {
                        _pokemonDetail.postValue(detail)
                    }
                }
            }

            override fun onFailure(call: Call<PokemonDetail?>, t: Throwable) {
                // Handle failure here
            }
        })
    }
}

