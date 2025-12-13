package com.azeemi.chineaze.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azeemi.chineaze.data.repository.VocabRepository
import com.azeemi.chineaze.domain.model.Vocabulary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class VocabViewModel(
    private val repo: VocabRepository
) : ViewModel() {

    private val _vocabList = MutableStateFlow<List<Vocabulary>>(emptyList())
    val vocabList: StateFlow<List<Vocabulary>> = _vocabList

    fun loadModule(moduleId: Int) {
        viewModelScope.launch {
            repo.getWordsByModule(moduleId).collect { words ->
                _vocabList.value = words
            }
        }
    }
}


