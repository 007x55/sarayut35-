package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AttachmentCategory
import com.example.model.AttachmentFile
import com.example.model.AttachmentType
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber600
import com.example.ui.theme.Blue600
import com.example.ui.theme.Blue700
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Rose600

@Composable
fun AttachmentItemCard(
    file: AttachmentFile,
    onPreview: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typeConfig = getAttachmentTypeConfig(file.fileType)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("attachment_item_${file.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Type Icon + Name + Type Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(typeConfig.containerColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = typeConfig.icon,
                            contentDescription = file.fileType.name,
                            tint = typeConfig.tintColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = file.fileName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 19.sp,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${file.category.label} • ${file.formattedSize}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TechBadge(
                    text = file.fileType.extension,
                    color = typeConfig.tintColor,
                    containerColor = typeConfig.containerColor
                )
            }

            // Description
            if (file.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = file.description,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom action row: Upload timestamp + View / Download / Delete buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = file.uploadedDate,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Preview Button
                    OutlinedButton(
                        onClick = onPreview,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(36.dp).testTag("preview_btn_${file.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Preview",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "ดูไฟล์", fontSize = 11.sp)
                    }

                    // Download / Share Button
                    FilledTonalButton(
                        onClick = onDownload,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(36.dp).testTag("download_btn_${file.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Download",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "ดาวน์โหลด", fontSize = 11.sp)
                    }

                    // Delete Button
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("delete_btn_${file.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Rose600,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentUploadDialog(
    onDismiss: () -> Unit,
    onUpload: (fileName: String, type: AttachmentType, sizeBytes: Long, category: AttachmentCategory, description: String, uriString: String?) -> Unit
) {
    val context = LocalContext.current
    var fileName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(AttachmentType.PDF) }
    var selectedCategory by remember { mutableStateOf(AttachmentCategory.PROPOSAL_DOC) }
    var description by remember { mutableStateOf("") }
    var pickedUriString by remember { mutableStateOf<String?>(null) }
    var pickedFileSize by remember { mutableStateOf(1800000L) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }

    // Launcher for real system document picking (zero permission needed)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            pickedUriString = uri.toString()
            // Read display name and size from content resolver
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                    if (nameIndex != -1) {
                        val name = it.getString(nameIndex)
                        if (!name.isNullOrBlank()) {
                            fileName = name
                            selectedType = determineTypeFromExtension(name)
                        }
                    }
                    if (sizeIndex != -1) {
                        val size = it.getLong(sizeIndex)
                        if (size > 0) pickedFileSize = size
                    }
                }
            }
            Toast.makeText(context, "เลือกไฟล์สำเร็จ: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "อัปโหลดไฟล์เอกสารแนบ",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // System Document Picker Trigger Button
                FilledTonalButton(
                    onClick = {
                        // Open system document picker supporting multiple document/media types
                        filePickerLauncher.launch(
                            arrayOf(
                                "application/pdf",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                "application/msword",
                                "image/*",
                                "video/*",
                                "*/*"
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("browse_files_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.FolderOpen, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (pickedUriString != null) "เปลี่ยนไฟล์ที่เลือก" else "เลือกไฟล์จากเครื่อง (PDF, Word, ภาพ, คลิป)"
                    )
                }

                if (pickedUriString != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFD1FAE5),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ ได้รับไฟล์จากระบบ: $fileName",
                            fontSize = 11.sp,
                            color = Color(0xFF065F46),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // File Name Input
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("ชื่อไฟล์ (เช่น เอกสารประกอบ.pdf)") },
                    placeholder = { Text("ระบุชื่อไฟล์พร้อมนามสกุล") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_file_name")
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = isCategoryDropdownExpanded,
                    onExpandedChange = { isCategoryDropdownExpanded = !isCategoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("หมวดหมู่เอกสาร") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCategoryDropdownExpanded,
                        onDismissRequest = { isCategoryDropdownExpanded = false }
                    ) {
                        AttachmentCategory.entries.filter { it != AttachmentCategory.ALL }.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.label) },
                                onClick = {
                                    selectedCategory = cat
                                    isCategoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = isTypeDropdownExpanded,
                    onExpandedChange = { isTypeDropdownExpanded = !isTypeDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = "${selectedType.label} (${selectedType.extension})",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("ประเภทไฟล์") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isTypeDropdownExpanded,
                        onDismissRequest = { isTypeDropdownExpanded = false }
                    ) {
                        AttachmentType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text("${type.label} (${type.extension})") },
                                onClick = {
                                    selectedType = type
                                    isTypeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("คำอธิบายหรือหมายเหตุของไฟล์") },
                    placeholder = { Text("เช่น รายละเอียดเพิ่มเติมสำหรับคณะกรรมการ") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_file_desc")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalName = if (fileName.isNotBlank()) {
                        if (!fileName.contains(".")) "$fileName.${selectedType.extension.lowercase()}" else fileName
                    } else {
                        "เอกสารแนบโครงการ_${System.currentTimeMillis().toString().takeLast(4)}.${selectedType.extension.lowercase()}"
                    }
                    onUpload(finalName, selectedType, pickedFileSize, selectedCategory, description, pickedUriString)
                },
                modifier = Modifier.testTag("confirm_upload_button")
            ) {
                Text("บันทึกการอัปโหลด")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("ยกเลิก")
            }
        }
    )
}

@Composable
fun AttachmentPreviewDialog(
    file: AttachmentFile,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    val context = LocalContext.current
    val typeConfig = getAttachmentTypeConfig(file.fileType)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = typeConfig.icon,
                        contentDescription = null,
                        tint = typeConfig.tintColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ดูรายละเอียดไฟล์แนบ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // File Name
                Text(
                    text = file.fileName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Tags Row
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TechBadge(
                        text = file.fileType.extension,
                        color = typeConfig.tintColor,
                        containerColor = typeConfig.containerColor
                    )
                    TechBadge(
                        text = file.category.label,
                        color = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Detail Specs
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        DetailItemRow("ขนาดไฟล์:", file.formattedSize)
                        DetailItemRow("วันที่บันทึก:", file.uploadedDate)
                        DetailItemRow("MIME Type:", file.fileType.mimeType)
                        DetailItemRow("สถานะ:", if (file.isPreloaded) "เอกสารทางการของโครงการ" else "ไฟล์ที่ผู้ใช้อัปโหลดเพิ่มเติม")
                    }
                }

                // Description Box
                Text(
                    text = "คำอธิบายไฟล์:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = file.description,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Simulated Content Preview Box
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ตัวอย่างเนื้อหาของไฟล์ (${file.fileType.label})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = getPreviewMockContent(file),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF94A3B8),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDownload,
                modifier = Modifier.testTag("preview_download_action")
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ดาวน์โหลด / แชร์ไฟล์")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("ปิด")
            }
        }
    )
}

@Composable
fun AttachmentDeleteConfirmDialog(
    file: AttachmentFile,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Rose600,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "ยืนยันการลบไฟล์?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "คุณต้องการลบไฟล์แนบ \"${file.fileName}\" (${file.formattedSize}) ออกจากระบบนำเสนอโครงการใช่หรือไม่? การดำเนินการนี้ไม่สามารถเรียกคืนได้",
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Rose600),
                modifier = Modifier.testTag("confirm_delete_button")
            ) {
                Text("ลบไฟล์นี้")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("ยกเลิก")
            }
        }
    )
}

@Composable
private fun DetailItemRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}

data class TypeConfig(
    val icon: ImageVector,
    val tintColor: Color,
    val containerColor: Color
)

fun getAttachmentTypeConfig(type: AttachmentType): TypeConfig {
    return when (type) {
        AttachmentType.PDF -> TypeConfig(Icons.Default.PictureAsPdf, Rose600, Color(0xFFFFE4E6))
        AttachmentType.DOCX -> TypeConfig(Icons.Default.Description, Blue700, Color(0xFFDBEAFE))
        AttachmentType.IMAGE -> TypeConfig(Icons.Default.Image, Emerald600, Color(0xFFD1FAE5))
        AttachmentType.VIDEO -> TypeConfig(Icons.Default.Movie, Color(0xFF7C3AED), Color(0xFFEDE9FE))
        AttachmentType.OTHER -> TypeConfig(Icons.Default.AttachFile, Amber600, Color(0xFFFEF3C7))
    }
}

fun determineTypeFromExtension(name: String): AttachmentType {
    val lower = name.lowercase()
    return when {
        lower.endsWith(".pdf") -> AttachmentType.PDF
        lower.endsWith(".docx") || lower.endsWith(".doc") -> AttachmentType.DOCX
        lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".webp") -> AttachmentType.IMAGE
        lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".mov") || lower.endsWith(".avi") -> AttachmentType.VIDEO
        else -> AttachmentType.OTHER
    }
}

fun getPreviewMockContent(file: AttachmentFile): String {
    return when (file.fileType) {
        AttachmentType.PDF -> """
            [PDF Document Preview]
            ชื่อเอกสาร: ${file.fileName}
            หมวดหมู่: ${file.category.label}
            เนื้อหา: ตรวจสอบความถูกต้องและลงนามรับรองเอกสารครบถ้วน
            พร้อมยื่นประกอบการขอรับสิทธิ ม.35 ต่อสำนักงานจัดหางาน
        """.trimIndent()

        AttachmentType.DOCX -> """
            [Microsoft Word Document]
            ตารางรายการและใบเสนอราคาอุปกรณ์ช่าง
            - ตู้เชื่อม TIG / Spot Welder 16,500 บาท
            - จักรอุตสาหกรรมเย็บเบาะ 15,000 บาท
            - อะไหล่สำรองซ่อมฟรี 28,500 บาท
            รวมงบประมาณ: 60,000 บาท
        """.trimIndent()

        AttachmentType.IMAGE -> """
            [Image Graphic Data]
            ความละเอียด: 3840 x 2160 (4K UHD)
            โหมดสี: sRGB 24-bit
            คำบรรยายภาพ: แสดงภาพงานประกอบจริงในพื้นที่ จ.นครนายก
        """.trimIndent()

        AttachmentType.VIDEO -> """
            [Video Media Track]
            ความยาว: 03:45 นาที (1080p 60fps)
            เสียง: สเตอริโอบรรยายภาษาไทย
            การสาธิต: ทดสอบ BMS Cell Balancing & Soyosource 1200W
        """.trimIndent()

        AttachmentType.OTHER -> """
            [Attachment File Data]
            ไฟล์ประกอบโครงการศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการ
        """.trimIndent()
    }
}
