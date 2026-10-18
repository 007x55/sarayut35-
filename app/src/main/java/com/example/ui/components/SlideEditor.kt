package com.example.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlaceholderPreset
import com.example.model.PresentationSlide
import com.example.model.SlideImagePlaceholder
import com.example.model.SlideLayoutType
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue600
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Rose600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideEditor(
    slide: PresentationSlide,
    onSave: (PresentationSlide) -> Unit,
    onDelete: (Int) -> Unit,
    onDismiss: () -> Unit,
    onOpenAiGenerator: ((initialSummary: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Form states initialized with current slide data
    var tag by remember { mutableStateOf(slide.tag) }
    var title by remember { mutableStateOf(slide.title) }
    var subtitle by remember { mutableStateOf(slide.subtitle) }
    val keyPoints = remember { mutableStateListOf(*slide.keyPoints.toTypedArray()) }
    var speakerNotes by remember { mutableStateOf(slide.speakerNotes) }
    var calloutText by remember { mutableStateOf(slide.calloutText ?: "") }
    var selectedLayout by remember { mutableStateOf(slide.layoutType) }

    // Image placeholder states
    var enableImagePlaceholder by remember { mutableStateOf(slide.imagePlaceholder != null) }
    var selectedPreset by remember { mutableStateOf(slide.imagePlaceholder?.preset ?: PlaceholderPreset.WHEELCHAIR_CLINIC) }
    var imageTitle by remember { mutableStateOf(slide.imagePlaceholder?.title ?: selectedPreset.defaultTitle) }
    var imageCaption by remember { mutableStateOf(slide.imagePlaceholder?.caption ?: selectedPreset.defaultCaption) }
    var customImageUri by remember { mutableStateOf(slide.imagePlaceholder?.uriString) }

    // Editor tab index: 0 = Content, 1 = Layout, 2 = Image Placeholder, 3 = Live Preview
    var selectedTab by remember { mutableIntStateOf(0) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    // Android zero-permission Photo Picker for custom images
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            customImageUri = uri.toString()
            selectedPreset = PlaceholderPreset.CUSTOM
            Toast.makeText(context, "เลือกรูปภาพประกอบสำเร็จ", Toast.LENGTH_SHORT).show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("slide_editor_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Top Bar: Title + Save / Cancel / Delete Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "แก้ไขสไลด์นำเสนอ (Slide Editor)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "ปรับแต่งข้อความ เลย์เอาต์ และรูปภาพประกอบสไลด์",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.testTag("editor_delete_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Slide",
                            tint = Rose600
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Editor Sub-tabs
            val tabTitles = listOf("1. ข้อความ & หัวข้อ", "2. เลย์เอาต์", "3. ภาพประกอบ", "4. ตัวอย่างจริง")
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { index, titleText ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = titleText,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Content with Scroll
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                when (selectedTab) {
                    // TAB 0: Content & Bullets
                    0 -> {
                        // AI Assistant Card
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val summarySeed = if (title.isNotBlank()) "$title: $subtitle" else tag
                                    onOpenAiGenerator?.invoke(summarySeed)
                                }
                                .testTag("editor_ai_assist_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(Amber500),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "ให้ Gemini AI ช่วยร่างเนื้อหาและ Bullet Points",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "วิเคราะห์ประเด็นสไลด์และสร้างข้อความนำเสนออัตโนมัติ",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Gemini 3.5",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Tag Input
                        OutlinedTextField(
                            value = tag,
                            onValueChange = { tag = it },
                            label = { Text("หมวดหมู่ / ป้ายกำกับสไลด์") },
                            placeholder = { Text("เช่น วิศวกรรม EV & IoT, งบประมาณ, CSR") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("slide_tag_input")
                        )

                        // Title Input
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("หัวข้อหลักของสไลด์ (Slide Title)") },
                            singleLine = false,
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("slide_title_input")
                        )

                        // Subtitle Input
                        OutlinedTextField(
                            value = subtitle,
                            onValueChange = { subtitle = it },
                            label = { Text("คำบรรยายรอง (Subtitle)") },
                            singleLine = false,
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("slide_subtitle_input")
                        )

                        // Key Points Bullet List
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "ประเด็นสำคัญในสไลด์ (${keyPoints.size} ข้อ)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    FilledTonalButton(
                                        onClick = { keyPoints.add("ประเด็นสำคัญข้อที่ ${keyPoints.size + 1}") },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("add_keypoint_button")
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("เพิ่มข้อความ", fontSize = 11.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                keyPoints.forEachIndexed { idx, point ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("${idx + 1}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        OutlinedTextField(
                                            value = point,
                                            onValueChange = { keyPoints[idx] = it },
                                            modifier = Modifier.weight(1f),
                                            maxLines = 3,
                                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                                        )
                                        IconButton(
                                            onClick = { if (keyPoints.size > 1) keyPoints.removeAt(idx) },
                                            enabled = keyPoints.size > 1
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // Callout Text Input
                        OutlinedTextField(
                            value = calloutText,
                            onValueChange = { calloutText = it },
                            label = { Text("กล่องข้อความไฮไลต์เน้นย้ำ (Callout Box)") },
                            placeholder = { Text("เช่น 2 สิทธิ ม.35 = 1 วิศวกร EV + 1 ช่างตัดเย็บ WFH") },
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("slide_callout_input")
                        )

                        // Speaker Notes Input
                        OutlinedTextField(
                            value = speakerNotes,
                            onValueChange = { speakerNotes = it },
                            label = { Text("บทพูดและประเด็นสำหรับผู้นำเสนอ (Speaker Notes)") },
                            placeholder = { Text("ข้อความช่วยจำที่แสดงเฉพาะผู้นำเสนอ...") },
                            maxLines = 4,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("slide_notes_input")
                        )
                    }

                    // TAB 1: Layout Options
                    1 -> {
                        Text(
                            text = "เลือกรูปแบบการจัดวางสไลด์ (Layout Options):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        SlideLayoutType.entries.forEach { layoutType ->
                            val isSelected = selectedLayout == layoutType
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedLayout = layoutType }
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = getLayoutIcon(layoutType),
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = layoutType.label,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = layoutType.description,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // TAB 2: Image Placeholder
                    2 -> {
                        // Toggle to enable/disable placeholder
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "เปิดใช้งานกล่องรูปภาพตัวอย่าง (Image Placeholder)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "แสดงภาพผลงานนวัตกรรม ผังวงจร หรือภาพถ่ายศูนย์ช่าง",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = enableImagePlaceholder,
                                    onCheckedChange = { enableImagePlaceholder = it }
                                )
                            }
                        }

                        if (enableImagePlaceholder) {
                            Text(
                                text = "เลือกชุดภาพตัวอย่างหรืออัปโหลดภาพของคุณเอง:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            // Preset Pills Grid
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                PlaceholderPreset.entries.forEach { preset ->
                                    val isChosen = selectedPreset == preset
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isChosen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedPreset = preset
                                                if (preset != PlaceholderPreset.CUSTOM) {
                                                    imageTitle = preset.defaultTitle
                                                    imageCaption = preset.defaultCaption
                                                } else {
                                                    photoPickerLauncher.launch(
                                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                    )
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = getPresetIcon(preset),
                                                contentDescription = null,
                                                tint = if (isChosen) MaterialTheme.colorScheme.primary else Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = preset.label,
                                                    fontSize = 13.sp,
                                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = preset.defaultTitle,
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            if (isChosen) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    }
                                }
                            }

                            // Image Title & Caption Inputs
                            OutlinedTextField(
                                value = imageTitle,
                                onValueChange = { imageTitle = it },
                                label = { Text("ชื่อรูปภาพ / ชิ้นงานนวัตกรรม") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = imageCaption,
                                onValueChange = { imageCaption = it },
                                label = { Text("คำบรรยายใต้ภาพ") },
                                maxLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Custom image picker button
                            FilledTonalButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (customImageUri != null) "เปลี่ยนรูปภาพจากเครื่องที่เลือกไว้" else "เลือกรูปภาพจริงจากคลังภาพ (Photo Picker)")
                            }

                            if (customImageUri != null) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFD1FAE5),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "✓ ใช้รูปภาพจากเครื่อง: $imageTitle",
                                        fontSize = 11.sp,
                                        color = Color(0xFF065F46),
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // TAB 3: Live Preview
                    3 -> {
                        Text(
                            text = "ตัวอย่างสไลด์เมื่อนำเสนอจริง:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )

                        // Render preview slide canvas
                        LiveSlidePreviewCanvas(
                            tag = tag,
                            title = title,
                            subtitle = subtitle,
                            keyPoints = keyPoints,
                            calloutText = calloutText.ifBlank { null },
                            layoutType = selectedLayout,
                            imagePlaceholder = if (enableImagePlaceholder) {
                                SlideImagePlaceholder(
                                    preset = selectedPreset,
                                    title = imageTitle,
                                    caption = imageCaption,
                                    uriString = customImageUri
                                )
                            } else null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Save & Cancel Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("ยกเลิก")
                }

                Button(
                    onClick = {
                        val finalSlide = slide.copy(
                            tag = tag.ifBlank { "สไลด์นำเสนอ" },
                            title = title.ifBlank { "หัวข้อสไลด์" },
                            subtitle = subtitle,
                            keyPoints = if (keyPoints.isNotEmpty()) keyPoints.toList() else listOf("ประเด็นสำคัญ"),
                            speakerNotes = speakerNotes,
                            calloutText = calloutText.ifBlank { null },
                            layoutType = selectedLayout,
                            imagePlaceholder = if (enableImagePlaceholder) {
                                SlideImagePlaceholder(
                                    preset = selectedPreset,
                                    title = imageTitle.ifBlank { selectedPreset.defaultTitle },
                                    caption = imageCaption.ifBlank { selectedPreset.defaultCaption },
                                    uriString = customImageUri
                                )
                            } else null
                        )
                        onSave(finalSlide)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(2f)
                        .testTag("save_slide_button")
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("บันทึกการแก้ไขสไลด์", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("ยืนยันการลบสไลด์นี้?") },
            text = { Text("คุณต้องการลบสไลด์ \"$title\" ออกจากการนำเสนอใช่หรือไม่?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete(slide.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose600)
                ) {
                    Text("ลบสไลด์")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }
}

@Composable
fun LiveSlidePreviewCanvas(
    tag: String,
    title: String,
    subtitle: String,
    keyPoints: List<String>,
    calloutText: String?,
    layoutType: SlideLayoutType,
    imagePlaceholder: SlideImagePlaceholder?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TechBadge(
                    text = tag.ifBlank { "สไลด์นำเสนอ" },
                    color = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
                Text(
                    text = layoutType.label,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Subtitle
            Text(
                text = title.ifBlank { "หัวข้อสไลด์" },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(12.dp))

            // Layout Switching
            when (layoutType) {
                SlideLayoutType.STANDARD -> {
                    // Standard Bullets
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        keyPoints.forEach { pt ->
                            BulletPointRow(pt)
                        }
                    }

                    // Optional Image Placeholder
                    if (imagePlaceholder != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        SlidePlaceholderBox(imagePlaceholder)
                    }

                    // Callout
                    if (calloutText != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        SlideCalloutBox(calloutText)
                    }
                }

                SlideLayoutType.TWO_COLUMN -> {
                    // Split 2-columns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Left column: Bullets
                        Column(
                            modifier = Modifier.weight(1.2f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            keyPoints.forEach { pt ->
                                BulletPointRow(pt)
                            }
                        }

                        // Right column: Image or Callout card
                        Column(modifier = Modifier.weight(1f)) {
                            if (imagePlaceholder != null) {
                                SlidePlaceholderBox(imagePlaceholder)
                            } else if (calloutText != null) {
                                SlideCalloutBox(calloutText)
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "สรุปคุณค่าหลัก", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Text(text = "ความเชี่ยวชาญผสานประโยชน์ต่อสังคม", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                    }
                                }
                            }
                        }
                    }
                }

                SlideLayoutType.HERO_STAT -> {
                    // Big impact statement + stat pills
                    if (calloutText != null) {
                        SlideCalloutBox(calloutText)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        keyPoints.take(3).forEachIndexed { i, pt ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = pt,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        maxLines = 3
                                    )
                                }
                            }
                        }
                    }

                    if (imagePlaceholder != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        SlidePlaceholderBox(imagePlaceholder)
                    }
                }

                SlideLayoutType.MEDIA_SPLIT -> {
                    // Media prominent split
                    if (imagePlaceholder != null) {
                        SlidePlaceholderBox(imagePlaceholder)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        keyPoints.forEach { pt ->
                            BulletPointRow(pt)
                        }
                    }

                    if (calloutText != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        SlideCalloutBox(calloutText)
                    }
                }
            }
        }
    }
}

@Composable
fun SlidePlaceholderBox(
    placeholder: SlideImagePlaceholder,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0F172A), // Premium Dark Tech Container
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getPresetIcon(placeholder.preset),
                    contentDescription = null,
                    tint = Cyan500,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = placeholder.title.ifBlank { placeholder.preset.defaultTitle },
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = placeholder.caption.ifBlank { placeholder.preset.defaultCaption },
                color = Color(0xFF94A3B8),
                fontSize = 10.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun SlideCalloutBox(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun BulletPointRow(point: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(12.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = point,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 17.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

fun getLayoutIcon(type: SlideLayoutType): ImageVector {
    return when (type) {
        SlideLayoutType.STANDARD -> Icons.Default.ViewHeadline
        SlideLayoutType.TWO_COLUMN -> Icons.Default.ViewColumn
        SlideLayoutType.HERO_STAT -> Icons.Default.AutoAwesome
        SlideLayoutType.MEDIA_SPLIT -> Icons.Default.PhotoLibrary
    }
}

fun getPresetIcon(preset: PlaceholderPreset): ImageVector {
    return when (preset) {
        PlaceholderPreset.HAYABUSA_EV -> Icons.Default.Speed
        PlaceholderPreset.BMS_TELEMETRY -> Icons.Default.Bolt
        PlaceholderPreset.WHEELCHAIR_CLINIC -> Icons.Default.Handyman
        PlaceholderPreset.BEDSORE_SEATING -> Icons.Default.ContentCut
        PlaceholderPreset.BUDGET_CHART -> Icons.Default.Calculate
        PlaceholderPreset.SOYOSOURCE_GIT_1200W -> Icons.Default.ElectricMeter
        PlaceholderPreset.CUSTOM -> Icons.Default.Image
    }
}
