package com.example.affirmations.viewmodel

import androidx.core.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.affirmations.model.AffirmationWithImage
import com.example.affirmations.service.Service
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AffirmationState {
    object Loading : AffirmationState()
    data class Success(val affirmation: AffirmationWithImage) : AffirmationState()
    data class Error(val message: String) : AffirmationState()
}

class AffirmationViewModel : ViewModel() {

    private val _affirmations = MutableStateFlow<List<AffirmationWithImage>>(emptyList())
    val affirmations: StateFlow<List<AffirmationWithImage>> = _affirmations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchAffirmation() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val imageIndex = (_affirmations.value.size % 10) + 1
                delay(1000) // API call
                val newAffirmation = Service.ApiClient.affirmationsService.getRandomAffirmation()
                _affirmations.value += AffirmationWithImage(newAffirmation.affirmation, "image${imageIndex}")

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}