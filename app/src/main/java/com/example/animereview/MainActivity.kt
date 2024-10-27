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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeReviewApp(animes = animeList) // Menampilkan daftar anime
        }
    }
}

@Composable
fun AnimeReviewApp(animes: List<Anime>) {
    var reviews by remember { mutableStateOf(mutableMapOf<Anime, List<String>>()) }
    var reviewText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedAnime by remember { mutableStateOf<Anime?>(null) } // Untuk menyimpan anime yang dipilih

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.top_app_bar_title)) })
        }
    ) { paddingValues ->
        // Column dengan pengguliran
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Purple40) // Menambahkan warna latar belakang
                .verticalScroll(rememberScrollState()), // Membuat Column dapat digulir
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menampilkan gambar anime dalam grid manual
            for (i in animeList.indices step 3) {
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
                                    .weight(1f), // Membuat kolom memiliki ukuran yang sama
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White) // Ganti dengan warna yang diinginkan
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(8.dp) // Padding di dalam Card
                                ) {
                                    Image(
                                        painter = painterResource(id = anime.imageResId),
                                        contentDescription = anime.title,
                                        modifier = Modifier
                                            .size(150.dp) // Ukuran gambar
                                            .clickable {
                                                selectedAnime = anime // Menyimpan anime yang dipilih
                                            }
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Kolom untuk detail anime
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

            // Input untuk ulasan
            selectedAnime?.let { anime ->
                TextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text(stringResource(id = R.string.review_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    if (reviewText.text.isNotEmpty()) {
                        // Tambahkan ulasan ke anime yang dipilih
                        val updatedReviews = reviews[anime]?.toMutableList() ?: mutableListOf()
                        // Format ulasan sesuai keinginan
                        updatedReviews.add("Ulasan: ${reviewText.text}")
                        reviews[anime] = updatedReviews // Simpan kembali ulasan

                        reviewText = TextFieldValue("") // Reset TextField
                    }
                }) {
                    Text(stringResource(id = R.string.submit_review),
                        color = Color.White ) // Mengubah warna teks menjadi putih
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan prologue yang dipilih jika ada
                Text(
                    text = stringResource(id = R.string.anime_prologue) + ": ${anime.prologue}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium // Anda dapat menyesuaikan gaya ini
                )

                Spacer(modifier = Modifier.height(16.dp)) // Menambahkan jarak antara prologue dan daftar ulasan

                // Menampilkan daftar ulasan untuk anime yang dipilih
                reviews[anime]?.forEach { review ->
                    Text(text = review, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
