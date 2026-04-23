package com.uniqueplayer.musicapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uniqueplayer.musicapp.ui.VibePlayerViewModel
import com.uniqueplayer.musicapp.ui.screen.VibePlayerScreen
import com.uniqueplayer.musicapp.ui.theme.VibePlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: VibePlayerViewModel by viewModels()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                viewModel.scanLibrary()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ensureAudioPermission()
        setContent {
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            VibePlayerTheme(dynamicColor = true) {
                VibePlayerScreen(
                    uiState = state.value,
                    onPlayPause = viewModel::togglePlayPause,
                    onSelectTrack = viewModel::playTrack,
                    onSwipeNext = viewModel::skipNext,
                    onSwipePrevious = viewModel::skipPrevious,
                    onDoubleTap = viewModel::togglePlayPause,
                    onCreateSmartPlaylist = viewModel::createSmartPlaylist,
                    onReorderQueue = viewModel::moveQueueTrack,
                    onSetEqualizerBand = viewModel::setEqualizerBand,
                    onSetSleepTimerMinutes = viewModel::setSleepTimer,
                    onCancelSleepTimer = viewModel::cancelSleepTimer
                )
            }
        }
    }

    private fun ensureAudioPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        val isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            viewModel.scanLibrary()
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
