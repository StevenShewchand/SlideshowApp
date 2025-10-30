package com.example.slideshow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.slideshow.ui.theme.SlideshowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlideshowTheme {
                SlideShowApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideShowApp() {
    
    val images = listOf(
        R.drawable.photo1,
        R.drawable.photo2,
        R.drawable.photo3,
        R.drawable.photo4,
        R.drawable.photo5,
        R.drawable.photo6,
        R.drawable.photo7,
        R.drawable.photo8
    )

    val captions = listOf(
        "PRS SE Mark Holcomb SVN Purple Burst\nMy First Seven String Guitar",
        "MiM Fender Strat BRG Seymour Duncan\nThe Ol'Reliable Guitar",
        "Squier Tele Lake Placid Blue\nmeh... ",
        "Custom Built Strat\nHad This Guitar Since I Was 4 Years Old",
        "Taylor GS Mini Solid Spruce Top\nMy Most \"Loved\" Guitar",
        "Modded Squier Strat\nMy First Full DIY Mod Guitar",
        "Strandberg Boden\ni rlly j want one of these",
        "Westcreek Revenge\ni also rlly want one of these..\npurpul is cool\nwoah purpul geetar\ni think its more pink than purple\nbut pink is cool too"
    )

    val n = images.size
    require(n == captions.size && n >= 5) { "Need >=5 images and captions of equal length" }

    var index by remember { mutableStateOf(0) } // 0-based slide index
    var jumpText by remember { mutableStateOf("") } // user enters 1..n
    var error by remember { mutableStateOf<String?>(null) }

    fun next() { index = (index + 1) % n }
    fun prev() { index = (index - 1 + n) % n }
    fun goTo() {
        val parsed = jumpText.toIntOrNull()
        if (parsed == null || parsed !in 1..n) {
            error = "Enter a number from 1 to $n"
        } else {
            error = null
            index = (parsed - 1) % n
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Simple Slideshow") }) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image display
            Image(
                painter = painterResource(id = images[index]),
                contentDescription = captions[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(12.dp))

            // Caption + counter
            Text(
                text = "${index + 1} / $n — ${captions[index]}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Back / Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { prev() }) { Text("Back") }
                Button(onClick = { next() }) { Text("Next") }
            }

            Spacer(Modifier.height(16.dp))

            // Jump-to input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = jumpText,
                    onValueChange = { jumpText = it.filter { ch -> ch.isDigit() }.take(3) },
                    label = { Text("Go to # (1..$n)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { goTo() }) { Text("Go") }
            }

            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SlideShowPreview() {
    SlideshowTheme { SlideShowApp() }
}