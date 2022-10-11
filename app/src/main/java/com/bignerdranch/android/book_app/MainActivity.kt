package com.bignerdranch.android.book_app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        if (userAnswer==correctAnswer){
            quizViewModel.rightAnswer++
        }
        val qSize = quizViewModel.qSize
        if (quizViewModel.answeredQuestions==qSize-1) {
            val percent = (100/(qSize.toDouble()))*quizViewModel.rightAnswer
            Toast.makeText(this, "$percent%", Toast.LENGTH_LONG).show()
        }
    }
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.button_false)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        trueButton.setOnClickListener {
            checkAnswer(true)
            quizViewModel.blockButton()
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            quizViewModel.answeredQuestions++
            Log.d("count", "true $quizViewModel.answeredQuestions")
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
            quizViewModel.blockButton()
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            quizViewModel.answeredQuestions++
            Log.d("count", "false $quizViewModel.answeredQuestions")
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            if (quizViewModel.buttonsAreBlocked[quizViewModel.currentIndex]){
                trueButton.isEnabled = true
                falseButton.isEnabled = true
            } else {
                trueButton.isEnabled = false
                falseButton.isEnabled = false
            }
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToBack()
            updateQuestion()
            if (quizViewModel.buttonsAreBlocked[quizViewModel.currentIndex]){
                trueButton.isEnabled = true
                falseButton.isEnabled = true
            } else {
                trueButton.isEnabled = false
                falseButton.isEnabled = false
            }
        }
        updateQuestion()
        }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}