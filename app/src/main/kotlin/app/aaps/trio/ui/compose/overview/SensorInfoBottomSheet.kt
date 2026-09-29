package app.aaps.trio.ui.compose.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import app.aaps.core.interfaces.utils.DateUtil
import app.aaps.core.ui.UiMode
import app.aaps.core.ui.compose.AapsSpacing
import app.aaps.core.ui.compose.AapsTheme
import app.aaps.core.ui.compose.LocalDateUtil
import app.aaps.shared.impl.utils.DateUtilImpl
import app.aaps.ui.R
import app.aaps.ui.compose.main.SensorInfo
import java.time.Duration
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorInfoBottomSheet(
    sensorInfo: SensorInfo,
    now: Long,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit,
    dateUtil: DateUtil = LocalDateUtil.current,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val startedAt = sensorInfo.startedAt
    val ageText = startedAt?.let {
        val age = (now - it).coerceAtLeast(0L)
        stringResource(R.string.trio_sensor_age, age / 86_400_000L, (age / 3_600_000L) % 24)
    } ?: stringResource(R.string.trio_sensor_age_unavailable)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AapsSpacing.large),
            verticalArrangement = Arrangement.spacedBy(AapsSpacing.medium)
        ) {
            Text(
                text = stringResource(R.string.trio_sensor_info),
                style = MaterialTheme.typography.titleLarge
            )
            SensorInfoRow(
                stringResource(R.string.trio_sensor_source),
                sensorInfo.sourceName.ifEmpty { stringResource(R.string.trio_sensor_value_unavailable) }
            )
            SensorInfoRow(
                stringResource(R.string.trio_sensor_started),
                startedAt?.let(dateUtil::dateAndTimeString)
                    ?: stringResource(R.string.trio_sensor_value_unavailable)
            )
            SensorInfoRow(stringResource(R.string.trio_sensor_age_label), ageText)
            if (sensorInfo.batteryLevel >= 0) {
                SensorInfoRow(
                    stringResource(R.string.trio_sensor_battery),
                    stringResource(R.string.trio_sensor_battery_value, sensorInfo.batteryLevel)
                )
            }
            Button(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.trio_sensor_open_settings))
            }
        }
    }
}

@Composable
private fun SensorInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, textAlign = TextAlign.End)
    }
}

@Preview(heightDp = 800, widthDp = 400)
@Composable
private fun SensorInfoPreview() {
    AapsTheme(
        uiMode = UiMode.SYSTEM,
    ) {
        val context = LocalContext.current
        val dateUtil = DateUtilImpl(
            context
        )
        SensorInfoBottomSheet(
            sensorInfo = SensorInfo(
                sourceName = "xDrip",
                startedAt = System.currentTimeMillis() - Duration.of(2, ChronoUnit.DAYS).toMillis(),
                batteryLevel = 20,
            ),
            now = System.currentTimeMillis(),
            dateUtil = dateUtil,
            onDismiss = {},
            onOpenSettings = {}
        )
    }
}
