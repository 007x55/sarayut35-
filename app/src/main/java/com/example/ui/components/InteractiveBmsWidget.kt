package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue600
import com.example.ui.theme.Cyan300
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.viewmodel.BmsSimState

@Composable
fun InteractiveBmsWidget(
    bmsState: BmsSimState,
    onToggleBalance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_bms_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Slate900
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: IoT telemetry status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Cyan500, Blue600))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = "IoT BMS",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Smart IoT BMS & Inverter Monitor",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusDot(isActive = bmsState.isConnected, activeColor = Cyan300)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Online Telemetry | Soyosource 1200W Live",
                                color = Cyan300,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Balance toggle button
                val balColor by animateColorAsState(
                    targetValue = if (bmsState.isBalancing) Emerald600 else Slate800,
                    label = "balColor"
                )
                ElevatedButton(
                    onClick = onToggleBalance,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = balColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("toggle_balance_button")
                ) {
                    Text(
                        text = if (bmsState.isBalancing) "Active Balance ON" else "Balance OFF",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Telemetry Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Pack Voltage
                MetricBox(
                    label = "Pack Voltage",
                    value = "${bmsState.packVoltage} V",
                    subtext = "16S LiFePO4",
                    icon = Icons.Default.Bolt,
                    accentColor = Amber400,
                    modifier = Modifier.weight(1f)
                )

                // SOC%
                MetricBox(
                    label = "Battery SOC",
                    value = "${bmsState.stateOfCharge}%",
                    subtext = "Healthy Pack",
                    icon = Icons.Default.BatteryChargingFull,
                    accentColor = Cyan300,
                    modifier = Modifier.weight(1f)
                )

                // Current Draw
                MetricBox(
                    label = "Output Current",
                    value = "${bmsState.currentAmp} A",
                    subtext = "Load Active",
                    icon = Icons.Default.Speed,
                    accentColor = Emerald600,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Soyosource 1200W Inverter Telemetry Bar
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Slate950,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ElectricMeter,
                            contentDescription = "Soyosource",
                            tint = Amber400,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Soyosource Inverter (1200W Max)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Grid-Tie / Off-Grid Clinic Power",
                                color = Color.Gray,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${bmsState.soyosourceWatts} Watts",
                            color = Amber400,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${bmsState.temperatureC} °C",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 16 Individual Cell Voltages Grid
            Text(
                text = "16S Individual Cell Monitoring (V)",
                color = Color.LightGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // 4 rows of 4 cells
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                bmsState.cellVoltages.chunked(4).forEachIndexed { rowIndex, rowCells ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowCells.forEachIndexed { colIndex, voltage ->
                            val cellNumber = rowIndex * 4 + colIndex + 1
                            CellVoltageChip(
                                cellNumber = cellNumber,
                                voltage = voltage,
                                isBalancing = bmsState.isBalancing && cellNumber % 3 == 0,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "※ ระบบจำลองการทำงานจริง: พัฒนาและเขียนโปรแกรมควบคุมโดย นายสรายุทธ มูลผล สำหรับตรวจเช็กแบตเตอรี่รถวีลแชร์ไฟฟ้าคนพิการฟรี",
                color = Cyan300.copy(alpha = 0.8f),
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Slate950,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtext,
                color = accentColor,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun CellVoltageChip(
    cellNumber: Int,
    voltage: Double,
    isBalancing: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isBalancing) Color(0xFF064E3B) else Slate950,
        border = if (isBalancing) androidx.compose.foundation.BorderStroke(1.dp, Emerald600) else null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "C$cellNumber",
                color = if (isBalancing) Emerald600 else Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = String.format("%.3f", voltage),
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
