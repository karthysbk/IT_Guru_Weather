package com.app.itguruweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.itguruweather.Repository.LocalRepository
import com.app.itguruweather.data.User
import com.app.retrofitroomhiltmvvm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
private val repo : LocalRepository
) : ViewModel() {

    sealed class ListUiState{
        class Success(val data: List<User>, val message: String?): ListUiState()
        class SuccessDelete(val data: Int, val message: String?): ListUiState()
        class Failure(val message: String?): ListUiState()
        object Loading: ListUiState()
        object Empty: ListUiState()
    }

    private val _uiSate = MutableStateFlow<ListUiState>(ListUiState.Empty)
    val uiState: StateFlow<ListUiState> = _uiSate.asStateFlow()

    init {
        callUsersList()
    }

    fun deleteUser(user: User){
        viewModelScope.launch {
            when(val result = repo.deleteUser(user)){
                is Resource.Success -> _uiSate.value = ListUiState.SuccessDelete(result.data!!, result.message)
                is Resource.Error -> _uiSate.value = ListUiState.Failure(result.message)
                is Resource.Loading -> _uiSate.value = ListUiState.Loading
            }
        }
    }

    fun callUsersList(){
        viewModelScope.launch {
            when(val result = repo.getUserList()){
                is Resource.Success -> _uiSate.value = ListUiState.Success(result.data ?: listOf(), result.message)
                is Resource.Error -> _uiSate.value = ListUiState.Failure(result.message)
                is Resource.Loading -> _uiSate.value = ListUiState.Loading
            }
        }
    }

}