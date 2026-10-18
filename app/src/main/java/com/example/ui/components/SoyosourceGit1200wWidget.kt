package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InverterComponentInfo
import com.example.model.SoyosourceInverterState
import com.example.model.SoyosourceWorkingMode
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber600
import com.example.ui.theme.Blue600
import com.example.ui.theme.Cyan300
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Rose600
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SoyosourceGit1200wWidget(
    state: SoyosourceInverterState,
    components: List<InverterComponentInfo>,
    onTogglePower: () -> Unit,
    onSelectMode: (SoyosourceWorkingMode) -> Unit,
    onSimulateLoad: (Int) -> Unit,
    onSetMaxPower: (Int) -> Unit,
    onSetCutoffVoltage: (Double) -> Unit,
    onRunDiagnostic: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isComponentsExpanded by remember { mutableStateOf(false) }
    var isMaintenanceExpanded by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("soyosource_git_1200w_widget")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // Header Chassis Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Amber500, Color(0xFFB45309))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricMeter,
                            contentDescription = "Soyosource GIT-1200W",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = state.modelName,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Grid-Tie Inverter with Limiter (กันย้อน)",
                            color = Amber400,
                            fontSize = 11.sp
                        )
                    }
                }

                // Power ON / OFF Button
                IconButton(
                    onClick = onTogglePower,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (state.isRunning) Emerald600 else Slate700)
                        .testTag("soyosource_power_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = if (state.isRunning) "Turn Inverter Off" else "Turn Inverter On",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hardware Status LED Bar
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Slate950,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LedIndicator(
                        label = "POWER",
                        isActive = state.isRunning,
                        activeColor = Emerald600
                    )
                    LedIndicator(
                        label = "GRID 50Hz",
                        isActive = state.isRunning && state.acFrequency > 49.0,
                        activeColor = Cyan300
                    )
                    LedIndicator(
                        label = "LIMITER CT",
                        isActive = state.ctSensorConnected && state.workingMode == SoyosourceWorkingMode.LIMITER_CT,
                        activeColor = Amber400,
                        isBlinking = state.isRunning
                    )
                    LedIndicator(
                        label = "FAN AUTO",
                        isActive = state.fanSpeedPercent > 0,
                        activeColor = Blue600
                    )
                    LedIndicator(
                        label = "FAULT",
                        isActive = false,
                        activeColor = Rose600
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Big Live Wattage Display (Wattmeter Display)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF06101E),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REAL-TIME AC OUTPUT POWER",
                        color = Slate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${state.acOutputWatts}",
                            color = if (state.isRunning) Amber400 else Slate400,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WATTS",
                            color = if (state.isRunning) Amber400 else Slate400,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    // Progress bar showing current watts vs 1200W rated capacity
                    val fraction = (state.acOutputWatts.toFloat() / 1200f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { fraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (fraction > 0.85f) Rose600 else Amber400,
                        trackColor = Slate800,
                        strokeCap = StrokeCap.Round
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "0W",
                            color = Slate400,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Max Limit: ${state.maxPowerLimitWatts}W",
                            color = Amber400,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Rated: 1200W",
                            color = Slate400,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Electrical Metric Grid: DC Input vs AC Output vs Efficiency
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // DC Input (Battery / Solar)
                InverterMetricPill(
                    label = "DC Input (48V-90V)",
                    value = "${state.dcInputVoltage} V",
                    subvalue = "${state.dcInputCurrent} A (DC)",
                    icon = Icons.Default.ElectricBolt,
                    accentColor = Cyan300,
                    modifier = Modifier.weight(1f)
                )

                // AC Output (Pure Sine Wave)
                InverterMetricPill(
                    label = "AC Output 220V",
                    value = "${state.acOutputVoltage} V",
                    subvalue = "${state.acFrequency} Hz (Pure)",
                    icon = Icons.Default.Waves,
                    accentColor = Emerald600,
                    modifier = Modifier.weight(1f)
                )

                // Conversion Efficiency
                InverterMetricPill(
                    label = "Efficiency (MPPT)",
                    value = String.format("%.1f %%", state.efficiencyPercent),
                    subvalue = "THD ${state.pureSineWaveThd}%",
                    icon = Icons.Default.Speed,
                    accentColor = Amber400,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Zero-Export (กันย้อน) Real-time Diagram
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Slate950,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A8A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                tint = Cyan300,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ระบบกันย้อนด้วย CT Clamp (Zero Export)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Emerald600.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Emerald600)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Emerald600)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "กฟภ. 0W Export",
                                    color = Emerald100,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Flow Diagram: [Inverter 945W] ---> [Clinic Load 945W] <--- [PEA Grid 0W]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FlowBox(
                            title = "Soyosource Inverter",
                            watts = "${state.acOutputWatts} W",
                            status = "จ่ายไฟชดเชย",
                            color = Amber400,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Amber400.copy(alpha = pulseAlpha),
                            modifier = Modifier.size(18.dp)
                        )

                        FlowBox(
                            title = "โหลดศูนย์ช่างชุมชน",
                            watts = "${state.clinicLoadWatts} W",
                            status = "ตู้เชื่อมสปอต/เครื่องมือ",
                            color = Cyan300,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "||",
                            color = Slate400,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        FlowBox(
                            title = "มิเตอร์ กฟภ.",
                            watts = "${state.gridExportWatts} W",
                            status = "ไม่หมุนย้อน (ปลอดภัย)",
                            color = Emerald600,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• เซนเซอร์ CT Clamp (Split Core) อ่านกระแสสายเมนหลัก และส่งสัญญาณผ่านพอร์ต RS485 มาที่ Soyosource 1200W ทุก 180ms เพื่อปรับกำลังไฟให้อยู่ในสภาวะ Zero Export 100%",
                        color = Slate400,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Working Mode Selector
            Text(
                text = "โหมดการทำงานของอินเวอร์เตอร์ (Operating Modes):",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SoyosourceWorkingMode.entries.forEach { mode ->
                    val isSelected = state.workingMode == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Amber500,
                            selectedLabelColor = Slate950,
                            containerColor = Slate800,
                            labelColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_${mode.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Workshop Load Simulator
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Slate950,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "⚡ ทดสอบการตอบสนองของระบบกันย้อน (Workshop Load Test)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "กดปุ่มเพื่อจำลองการเปิดเครื่องมือช่างในศูนย์ซ่อม วีลแชร์ แล้วสังเกตว่าอินเวอร์เตอร์ชดเชยกำลังไฟฟ้าทันที",
                        color = Slate400,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LoadPresetButton("Standby 150W", 150, state.clinicLoadWatts) { onSimulateLoad(150) }
                        LoadPresetButton("+450W หัวแร้งบัดกรี", 450, state.clinicLoadWatts) { onSimulateLoad(450) }
                        LoadPresetButton("+850W ตู้สปอตแบตลิเธียม", 850, state.clinicLoadWatts) { onSimulateLoad(850) }
                        LoadPresetButton("+1150W แท่นเจาะ/เครื่องกลึง", 1150, state.clinicLoadWatts) { onSimulateLoad(1150) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thermal & Cooling Fan Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate950,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = null,
                            tint = if (state.coreTemperatureC > 50.0) Amber400 else Cyan300,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Core Temp", color = Slate400, fontSize = 10.sp)
                            Text(
                                "${state.coreTemperatureC} °C",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate950,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = Blue600,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Dual Fan PWM", color = Slate400, fontSize = 10.sp)
                            Text(
                                "${state.fanSpeedPercent}% (${state.fanState.label})",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hardware Diagnostics Center Button
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0F2642),
                border = androidx.compose.foundation.BorderStroke(1.dp, Blue600),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Cyan300,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ตรวจเช็กความสมบูรณ์บอร์ด (Hardware Diagnostics)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onRunDiagnostic,
                            enabled = !state.isDiagnosticRunning,
                            colors = ButtonDefaults.buttonColors(containerColor = Cyan500),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            ),
                            modifier = Modifier.testTag("run_inverter_diagnostic_button")
                        ) {
                            if (state.isDiagnosticRunning) {
                                Text("ตรวจเช็ก...", fontSize = 11.sp, color = Slate950)
                            } else {
                                Text("เริ่มทดสอบ 5 จุด", fontSize = 11.sp, color = Slate950, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = state.diagnosticMessage,
                        color = if (state.isDiagnosticRunning) Amber400 else Emerald100,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (state.isDiagnosticRunning) {
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { (state.diagnosticStep / 5f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = Cyan300,
                            trackColor = Slate800
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Expandable: Board Circuit & MOSFET Repair Details (Sarayut's Core Skill!)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Slate950,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isComponentsExpanded = !isComponentsExpanded }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = null,
                                tint = Amber400,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ผังวงจรและการซ่อมบำรุงบอร์ดระดับชิ้นส่วน (${components.size} ชิ้นส่วนหลัก)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = if (isComponentsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    AnimatedVisibility(visible = isComponentsExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            components.forEach { comp ->
                                ComponentItemRow(comp)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Community & Wheelchair Clinic Application Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF132238),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Amber400,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "การประยุกต์ใช้เพื่อคนพิการและชุมชนนครนายก",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "1. แหล่งพลังงานสะอาดในศูนย์ช่าง: จ่ายไฟ 220V ขับเคลื่อนเครื่องสปอตแบตเตอรี่ แท่นชาร์จ และหัวแร้ง โดยไม่สร้างภาระค่าไฟฟ้าให้ศูนย์บริการฟรี\n2. ระบบสำรองไฟฉุกเฉิน (Emergency UPS): ในกรณีพายุฝนและไฟฟ้าดับในนครนายก สามารถดึงไฟจากแบตเตอรี่ 48V จ่ายให้เครื่องผลิตออกซิเจนและที่นอนลมของผู้ป่วยติดเตียงได้ต่อเนื่อง 8-12 ชั่วโมง\n3. การซ่อมแซมและถ่ายทอดวิชาชีพ: นายสรายุทธสามารถตรวจซ่อมอินเวอร์เตอร์ที่เสียหายให้ชาวบ้านในพื้นที่ได้เอง โดยไม่ต้องส่งซ่อม กทม.",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LedIndicator(
    label: String,
    isActive: Boolean,
    activeColor: Color,
    isBlinking: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isActive) activeColor else Slate700)
                .border(1.dp, if (isActive) Color.White.copy(alpha = 0.5f) else Color.Transparent, CircleShape)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = if (isActive) Color.White else Slate400,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InverterMetricPill(
    label: String,
    value: String,
    subvalue: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Slate950,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    color = Slate400,
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subvalue,
                color = accentColor,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun FlowBox(
    title: String,
    watts: String,
    status: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = Slate400, fontSize = 9.sp, textAlign = TextAlign.Center)
            Text(
                watts,
                color = color,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(status, color = Color.Gray, fontSize = 8.sp, maxLines = 1)
        }
    }
}

@Composable
private fun LoadPresetButton(
    label: String,
    watts: Int,
    currentWatts: Int,
    onClick: () -> Unit
) {
    val isSelected = currentWatts == watts
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Amber500 else Slate800,
        modifier = Modifier
            .clickable { onClick() }
            .testTag("load_preset_$watts")
    ) {
        Text(
            text = label,
            color = if (isSelected) Slate950 else Color.White,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ComponentItemRow(comp: InverterComponentInfo) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comp.name,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Emerald600.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = comp.status,
                        color = Emerald100,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "Part: ${comp.partNumber}",
                color = Amber400,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "หน้าที่: ${comp.role}",
                color = Color.LightGray,
                fontSize = 10.sp
            )

            Text(
                text = "ทักษะช่าง: ${comp.repairSkill}",
                color = Cyan300,
                fontSize = 10.sp
            )
        }
    }
}
