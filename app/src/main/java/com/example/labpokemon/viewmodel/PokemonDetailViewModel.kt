package com.example.labpokemon.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.labpokemon.api.PokemonApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PokemonDetailViewModel : ViewModel() {
    private val _detail: MutableLiveData<PokemonDetail> = MutableLiveData()
    val pokemonDetail: LiveData<PokemonDetail> = _detail

    fun fetchPokemonDetail(pokemonId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(PokemonApi::class.java)
        val call: Call<PokemonDetail> = api.getPokemonDetail(pokemonId)

        call.enqueue(object : Callback<PokemonDetail?> {
            override fun onResponse(
                call: Call<PokemonDetail?>,
                response: Response<PokemonDetail?>
            ) {
                if (response.isSuccessful) {
                    Log.d("PokemonDetailViewModel", "success! " + response.body().toString())
                    val pokemonDetail = response.body()
                    _detail.postValue(pokemonDetail)
                }
            }

            override fun onFailure(call: Call<PokemonDetail?>, t: Throwable) {
                Log.e("PokemonDetailViewModel", "Failed mate " + t.message.toString())
            }
        })
    }
}


