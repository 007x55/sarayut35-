package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ContactModalBottomSheet
import com.example.ui.components.ExecutiveSummaryDialog
import com.example.ui.screens.AttachmentManagerScreen
import com.example.ui.screens.CommunityImpactScreen
import com.example.ui.screens.PitchDeckScreen
import com.example.ui.screens.ProjectDossierScreen
import com.example.ui.screens.Section35CalculatorScreen
import com.example.ui.screens.TechShowcaseScreen
import com.example.ui.theme.Blue700
import com.example.ui.theme.Cyan300
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.ProjectViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ProjectViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                androidx.compose.foundation.layout.Column(
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "ศูนย์ซ่อมวีลแชร์นวัตกรรม ม.35",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Text(
                                        text = "ข้อเสนอ จ.นครนายก • นายสรายุทธ มูลผล",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { viewModel.selectTab(AppNavTab.ATTACHMENTS) },
                                    modifier = Modifier.testTag("top_attachments_button")
                                ) {
                                    androidx.compose.material3.BadgedBox(
                                        badge = {
                                            androidx.compose.material3.Badge(
                                                containerColor = MaterialTheme.colorScheme.tertiary,
                                                contentColor = Color.Black
                                            ) {
                                                Text("${uiState.attachments.size}", fontSize = 9.sp)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AttachFile,
                                            contentDescription = "Attachments",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { viewModel.openSummaryShareDialog(true) },
                                    modifier = Modifier.testTag("top_share_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share Proposal",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.openContactSheet(true) },
                                    modifier = Modifier.testTag("top_contact_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Contact",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("bottom_navigation_bar")
                        ) {
                            AppNavTab.entries.forEach { tab ->
                                val isSelected = uiState.activeTab == tab
                                val tabIcon = getTabIcon(tab)

                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { viewModel.selectTab(tab) },
                                    icon = {
                                        if (tab == AppNavTab.ATTACHMENTS && uiState.attachments.isNotEmpty()) {
                                            androidx.compose.material3.BadgedBox(
                                                badge = {
                                                    androidx.compose.material3.Badge(
                                                        containerColor = MaterialTheme.colorScheme.primary,
                                                        contentColor = Color.White
                                                    ) {
                                                        Text("${uiState.attachments.size}", fontSize = 9.sp)
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = tabIcon,
                                                    contentDescription = tab.label,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        } else {
                                            Icon(
                                                imageVector = tabIcon,
                                                contentDescription = tab.label,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = tab.label,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                                        unselectedTextColor = MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.activeTab) {
                            AppNavTab.PITCH_DECK -> {
                                PitchDeckScreen(
                                    slides = viewModel.slides,
                                    currentIndex = uiState.currentSlideIndex,
                                    showSpeakerNotes = uiState.showSpeakerNotes,
                                    isAutoPlay = uiState.isAutoPlay,
                                    onNext = { viewModel.nextSlide() },
                                    onPrev = { viewModel.prevSlide() },
                                    onSelectSlide = { viewModel.goToSlide(it) },
                                    onToggleNotes = { viewModel.toggleSpeakerNotes() },
                                    onToggleAutoPlay = { viewModel.toggleAutoPlay() },
                                    onShareSummary = { viewModel.openSummaryShareDialog(true) },
                                    onNavigateToAttachments = { viewModel.selectTab(AppNavTab.ATTACHMENTS) },
                                    onOpenEditor = { viewModel.openSlideEditor(it) },
                                    onAddNewSlide = { viewModel.openSlideEditorForNewSlide() },
                                    onOpenAiSlideGen = { viewModel.openAiSlideGenSheet() }
                                )
                            }
                            AppNavTab.PROJECT_DOSSIER -> {
                                ProjectDossierScreen(
                                    references = viewModel.references,
                                    scopeSections = viewModel.scopeSections,
                                    csrBenefits = viewModel.csrBenefits,
                                    budgetItems = viewModel.budgetItems,
                                    attachments = uiState.attachments,
                                    onNavigateToAttachments = { viewModel.selectTab(AppNavTab.ATTACHMENTS) }
                                )
                            }
                            AppNavTab.TECH_SHOWCASE -> {
                                TechShowcaseScreen(
                                    bmsState = uiState.bmsSimState,
                                    soyosourceState = uiState.soyosourceState,
                                    soyosourceComponents = viewModel.soyosourceComponents,
                                    activeSubTab = uiState.activeTechSubTab,
                                    onSelectSubTab = { viewModel.setTechSubTab(it) },
                                    onToggleBalance = { viewModel.toggleBmsBalancing() },
                                    onToggleInverterPower = { viewModel.toggleInverterPower() },
                                    onSelectInverterMode = { viewModel.setSoyosourceMode(it) },
                                    onSimulateInverterLoad = { viewModel.simulateWorkshopLoad(it) },
                                    onSetInverterMaxPower = { viewModel.setInverterMaxPower(it) },
                                    onSetInverterCutoffVoltage = { viewModel.setInverterCutoffVoltage(it) },
                                    onRunInverterDiagnostic = { viewModel.runInverterDiagnostic() }
                                )
                            }
                            AppNavTab.SECTION35_CALC -> {
                                Section35CalculatorScreen(
                                    calcState = uiState.calcState,
                                    onRightsChanged = { viewModel.updateSupportedRights(it) },
                                    onToggleEquipment = { viewModel.toggleIncludeStartupEquipment() }
                                )
                            }
                            AppNavTab.ATTACHMENTS -> {
                                AttachmentManagerScreen(
                                    attachments = uiState.attachments,
                                    selectedCategory = uiState.attachmentCategoryFilter,
                                    searchQuery = uiState.attachmentSearchQuery,
                                    selectedFileForPreview = uiState.selectedAttachmentForPreview,
                                    fileToDelete = uiState.attachmentToDelete,
                                    showUploadDialog = uiState.showUploadDialog,
                                    onCategorySelected = { viewModel.setAttachmentCategoryFilter(it) },
                                    onSearchQueryChanged = { viewModel.setAttachmentSearchQuery(it) },
                                    onOpenUploadDialog = { viewModel.setShowUploadDialog(true) },
                                    onCloseUploadDialog = { viewModel.setShowUploadDialog(false) },
                                    onUploadNewFile = { name, type, size, cat, desc, uri ->
                                        viewModel.addAttachment(name, type, size, cat, desc, uri)
                                    },
                                    onSelectPreview = { viewModel.setSelectedAttachmentForPreview(it) },
                                    onSelectDelete = { viewModel.setAttachmentToDelete(it) },
                                    onConfirmDelete = { viewModel.confirmDeleteAttachment() },
                                    onResetDefaults = { viewModel.resetDefaultAttachments() }
                                )
                            }
                            AppNavTab.COMMUNITY_QUEUE -> {
                                CommunityImpactScreen(
                                    repairRecords = viewModel.communityRepairs
                                )
                            }
                        }
                    }

                    // Toast Notifications
                    androidx.compose.runtime.LaunchedEffect(uiState.userNotification) {
                        uiState.userNotification?.let { msg ->
                            android.widget.Toast.makeText(this@MainActivity, msg, android.widget.Toast.LENGTH_SHORT).show()
                            viewModel.clearNotification()
                        }
                    }

                    // Slide Editor Modal Bottom Sheet
                    if (uiState.isSlideEditorOpen && uiState.slideBeingEdited != null) {
                        com.example.ui.components.SlideEditor(
                            slide = uiState.slideBeingEdited!!,
                            onSave = { viewModel.saveSlide(it) },
                            onDelete = { viewModel.deleteSlide(it) },
                            onDismiss = { viewModel.closeSlideEditor() },
                            onOpenAiGenerator = { viewModel.openAiSlideGenSheet(initialSummary = it) }
                        )
                    }

                    // Gemini AI Slide Generator Modal Bottom Sheet
                    if (uiState.isAiSlideGenSheetOpen) {
                        com.example.ui.components.AiSlideGeneratorSheet(
                            isOpen = uiState.isAiSlideGenSheetOpen,
                            state = uiState.aiSlideGenState,
                            promptSummary = uiState.aiPromptSummary,
                            selectedTone = uiState.aiSelectedTone,
                            targetBulletsCount = uiState.aiTargetBulletsCount,
                            hasValidApiKey = viewModel.geminiSlideService.hasValidApiKey(),
                            onSummaryChanged = { viewModel.setAiPromptSummary(it) },
                            onToneSelected = { viewModel.setAiSelectedTone(it) },
                            onBulletsCountChanged = { viewModel.setAiTargetBulletsCount(it) },
                            onGenerate = { viewModel.generateSlideWithGemini() },
                            onApplyAsNewSlide = { viewModel.applyGeneratedSlideAsNew(it) },
                            onApplyToEditor = { viewModel.applyGeneratedSlideToEditor(it) },
                            onDismiss = { viewModel.closeAiSlideGenSheet() }
                        )
                    }

                    // Contact Modal Bottom Sheet
                    if (uiState.showContactSheet) {
                        ContactModalBottomSheet(
                            onDismiss = { viewModel.openContactSheet(false) }
                        )
                    }

                    // Executive Summary Share Dialog
                    if (uiState.showSummaryShareDialog) {
                        ExecutiveSummaryDialog(
                            summaryText = viewModel.getExecutiveSummaryText(),
                            onDismiss = { viewModel.openSummaryShareDialog(false) }
                        )
                    }
                }
            }
        }
    }

    private fun getTabIcon(tab: AppNavTab): ImageVector {
        return when (tab) {
            AppNavTab.PITCH_DECK -> Icons.Default.Slideshow
            AppNavTab.PROJECT_DOSSIER -> Icons.Default.Description
            AppNavTab.TECH_SHOWCASE -> Icons.Default.PrecisionManufacturing
            AppNavTab.SECTION35_CALC -> Icons.Default.Calculate
            AppNavTab.ATTACHMENTS -> Icons.Default.AttachFile
            AppNavTab.COMMUNITY_QUEUE -> Icons.Default.VolunteerActivism
        }
    }
}
