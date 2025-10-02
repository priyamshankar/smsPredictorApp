package com.ignite.smspredictor

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ignite.smspredictor.ui.theme.SmsPredictorTheme
import predictSms

const val TAG = "smsPredictor"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmsPredictorTheme {
                val classifier = predictSms(this)
                Greeting(classifier)
            }
        }
    }
}

@Composable
fun Greeting(classifier: predictSms) {
    var displayedText by remember { mutableStateOf("") }
    var prob: Float by remember { mutableStateOf(1.0000010001F) }
    var inputText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input field
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text here") }
        )

        // Button
        Button(
            onClick = {
                val res = classifier.predict(inputText)
                prob = res.second
                displayedText = if (res.first) "true" else "false"
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Show Text")
        }

        // Display text based on the button click
        Text(
            text = displayedText,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = prob.toString(),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}