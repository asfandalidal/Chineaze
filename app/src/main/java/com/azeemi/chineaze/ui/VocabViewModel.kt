package com.azeemi.chineaze.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azeemi.chineaze.data.repository.VocabRepository
import com.azeemi.chineaze.domain.model.Vocabulary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VocabViewModel(private val repo: VocabRepository) : ViewModel() {

    private val _words = MutableStateFlow<List<Vocabulary>>(emptyList())
    val words: StateFlow<List<Vocabulary>> = _words

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadAll() {
        viewModelScope.launch {
            _loading.value = true
            _words.value = repo.getAllWords()
            _loading.value = false
        }
    }

    fun loadModule(moduleId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _words.value = repo.getWordsByModule(moduleId)
            _loading.value = false
        }
    }
}