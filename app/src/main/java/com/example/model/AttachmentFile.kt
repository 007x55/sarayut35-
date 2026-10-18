package com.example.model

enum class AttachmentType(val extension: String, val label: String, val mimeType: String) {
    PDF("PDF", "เอกสาร PDF", "application/pdf"),
    DOCX("DOCX", "เอกสาร Word", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    IMAGE("JPG/PNG", "รูปภาพ", "image/*"),
    VIDEO("MP4", "วิดีโอ", "video/*"),
    OTHER("OTHER", "ไฟล์อื่นๆ", "*/*")
}

enum class AttachmentCategory(val label: String) {
    ALL("ทั้งหมด"),
    PROPOSAL_DOC("เอกสารโครงการ"),
    CERTIFICATE("หนังสือรับรองคุณวุฒิ"),
    TECH_PHOTO("ภาพถ่ายนวัตกรรม"),
    DEMO_VIDEO("วิดีโอสาธิตการทำงาน"),
    QUOTATION("ใบเสนอราคา & อุปกรณ์"),
    GOV_FORM("แบบฟอร์มราชการ พม.")
}

data class AttachmentFile(
    val id: String,
    val fileName: String,
    val fileType: AttachmentType,
    val fileSizeBytes: Long,
    val uploadedDate: String,
    val category: AttachmentCategory,
    val description: String,
    val uriString: String? = null,
    val isPreloaded: Boolean = false
) {
    val formattedSize: String
        get() {
            return when {
                fileSizeBytes >= 1024 * 1024 * 1024 -> String.format("%.2f GB", fileSizeBytes / (1024.0 * 1024.0 * 1024.0))
                fileSizeBytes >= 1024 * 1024 -> String.format("%.1f MB", fileSizeBytes / (1024.0 * 1024.0))
                fileSizeBytes >= 1024 -> String.format("%d KB", fileSizeBytes / 1024)
                else -> "$fileSizeBytes B"
            }
        }
}
