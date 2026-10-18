package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber600
import com.example.ui.theme.Blue600
import com.example.ui.theme.Cyan300
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Rose600
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

enum class RobotFaceMode(val title: String, val thaiDesc: String) {
    NEUTRAL("Neutral / ปกติ", "ตากลมโต กะพริบตาเป็นธรรมชาติ"),
    HAPPY("Happy / ร่าเริง", "ตายิ้มโค้ง แก้มอมชมพู สดใส"),
    WINK("Wink / ขยิบตา", "ตาข้างหนึ่งยิ้ม อีกข้างกะพริบขี้เล่น"),
    LOOK_AROUND("Look / สำรวจ", "กลอกตาซ้าย-ขวา ตรวจจับสิ่งแวดล้อม"),
    SURPRISED("Surprised / ตกใจ", "ตาเบิกกว้าง รูม่านตาขยาย อ้าปาก"),
    SLEEPY("Sleepy / หลับพัก", "เปลือกตาหรี่ลง มีสัญลักษณ์ Zzz ลอย"),
    TALKING("Talking / กำลังพูด", "ขยับปากดิจิทัลประสานกับคำพูด"),
    ANGRY("Focused / มุ่งมั่น", "คิ้วเฉียงลง สายตาคมชัด พร้อมลุย")
}

@Composable
fun Esp32RobotOledWidget(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedMode by remember { mutableStateOf(RobotFaceMode.NEUTRAL) }
    var isAutoCycle by remember { mutableStateOf(false) }
    var isManualBlink by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }
    var selectedCodeTab by remember { mutableIntStateOf(0) }
    var isCopied by remember { mutableStateOf(false) }

    // Auto cycle animations every 4.5 seconds if enabled
    LaunchedEffect(isAutoCycle) {
        if (isAutoCycle) {
            val modes = RobotFaceMode.entries
            var index = 0
            while (true) {
                delay(4000)
                index = (index + 1) % modes.size
                selectedMode = modes[index]
            }
        }
    }

    // Natural random blink timer
    var autoBlinkState by remember { mutableFloatStateOf(1f) }
    LaunchedEffect(selectedMode) {
        if (selectedMode == RobotFaceMode.NEUTRAL || selectedMode == RobotFaceMode.LOOK_AROUND) {
            while (true) {
                delay((2500..4500).random().toLong())
                // Blink down
                autoBlinkState = 0.05f
                delay(120)
                // Open up
                autoBlinkState = 1f
                delay(80)
            }
        } else {
            autoBlinkState = 1f
        }
    }

    // Trigger manual blink
    LaunchedEffect(isManualBlink) {
        if (isManualBlink) {
            autoBlinkState = 0.05f
            delay(150)
            autoBlinkState = 1f
            isManualBlink = false
        }
    }

    // Speech cycle
    val infiniteTransition = rememberInfiniteTransition(label = "robot_anim")
    val speechPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(280, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speech_pulse"
    )

    val lookOffset by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "look_offset"
    )

    val zzzFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "zzz_float"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("esp32_robot_oled_widget"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "ESP32 + OLED SSD1306 Robot Face",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "แอนิเมชันใบหน้าหุ่นยนต์ C++ พร้อมผังต่อสาย I2C (GPIO 4 / GPIO 5)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Emerald600.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Emerald600)
                    ) {
                        Text(
                            text = "Adafruit_SSD1306",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald600,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Live Simulated OLED Screen Display (128x64 Aspect Ratio = 2:1)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF10141D)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFF2B3245), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // OLED Module PCB Bezel Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF38EF7D))
                        )
                        Text(
                            text = "OLED 0.96\" SSD1306 (128x64) • I2C: 0x3C",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8E9BAE)
                        )
                    }

                    Text(
                        text = "SDA: GPIO 4 | SCL: GPIO 5",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Amber400
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // The OLED Screen itself (Monochrome Blue/Cyan Phosphor Display)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f) // 128:64 = 2:1 ratio
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF020712))
                        .border(1.dp, Color(0xFF1E283D), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        drawRobotFaceOnCanvas(
                            mode = selectedMode,
                            blinkFactor = autoBlinkState,
                            speechPulse = speechPulse,
                            lookOffset = lookOffset,
                            zzzFloat = zzzFloat,
                            isSpeaking = isSpeaking
                        )
                    }

                    // Mode Tag Overlay in Corner
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0x99000000),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = selectedMode.title,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00F2FE),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive Quick Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { isManualBlink = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color(0xFF38EF7D),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "สั่งกะพริบตา",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            selectedMode = RobotFaceMode.TALKING
                            isSpeaking = !isSpeaking
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMode == RobotFaceMode.TALKING) MaterialTheme.colorScheme.primary else Color(0xFF1E293B)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Amber400,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ขยับปากพูด",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = { isAutoCycle = !isAutoCycle },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAutoCycle) Cyan300.copy(alpha = 0.3f) else Color(0xFF1E293B)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = if (isAutoCycle) Color(0xFF00F2FE) else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAutoCycle) "วนลูปอยู่" else "วนลูปอารมณ์",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Horizontal Mood Selector Chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "เลือกอารมณ์แอนิเมชันใบหน้าหุ่นยนต์ (Expressions):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RobotFaceMode.entries.forEach { mode ->
                    val isSelected = selectedMode == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedMode = mode
                            isAutoCycle = false
                        },
                        label = {
                            Text(
                                text = mode.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        // Hardware Pinout Guide: ESP32-WROOM-32 to SSD1306 OLED (Exact User Specifications)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = Amber600,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "ผังการต่อสายฮาร์ดแวร์ (ESP32-WROOM-32 ↔ SSD1306 OLED)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Table of Connections
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PinConnectionRow(
                        oledPin = "VCC",
                        espPin = "3.3V (ห้ามต่อ 5V)",
                        wireColor = Color(0xFFEF4444),
                        desc = "แหล่งจ่ายไฟเลี้ยงโมดูลจอ OLED"
                    )
                    PinConnectionRow(
                        oledPin = "GND",
                        espPin = "GND",
                        wireColor = Color(0xFF475569),
                        desc = "กราวด์ร่วมระบบวงจร"
                    )
                    PinConnectionRow(
                        oledPin = "SDA",
                        espPin = "GPIO 4 / D4",
                        wireColor = Color(0xFF3B82F6),
                        desc = "I2C Serial Data (ส่งข้อมูลพิกเซล)"
                    )
                    PinConnectionRow(
                        oledPin = "SCL",
                        espPin = "GPIO 5 / D5",
                        wireColor = Color(0xFFEAB308),
                        desc = "I2C Serial Clock (สัญญาณนาฬิกา)"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "คำสั่งตั้งค่าในโค้ด C++: Wire.begin(4, 5); และ Wire.setClock(400000);",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // C++ Source Code Tabs: oled_display.h, oled_display.cpp, robot_face.ino
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = Color(0xFF38EF7D),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "ชุดซอร์สโค้ด C++ พร้อมใช้งาน (Ready-to-Flash)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Copy Code Button
                    Button(
                        onClick = {
                            val codeToCopy = when (selectedCodeTab) {
                                0 -> oledDisplayHeaderCode
                                1 -> oledDisplayCppCode
                                else -> robotFaceInoCode
                            }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("ESP32 Robot Face Code", codeToCopy)
                            clipboard.setPrimaryClip(clip)
                            isCopied = true
                            Toast.makeText(context, "คัดลอกโค้ด C++ สำเร็จแล้ว!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38EF7D)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isCopied) "คัดลอกแล้ว" else "คัดลอกไฟล์นี้",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Code Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedCodeTab,
                    containerColor = Color(0xFF1E293B),
                    contentColor = Color.White,
                    edgePadding = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Tab(
                        selected = selectedCodeTab == 0,
                        onClick = {
                            selectedCodeTab = 0
                            isCopied = false
                        },
                        text = {
                            Text(
                                "oled_display.h",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (selectedCodeTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedCodeTab == 1,
                        onClick = {
                            selectedCodeTab = 1
                            isCopied = false
                        },
                        text = {
                            Text(
                                "oled_display.cpp",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (selectedCodeTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedCodeTab == 2,
                        onClick = {
                            selectedCodeTab = 2
                            isCopied = false
                        },
                        text = {
                            Text(
                                "robot_face.ino (Main)",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (selectedCodeTab == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Code Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF020712))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                        .horizontalScroll(rememberScrollState())
                ) {
                    val currentCode = when (selectedCodeTab) {
                        0 -> oledDisplayHeaderCode
                        1 -> oledDisplayCppCode
                        else -> robotFaceInoCode
                    }
                    Text(
                        text = currentCode,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = Color(0xFF38EF7D),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PinConnectionRow(
    oledPin: String,
    espPin: String,
    wireColor: Color,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(wireColor)
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFF1E293B)
            ) {
                Text(
                    text = "OLED $oledPin",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Text(
                text = "➔",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "ESP32 $espPin",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Text(
            text = desc,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    }
}

// Procedural Drawing Logic for 128x64 Canvas simulating SSD1306 OLED
private fun DrawScope.drawRobotFaceOnCanvas(
    mode: RobotFaceMode,
    blinkFactor: Float,
    speechPulse: Float,
    lookOffset: Float,
    zzzFloat: Float,
    isSpeaking: Boolean
) {
    val oledColor = Color(0xFF00F2FE) // Glowing Cyan OLED Phosphor
    val glowColor = Color(0x3300F2FE)
    val width = size.width
    val height = size.height

    // Coordinate mapping: 128x64 scaled to Canvas size
    val scaleX = width / 128f
    val scaleY = height / 64f

    fun px(x: Float) = x * scaleX
    fun py(y: Float) = y * scaleY

    val leftEyeCenterX = 40f
    val rightEyeCenterX = 88f
    val eyeCenterY = 30f
    val baseEyeWidth = 28f
    val baseEyeHeight = 34f

    when (mode) {
        RobotFaceMode.NEUTRAL -> {
            val curHeight = baseEyeHeight * blinkFactor
            val curY = eyeCenterY - (curHeight / 2f)

            // Left Eye
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(leftEyeCenterX - baseEyeWidth / 2f), py(curY)),
                size = Size(px(baseEyeWidth), py(curHeight)),
                cornerRadius = CornerRadius(px(6f), px(6f))
            )
            // Right Eye
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(rightEyeCenterX - baseEyeWidth / 2f), py(curY)),
                size = Size(px(baseEyeWidth), py(curHeight)),
                cornerRadius = CornerRadius(px(6f), px(6f))
            )

            // Cute Pupil Highlights (when eyes open)
            if (blinkFactor > 0.6f) {
                drawCircle(
                    color = Color(0xFF020712),
                    radius = px(3.5f),
                    center = Offset(px(leftEyeCenterX + 4f), py(eyeCenterY - 6f))
                )
                drawCircle(
                    color = Color(0xFF020712),
                    radius = px(3.5f),
                    center = Offset(px(rightEyeCenterX + 4f), py(eyeCenterY - 6f))
                )
            }
        }

        RobotFaceMode.HAPPY -> {
            // Arched smiling crescent eyes
            val pathLeft = Path().apply {
                moveTo(px(leftEyeCenterX - 14f), py(eyeCenterY + 6f))
                quadraticTo(
                    px(leftEyeCenterX), py(eyeCenterY - 14f),
                    px(leftEyeCenterX + 14f), py(eyeCenterY + 6f)
                )
            }
            drawPath(pathLeft, oledColor, style = Stroke(width = px(5.5f), cap = StrokeCap.Round))

            val pathRight = Path().apply {
                moveTo(px(rightEyeCenterX - 14f), py(eyeCenterY + 6f))
                quadraticTo(
                    px(rightEyeCenterX), py(eyeCenterY - 14f),
                    px(rightEyeCenterX + 14f), py(eyeCenterY + 6f)
                )
            }
            drawPath(pathRight, oledColor, style = Stroke(width = px(5.5f), cap = StrokeCap.Round))

            // Blush Cheeks (Dotted or mini circles)
            drawCircle(oledColor, radius = px(3.5f), center = Offset(px(20f), py(46f)))
            drawCircle(oledColor, radius = px(3.5f), center = Offset(px(108f), py(46f)))

            // Gentle mouth smile curve
            val mouth = Path().apply {
                moveTo(px(58f), py(50f))
                quadraticTo(px(64f), py(56f), px(70f), py(50f))
            }
            drawPath(mouth, oledColor, style = Stroke(width = px(2.5f), cap = StrokeCap.Round))
        }

        RobotFaceMode.WINK -> {
            // Left Eye: Happy Arc
            val pathLeft = Path().apply {
                moveTo(px(leftEyeCenterX - 14f), py(eyeCenterY + 4f))
                quadraticTo(
                    px(leftEyeCenterX), py(eyeCenterY - 14f),
                    px(leftEyeCenterX + 14f), py(eyeCenterY + 4f)
                )
            }
            drawPath(pathLeft, oledColor, style = Stroke(width = px(5.5f), cap = StrokeCap.Round))

            // Right Eye: Wink Line with playful arrow/star
            drawLine(
                color = oledColor,
                start = Offset(px(rightEyeCenterX - 14f), py(eyeCenterY + 2f)),
                end = Offset(px(rightEyeCenterX + 14f), py(eyeCenterY + 2f)),
                strokeWidth = px(5.5f),
                cap = StrokeCap.Round
            )
            // Wink eyelash accents
            drawLine(
                color = oledColor,
                start = Offset(px(rightEyeCenterX + 14f), py(eyeCenterY + 2f)),
                end = Offset(px(rightEyeCenterX + 20f), py(eyeCenterY - 4f)),
                strokeWidth = px(2.5f),
                cap = StrokeCap.Round
            )

            // Smile
            val mouth = Path().apply {
                moveTo(px(60f), py(50f))
                quadraticTo(px(66f), py(56f), px(72f), py(51f))
            }
            drawPath(mouth, oledColor, style = Stroke(width = px(2.5f), cap = StrokeCap.Round))
        }

        RobotFaceMode.LOOK_AROUND -> {
            val shift = lookOffset
            val curHeight = baseEyeHeight * blinkFactor
            val curY = eyeCenterY - (curHeight / 2f)

            // Left Eye
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(leftEyeCenterX - baseEyeWidth / 2f + shift), py(curY)),
                size = Size(px(baseEyeWidth), py(curHeight)),
                cornerRadius = CornerRadius(px(6f), px(6f))
            )
            // Right Eye
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(rightEyeCenterX - baseEyeWidth / 2f + shift), py(curY)),
                size = Size(px(baseEyeWidth), py(curHeight)),
                cornerRadius = CornerRadius(px(6f), px(6f))
            )

            // Pupils looking in direction
            if (blinkFactor > 0.6f) {
                val pupilShift = if (shift > 0) 5f else -5f
                drawCircle(
                    color = Color(0xFF020712),
                    radius = px(4f),
                    center = Offset(px(leftEyeCenterX + shift + pupilShift), py(eyeCenterY))
                )
                drawCircle(
                    color = Color(0xFF020712),
                    radius = px(4f),
                    center = Offset(px(rightEyeCenterX + shift + pupilShift), py(eyeCenterY))
                )
            }
        }

        RobotFaceMode.SURPRISED -> {
            // Big round dilated circular eyes
            drawCircle(
                color = oledColor,
                radius = px(17f),
                center = Offset(px(leftEyeCenterX), py(eyeCenterY - 2f))
            )
            drawCircle(
                color = oledColor,
                radius = px(17f),
                center = Offset(px(rightEyeCenterX), py(eyeCenterY - 2f))
            )

            // Inner dark pupils
            drawCircle(
                color = Color(0xFF020712),
                radius = px(7f),
                center = Offset(px(leftEyeCenterX), py(eyeCenterY - 2f))
            )
            drawCircle(
                color = Color(0xFF020712),
                radius = px(7f),
                center = Offset(px(rightEyeCenterX), py(eyeCenterY - 2f))
            )

            // Open surprised mouth ("O")
            drawOval(
                color = oledColor,
                topLeft = Offset(px(60f), py(46f)),
                size = Size(px(8f), py(12f)),
                style = Stroke(width = px(2f))
            )
        }

        RobotFaceMode.SLEEPY -> {
            // Half-closed eyelids (horizontal slits or bottom half)
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(leftEyeCenterX - baseEyeWidth / 2f), py(eyeCenterY + 4f)),
                size = Size(px(baseEyeWidth), py(8f)),
                cornerRadius = CornerRadius(px(4f), px(4f))
            )
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(rightEyeCenterX - baseEyeWidth / 2f), py(eyeCenterY + 4f)),
                size = Size(px(baseEyeWidth), py(8f)),
                cornerRadius = CornerRadius(px(4f), px(4f))
            )

            // Floating Zzz animation
            val zY = 36f - zzzFloat
            if (zY > 4f) {
                // Large Z
                drawLine(oledColor, Offset(px(100f), py(zY)), Offset(px(112f), py(zY)), strokeWidth = px(2f))
                drawLine(oledColor, Offset(px(112f), py(zY)), Offset(px(100f), py(zY + 8f)), strokeWidth = px(2f))
                drawLine(oledColor, Offset(px(100f), py(zY + 8f)), Offset(px(112f), py(zY + 8f)), strokeWidth = px(2f))

                // Small z higher up
                val smZ = zY - 10f
                if (smZ > 4f) {
                    drawLine(oledColor, Offset(px(112f), py(smZ)), Offset(px(118f), py(smZ)), strokeWidth = px(1.5f))
                    drawLine(oledColor, Offset(px(118f), py(smZ)), Offset(px(112f), py(smZ + 5f)), strokeWidth = px(1.5f))
                    drawLine(oledColor, Offset(px(112f), py(smZ + 5f)), Offset(px(118f), py(smZ + 5f)), strokeWidth = px(1.5f))
                }
            }
        }

        RobotFaceMode.TALKING -> {
            // Regular eyes with slight lively pulse
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(leftEyeCenterX - baseEyeWidth / 2f), py(eyeCenterY - baseEyeHeight / 2f)),
                size = Size(px(baseEyeWidth), py(baseEyeHeight)),
                cornerRadius = CornerRadius(px(6f), px(6f))
            )
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(rightEyeCenterX - baseEyeWidth / 2f), py(eyeCenterY - baseEyeHeight / 2f)),
                size = Size(px(baseEyeWidth), py(baseEyeHeight)),
                cornerRadius = CornerRadius(px(6f), px(6f))
            )

            // Animated Digital Equalizer / Mouth Speech Bar
            val mouthHeight = 3f + (speechPulse * 12f)
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(52f), py(54f - mouthHeight / 2f)),
                size = Size(px(24f), py(mouthHeight)),
                cornerRadius = CornerRadius(px(3f), px(3f))
            )
            // Left and right mini speech bars
            val sideHeight = 2f + ((1f - speechPulse) * 8f)
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(46f), py(54f - sideHeight / 2f)),
                size = Size(px(4f), py(sideHeight)),
                cornerRadius = CornerRadius(px(2f), px(2f))
            )
            drawRoundRect(
                color = oledColor,
                topLeft = Offset(px(78f), py(54f - sideHeight / 2f)),
                size = Size(px(4f), py(sideHeight)),
                cornerRadius = CornerRadius(px(2f), px(2f))
            )
        }

        RobotFaceMode.ANGRY -> {
            // Narrow eyes with diagonal slanted brows cutting downward
            val pathLeft = Path().apply {
                moveTo(px(leftEyeCenterX - 14f), py(eyeCenterY - 4f))
                lineTo(px(leftEyeCenterX + 14f), py(eyeCenterY - 14f))
                lineTo(px(leftEyeCenterX + 14f), py(eyeCenterY + 12f))
                lineTo(px(leftEyeCenterX - 14f), py(eyeCenterY + 12f))
                close()
            }
            drawPath(pathLeft, oledColor)

            val pathRight = Path().apply {
                moveTo(px(rightEyeCenterX + 14f), py(eyeCenterY - 4f))
                lineTo(px(rightEyeCenterX - 14f), py(eyeCenterY - 14f))
                lineTo(px(rightEyeCenterX - 14f), py(eyeCenterY + 12f))
                lineTo(px(rightEyeCenterX + 14f), py(eyeCenterY + 12f))
                close()
            }
            drawPath(pathRight, oledColor)

            // Firm mouth line
            drawLine(
                color = oledColor,
                start = Offset(px(56f), py(52f)),
                end = Offset(px(72f), py(52f)),
                strokeWidth = px(3f),
                cap = StrokeCap.Round
            )
        }
    }
}

// C++ Source Code Templates for ESP32 and SSD1306
private const val oledDisplayHeaderCode = """// ============================================================================
// oled_display.h - Expressive Robot Face Animation Engine for SSD1306 OLED
// Target Hardware: ESP32-WROOM-32 (SDA=GPIO4, SCL=GPIO5)
// Library: Adafruit_GFX + Adafruit_SSD1306
// ============================================================================

#ifndef OLED_DISPLAY_H
#define OLED_DISPLAY_H

#include <Arduino.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

// OLED Hardware Dimensions
#define SCREEN_WIDTH  128
#define SCREEN_HEIGHT 64
#define OLED_RESET    -1     // Reset pin # (or -1 if sharing ESP32 reset pin)
#define SCREEN_I2C_ADDR 0x3C // Typical I2C address for SSD1306 (or 0x3D)

// ESP32 I2C Pinout Mapping (As Requested)
#define OLED_SDA_PIN  4      // ESP32 GPIO 4 / D4
#define OLED_SCL_PIN  5      // ESP32 GPIO 5 / D5

// Robot Expressions Enum
enum RobotExpression {
  EXPR_NEUTRAL,     // Normal open eyes with procedural natural blinking
  EXPR_HAPPY,       // Arched laughing crescent eyes + cheek blush + smile
  EXPR_WINK,        // Playful wink with one happy eye and one wink line
  EXPR_LOOK_LEFT,   // Pupils smoothly scanning left
  EXPR_LOOK_RIGHT,  // Pupils smoothly scanning right
  EXPR_SURPRISED,   // Dilated circular eyes + open "O" mouth
  EXPR_SLEEPY,      // Drooping half-lidded eyes + floating Zzz animation
  EXPR_TALKING,     // Dynamic audio waveform / mouth moving
  EXPR_ANGRY        // Sharp slanted brows + intense focused gaze
};

class RobotFaceDisplay {
public:
  RobotFaceDisplay();

  // Lifecycle
  bool begin();
  void update(); // Call frequently in loop() - Non-blocking!

  // Expression Controls
  void setExpression(RobotExpression expr);
  RobotExpression getExpression() const { return _currentExpr; }
  void triggerBlink();
  void setSpeaking(bool speaking);

  // Diagnostics
  Adafruit_SSD1306& getDisplay() { return _display; }

private:
  Adafruit_SSD1306 _display;
  RobotExpression _currentExpr;

  // Eye Geometry Parameters
  int _eyeWidth;
  int _eyeHeight;
  int _leftEyeX;
  int _rightEyeX;
  int _eyeY;
  int _cornerRadius;

  // Procedural Blink State Machine
  bool _isBlinking;
  unsigned long _lastBlinkTime;
  unsigned long _blinkDuration;
  unsigned long _blinkInterval;
  float _blinkProgress; // 0.0 (open) to 1.0 (closed)

  // Floating Zzz State Machine (Sleep)
  int _zzzOffset;
  unsigned long _lastZzzTime;

  // Talking / Speech State Machine
  bool _isSpeaking;
  unsigned long _lastSpeechTime;
  int _mouthStep;

  // Internal Draw Routines
  void drawNeutralEyes(float blinkRatio);
  void drawHappyFace();
  void drawWinkFace();
  void drawLookEyes(int offsetX, int offsetY);
  void drawSurprisedFace();
  void drawSleepyFace();
  void drawTalkingFace();
  void drawAngryFace();

  // Helper primitives
  void drawCheekBlush(int x, int y);
  void drawZzz(int x, int y, int size);
};

#endif // OLED_DISPLAY_H
"""

private const val oledDisplayCppCode = """// ============================================================================
// oled_display.cpp - Implementation of Robot Face Animations for ESP32
// ============================================================================

#include "oled_display.h"

RobotFaceDisplay::RobotFaceDisplay()
  : _display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET),
    _currentExpr(EXPR_NEUTRAL),
    _eyeWidth(28),
    _eyeHeight(34),
    _leftEyeX(26),
    _rightEyeX(74),
    _eyeY(15),
    _cornerRadius(6),
    _isBlinking(false),
    _lastBlinkTime(0),
    _blinkDuration(140),
    _blinkInterval(3500),
    _blinkProgress(0.0f),
    _zzzOffset(0),
    _lastZzzTime(0),
    _isSpeaking(false),
    _lastSpeechTime(0),
    _mouthStep(0)
{
}

bool RobotFaceDisplay::begin() {
  // Initialize Custom I2C Bus on ESP32 Pins: SDA = GPIO 4, SCL = GPIO 5
  Wire.begin(OLED_SDA_PIN, OLED_SCL_PIN);
  Wire.setClock(400000); // 400kHz Fast I2C for 45+ FPS smooth animations

  // Initialize SSD1306 Display Controller
  if (!_display.begin(SSD1306_SWITCHCAPVCC, SCREEN_I2C_ADDR)) {
    Serial.println(F("[ERROR] SSD1306 OLED initialization failed! Check wiring."));
    return false;
  }

  _display.clearDisplay();
  _display.setTextColor(SSD1306_WHITE);
  _display.display();

  Serial.println(F("[SUCCESS] OLED SSD1306 initialized on ESP32 (SDA:4, SCL:5)"));
  return true;
}

void RobotFaceDisplay::setExpression(RobotExpression expr) {
  _currentExpr = expr;
  _isBlinking = false;
  _blinkProgress = 0.0f;
}

void RobotFaceDisplay::triggerBlink() {
  _isBlinking = true;
  _lastBlinkTime = millis();
}

void RobotFaceDisplay::setSpeaking(bool speaking) {
  _isSpeaking = speaking;
  if (speaking) {
    _currentExpr = EXPR_TALKING;
  } else if (_currentExpr == EXPR_TALKING) {
    _currentExpr = EXPR_NEUTRAL;
  }
}

void RobotFaceDisplay::update() {
  unsigned long now = millis();

  // Natural Blink State Machine
  if (_currentExpr == EXPR_NEUTRAL || _currentExpr == EXPR_LOOK_LEFT || _currentExpr == EXPR_LOOK_RIGHT) {
    if (!_isBlinking && (now - _lastBlinkTime >= _blinkInterval)) {
      _isBlinking = true;
      _lastBlinkTime = now;
      _blinkInterval = random(2500, 5000); // Randomized realistic timing
    }

    if (_isBlinking) {
      unsigned long elapsed = now - _lastBlinkTime;
      if (elapsed <= _blinkDuration / 2) {
        // Eye closing
        _blinkProgress = (float)elapsed / (_blinkDuration / 2.0f);
      } else if (elapsed <= _blinkDuration) {
        // Eye opening
        _blinkProgress = 1.0f - ((float)(elapsed - (_blinkDuration / 2)) / (_blinkDuration / 2.0f));
      } else {
        _isBlinking = false;
        _blinkProgress = 0.0f;
      }
    }
  }

  // Clear Frame Buffer
  _display.clearDisplay();

  // Render Active Emotion Expression
  switch (_currentExpr) {
    case EXPR_NEUTRAL:
      drawNeutralEyes(_blinkProgress);
      break;

    case EXPR_HAPPY:
      drawHappyFace();
      break;

    case EXPR_WINK:
      drawWinkFace();
      break;

    case EXPR_LOOK_LEFT:
      drawLookEyes(-8, 0);
      break;

    case EXPR_LOOK_RIGHT:
      drawLookEyes(8, 0);
      break;

    case EXPR_SURPRISED:
      drawSurprisedFace();
      break;

    case EXPR_SLEEPY:
      drawSleepyFace();
      break;

    case EXPR_TALKING:
      drawTalkingFace();
      break;

    case EXPR_ANGRY:
      drawAngryFace();
      break;
  }

  // Push Frame to OLED Display via Hardware I2C
  _display.display();
}

// ----------------------------------------------------------------------------
// Procedural Expression Draw Routines
// ----------------------------------------------------------------------------

void RobotFaceDisplay::drawNeutralEyes(float blinkRatio) {
  int h = _eyeHeight * (1.0f - (blinkRatio * 0.92f));
  if (h < 2) h = 2;
  int y = _eyeY + ((_eyeHeight - h) / 2);

  // Left & Right Rounded Eyes
  _display.fillRoundRect(_leftEyeX, y, _eyeWidth, h, _cornerRadius, SSD1306_WHITE);
  _display.fillRoundRect(_rightEyeX, y, _eyeWidth, h, _cornerRadius, SSD1306_WHITE);

  // Inner Pupil Glimmer (only visible when eyes are mostly open)
  if (blinkRatio < 0.4f) {
    _display.fillCircle(_leftEyeX + _eyeWidth - 7, _eyeY + 8, 3, SSD1306_BLACK);
    _display.fillCircle(_rightEyeX + _eyeWidth - 7, _eyeY + 8, 3, SSD1306_BLACK);
  }
}

void RobotFaceDisplay::drawHappyFace() {
  // Left Happy Crescent Eye
  for (int t = 0; t < 5; t++) {
    _display.drawLine(_leftEyeX, _eyeY + 24 - t, _leftEyeX + 14, _eyeY + 8 - t, SSD1306_WHITE);
    _display.drawLine(_leftEyeX + 14, _eyeY + 8 - t, _leftEyeX + _eyeWidth, _eyeY + 24 - t, SSD1306_WHITE);
  }

  // Right Happy Crescent Eye
  for (int t = 0; t < 5; t++) {
    _display.drawLine(_rightEyeX, _eyeY + 24 - t, _rightEyeX + 14, _eyeY + 8 - t, SSD1306_WHITE);
    _display.drawLine(_rightEyeX + 14, _eyeY + 8 - t, _rightEyeX + _eyeWidth, _eyeY + 24 - t, SSD1306_WHITE);
  }

  // Cute Cheek Blush Dots
  drawCheekBlush(16, 44);
  drawCheekBlush(104, 44);

  // Gentle Smiling Mouth
  _display.drawPixel(61, 51, SSD1306_WHITE);
  _display.drawPixel(62, 52, SSD1306_WHITE);
  _display.drawLine(63, 53, 65, 53, SSD1306_WHITE);
  _display.drawPixel(66, 52, SSD1306_WHITE);
  _display.drawPixel(67, 51, SSD1306_WHITE);
}

void RobotFaceDisplay::drawWinkFace() {
  // Left Eye: Happy Arched Crescent
  for (int t = 0; t < 5; t++) {
    _display.drawLine(_leftEyeX, _eyeY + 22 - t, _leftEyeX + 14, _eyeY + 8 - t, SSD1306_WHITE);
    _display.drawLine(_leftEyeX + 14, _eyeY + 8 - t, _leftEyeX + _eyeWidth, _eyeY + 22 - t, SSD1306_WHITE);
  }

  // Right Eye: Wink Line with Eyelash Accent
  _display.fillRoundRect(_rightEyeX, _eyeY + 16, _eyeWidth, 5, 2, SSD1306_WHITE);
  _display.drawLine(_rightEyeX + _eyeWidth, _eyeY + 16, _rightEyeX + _eyeWidth + 5, _eyeY + 10, SSD1306_WHITE);

  // Smile
  _display.drawLine(62, 51, 66, 51, SSD1306_WHITE);
}

void RobotFaceDisplay::drawLookEyes(int offsetX, int offsetY) {
  int curLeftX = _leftEyeX + offsetX;
  int curRightX = _rightEyeX + offsetX;
  int curY = _eyeY + offsetY;

  _display.fillRoundRect(curLeftX, curY, _eyeWidth, _eyeHeight, _cornerRadius, SSD1306_WHITE);
  _display.fillRoundRect(curRightX, curY, _eyeWidth, _eyeHeight, _cornerRadius, SSD1306_WHITE);

  // Inward looking pupils
  int pOffset = (offsetX < 0) ? -5 : 5;
  _display.fillCircle(curLeftX + (_eyeWidth / 2) + pOffset, curY + (_eyeHeight / 2), 4, SSD1306_BLACK);
  _display.fillCircle(curRightX + (_eyeWidth / 2) + pOffset, curY + (_eyeHeight / 2), 4, SSD1306_BLACK);
}

void RobotFaceDisplay::drawSurprisedFace() {
  // Wide open circular eyes
  _display.fillCircle(_leftEyeX + 14, _eyeY + 16, 16, SSD1306_WHITE);
  _display.fillCircle(_rightEyeX + 14, _eyeY + 16, 16, SSD1306_WHITE);

  // Dilated black center pupils
  _display.fillCircle(_leftEyeX + 14, _eyeY + 16, 7, SSD1306_BLACK);
  _display.fillCircle(_rightEyeX + 14, _eyeY + 16, 7, SSD1306_BLACK);

  // Open "O" mouth
  _display.drawCircle(64, 52, 6, SSD1306_WHITE);
  _display.drawCircle(64, 52, 5, SSD1306_WHITE);
}

void RobotFaceDisplay::drawSleepyFace() {
  // Half-closed eyelids (drooping bars)
  _display.fillRoundRect(_leftEyeX, _eyeY + 18, _eyeWidth, 8, 3, SSD1306_WHITE);
  _display.fillRoundRect(_rightEyeX, _eyeY + 18, _eyeWidth, 8, 3, SSD1306_WHITE);

  // Floating Zzz animation
  if (millis() - _lastZzzTime > 80) {
    _lastZzzTime = millis();
    _zzzOffset = (_zzzOffset + 1) % 24;
  }

  int zY = 32 - _zzzOffset;
  if (zY > 4) {
    drawZzz(100, zY, 8);
  }
}

void RobotFaceDisplay::drawTalkingFace() {
  // Normal open eyes
  _display.fillRoundRect(_leftEyeX, _eyeY, _eyeWidth, _eyeHeight, _cornerRadius, SSD1306_WHITE);
  _display.fillRoundRect(_rightEyeX, _eyeY, _eyeWidth, _eyeHeight, _cornerRadius, SSD1306_WHITE);

  // Animated speaking waveform mouth
  if (millis() - _lastSpeechTime > 90) {
    _lastSpeechTime = millis();
    _mouthStep = (_mouthStep + 1) % 4;
  }

  int mouthH = 3 + (_mouthStep * 3);
  _display.fillRoundRect(52, 54 - (mouthH / 2), 24, mouthH, 3, SSD1306_WHITE);
  _display.fillRoundRect(46, 54 - 2, 4, 4, 1, SSD1306_WHITE);
  _display.fillRoundRect(78, 54 - 2, 4, 4, 1, SSD1306_WHITE);
}

void RobotFaceDisplay::drawAngryFace() {
  // Left eye cut by 45 degree slanted brow
  for (int x = 0; x < _eyeWidth; x++) {
    int topY = _eyeY + (x * 0.5f);
    int h = _eyeHeight - (x * 0.5f);
    if (h > 0) {
      _display.drawFastVLine(_leftEyeX + x, topY, h, SSD1306_WHITE);
    }
  }

  // Right eye cut by opposite slanted brow
  for (int x = 0; x < _eyeWidth; x++) {
    int topY = _eyeY + ((_eyeWidth - 1 - x) * 0.5f);
    int h = _eyeHeight - ((_eyeWidth - 1 - x) * 0.5f);
    if (h > 0) {
      _display.drawFastVLine(_rightEyeX + x, topY, h, SSD1306_WHITE);
    }
  }

  // Firm mouth line
  _display.drawLine(56, 54, 72, 54, SSD1306_WHITE);
  _display.drawLine(56, 55, 72, 55, SSD1306_WHITE);
}

void RobotFaceDisplay::drawCheekBlush(int x, int y) {
  _display.drawCircle(x, y, 3, SSD1306_WHITE);
  _display.drawPixel(x, y, SSD1306_WHITE);
}

void RobotFaceDisplay::drawZzz(int x, int y, int size) {
  _display.drawLine(x, y, x + size, y, SSD1306_WHITE);
  _display.drawLine(x + size, y, x, y + size, SSD1306_WHITE);
  _display.drawLine(x, y + size, x + size, y + size, SSD1306_WHITE);
}
"""

private const val robotFaceInoCode = """// ============================================================================
// robot_face.ino - Main Arduino Sketch for ESP32-WROOM-32 & SSD1306 OLED
//
// Wiring Connection:
// OLED Module       ESP32-WROOM-32
// -----------       --------------
// VCC               3.3V (Do not use 5V unless board has 3.3V regulator!)
// GND               GND
// SDA               GPIO 4 / D4
// SCL               GPIO 5 / D5
//
// Required Libraries (Install via Arduino Library Manager):
// 1. Adafruit SSD1306 by Adafruit
// 2. Adafruit GFX Library by Adafruit
// 3. Adafruit BusIO by Adafruit
// ============================================================================

#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include "oled_display.h"

// Instantiate Robot Face Controller
RobotFaceDisplay robotFace;

// Non-blocking timer for demo emotion switching
unsigned long lastDemoSwitchTime = 0;
int currentDemoMode = 0;

void setup() {
  Serial.begin(115200);
  delay(500);

  Serial.println(F("\n=============================================="));
  Serial.println(F("ESP32-WROOM-32 Robot Face Animation System"));
  Serial.println(F("Hardware I2C: SDA=GPIO4, SCL=GPIO5"));
  Serial.println(F("=============================================="));

  // Initialize Robot Face OLED Engine
  if (!robotFace.begin()) {
    Serial.println(F("[FATAL] Check OLED power and wiring!"));
    while (true) { delay(1000); }
  }

  Serial.println(F("Ready! Send keys via Serial Monitor to test:"));
  Serial.println(F("  '1' -> Neutral (Auto-blink)"));
  Serial.println(F("  '2' -> Happy (Crescent smile + blush)"));
  Serial.println(F("  '3' -> Wink (Playful)"));
  Serial.println(F("  '4' -> Look Left"));
  Serial.println(F("  '5' -> Look Right"));
  Serial.println(F("  '6' -> Surprised (Dilated pupils)"));
  Serial.println(F("  '7' -> Sleepy (Zzz floating)"));
  Serial.println(F("  '8' -> Talking (Mouth soundbar)"));
  Serial.println(F("  '9' -> Angry (Focused)"));
  Serial.println(F("  'b' -> Trigger instant blink"));
}

void loop() {
  // Update animation frame (MUST be called as fast as possible in loop)
  robotFace.update();

  // Handle Serial Commands for interactive testing
  if (Serial.available()) {
    char c = Serial.read();
    switch (c) {
      case '1': robotFace.setExpression(EXPR_NEUTRAL); break;
      case '2': robotFace.setExpression(EXPR_HAPPY); break;
      case '3': robotFace.setExpression(EXPR_WINK); break;
      case '4': robotFace.setExpression(EXPR_LOOK_LEFT); break;
      case '5': robotFace.setExpression(EXPR_LOOK_RIGHT); break;
      case '6': robotFace.setExpression(EXPR_SURPRISED); break;
      case '7': robotFace.setExpression(EXPR_SLEEPY); break;
      case '8': robotFace.setExpression(EXPR_TALKING); break;
      case '9': robotFace.setExpression(EXPR_ANGRY); break;
      case 'b': robotFace.triggerBlink(); break;
    }
  }

  // Automatic Demo Mode: Switch expression every 5 seconds
  /*
  if (millis() - lastDemoSwitchTime >= 5000) {
    lastDemoSwitchTime = millis();
    currentDemoMode = (currentDemoMode + 1) % 8;
    robotFace.setExpression((RobotExpression)currentDemoMode);
  }
  */
}
"""
