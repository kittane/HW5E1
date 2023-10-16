package com.frey.msu.geoquiz

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.SavedStateHandle

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val NUMBER_CORRECT_KEY = "NUMBER_CORRECT_KEY"
const val USER_SCORE_KEY = "USER_SCORE_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var isCheater: List<Boolean>
        get() = savedStateHandle.get<List<Boolean>>(IS_CHEATER_KEY) ?: List(questionBank.size) { false }
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)


    // These variables have been switched to public variables for the purposes of ensuring score calculation can occur in MainActivity.
    // TO-DO for future builds -- implement getters and setters
    var currentIndex: Int
    get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
    set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    var numberCorrect: Int
        get() = savedStateHandle.get(NUMBER_CORRECT_KEY) ?: 0
        set(value) = savedStateHandle.set(NUMBER_CORRECT_KEY, value)
    var userScore: Int
        get() = savedStateHandle.get(USER_SCORE_KEY) ?: 0
        set(value) = savedStateHandle.set(USER_SCORE_KEY, value)


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex - 1) % questionBank.size
    }

    fun correctAnswer() {
        numberCorrect++
    }

    fun isCurrentQuestionCheated(): Boolean {
        Log.d(TAG, "Checking if current question is cheated.")
        val isCheaterList = savedStateHandle.get<List<Boolean>>(IS_CHEATER_KEY)
        return isCheaterList?.get(currentIndex) ?: false
    }

    fun setCheatingStatus(isCheater: Boolean) {
        val currentList = (savedStateHandle.get<List<Boolean>>(IS_CHEATER_KEY) ?: List(questionBank.size) { false }).toMutableList()
        currentList[currentIndex] = isCheater
        savedStateHandle.set(IS_CHEATER_KEY, currentList)
    }

}