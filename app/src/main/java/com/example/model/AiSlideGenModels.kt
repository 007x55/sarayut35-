package com.example.model

enum class AiSlideGenTone(val label: String, val shortDesc: String) {
    CSR_FUNDING(
        "ขอทุน CSR มาตรา 35",
        "เน้นความคุ้มค่าทางภาษี ผลกระทบต่อคนพิการ และความโปร่งใสของงบประมาณ"
    ),
    TECHNICAL_ENGINEERING(
        "เชิงวิศวกรรม & นวัตกรรม",
        "เน้นสเปกฮาร์ดแวร์ วงจรไฟฟ้า ความปลอดภัย และระบบอัตโนมัติ"
    ),
    COMMUNITY_EMPOWERMENT(
        "พัฒนาชุมชน & สร้างอาชีพ",
        "เน้นการพึ่งพาตนเอง ฝึกทักษะช่างคนพิการ และการจ้างงานที่ยั่งยืน"
    ),
    GENERAL_PITCH(
        "นำเสนอมาตรฐาน (Balanced Pitch)",
        "โครงสร้างสมดุล: ปัญหา โซลูชัน ผลลัพธ์ และงบประมาณสรุป"
    )
}

data class AiSlideRequest(
    val summary: String,
    val tone: AiSlideGenTone = AiSlideGenTone.CSR_FUNDING,
    val targetBulletsCount: Int = 4,
    val includeSpeakerNotes: Boolean = true
)

data class AiSlideResult(
    val tag: String,
    val title: String,
    val subtitle: String,
    val bulletPoints: List<String>,
    val calloutText: String? = null,
    val speakerNotes: String = "",
    val suggestedLayout: SlideLayoutType = SlideLayoutType.STANDARD,
    val suggestedPreset: PlaceholderPreset = PlaceholderPreset.WHEELCHAIR_CLINIC,
    val isLiveApiCall: Boolean = false,
    val modelName: String = "gemini-3.5-flash",
    val promptSummary: String = ""
)

sealed class AiSlideGenState {
    object Idle : AiSlideGenState()
    object Loading : AiSlideGenState()
    data class Success(val result: AiSlideResult) : AiSlideGenState()
    data class Error(val message: String, val canRetry: Boolean = true) : AiSlideGenState()
}

data class PromptTemplate(
    val chipLabel: String,
    val promptText: String,
    val tone: AiSlideGenTone,
    val suggestedPreset: PlaceholderPreset
)
