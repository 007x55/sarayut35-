package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ProjectRepository
import com.example.model.BudgetItem
import com.example.model.CsrImpactBenefit
import com.example.model.PresentationSlide
import com.example.model.ReferencePerson
import com.example.model.ScopeSection
import com.example.model.ServiceRepairRecord
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.example.model.AttachmentCategory
import com.example.model.AttachmentFile
import com.example.model.AttachmentType
import com.example.model.InverterComponentInfo
import com.example.model.SoyosourceInverterState
import com.example.model.SoyosourceWorkingMode
import com.example.model.AiSlideGenState
import com.example.model.AiSlideGenTone
import com.example.model.AiSlideRequest
import com.example.model.AiSlideResult
import com.example.service.GeminiSlideService
import kotlin.random.Random

enum class AppNavTab(val label: String, val iconName: String) {
    PITCH_DECK("สไลด์", "slideshow"),
    PROJECT_DOSSIER("เล่มโครงการ", "description"),
    TECH_SHOWCASE("นวัตกรรม", "precision_manufacturing"),
    SECTION35_CALC("คำนวณ ม.35", "calculate"),
    ATTACHMENTS("เอกสารแนบ", "attach_file"),
    COMMUNITY_QUEUE("งานชุมชน", "volunteer_activism")
}

enum class TechSubTab(val label: String, val iconTag: String) {
    SOYOSOURCE_1200W("Soyosource GIT-1200W", "bolt"),
    ROBOT_OLED("ESP32 Robot Face", "smart_toy"),
    HAYABUSA_EV("Hayabusa EV", "speed"),
    SMART_BMS("Smart BMS 16S", "battery_charging_full"),
    ANTI_BEDSORE("เบาะ WFH 3 ชั้น", "content_cut"),
    ALL("ดูทั้งหมด", "grid_view")
}

data class BmsSimState(
    val packVoltage: Double = 53.2,
    val stateOfCharge: Int = 88,
    val currentAmp: Double = 12.4,
    val temperatureC: Double = 31.5,
    val isCharging: Boolean = false,
    val isDischarging: Boolean = true,
    val isBalancing: Boolean = true,
    val cellVoltages: List<Double> = listOf(
        3.325, 3.324, 3.326, 3.323,
        3.325, 3.327, 3.324, 3.325,
        3.326, 3.324, 3.325, 3.326,
        3.323, 3.325, 3.324, 3.325
    ),
    val soyosourceWatts: Int = 950,
    val isConnected: Boolean = true
)

data class Section35CalcState(
    val supportedRights: Int = 2,
    val annualWagePerRight: Long = 119720L, // ค่าจ้างเหมาบริการขั้นต่ำตามระเบียบ พม. (ประมาณการ 328 บ./วัน x 365 วัน)
    val includeStartupEquipment: Boolean = true,
    val startupEquipmentBudget: Long = 60000L,
    val corporateTaxRatePercent: Double = 20.0
) {
    val totalDirectInvestment: Long
        get() = (supportedRights * annualWagePerRight) + (if (includeStartupEquipment) startupEquipmentBudget else 0L)

    val fundContributionEquivalent: Long
        get() = supportedRights * annualWagePerRight

    // สิทธิประโยชน์ทางภาษียกเว้นภาษีเงินได้นิติบุคคลจากรายจ่ายตามมาตรา 35 (100% หรือตามที่กรมสรรพากรกำหนด)
    val taxDeductionSavings: Long
        get() = (totalDirectInvestment * (corporateTaxRatePercent / 100.0)).toLong()

    val netInvestmentAfterTax: Long
        get() = (totalDirectInvestment - taxDeductionSavings).coerceAtLeast(0L)

    val estimatedWheelchairsRepairedFree: Int
        get() = (supportedRights * 45) // ประมาณการซ่อมฟรี 40-50 คันต่อสิทธิต่อปี
}

data class UiState(
    val activeTab: AppNavTab = AppNavTab.PITCH_DECK,
    val currentSlideIndex: Int = 0,
    val showSpeakerNotes: Boolean = true,
    val isAutoPlay: Boolean = false,
    val slides: List<PresentationSlide> = ProjectRepository.presentationSlides,
    val isSlideEditorOpen: Boolean = false,
    val slideBeingEdited: PresentationSlide? = null,
    val bmsSimState: BmsSimState = BmsSimState(),
    val calcState: Section35CalcState = Section35CalcState(),
    val showContactSheet: Boolean = false,
    val showSummaryShareDialog: Boolean = false,
    val attachments: List<AttachmentFile> = ProjectRepository.defaultAttachments,
    val attachmentCategoryFilter: AttachmentCategory = AttachmentCategory.ALL,
    val attachmentSearchQuery: String = "",
    val selectedAttachmentForPreview: AttachmentFile? = null,
    val attachmentToDelete: AttachmentFile? = null,
    val showUploadDialog: Boolean = false,
    val soyosourceState: SoyosourceInverterState = SoyosourceInverterState(),
    val activeTechSubTab: TechSubTab = TechSubTab.SOYOSOURCE_1200W,
    val isAiSlideGenSheetOpen: Boolean = false,
    val aiSlideGenState: AiSlideGenState = AiSlideGenState.Idle,
    val aiPromptSummary: String = "",
    val aiSelectedTone: AiSlideGenTone = AiSlideGenTone.CSR_FUNDING,
    val aiTargetBulletsCount: Int = 4,
    val lastGeneratedAiResult: AiSlideResult? = null,
    val userNotification: String? = null
)

class ProjectViewModel : ViewModel() {
    val geminiSlideService = GeminiSlideService()
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val slides: List<PresentationSlide> get() = _uiState.value.slides
    val references: List<ReferencePerson> = ProjectRepository.references
    val scopeSections: List<ScopeSection> = ProjectRepository.scopeSections
    val csrBenefits: List<CsrImpactBenefit> = ProjectRepository.csrBenefits
    val budgetItems: List<BudgetItem> = ProjectRepository.budgetItems
    val communityRepairs: List<ServiceRepairRecord> = ProjectRepository.sampleCommunityRepairs
    val soyosourceComponents: List<InverterComponentInfo> = ProjectRepository.soyosourceComponents

    private var autoPlayJob: Job? = null
    private var bmsSimulationJob: Job? = null

    init {
        startBmsSimulation()
    }

    fun selectTab(tab: AppNavTab) {
        _uiState.update { it.copy(activeTab = tab) }
        if (tab != AppNavTab.PITCH_DECK) {
            stopAutoPlay()
        }
    }

    fun nextSlide() {
        _uiState.update { current ->
            val next = (current.currentSlideIndex + 1).coerceAtMost(slides.lastIndex)
            current.copy(currentSlideIndex = next)
        }
    }

    fun prevSlide() {
        _uiState.update { current ->
            val prev = (current.currentSlideIndex - 1).coerceAtLeast(0)
            current.copy(currentSlideIndex = prev)
        }
    }

    fun goToSlide(index: Int) {
        if (index in slides.indices) {
            _uiState.update { it.copy(currentSlideIndex = index) }
        }
    }

    fun toggleSpeakerNotes() {
        _uiState.update { it.copy(showSpeakerNotes = !it.showSpeakerNotes) }
    }

    fun toggleAutoPlay() {
        val nextState = !_uiState.value.isAutoPlay
        _uiState.update { it.copy(isAutoPlay = nextState) }
        if (nextState) {
            startAutoPlay()
        } else {
            stopAutoPlay()
        }
    }

    private fun startAutoPlay() {
        autoPlayJob?.cancel()
        autoPlayJob = viewModelScope.launch {
            while (isActive) {
                delay(8000)
                _uiState.update { current ->
                    val next = if (current.currentSlideIndex >= slides.lastIndex) 0 else current.currentSlideIndex + 1
                    current.copy(currentSlideIndex = next)
                }
            }
        }
    }

    private fun stopAutoPlay() {
        autoPlayJob?.cancel()
        autoPlayJob = null
        _uiState.update { it.copy(isAutoPlay = false) }
    }

    private fun startBmsSimulation() {
        bmsSimulationJob?.cancel()
        bmsSimulationJob = viewModelScope.launch {
            while (isActive) {
                delay(2200)
                _uiState.update { current ->
                    val sim = current.bmsSimState
                    val deltaWatts = Random.nextInt(-35, 40)
                    val newWatts = (sim.soyosourceWatts + deltaWatts).coerceIn(750, 1180)
                    val newAmp = (newWatts / 53.0)
                    val newVoltages = sim.cellVoltages.map { v ->
                        val jitter = (Random.nextDouble(-0.003, 0.003))
                        (v + jitter).coerceIn(3.310, 3.338)
                    }
                    val newPackVolt = newVoltages.sum()

                    val inv = current.soyosourceState
                    val updatedInverter = if (inv.isRunning) {
                        val invWatts = if (inv.workingMode == SoyosourceWorkingMode.LIMITER_CT) {
                            (inv.clinicLoadWatts + Random.nextInt(-8, 10)).coerceIn(100, inv.maxPowerLimitWatts)
                        } else {
                            (inv.acOutputWatts + Random.nextInt(-12, 15)).coerceIn(120, inv.maxPowerLimitWatts)
                        }
                        val dcV = Math.round((newPackVolt) * 10.0) / 10.0
                        val dcA = Math.round(((invWatts / 0.93) / dcV) * 10.0) / 10.0
                        val acV = Math.round((225.0 + Random.nextDouble(-1.5, 1.8)) * 10.0) / 10.0
                        val tempJitter = (inv.coreTemperatureC + Random.nextDouble(-0.2, 0.3)).coerceIn(38.0, 58.0)
                        val fanPct = ((tempJitter - 35.0) / 25.0 * 100).toInt().coerceIn(20, 100)
                        inv.copy(
                            acOutputWatts = invWatts,
                            dcInputVoltage = dcV,
                            dcInputCurrent = dcA,
                            acOutputVoltage = acV,
                            coreTemperatureC = Math.round(tempJitter * 10.0) / 10.0,
                            fanSpeedPercent = fanPct
                        )
                    } else {
                        inv.copy(acOutputWatts = 0, dcInputCurrent = 0.0, fanSpeedPercent = 0)
                    }

                    current.copy(
                        bmsSimState = sim.copy(
                            soyosourceWatts = newWatts,
                            currentAmp = Math.round(newAmp * 10.0) / 10.0,
                            packVoltage = Math.round(newPackVolt * 100.0) / 100.0,
                            cellVoltages = newVoltages
                        ),
                        soyosourceState = updatedInverter
                    )
                }
            }
        }
    }

    fun setTechSubTab(tab: TechSubTab) {
        _uiState.update { it.copy(activeTechSubTab = tab) }
    }

    fun setSoyosourceMode(mode: SoyosourceWorkingMode) {
        _uiState.update { current ->
            current.copy(
                soyosourceState = current.soyosourceState.copy(workingMode = mode),
                userNotification = "เปลี่ยนโหมดเป็น: ${mode.label}"
            )
        }
    }

    fun simulateWorkshopLoad(loadWatts: Int) {
        _uiState.update { current ->
            val safeLoad = loadWatts.coerceIn(100, 1200)
            val inv = current.soyosourceState
            val matchedOutput = if (inv.workingMode == SoyosourceWorkingMode.LIMITER_CT) {
                safeLoad.coerceAtMost(inv.maxPowerLimitWatts)
            } else inv.acOutputWatts

            val targetDcAmps = Math.round(((matchedOutput / 0.93) / inv.dcInputVoltage) * 10.0) / 10.0
            val targetTemp = (39.0 + (matchedOutput / 1200.0) * 16.0)

            current.copy(
                soyosourceState = inv.copy(
                    clinicLoadWatts = safeLoad,
                    acOutputWatts = matchedOutput,
                    dcInputCurrent = targetDcAmps,
                    gridExportWatts = 0,
                    coreTemperatureC = Math.round(targetTemp * 10.0) / 10.0
                ),
                userNotification = "ปรับโหลดคลินิกเป็น $safeLoad W (จ่ายไฟ $matchedOutput W, กันย้อน กฟภ. 0 W)"
            )
        }
    }

    fun setInverterMaxPower(watts: Int) {
        val safeWatts = watts.coerceIn(200, 1200)
        _uiState.update { current ->
            current.copy(
                soyosourceState = current.soyosourceState.copy(maxPowerLimitWatts = safeWatts)
            )
        }
    }

    fun setInverterCutoffVoltage(volts: Double) {
        val safeVolts = Math.round(volts * 10.0) / 10.0
        _uiState.update { current ->
            current.copy(
                soyosourceState = current.soyosourceState.copy(batteryCutoffVoltage = safeVolts)
            )
        }
    }

    fun toggleInverterPower() {
        _uiState.update { current ->
            val newState = !current.soyosourceState.isRunning
            current.copy(
                soyosourceState = current.soyosourceState.copy(
                    isRunning = newState,
                    acOutputWatts = if (newState) 850 else 0,
                    dcInputCurrent = if (newState) 16.5 else 0.0
                ),
                userNotification = if (newState) "เปิดการทำงาน Soyosource GIT-1200W" else "ปิดพักการทำงานอินเวอร์เตอร์"
            )
        }
    }

    fun runInverterDiagnostic() {
        if (_uiState.value.soyosourceState.isDiagnosticRunning) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    soyosourceState = it.soyosourceState.copy(
                        isDiagnosticRunning = true,
                        diagnosticStep = 1,
                        diagnosticMessage = "กำลังตรวจเช็กแรงดัน DC Bus 48V-90V..."
                    )
                )
            }
            delay(800)
            _uiState.update {
                it.copy(
                    soyosourceState = it.soyosourceState.copy(
                        diagnosticStep = 2,
                        diagnosticMessage = "กำลังตรวจสอบสัญญาณ Gate Driver MOSFET IRFP4668 (50 kHz)..."
                    )
                )
            }
            delay(800)
            _uiState.update {
                it.copy(
                    soyosourceState = it.soyosourceState.copy(
                        diagnosticStep = 3,
                        diagnosticMessage = "กำลังวิเคราะห์ Pure Sine Wave 50Hz (THD 2.1% ผ่านเกณฑ์)..."
                    )
                )
            }
            delay(800)
            _uiState.update {
                it.copy(
                    soyosourceState = it.soyosourceState.copy(
                        diagnosticStep = 4,
                        diagnosticMessage = "กำลังทดสอบพอร์ตสื่อสาร RS485 & CT Sensor กันย้อน (ตอบสนอง 180ms)..."
                    )
                )
            }
            delay(800)
            _uiState.update {
                it.copy(
                    soyosourceState = it.soyosourceState.copy(
                        diagnosticStep = 5,
                        diagnosticMessage = "กำลังทดสอบระบบตัดไฟฟ้าอัตโนมัติ Anti-Islanding Protection (มาตรฐาน กฟภ.)..."
                    )
                )
            }
            delay(800)
            _uiState.update {
                it.copy(
                    soyosourceState = it.soyosourceState.copy(
                        isDiagnosticRunning = false,
                        diagnosticStep = 6,
                        diagnosticMessage = "ตรวจเช็กเสร็จสมบูรณ์ 100%: บอร์ดอินเวอร์เตอร์ Soyosource GIT-1200W อยู่ในสภาพสมบูรณ์พร้อมใช้งาน"
                    ),
                    userNotification = "ตรวจเช็กสุขภาพบอร์ดอินเวอร์เตอร์ผ่านเกณฑ์ 100%"
                )
            }
        }
    }

    fun toggleBmsBalancing() {
        _uiState.update { current ->
            val bms = current.bmsSimState
            current.copy(bmsSimState = bms.copy(isBalancing = !bms.isBalancing))
        }
    }

    fun updateSupportedRights(count: Int) {
        val safeCount = count.coerceIn(1, 10)
        _uiState.update { current ->
            current.copy(calcState = current.calcState.copy(supportedRights = safeCount))
        }
    }

    fun toggleIncludeStartupEquipment() {
        _uiState.update { current ->
            val calc = current.calcState
            current.copy(calcState = calc.copy(includeStartupEquipment = !calc.includeStartupEquipment))
        }
    }

    fun openContactSheet(show: Boolean) {
        _uiState.update { it.copy(showContactSheet = show) }
    }

    fun openSummaryShareDialog(show: Boolean) {
        _uiState.update { it.copy(showSummaryShareDialog = show) }
    }

    fun getExecutiveSummaryText(): String {
        return buildString {
            appendLine("════════════════════════════════════════")
            appendLine("สรุปข้อเสนอโครงการขอรับสิทธิตามมาตรา 35")
            appendLine("ศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการชุมชนนวัตกรรม")
            appendLine("พื้นที่: จังหวัดนครนายก")
            appendLine("ผู้เสนอ: ${ProjectRepository.PROPOSER_NAME} (${ProjectRepository.PROPOSER_GROUP})")
            appendLine("════════════════════════════════════════")
            appendLine()
            appendLine("1. วัตถุประสงค์:")
            ProjectRepository.objectives.forEachIndexed { i, o -> appendLine("  1.${i + 1} $o") }
            appendLine()
            appendLine("2. จุดเด่นและนวัตกรรม:")
            appendLine("  • งานวิศวกรรม EV & BMS: ดัดแปลง Hayabusa EV, ซ่อมอินเวอร์เตอร์ Soyosource 1200W, แบตลิเธียม, ระบบ IoT ตรวจสอบ BMS ออนไลน์")
            appendLine("  • งานตัดเย็บ WFH: ตัดเย็บเบาะพนักพิง Medical PVC ป้องกันแผลกดทับด้วยจักรอุตสาหกรรม")
            appendLine()
            appendLine("3. ผู้ทรงคุณวุฒิรับรองความสามารถ:")
            ProjectRepository.references.forEach { ref ->
                appendLine("  • ${ref.name} (${ref.title} ${ref.organization}) - ${ref.badge}")
            }
            appendLine()
            appendLine("4. ผลลัพธ์ CSR ต่อสถานประกอบการ:")
            appendLine("  • ถูกต้องตามกฎหมาย พม. 100% พร้อมเอกสารยื่นภาษี")
            appendLine("  • บริการซ่อมวีลแชร์ฟรีให้คนพิการในชุมชน พร้อมติดโลโก้บริษัท")
            appendLine("  • สร้างงานที่มั่นคงแก่ครอบครัวคนพิการอย่างยั่งยืน")
            appendLine()
            appendLine("5. งบประมาณเริ่มต้นจัดหาอุปกรณ์: 60,000 บาท")
            appendLine()
            appendLine("ติดต่อผู้เสนอโครงการ:")
            appendLine("อีเมล: ${ProjectRepository.CONTACT_EMAIL}")
            appendLine("พื้นที่ดำเนินงาน: จังหวัดนครนายก")
        }
    }

    fun setAttachmentCategoryFilter(category: AttachmentCategory) {
        _uiState.update { it.copy(attachmentCategoryFilter = category) }
    }

    fun setAttachmentSearchQuery(query: String) {
        _uiState.update { it.copy(attachmentSearchQuery = query) }
    }

    fun addAttachment(
        fileName: String,
        fileType: AttachmentType,
        sizeBytes: Long,
        category: AttachmentCategory,
        description: String,
        uriString: String? = null
    ) {
        val newFile = AttachmentFile(
            id = "USER-" + System.currentTimeMillis().toString().takeLast(6),
            fileName = fileName,
            fileType = fileType,
            fileSizeBytes = if (sizeBytes > 0) sizeBytes else 1500000L,
            uploadedDate = "เพิ่งอัปโหลด",
            category = category,
            description = description.ifBlank { "ไฟล์แนบเพิ่มเติมสำหรับประกอบการพิจารณาโครงการ" },
            uriString = uriString,
            isPreloaded = false
        )
        _uiState.update { current ->
            current.copy(
                attachments = listOf(newFile) + current.attachments,
                showUploadDialog = false,
                userNotification = "อัปโหลดไฟล์ \"$fileName\" เรียบร้อยแล้ว"
            )
        }
    }

    fun setAttachmentToDelete(file: AttachmentFile?) {
        _uiState.update { it.copy(attachmentToDelete = file) }
    }

    fun confirmDeleteAttachment() {
        val target = _uiState.value.attachmentToDelete ?: return
        _uiState.update { current ->
            current.copy(
                attachments = current.attachments.filter { it.id != target.id },
                attachmentToDelete = null,
                userNotification = "ลบไฟล์ \"${target.fileName}\" เรียบร้อยแล้ว"
            )
        }
    }

    fun resetDefaultAttachments() {
        _uiState.update { current ->
            current.copy(
                attachments = ProjectRepository.defaultAttachments,
                userNotification = "รีเซ็ตไฟล์แนบตั้งต้นของโครงการเรียบร้อยแล้ว"
            )
        }
    }

    fun setSelectedAttachmentForPreview(file: AttachmentFile?) {
        _uiState.update { it.copy(selectedAttachmentForPreview = file) }
    }

    fun setShowUploadDialog(show: Boolean) {
        _uiState.update { it.copy(showUploadDialog = show) }
    }

    fun clearNotification() {
        _uiState.update { it.copy(userNotification = null) }
    }

    fun openSlideEditor(slideIndex: Int? = null) {
        val targetIndex = slideIndex ?: _uiState.value.currentSlideIndex
        val targetSlide = _uiState.value.slides.getOrNull(targetIndex)
        _uiState.update { it.copy(isSlideEditorOpen = true, slideBeingEdited = targetSlide) }
    }

    fun openSlideEditorForNewSlide() {
        val newId = (_uiState.value.slides.maxOfOrNull { it.id } ?: 0) + 1
        val newSlide = PresentationSlide(
            id = newId,
            tag = "สไลด์เพิ่มเติม",
            title = "หัวข้อสไลด์ใหม่",
            subtitle = "คำอธิบายประเด็นสำคัญของโครงการ",
            keyPoints = listOf("ประเด็นสำคัญที่ 1", "ประเด็นสำคัญที่ 2", "ประเด็นสำคัญที่ 3"),
            speakerNotes = "แนวทางการนำเสนอสำหรับสไลด์นี้ต่อคณะกรรมการ",
            calloutText = "ข้อความเน้นย้ำหรือเป้าหมายสำคัญ",
            layoutType = com.example.model.SlideLayoutType.STANDARD
        )
        _uiState.update { it.copy(isSlideEditorOpen = true, slideBeingEdited = newSlide) }
    }

    fun closeSlideEditor() {
        _uiState.update { it.copy(isSlideEditorOpen = false, slideBeingEdited = null) }
    }

    fun saveSlide(slide: PresentationSlide) {
        _uiState.update { current ->
            val existingIndex = current.slides.indexOfFirst { it.id == slide.id }
            val updatedList = if (existingIndex != -1) {
                current.slides.toMutableList().apply { set(existingIndex, slide) }
            } else {
                current.slides + slide
            }
            val newCurrentIndex = if (existingIndex != -1) existingIndex else updatedList.lastIndex
            current.copy(
                slides = updatedList,
                currentSlideIndex = newCurrentIndex,
                isSlideEditorOpen = false,
                slideBeingEdited = null,
                userNotification = "บันทึกสไลด์ \"${slide.title}\" เรียบร้อยแล้ว"
            )
        }
    }

    fun deleteSlide(slideId: Int) {
        _uiState.update { current ->
            if (current.slides.size <= 1) {
                current.copy(userNotification = "ไม่สามารถลบสไลด์ได้ ต้องมีอย่างน้อย 1 สไลด์")
            } else {
                val updatedList = current.slides.filter { it.id != slideId }
                val newIndex = current.currentSlideIndex.coerceAtMost(updatedList.lastIndex)
                current.copy(
                    slides = updatedList,
                    currentSlideIndex = newIndex,
                    isSlideEditorOpen = false,
                    slideBeingEdited = null,
                    userNotification = "ลบสไลด์เรียบร้อยแล้ว"
                )
            }
        }
    }

    fun resetSlidesToDefault() {
        _uiState.update { current ->
            current.copy(
                slides = ProjectRepository.presentationSlides,
                currentSlideIndex = 0,
                isSlideEditorOpen = false,
                slideBeingEdited = null,
                userNotification = "คืนค่าสไลด์นำเสนอตั้งต้น 8 สไลด์เรียบร้อยแล้ว"
            )
        }
    }

    fun openAiSlideGenSheet(initialSummary: String? = null, initialTone: AiSlideGenTone? = null) {
        _uiState.update { current ->
            current.copy(
                isAiSlideGenSheetOpen = true,
                aiPromptSummary = initialSummary ?: current.aiPromptSummary,
                aiSelectedTone = initialTone ?: current.aiSelectedTone
            )
        }
    }

    fun closeAiSlideGenSheet() {
        _uiState.update { it.copy(isAiSlideGenSheetOpen = false) }
    }

    fun setAiPromptSummary(summary: String) {
        _uiState.update { it.copy(aiPromptSummary = summary) }
    }

    fun setAiSelectedTone(tone: AiSlideGenTone) {
        _uiState.update { it.copy(aiSelectedTone = tone) }
    }

    fun setAiTargetBulletsCount(count: Int) {
        _uiState.update { it.copy(aiTargetBulletsCount = count) }
    }

    fun generateSlideWithGemini() {
        val summary = _uiState.value.aiPromptSummary.trim()
        if (summary.isBlank()) {
            _uiState.update { it.copy(userNotification = "กรุณาระบุสรุปโครงการหรือหัวข้อก่อนกดสร้างสไลด์") }
            return
        }

        val request = AiSlideRequest(
            summary = summary,
            tone = _uiState.value.aiSelectedTone,
            targetBulletsCount = _uiState.value.aiTargetBulletsCount,
            includeSpeakerNotes = true
        )

        _uiState.update { it.copy(aiSlideGenState = AiSlideGenState.Loading) }

        viewModelScope.launch {
            try {
                val result = geminiSlideService.generateSlideContent(request)
                _uiState.update { current ->
                    current.copy(
                        aiSlideGenState = AiSlideGenState.Success(result),
                        lastGeneratedAiResult = result,
                        userNotification = if (result.isLiveApiCall)
                            "สร้างเนื้อหาสไลด์ด้วย Gemini 3.5 Flash สำเร็จ!"
                        else
                            "สร้างเนื้อหาสไลด์สำเร็จ"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        aiSlideGenState = AiSlideGenState.Error("เกิดข้อผิดพลาด: ${e.message ?: "ไม่สามารถสร้างสไลด์ได้"}"),
                        userNotification = "สร้างสไลด์ไม่สำเร็จ: ${e.message}"
                    )
                }
            }
        }
    }

    fun applyGeneratedSlideAsNew(result: AiSlideResult) {
        val newId = (_uiState.value.slides.maxOfOrNull { it.id } ?: 0) + 1
        val newSlide = PresentationSlide(
            id = newId,
            tag = result.tag,
            title = result.title,
            subtitle = result.subtitle,
            keyPoints = result.bulletPoints,
            speakerNotes = result.speakerNotes,
            calloutText = result.calloutText,
            layoutType = result.suggestedLayout,
            imagePlaceholder = com.example.model.SlideImagePlaceholder(
                preset = result.suggestedPreset,
                title = result.suggestedPreset.defaultTitle,
                caption = result.suggestedPreset.defaultCaption
            )
        )
        _uiState.update { current ->
            val updated = current.slides + newSlide
            current.copy(
                slides = updated,
                currentSlideIndex = updated.lastIndex,
                isAiSlideGenSheetOpen = false,
                aiSlideGenState = AiSlideGenState.Idle,
                userNotification = "เพิ่มสไลด์ \"${result.title}\" จาก Gemini AI สำเร็จ!"
            )
        }
    }

    fun applyGeneratedSlideToEditor(result: AiSlideResult) {
        val targetSlide = _uiState.value.slideBeingEdited?.copy(
            tag = result.tag,
            title = result.title,
            subtitle = result.subtitle,
            keyPoints = result.bulletPoints,
            speakerNotes = result.speakerNotes,
            calloutText = result.calloutText,
            layoutType = result.suggestedLayout,
            imagePlaceholder = com.example.model.SlideImagePlaceholder(
                preset = result.suggestedPreset,
                title = result.suggestedPreset.defaultTitle,
                caption = result.suggestedPreset.defaultCaption
            )
        ) ?: PresentationSlide(
            id = (_uiState.value.slides.maxOfOrNull { it.id } ?: 0) + 1,
            tag = result.tag,
            title = result.title,
            subtitle = result.subtitle,
            keyPoints = result.bulletPoints,
            speakerNotes = result.speakerNotes,
            calloutText = result.calloutText,
            layoutType = result.suggestedLayout,
            imagePlaceholder = com.example.model.SlideImagePlaceholder(
                preset = result.suggestedPreset,
                title = result.suggestedPreset.defaultTitle,
                caption = result.suggestedPreset.defaultCaption
            )
        )

        _uiState.update { current ->
            current.copy(
                isAiSlideGenSheetOpen = false,
                isSlideEditorOpen = true,
                slideBeingEdited = targetSlide,
                aiSlideGenState = AiSlideGenState.Idle,
                userNotification = "นำเข้าเนื้อหาสไลด์จาก Gemini AI เข้าสู่ Slide Editor เรียบร้อยแล้ว"
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoPlayJob?.cancel()
        bmsSimulationJob?.cancel()
    }
}
