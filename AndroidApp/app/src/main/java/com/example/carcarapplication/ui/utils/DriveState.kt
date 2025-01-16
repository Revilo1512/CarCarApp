import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf

object DriveState {
    var isDriving = mutableStateOf(false)
    var startTime = mutableLongStateOf(System.currentTimeMillis())
}