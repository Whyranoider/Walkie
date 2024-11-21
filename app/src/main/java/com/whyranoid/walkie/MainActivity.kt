package com.whyranoid.walkie

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.whyranoid.presentation.screens.AppScreen
import com.whyranoid.presentation.screens.setting.SettingViewModel
import com.whyranoid.presentation.theme.WalkieTheme
import com.whyranoid.walkie.walkiedialog.AppManageDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            WalkieTheme {
                AppManageDialog()
                AppScreen { startWorker(this) }
            }
        }

        observeRestart()
    }

    private fun observeRestart() {
        lifecycleScope.launch {
            SettingViewModel.appRestartEvent.collectLatest { isRestart ->
                if (isRestart == true) {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
