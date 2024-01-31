package com.example.labpokemon

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.labpokemon.ui.theme.LabPokemonTheme
import coil.compose.AsyncImage
import com.example.labpokemon.model.Pokemon
import com.example.labpokemon.viewmodel.PokemonDetailViewModel
import com.example.labpokemon.viewmodel.PokemonViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabPokemonTheme {
                PokemonApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonApp(navController: NavHostController = rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    var currentScreen = backStackEntry?.destination?.route ?: "List"
    if(currentScreen.contains("/"))
        currentScreen = currentScreen.split("/")[0]

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Pokemon")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = {navController.navigateUp()}) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back Navigation"
                            )
                        }
                    }
                }
            )
        }
    ){
            paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "List",
            modifier = Modifier.padding(paddingValues)
        ){
            composable(route = "List"){
                PokekonList(
                    onItemClick = {
                            pokemonId -> navController.navigate(route = "Detail/" + pokemonId)
                    },
                    navigateUp = { navController.navigateUp()})
            }
            composable(route = "Detail/{pokemonId}"){
                    backStackEntry -> PokemonDetail(
                navController = navController,
                pokemonId = backStackEntry.arguments?.getString("pokemonId"))
            }
        }
    }
}
@Composable
fun PokekonList(
    navigateUp:() -> Unit,
    onItemClick: (String) -> Unit,
    pokemonViewModel: PokemonViewModel = viewModel()
){
    val pokemonList by pokemonViewModel.pokemonList.observeAsState(initial = emptyList())
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(pokemonList){
                item: Pokemon ->
            PokemonItem(item, onClick = onItemClick)
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onClick:(id:String) -> Unit
){
    var context = LocalContext.current
    var imageUrl:String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    var urlSplited:List<String> = pokemon.url.split('/')
    var pokemonId = urlSplited[urlSplited.size - 2]
    var pokemonImage:String = imageUrl + pokemonId + ".png"
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        onClick = {
            //Toast.makeText(context, pokemon.name, Toast.LENGTH_SHORT).show()
            onClick(pokemonId)
        },
        modifier = Modifier
            .size(width = 100.dp, height = 100.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
        ) {
            AsyncImage(
                model = pokemonImage,
                contentDescription = "Translated description of what the image contains"
            )
            Text(text = pokemon.name)
        }
    }

}

@Composable
fun PokemonDetail(
    pokemonId: String?,
    pokemonDetailViewModel: PokemonDetailViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val pokemonDetail by pokemonDetailViewModel.pokemonDetail.observeAsState()

    LaunchedEffect(key1 = pokemonId) {
        if (!pokemonId.isNullOrBlank()) {
            pokemonDetailViewModel.fetchPokemonDetail(pokemonId)
        }
    }

    if (pokemonDetail != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display other details as needed
            Text("Name: ${pokemonDetail!!.name}")
            Text("Height: ${pokemonDetail!!.height}")
            Text("Weight: ${pokemonDetail!!.weight}")

            // Display types
            Text("Types:")
            for (type in pokemonDetail!!.types) {
                Text(" - ${type.type.name}")
            }
        }
    } else {
        // Loading indicator or error message
        CircularProgressIndicator()
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LabPokemonTheme {
        PokemonApp()
    }
}
