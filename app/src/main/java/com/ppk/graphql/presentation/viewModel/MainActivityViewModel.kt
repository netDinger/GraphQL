package com.ppk.graphql.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.ppk.graphql.CharacterQuery
import com.ppk.graphql.CharactersListQuery
import com.ppk.graphql.domain.interactor.GetLaunchListUseCase
import com.ppk.graphql.presentation.view.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var useCase: GetLaunchListUseCase

    private val myCharacterList by lazy { MutableLiveData<ViewState<ApolloResponse<CharactersListQuery.Data>>>() }
    val characterList: LiveData<ViewState<ApolloResponse<CharactersListQuery.Data>>>
        get() = myCharacterList


    fun queryCharactersList() = viewModelScope.launch {
        myCharacterList.postValue(ViewState.Loading())
        try {
            val response =useCase.getCharacterList()

            myCharacterList.postValue(ViewState.Success(response))
        } catch (e: ApolloException) {
            e.message?.let { Log.e("error", it) }
            myCharacterList.postValue(ViewState.Error("Error fetching characterList"))
        }
    }

}