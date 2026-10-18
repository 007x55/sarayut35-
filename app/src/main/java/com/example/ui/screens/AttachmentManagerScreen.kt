package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AttachmentCategory
import com.example.model.AttachmentFile
import com.example.model.AttachmentType
import com.example.ui.components.AttachmentDeleteConfirmDialog
import com.example.ui.components.AttachmentItemCard
import com.example.ui.components.AttachmentPreviewDialog
import com.example.ui.components.AttachmentUploadDialog
import com.example.ui.components.TechBadge
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue600
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Rose600

@Composable
fun AttachmentManagerScreen(
    attachments: List<AttachmentFile>,
    selectedCategory: AttachmentCategory,
    searchQuery: String,
    selectedFileForPreview: AttachmentFile?,
    fileToDelete: AttachmentFile?,
    showUploadDialog: Boolean,
    onCategorySelected: (AttachmentCategory) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onOpenUploadDialog: () -> Unit,
    onCloseUploadDialog: () -> Unit,
    onUploadNewFile: (String, AttachmentType, Long, AttachmentCategory, String, String?) -> Unit,
    onSelectPreview: (AttachmentFile?) -> Unit,
    onSelectDelete: (AttachmentFile?) -> Unit,
    onConfirmDelete: () -> Unit,
    onResetDefaults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Filter attachments based on category and search query
    val filteredFiles = attachments.filter { file ->
        val matchesCategory = (selectedCategory == AttachmentCategory.ALL) || (file.category == selectedCategory)
        val matchesSearch = searchQuery.isBlank() ||
                file.fileName.contains(searchQuery, ignoreCase = true) ||
                file.description.contains(searchQuery, ignoreCase = true) ||
                file.fileType.name.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    // Storage summary calculations
    val totalSizeBytes = attachments.sumOf { it.fileSizeBytes }
    val totalSizeFormatted = when {
        totalSizeBytes >= 1024 * 1024 * 1024 -> String.format("%.2f GB", totalSizeBytes / (1024.0 * 1024.0 * 1024.0))
        totalSizeBytes >= 1024 * 1024 -> String.format("%.1f MB", totalSizeBytes / (1024.0 * 1024.0))
        else -> "${totalSizeBytes / 1024} KB"
    }

    val pdfCount = attachments.count { it.fileType == AttachmentType.PDF }
    val docxCount = attachments.count { it.fileType == AttachmentType.DOCX }
    val imageCount = attachments.count { it.fileType == AttachmentType.IMAGE }
    val videoCount = attachments.count { it.fileType == AttachmentType.VIDEO }

    // Download/Share handler
    val handleDownloadOrShare: (AttachmentFile) -> Unit = { file ->
        if (file.uriString != null) {
            try {
                val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(file.uriString), file.fileType.mimeType)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(viewIntent, "เปิดไฟล์ ${file.fileName}"))
            } catch (e: Exception) {
                // Fallback to share intent
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = file.fileType.mimeType
                    putExtra(Intent.EXTRA_TITLE, file.fileName)
                    putExtra(Intent.EXTRA_TEXT, "ไฟล์แนบโครงการมาตรา 35: ${file.fileName}\n${file.description}")
                }
                context.startActivity(Intent.createChooser(shareIntent, "แชร์ไฟล์ ${file.fileName}"))
            }
        } else {
            // Share proposal text & document summary info
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "เอกสารแนบโครงการ: ${file.fileName}")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "เอกสารแนบโครงการศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการชุมชนนวัตกรรม จ.นครนายก (มาตรา 35)\n\n" +
                            "ชื่อไฟล์: ${file.fileName}\n" +
                            "ประเภท: ${file.fileType.label} (${file.formattedSize})\n" +
                            "หมวดหมู่: ${file.category.label}\n" +
                            "รายละเอียด: ${file.description}\n\n" +
                            "ผู้เสนอโครงการ: นายสรายุทธ มูลผล และครอบครัว (เครือข่ายช่างชุมชนจิตอาสา จ.นครนายก)\n" +
                            "อีเมล: sarayutm2016@gmail.com"
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "ดาวน์โหลด / ส่งต่อเอกสาร"))
            Toast.makeText(context, "กำลังส่งออกข้อมูลเอกสาร: ${file.fileName}", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("attachment_manager_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header & Storage Summary
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TechBadge(
                            text = "Attachment Center",
                            color = Color.White,
                            containerColor = Color.White.copy(alpha = 0.2f)
                        )
                        Text(
                            text = "มาตรา 35 • จ.นครนายก",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ระบบจัดการไฟล์แนบโครงการ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "รวบรวมเล่มข้อเสนอ หนังสือรับรอง ภาพนวัตกรรม Hayabusa EV และคลิปสาธิต IoT",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Storage and counts row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "จำนวนไฟล์ทั้งหมด", fontSize = 10.sp, color = Color.Gray)
                                Text(
                                    text = "${attachments.size} ไฟล์",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "พื้นที่จัดเก็บรวม", fontSize = 10.sp, color = Color.Gray)
                                Text(
                                    text = totalSizeFormatted,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald600
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Type breakdown badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TypePill(icon = Icons.Default.PictureAsPdf, label = "PDF ($pdfCount)", color = Rose600, modifier = Modifier.weight(1f))
                        TypePill(icon = Icons.Default.Description, label = "Word ($docxCount)", color = Blue600, modifier = Modifier.weight(1f))
                        TypePill(icon = Icons.Default.Image, label = "JPG ($imageCount)", color = Emerald600, modifier = Modifier.weight(1f))
                        TypePill(icon = Icons.Default.Movie, label = "MP4 ($videoCount)", color = Color(0xFF7C3AED), modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Action Buttons Row: Upload File + Reset Default Files
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onOpenUploadDialog,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("upload_file_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "อัปโหลดไฟล์ใหม่", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onResetDefaults,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("reset_defaults_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "คืนค่าตั้งต้น", fontSize = 12.sp)
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("ค้นหาชื่อไฟล์, หมวดหมู่ หรือคำอธิบาย...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("attachment_search_bar")
            )
        }

        // Horizontal Category Filter Chips
        item {
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AttachmentCategory.entries.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelected(category) },
                        label = {
                            Text(
                                text = category.label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Results Count Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "รายการไฟล์แนบ (${filteredFiles.size} รายการ)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (selectedCategory != AttachmentCategory.ALL || searchQuery.isNotEmpty()) {
                    Text(
                        text = "กำลังกรองข้อมูล",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Empty state when filtered out
        if (filteredFiles.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "ไม่พบไฟล์แนบที่ตรงกับเงื่อนไข",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "ลองเปลี่ยนคำค้นหา หรือกดปุ่มอัปโหลดไฟล์ใหม่เพื่อเพิ่มเอกสาร",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onOpenUploadDialog,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("อัปโหลดไฟล์ทันที")
                        }
                    }
                }
            }
        } else {
            // List of Attachments
            items(filteredFiles, key = { it.id }) { file ->
                AttachmentItemCard(
                    file = file,
                    onPreview = { onSelectPreview(file) },
                    onDownload = { handleDownloadOrShare(file) },
                    onDelete = { onSelectDelete(file) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Upload Dialog
    if (showUploadDialog) {
        AttachmentUploadDialog(
            onDismiss = onCloseUploadDialog,
            onUpload = onUploadNewFile
        )
    }

    // Preview Dialog
    if (selectedFileForPreview != null) {
        AttachmentPreviewDialog(
            file = selectedFileForPreview,
            onDismiss = { onSelectPreview(null) },
            onDownload = { handleDownloadOrShare(selectedFileForPreview) }
        )
    }

    // Delete Confirmation Dialog
    if (fileToDelete != null) {
        AttachmentDeleteConfirmDialog(
            file = fileToDelete,
            onConfirm = onConfirmDelete,
            onDismiss = { onSelectDelete(null) }
        )
    }
}

@Composable
private fun TypePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.15f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
