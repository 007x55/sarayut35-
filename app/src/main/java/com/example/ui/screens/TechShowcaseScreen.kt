package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InverterComponentInfo
import com.example.model.SoyosourceInverterState
import com.example.model.SoyosourceWorkingMode
import com.example.ui.components.Esp32RobotOledWidget
import com.example.ui.components.InteractiveBmsWidget
import com.example.ui.components.SoyosourceGit1200wWidget
import com.example.ui.components.TechBadge
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber600
import com.example.ui.theme.Blue600
import com.example.ui.theme.Cyan300
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Slate900
import com.example.viewmodel.BmsSimState
import com.example.viewmodel.TechSubTab

@Composable
fun TechShowcaseScreen(
    bmsState: BmsSimState,
    soyosourceState: SoyosourceInverterState,
    soyosourceComponents: List<InverterComponentInfo>,
    activeSubTab: TechSubTab,
    onSelectSubTab: (TechSubTab) -> Unit,
    onToggleBalance: () -> Unit,
    onToggleInverterPower: () -> Unit,
    onSelectInverterMode: (SoyosourceWorkingMode) -> Unit,
    onSimulateInverterLoad: (Int) -> Unit,
    onSetInverterMaxPower: (Int) -> Unit,
    onSetInverterCutoffVoltage: (Double) -> Unit,
    onRunInverterDiagnostic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("tech_showcase_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Intro
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                TechBadge(
                    text = "Engineering & Craftsmanship",
                    color = MaterialTheme.colorScheme.primary,
                    containerColor = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "ห้องทดสอบและสาธิตนวัตกรรมช่างชุมชน",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "รวบรวมทักษะวิศวกรรมระดับสูง (Soyosource GIT 1200W Inverter, Hayabusa EV, Smart IoT BMS) และงานช่างหัตถกรรมตัดเย็บเบาะวีลแชร์เพื่อคนพิการ",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    lineHeight = 17.sp
                )
            }
        }

        // Horizontal Category / Sub-tab Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TechSubTab.entries.forEach { tab ->
                val isSelected = activeSubTab == tab
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectSubTab(tab) },
                    leadingIcon = {
                        val icon = when (tab) {
                            TechSubTab.SOYOSOURCE_1200W -> Icons.Default.ElectricMeter
                            TechSubTab.ROBOT_OLED -> Icons.Default.SmartToy
                            TechSubTab.HAYABUSA_EV -> Icons.Default.Speed
                            TechSubTab.SMART_BMS -> Icons.Default.BatteryChargingFull
                            TechSubTab.ANTI_BEDSORE -> Icons.Default.ContentCut
                            TechSubTab.ALL -> Icons.Default.GridView
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("tech_tab_${tab.name.lowercase()}")
                )
            }
        }

        // Section 1: Soyosource GIT-1200W Inverter Lab
        if (activeSubTab == TechSubTab.SOYOSOURCE_1200W || activeSubTab == TechSubTab.ALL) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "1. ห้องทดลองอินเวอร์เตอร์ Soyosource GIT 1200W (Grid-Tie with Limiter)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                SoyosourceGit1200wWidget(
                    state = soyosourceState,
                    components = soyosourceComponents,
                    onTogglePower = onToggleInverterPower,
                    onSelectMode = onSelectInverterMode,
                    onSimulateLoad = onSimulateInverterLoad,
                    onSetMaxPower = onSetInverterMaxPower,
                    onSetCutoffVoltage = onSetInverterCutoffVoltage,
                    onRunDiagnostic = onRunInverterDiagnostic
                )
            }
        }

        // Section 2: ESP32 + SSD1306 OLED Robot Face Animation & C++ Code Generator
        if (activeSubTab == TechSubTab.ROBOT_OLED || activeSubTab == TechSubTab.ALL) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "2. หุ่นยนต์ช่วยฟื้นฟู & แอนิเมชันใบหน้า ESP32 OLED SSD1306 (C++)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Esp32RobotOledWidget()
            }
        }

        // Section 3: Live Interactive BMS Telemetry Demo
        if (activeSubTab == TechSubTab.SMART_BMS || activeSubTab == TechSubTab.ALL) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "3. ระบบตรวจสอบ Smart IoT BMS 16S (LiFePO4 Telemetry)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                InteractiveBmsWidget(
                    bmsState = bmsState,
                    onToggleBalance = onToggleBalance
                )
            }
        }

        // Section 4: Hayabusa EV Conversion & High-Power Drive
        if (activeSubTab == TechSubTab.HAYABUSA_EV || activeSubTab == TechSubTab.ALL) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "4. วิศวกรรมดัดแปลงมอเตอร์ไซค์ไฟฟ้าแรงสูง (Hayabusa EV)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "EV Conversion",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Hayabusa EV Conversion & Inverters",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "รับรองโดย ดร. อภิรักษ์ จันทร (ผู้เชี่ยวชาญ EV)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "โครงการดัดแปลงรถจักรยานยนต์ไฟฟ้าฮายาบูซะ (Suzuki Hayabusa EV) เป็นเครื่องพิสูจน์ขีดความสามารถขั้นสูงในการคำนวณกำลังขับเคลื่อน, การจับคู่อินเวอร์เตอร์แรงบิดสูง, การออกแบบระบบระบายความร้อน, และการแพ็กแบตเตอรี่ลิเธียมที่จ่ายกระแสระดับสูงได้อย่างปลอดภัย",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Highlighting Soyosource 1200W Inverter Link
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ElectricMeter,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "การเชื่อมโยงระบบ: Soyosource GIT-1200W Inverter",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "• นำประสบการณ์ระบบควบคุมอิเล็กทรอนิกส์กำลังจาก Hayabusa EV มาประยุกต์ใช้ในการซ่อมและจูนบอร์ด Soyosource GIT 1200W ในศูนย์ช่างนครนายก\n• จัดการแรงดันและกระแสชาร์จแบตเตอรี่วีลแชร์ได้อย่างแม่นยำ ไม่ทำให้เซลล์บวมหรือเสื่อมสภาพเร็ว",
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 4: Battery Spot Welding & LiFePO4 Pack Assembly
        if (activeSubTab == TechSubTab.SMART_BMS || activeSubTab == TechSubTab.ALL) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BatteryChargingFull,
                                contentDescription = "Battery Clinic",
                                tint = Amber600,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "คลินิกฟื้นฟูและสปอตเซลล์แบตเตอรี่ลิเธียม",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "LiFePO4 & Li-ion Battery Clinic",
                                fontSize = 11.sp,
                                color = Amber600
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "รถวีลแชร์ไฟฟ้าส่วนใหญ่ในนครนายกใช้แบตเตอรี่ตะกั่วกรดที่เสื่อมสภาพเร็วและมีน้ำหนักมาก คลินิกช่างจะนำเครื่องสปอตแบตเตอรี่และเครื่อง Active Balancer มาปรับปรุงเป็นแบตเตอรี่ลิเธียม LiFePO4 ที่มีน้ำหนักเบากว่า 3 เท่า และมีรอบการชาร์จยาวนานกว่า 2,000 รอบ",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TechSpecPill("ความจุ: 24V 20-30Ah", Modifier.weight(1f))
                        TechSpecPill("อายุงาน: 5-8 ปี", Modifier.weight(1f))
                        TechSpecPill("ลดน้ำหนักลง 15-20 กก.", Modifier.weight(1f))
                    }
                }
            }
        }

        // Section 5: Upholstery & Anti-Bedsore Cushion Craft
        if (activeSubTab == TechSubTab.ANTI_BEDSORE || activeSubTab == TechSubTab.ALL) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "5. งานตัดเย็บเบาะพนักพิงป้องกันแผลกดทับ (WFH)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFCFFAFE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCut,
                                    contentDescription = "Upholstery",
                                    tint = Cyan500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "งานตัดเย็บเบาะพนักพิง ป้องกันแผลกดทับ",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "จักรอุตสาหกรรม WFH โดยน้องผู้พิการและมารดา",
                                    fontSize = 11.sp,
                                    color = Cyan500
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "แผลกดทับ (Pressure Ulcers) เป็นสาเหตุสำคัญที่ทำให้ผู้ป่วยวีลแชร์เกิดการติดเชื้อในกระแสเลือด การตัดเย็บเบาะรองนั่งด้วยฟองน้ำอัดแน่นพิเศษผสมเมมโมรี่โฟมทางการแพทย์ หุ้มด้วยหนัง Medical PVC กันน้ำและทำความสะอาดง่าย จะช่วยกระจายแรงกดทับของกระดูกก้นกบและกระดูกสะโพกได้อย่างมีนัยสำคัญ",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "โครงสร้างเบาะ 3 ชั้น (Anti-Bedsore Construction):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "1. ผิวสัมผัสบน: หนัง Medical PVC กันซึม เช็ดฆ่าเชื้อแอลกอฮอล์ได้ 100%\n2. ชั้นกลาง: ฟองน้ำความยืดหยุ่นสูง (High-Resilience Foam) คืนรูปทันที\n3. ชั้นฐานล่าง: ฟองน้ำอัดแน่นเสริมความมั่นคง ไม่ยวบติดโครงเก้าอี้",
                                    fontSize = 10.sp,
                                    lineHeight = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TechSpecPill(text: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
