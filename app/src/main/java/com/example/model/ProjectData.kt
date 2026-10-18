package com.example.model

data class ReferencePerson(
    val id: String,
    val name: String,
    val title: String,
    val organization: String,
    val endorsement: String,
    val badge: String
)

data class ScopeSection(
    val id: String,
    val title: String,
    val leader: String,
    val role: String,
    val iconName: String,
    val items: List<ScopeItem>
)

data class ScopeItem(
    val title: String,
    val detail: String,
    val highlight: String
)

data class BudgetItem(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val amountThb: Long,
    val iconName: String
)

data class CsrImpactBenefit(
    val number: Int,
    val title: String,
    val description: String,
    val tangibleOutcome: String,
    val sdgTarget: String
)

data class PresentationSlide(
    val id: Int,
    val tag: String,
    val title: String,
    val subtitle: String,
    val keyPoints: List<String>,
    val speakerNotes: String,
    val calloutText: String? = null,
    val layoutType: SlideLayoutType = SlideLayoutType.STANDARD,
    val imagePlaceholder: SlideImagePlaceholder? = null
)

data class ServiceRepairRecord(
    val id: String,
    val beneficiaryName: String,
    val disabilityType: String,
    val location: String,
    val deviceType: String,
    val problemDescription: String,
    val solutionApplied: String,
    val savedCostForUserThb: Long,
    val date: String,
    val status: String
)
