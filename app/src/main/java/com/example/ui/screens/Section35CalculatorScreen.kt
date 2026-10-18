package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BudgetCalculatorCard
import com.example.ui.components.TechBadge
import com.example.ui.theme.Emerald600
import com.example.viewmodel.Section35CalcState

@Composable
fun Section35CalculatorScreen(
    calcState: Section35CalcState,
    onRightsChanged: (Int) -> Unit,
    onToggleEquipment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("section35_calculator_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Calculator Card
        BudgetCalculatorCard(
            calcState = calcState,
            onRightsChanged = onRightsChanged,
            onToggleEquipment = onToggleEquipment
        )

        // Comparison Table: มาตรา 34 vs มาตรา 35
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Balance,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "เปรียบเทียบ: ส่งเงินกองทุน (ม.34) vs จ้างโครงการนี้ (ม.35)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                ComparisonPoint(
                    title = "1. ด้านผลสัมฤทธิ์ CSR และภาพลักษณ์แบรนด์",
                    fundOption = "ม.34: จ่ายเงินเข้ากองทุนฟื้นฟูฯ องค์กรไม่ได้รับผลงาน CSR ที่นำไปสื่อสารต่อสังคมได้",
                    projectOption = "ม.35: ได้ภาพลักษณ์ 'องค์กรสร้างนวัตกรรมเพื่อคนพิการ' ซ่อมวีลแชร์ฟรีให้คนยากไร้ใน จ.นครนายก ติดโลโก้บริษัทตลอดทั้งปี"
                )

                Spacer(modifier = Modifier.height(10.dp))

                ComparisonPoint(
                    title = "2. ด้านความถูกต้องตามกฎหมายและการรายงาน พม.",
                    fundOption = "ม.34: ส่งใบเสร็จเงินสมทบเข้ากองทุน",
                    projectOption = "ม.35: มีสัญญาจ้างเหมาบริการถูกต้อง 100% พร้อมรายงานผลการปฏิบัติงานจริงและรูปถ่ายส่งกรม พม. ครบถ้วน"
                )

                Spacer(modifier = Modifier.height(10.dp))

                ComparisonPoint(
                    title = "3. สิทธิประโยชน์ทางภาษี (Tax Benefit)",
                    fundOption = "ม.34: ได้ตามเกณฑ์ทั่วไป",
                    projectOption = "ม.35: นำรายจ่ายตามสัญญาจ้างเหมาบริการไปใช้สิทธิยกเว้นภาษีเงินได้นิติบุคคล ช่วยลดภาระภาษีของบริษัท"
                )
            }
        }

        // Section 35 Procedural Steps Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ขั้นตอนการยื่นขอใช้สิทธิ ม.35 ร่วมกับเรา",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val steps = listOf(
                    "1. ตกลงและลงนามในหนังสือแสดงเจตจำนงการให้สิทธิสัมปทาน/จ้างเหมาบริการ",
                    "2. ทีมงานจัดทำเอกสารโครงการพร้อมรายละเอียดผู้พิการและผู้รับรองเสนอต่อสำนักงานจัดหางานจังหวัด",
                    "3. สำนักงานจัดหางานจังหวัดอนุมัติโครงการตามระเบียบ พม.",
                    "4. บริษัทลงนามสัญญาจ้างเหมาบริการรายปี และเริ่มส่งมอบสิทธิประโยชน์",
                    "5. ทีมงานเริ่มภารกิจซ่อมวีลแชร์ฟรีใน จ.นครนายก และจัดส่งรายงานผลงานประจำงวดให้บริษัท"
                )

                steps.forEach { step ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Emerald600,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = step,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun ComparisonPoint(
    title: String,
    fundOption: String,
    projectOption: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = fundOption,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = projectOption,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
