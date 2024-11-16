package com.whyranoid.presentation.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.usecase.GetMyUidUseCase
import com.whyranoid.presentation.screens.mypage.editprofile.UserInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel(
    private val accountRepository: AccountRepository,
    getMyUidUseCase: GetMyUidUseCase,
): ViewModel() {
    private val _userInfoUiState = MutableStateFlow<UserInfoUiState?>(null)
    val userInfoUiState: StateFlow<UserInfoUiState?> = _userInfoUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val uid = getMyUidUseCase().getOrNull() ?: return@launch

            accountRepository.getUserInfo(uid)
                .onSuccess { userInfo ->
                    _userInfoUiState.update {
                        UserInfoUiState(
                            userInfo.name,
                            userInfo.nickname,
                            userInfo.profileImg
                        )
                    }
                }
        }
    }
}