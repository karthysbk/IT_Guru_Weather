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
class UserFormViewModel @Inject constructor(
    private val repo: LocalRepository
) : ViewModel() {

    sealed class ResultState {
        class Success(val result: Boolean?, val message: String?) : ResultState()
        class Failure(val message: String?) : ResultState()
        object Loading : ResultState()
        object Empty : ResultState()
    }

    private val _state = MutableStateFlow<ResultState>(ResultState.Empty)
    val state: StateFlow<ResultState> = _state.asStateFlow()

    fun saveUser(user: User) {
        viewModelScope.launch {
            _state.value = ResultState.Loading
            when (val response = repo.insertUser(user)) {
                is Resource.Success -> _state.value = ResultState.Success(true, response.message)
                is Resource.Error -> _state.value = ResultState.Failure(response.message ?: "User not saved")
                is Resource.Loading -> _state.value = ResultState.Loading
            }

        }
    }
}