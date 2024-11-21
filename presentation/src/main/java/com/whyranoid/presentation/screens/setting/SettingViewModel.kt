package com.whyranoid.presentation.screens.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.usecase.GetMyUidUseCase
import com.whyranoid.presentation.screens.mypage.editprofile.UserInfoUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel(
    private val accountRepository: AccountRepository,
    getMyUidUseCase: GetMyUidUseCase,
) : ViewModel() {
    private val _userInfoUiState = MutableStateFlow<UserInfoUiState?>(null)
    val userInfoUiState: StateFlow<UserInfoUiState?> = _userInfoUiState.asStateFlow()

    companion object {
        private val _appRestartEvent = MutableSharedFlow<Boolean>()
        val appRestartEvent: SharedFlow<Boolean?> = _appRestartEvent.asSharedFlow()
    }

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

    fun signOutFromGoogle(
        context: Context,
        onSignOutComplete: () -> Unit
    ) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 로그아웃 성공 시 처리
                onSignOutComplete()
                viewModelScope.launch {
                    accountRepository.singOut()
                    _appRestartEvent.emit(true)
                }
            } else {
                // 로그아웃 실패 시 처리
                Log.e("GoogleSignOut", "Sign out failed", task.exception)
            }
        }
    }

    fun revokeGoogleAccess(
        context: Context,
        onRevokeComplete: () -> Unit,
    ) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.revokeAccess().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                googleSignInClient.signOut().addOnCompleteListener { signOutTask ->
                    if (signOutTask.isSuccessful) {
                        // 3. 클라이언트 초기화
                        googleSignInClient.silentSignIn().addOnCompleteListener { silentTask ->
                            // 캐시 완전 제거
                            // 연결 해제 성공 시 처리
                            onRevokeComplete()
                            viewModelScope.launch {
                                accountRepository.leave(accountRepository.getUID())
                                accountRepository.singOut()
                                _appRestartEvent.emit(true)
                            }
                        }
                    }
                }
            } else {
                // 연결 해제 실패 시 처리
                Log.e("GoogleRevokeAccess", "Revoke access failed", task.exception)
            }
        }
    }
}