package com.example

import com.example.data.ProjectRepository
import com.example.viewmodel.Section35CalcState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectDataTest {

    @Test
    fun testProjectRepositoryIntegrity() {
        assertEquals("ศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการชุมชนนวัตกรรม", ProjectRepository.PROJECT_TITLE)
        assertEquals("จังหวัดนครนายก", ProjectRepository.TARGET_AREA)
        assertEquals(4, ProjectRepository.references.size)
        assertEquals(9, ProjectRepository.presentationSlides.size)
        assertEquals(3, ProjectRepository.objectives.size)
        assertEquals(2, ProjectRepository.scopeSections.size)
        assertEquals(3, ProjectRepository.csrBenefits.size)
        assertEquals(60000L, ProjectRepository.totalEquipmentBudgetThb)
    }

    @Test
    fun testReferencesIncludeKeyMentors() {
        val names = ProjectRepository.references.map { it.name }
        assertTrue(names.any { it.contains("สุพร ทำมงคลสวัสดิ์") })
        assertTrue(names.any { it.contains("อภิรักษ์ จันทร") })
        assertTrue(names.any { it.contains("ปรีดา นันตคุณ") })
        assertTrue(names.any { it.contains("อุดมโชค ชูรัตน์") })
    }

    @Test
    fun testSection35CalculatorCalculations() {
        val calc = Section35CalcState(
            supportedRights = 2,
            annualWagePerRight = 119720L,
            includeStartupEquipment = true,
            startupEquipmentBudget = 60000L,
            corporateTaxRatePercent = 20.0
        )
        // 2 * 119720 + 60000 = 239440 + 60000 = 299440
        assertEquals(299440L, calc.totalDirectInvestment)
        // 20% of 299440 = 59888
        assertEquals(59888L, calc.taxDeductionSavings)
        // 299440 - 59888 = 239552
        assertEquals(239552L, calc.netInvestmentAfterTax)
        // Repaired estimate 2 * 45 = 90
        assertEquals(90, calc.estimatedWheelchairsRepairedFree)
    }

    @Test
    fun testAttachmentsIntegrity() {
        val attachments = ProjectRepository.defaultAttachments
        assertTrue(attachments.size >= 8)
        assertTrue(attachments.any { it.fileType == com.example.model.AttachmentType.PDF })
        assertTrue(attachments.any { it.fileType == com.example.model.AttachmentType.DOCX })
        assertTrue(attachments.any { it.fileType == com.example.model.AttachmentType.IMAGE })
        assertTrue(attachments.any { it.fileType == com.example.model.AttachmentType.VIDEO })

        // Check specific key proposal files
        assertTrue(attachments.any { it.fileName.contains("เล่มข้อเสนอโครงการ_ม35") })
        assertTrue(attachments.any { it.fileName.contains("หนังสือรับรองความเชี่ยวชาญ_ดร_อภิรักษ์") })
        assertTrue(attachments.any { it.fileName.contains("Hayabusa_EV") })
        assertTrue(attachments.any { it.fileName.contains("Smart_BMS_IoT") })
    }

    @Test
    fun testAttachmentFormatting() {
        val file = com.example.model.AttachmentFile(
            id = "T1",
            fileName = "test.pdf",
            fileType = com.example.model.AttachmentType.PDF,
            fileSizeBytes = 2500000L,
            uploadedDate = "17 ต.ค. 2569",
            category = com.example.model.AttachmentCategory.PROPOSAL_DOC,
            description = "Test description"
        )
        assertEquals("2.4 MB", file.formattedSize)
    }

    @Test
    fun testSoyosourceInverterData() {
        val attachments = ProjectRepository.defaultAttachments
        // Verify Soyosource manual and schematic attachments exist
        assertTrue(attachments.any { it.fileName.contains("Soyosource_GIT_1200W") })
        assertTrue(attachments.any { it.fileName.contains("Soyosource_IRFP4668") })
        assertTrue(attachments.any { it.fileName.contains("Soyosource_GIT1200W") })

        // Verify dedicated Soyosource slide in presentation deck
        val slides = ProjectRepository.presentationSlides
        val soyoSlide = slides.find { it.tag.contains("Soyosource") }
        assertNotNull(soyoSlide)
        assertTrue(soyoSlide!!.title.contains("พลังงานหมุนเวียน") || soyoSlide.title.contains("Soyosource"))

        // Verify Soyosource hardware components
        val components = ProjectRepository.soyosourceComponents
        assertTrue(components.isNotEmpty())
        assertTrue(components.any { it.partNumber.contains("IRFP4668") || it.partNumber.contains("HY4008") })
        assertTrue(components.any { it.partNumber.contains("EG8010") })
        assertTrue(components.any { it.name.contains("CT") || it.partNumber.contains("RS485") })
    }

    @Test
    fun testGeminiSlideGenerationFallback() {
        val service = com.example.service.GeminiSlideService()
        val request = com.example.model.AiSlideRequest(
            summary = "ติดตั้งอินเวอร์เตอร์กันย้อน Soyosource GIT-1200W ในศูนย์ซ่อม",
            tone = com.example.model.AiSlideGenTone.TECHNICAL_ENGINEERING,
            targetBulletsCount = 4
        )
        val result = service.generateIntelligentFallback(request, isKeyMissing = true)

        assertNotNull(result)
        assertTrue(result.title.isNotBlank())
        assertTrue(result.subtitle.isNotBlank())
        assertTrue(result.bulletPoints.size >= 3)
        assertTrue(result.bulletPoints.any { it.contains("Soyosource") || it.contains("Inverter") || it.contains("CT") })
        assertNotNull(result.calloutText)
        assertTrue(result.speakerNotes.isNotBlank())
        assertEquals(com.example.model.PlaceholderPreset.SOYOSOURCE_GIT_1200W, result.suggestedPreset)
    }

    @Test
    fun testGeminiPromptTemplatesIntegrity() {
        val templates = com.example.service.GeminiSlideService.samplePromptTemplates
        assertTrue(templates.size >= 4)
        templates.forEach { template ->
            assertTrue(template.chipLabel.isNotBlank())
            assertTrue(template.promptText.isNotBlank())
            assertNotNull(template.tone)
            assertNotNull(template.suggestedPreset)
        }
    }

    @Test
    fun testEsp32RobotFaceData() {
        // Verify ROBOT_OLED exists in TechSubTab
        val tabs = com.example.viewmodel.TechSubTab.entries
        assertTrue(tabs.any { it == com.example.viewmodel.TechSubTab.ROBOT_OLED })

        // Verify Robot Face attachment
        val attachments = ProjectRepository.defaultAttachments
        val robotAttachment = attachments.find { it.fileName.contains("ESP32_Robot_Face") }
        assertNotNull(robotAttachment)
        assertEquals(com.example.model.AttachmentType.OTHER, robotAttachment!!.fileType)
        assertTrue(robotAttachment.description.contains("GPIO4") || robotAttachment.description.contains("C++"))
    }
}
