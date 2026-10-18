package com.example

import com.example.data.ProjectRepository
import com.example.model.InverterFanState
import com.example.model.PlaceholderPreset
import com.example.model.SoyosourceInverterState
import com.example.model.SoyosourceWorkingMode
import com.example.viewmodel.ProjectViewModel
import com.example.viewmodel.TechSubTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun `test soyosource inverter state calculations and zero export`() {
        val state = SoyosourceInverterState(
            dcInputVoltage = 53.4,
            dcInputCurrent = 18.6,
            acOutputWatts = 945,
            clinicLoadWatts = 945,
            gridExportWatts = 0,
            coreTemperatureC = 44.5
        )

        // Efficiency calculation should be in realistic range (88-95%)
        assertTrue(state.efficiencyPercent in 88.0..95.0)

        // Zero export to PEA grid should strictly be 0 Watts
        assertEquals(0, state.gridExportWatts)

        // Fan state should be LOW between 40°C and 48°C
        assertEquals(InverterFanState.LOW, state.fanState)
    }

    @Test
    fun `test presentation slides contain dedicated Soyosource GIT-1200W slide`() {
        val slides = ProjectRepository.presentationSlides
        val soyosourceSlide = slides.find { it.tag.contains("Soyosource", ignoreCase = true) }

        assertNotNull("Soyosource GIT-1200W slide should be present in pitch deck", soyosourceSlide)
        assertTrue(soyosourceSlide!!.title.contains("Soyosource"))
        assertEquals(PlaceholderPreset.SOYOSOURCE_GIT_1200W, soyosourceSlide.imagePlaceholder?.preset)
    }

    @Test
    fun `test default attachments contain Soyosource technical documents`() {
        val attachments = ProjectRepository.defaultAttachments
        val soyosourceDocs = attachments.filter { it.fileName.contains("Soyosource", ignoreCase = true) }

        assertTrue("Should have at least 3 Soyosource technical attachments", soyosourceDocs.size >= 3)
    }
}
