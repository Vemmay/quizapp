package com.example.myquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { FlashcardQuizViewModel() }
            FlashcardQuizApp(viewModel)
        }
    }
}


@Composable
fun FlashcardQuizApp(viewModel: FlashcardQuizViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val quizState = viewModel.quizState.value
    val questions = viewModel.questions

    Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = { SnackbarHost(snackbarHostState) }) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (!quizState.quizComplete) {

                    Text("Question ${quizState.currentQuestionIndex + 1}: ${questions[quizState.currentQuestionIndex].question}")
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = quizState.userAnswer,
                        onValueChange = { viewModel.onAnswerChange(it) },
                        label = { Text("Answer") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        ElevatedButton(onClick = { viewModel.nextQuestion() }) {
                            Text("Next")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        ElevatedButton(onClick = {
                            val isCorrect = viewModel.isAnswerCorrect(
                                questions[quizState.currentQuestionIndex],
                                quizState.userAnswer
                            )
                            coroutineScope.launch { snackbarHostState.showSnackbar(if (isCorrect) "Correct!" else "Incorrect!") }
                            if (quizState.currentQuestionIndex == questions.size - 1) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Quiz Complete!") }
                            }
                        }) {

                            Text("Submit")
                        }

                    }
                } else {
                    //display "Quiz Complete" message in Card & restart button
                    Text("Quiz Complete!")
                    Spacer(modifier = Modifier.height(16.dp))

                    ElevatedButton(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.restartQuiz()
                        }) {
                        Text("Restart")
                    }
                }
            }
        }
    }
}


