package com.example.myquizapp

import androidx.compose.animation.core.copy
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel


data class Flashcard(val question: String, val answer: String)

class FlashcardQuizViewModel : ViewModel() {
    data class QuizState(
        val currentQuestionIndex: Int = 0,
        val userAnswer: String = "",
        val quizComplete: Boolean = false,
        val showSnackbar: Boolean = false
    )

    private val _quizState = mutableStateOf(QuizState())
    val quizState: State<QuizState> = _quizState

    val questions = listOf(
        Flashcard("What is the capital of France?", "Paris"),
        Flashcard("What is 2 + 2?", "4"),
        Flashcard("What is the largest planet in our solar system?", "Jupiter"),
        Flashcard("What year did the Titanic sink?", "1912"),
        Flashcard("What is the chemical symbol for water?", "H2O")
    )

    fun isAnswerCorrect(currentQuestion: Flashcard, userAnswer: String): Boolean {
        return userAnswer.trim().equals(currentQuestion.answer, ignoreCase = true)
    }

    fun nextQuestion() { //gemini
        if (_quizState.value.currentQuestionIndex < questions.size - 1) {
            _quizState.value = _quizState.value.copy(
                currentQuestionIndex = _quizState.value.currentQuestionIndex + 1,
                userAnswer = ""
            )
        } else {
            _quizState.value = _quizState.value.copy(quizComplete = true)
        }
    }

    fun restartQuiz() {
        _quizState.value = QuizState()
    }

    fun onAnswerChange(answer: String) {
        _quizState.value = _quizState.value.copy(userAnswer = answer)
    }
}
