package com.example.labpokemon.viewmodel

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonType>,
    // Add other details as needed
)

data class PokemonType(
    val slot: Int,
    val type: PokemonTypeItem
)

data class PokemonTypeItem(
    val name: String,
    val url: String
)