package com.myebra.marvelapp.ui.features.characterdetails.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myebra.marvelapp.domain.interactors.features.characters.GetCharacterDetailsUseCase
import com.myebra.marvelapp.domain.interactors.features.characters.GetComicsCharacterUseCase
import com.myebra.marvelapp.ui.common.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val getComicsCharacterUseCase: GetComicsCharacterUseCase
):ViewModel(){

    private val _characterState by lazy { MutableStateFlow<ResourceState<*>>(ResourceState.Idle) }
    val characterState: StateFlow<ResourceState<*>> get() = _characterState

    private val _comicState by lazy { MutableStateFlow<ResourceState<*>>(ResourceState.Idle) }
    val comicState:  StateFlow<ResourceState<*>> get() = _comicState

     fun fetchCharacterDetails(idCharacter: Long){
        viewModelScope.launch(Dispatchers.IO) {
            getCharacterDetailsUseCase(idCharacter)
                .catch {error ->
                    _characterState.update { ResourceState.Error(error) }
                }
                .onStart {
                    _characterState.update { ResourceState.Loading(View.VISIBLE) }
                    delay(1000)
                }
                .collectLatest{characterDetails->
                    _characterState.update { ResourceState.Success(characterDetails) }
                }
        }
    }

    fun fetchComicsCharacter(idCharacter:Long){
        viewModelScope.launch(Dispatchers.IO){
            getComicsCharacterUseCase(idCharacter)
                .catch { error ->
                    _comicState.update { ResourceState.Error(error) }
                }
                .collectLatest { comics ->
                    _comicState.update { ResourceState.Success(comics) }
                }
        }
    }
}