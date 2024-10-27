package com.example.animereview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.animereview.ui.theme.Anime
import com.example.animereview.ui.theme.animeList
import com.example.animereview.ui.theme.Purple40 // Import warna yang diinginkan

// MainActivity adalah kelas utama yang dijalankan saat aplikasi dimulai
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Memanggil fungsi AnimeReviewApp untuk menampilkan daftar anime
            AnimeReviewApp(animes = animeList)
        }
    }
}

// Composable function utama untuk aplikasi review anime
@Composable
fun AnimeReviewApp(animes: List<Anime>) {
    // State untuk menyimpan daftar ulasan per anime
    var reviews by remember { mutableStateOf(mutableMapOf<Anime, List<String>>()) }
    // State untuk menyimpan teks ulasan yang sedang ditulis
    var reviewText by remember { mutableStateOf(TextFieldValue("")) }
    // State untuk menyimpan anime yang dipilih
    var selectedAnime by remember { mutableStateOf<Anime?>(null) }

    // Scaffold untuk struktur UI aplikasi dengan top bar
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.top_app_bar_title)) })
        }
    ) { paddingValues ->
        // Column yang dapat digulir untuk menampilkan daftar anime
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Purple40) // Warna latar belakang Purple40
                .verticalScroll(rememberScrollState()), // Column dapat digulir
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menampilkan gambar anime dalam grid manual
            for (i in animeList.indices step 3) { // Mengelompokkan per 3 anime dalam satu baris
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (j in 0 until 3) {
                        if (i + j < animeList.size) {
                            val anime = animeList[i + j]
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f), // Mengatur ukuran kolom sama rata
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White) // Warna latar kartu
                            ) {
                                // Kolom untuk konten dalam kartu
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    // Menampilkan gambar anime yang dapat diklik untuk memilih anime
                                    Image(
                                        painter = painterResource(id = anime.imageResId),
                                        contentDescription = anime.title,
                                        modifier = Modifier
                                            .size(150.dp)
                                            .clickable {
                                                selectedAnime = anime // Menyimpan anime yang dipilih
                                            }
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Menampilkan informasi detail anime
                                    Text(
                                        text = stringResource(id = R.string.anime_title) + ": ${anime.title}",
                                        modifier = Modifier.padding(4.dp)
                                    )
                                    Text(
                                        text = stringResource(id = R.string.anime_genre) + ": ${anime.genre}",
                                        modifier = Modifier.padding(4.dp)
                                    )
                                    Text(
                                        text = stringResource(id = R.string.anime_rating) + ": ${anime.rating}",
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bagian input untuk menambahkan ulasan
            selectedAnime?.let { anime ->
                // TextField untuk menuliskan ulasan
                TextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text(stringResource(id = R.string.review_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tombol untuk menyimpan ulasan
                Button(onClick = {
                    if (reviewText.text.isNotEmpty()) {
                        // Menambahkan ulasan baru ke daftar ulasan anime yang dipilih
                        val updatedReviews = reviews[anime]?.toMutableList() ?: mutableListOf()
                        updatedReviews.add("Ulasan: ${reviewText.text}") // Format ulasan
                        reviews[anime] = updatedReviews // Menyimpan daftar ulasan

                        reviewText = TextFieldValue("") // Reset TextField
                    }
                }) {
                    Text(stringResource(id = R.string.submit_review), color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan prologue dari anime yang dipilih
                Text(
                    text = stringResource(id = R.string.anime_prologue) + ": ${anime.prologue}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp)) // Jarak antara prologue dan daftar ulasan

                // Menampilkan daftar ulasan untuk anime yang dipilih
                reviews[anime]?.forEach { review ->
                    Text(text = review, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
