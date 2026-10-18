package com.example.service

import android.util.Log
import com.example.BuildConfig
import com.example.model.AiSlideGenTone
import com.example.model.AiSlideRequest
import com.example.model.AiSlideResult
import com.example.model.PlaceholderPreset
import com.example.model.PromptTemplate
import com.example.model.SlideLayoutType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiSlideService {

    private val tag = "GeminiSlideService"

    // OkHttpClient with 60-second timeouts as mandated by Gemini API guidelines
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    /**
     * Checks if a valid, non-placeholder Gemini API Key is available in BuildConfig.
     */
    fun hasValidApiKey(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() &&
                key != "MY_GEMINI_API_KEY" &&
                key != "TODO" &&
                !key.startsWith("YOUR_")
    }

    /**
     * Generates slide content and bullet points based on a project summary and tone.
     * Uses Gemini 3.5 Flash REST API if key is present, or intelligent local synthesis.
     */
    suspend fun generateSlideContent(request: AiSlideRequest): AiSlideResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val hasKey = hasValidApiKey()

        if (!hasKey) {
            Log.i(tag, "Gemini API key is placeholder or empty. Using intelligent domain fallback generator.")
            return@withContext generateIntelligentFallback(request, isKeyMissing = true)
        }

        try {
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val promptText = buildPrompt(request)

            val requestBodyJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", promptText)
                            })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val generationConfig = JSONObject().apply {
                    put("temperature", 0.7)
                    put("topP", 0.95)
                    put("responseMimeType", "application/json")
                }
                put("generationConfig", generationConfig)
            }

            val httpRequest = Request.Builder()
                .url(endpoint)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .addHeader("Content-Type", "application/json")
                .build()

            client.newCall(httpRequest).execute().use { response ->
                val responseBody = response.body?.string() ?: ""

                if (!response.isSuccessful) {
                    Log.e(tag, "Gemini API HTTP ${response.code}: $responseBody")
                    throw Exception("Gemini API error (HTTP ${response.code})")
                }

                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates == null || candidates.length() == 0) {
                    throw Exception("No candidates returned from Gemini API")
                }

                val firstCandidate = candidates.getJSONObject(0)
                val contentObj = firstCandidate.optJSONObject("content")
                val parts = contentObj?.optJSONArray("parts")
                val firstPart = parts?.optJSONObject(0)
                val rawText = firstPart?.optString("text") ?: ""

                if (rawText.isBlank()) {
                    throw Exception("Empty content text from Gemini API")
                }

                parseGeminiJsonOutput(rawText, request)
            }
        } catch (e: Exception) {
            Log.w(tag, "Exception calling Gemini API: ${e.message}. Falling back gracefully.", e)
            generateIntelligentFallback(request, isKeyMissing = false, errorMessage = e.message)
        }
    }

    private fun buildPrompt(request: AiSlideRequest): String {
        val toneDescription = when (request.tone) {
            AiSlideGenTone.CSR_FUNDING -> "ขออนุมัติทุน CSR ตามมาตรา 35 เน้นความคุ้มค่าทางภาษี (ลดหย่อนภาษี 100%), ผลลัพธ์ต่อคนพิการ, และความโปร่งใสของงบประมาณ"
            AiSlideGenTone.TECHNICAL_ENGINEERING -> "เชิงวิศวกรรมขั้นสูง เน้นสเปกฮาร์ดแวร์ วงจรไฟฟ้า (เช่น Soyosource GIT-1200W, LiFePO4 48V, Smart BMS 16S, Pure Sine Wave), ความปลอดภัย และระบบอัตโนมัติ"
            AiSlideGenTone.COMMUNITY_EMPOWERMENT -> "เพื่อชุมชนคนพิการและการพึ่งพาตนเอง เน้นพัฒนาทักษะช่าง สร้างงานตัดเย็บ WFH และการซ่อมแซมฟรีแก่ผู้ยากไร้"
            AiSlideGenTone.GENERAL_PITCH -> "นำเสนอภาพรวมแบบสมดุลสำหรับคณะกรรมการ: ปัญหา โซลูชัน นวัตกรรม และผลกระทบต่อสังคม"
        }

        return """
คุณคือผู้เชี่ยวชาญการออกแบบสไลด์ Pitch Deck ระดับมืออาชีพ สำหรับโครงการนวัตกรรมเพื่อสังคม "ศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการชุมชนนวัตกรรม จ.นครนายก" และการขอรับสิทธิตาม พ.ร.บ. ส่งเสริมและพัฒนาคุณภาพชีวิตคนพิการ มาตรา 35

ผู้ใช้ต้องการสร้างเนื้อหาสไลด์และ Bullet Points จากสรุปโครงการดังนี้:
---
สรุปโครงการ: "${request.summary}"
โทนการนำเสนอ: "$toneDescription"
จำนวน Bullet Points ที่ต้องการ: ${request.targetBulletsCount} ข้อ
---

กรุณาวิเคราะห์และสร้างเนื้อหาสไลด์ 1 สไลด์ในรูปแบบ JSON Object ภาษาไทย โดยมีองค์ประกอบครบถ้วน:
- tag: หมวดหมู่สั้นๆ เช่น "นวัตกรรมเพื่อสังคม", "ระบบพลังงานหมุนเวียน", "แผนงานและงบประมาณ CSR"
- title: หัวข้อหลักของสไลด์ที่น่าสนใจและทรงพลัง (ไม่เกิน 60 ตัวอักษร)
- subtitle: วัตถุประสงค์หรือประเด็นหลักสั้นๆ (1-2 บรรทัด)
- bulletPoints: อาร์เรย์ของข้อความประเด็นสำคัญจำนวน ${request.targetBulletsCount} ข้อ (แต่ละข้อกระชับ ชัดเจน มีข้อมูลเชิงปริมาณหรือเทคนิคสนับสนุน)
- calloutText: ข้อความไฮไลต์หรือตัวเลขสำคัญ 1 ประโยค (เช่น "ลดค่าใช้จ่ายชุมชนกว่า 150,000 บาท/ปี" หรือ "ประสิทธิภาพแปลงไฟ 92% พร้อมระบบ Zero Export")
- speakerNotes: บันทึกช่วยพูดสำหรับผู้นำเสนอ (Speaker Notes) 2-3 ประโยค สำหรับใช้อธิบายขยายความ
- suggestedLayout: เลือกเลย์เอาต์ที่เหมาะสมที่สุด 1 แบบจาก: "STANDARD", "TWO_COLUMN", "HERO_STAT", "MEDIA_SPLIT"
- suggestedPreset: เลือกภาพประกอบที่เหมาะสมที่สุด 1 แบบจาก: "WHEELCHAIR_CLINIC", "SOYOSOURCE_GIT_1200W", "BMS_TELEMETRY", "BEDSORE_SEATING", "BUDGET_CHART", "HAYABUSA_EV"

ตอบกลับเป็น JSON Object ตามโครงสร้างข้างต้นเท่านั้น โดยไม่มี Markdown syntax หรือข้อความอื่นนอก JSON
""".trimIndent()
    }

    private fun parseGeminiJsonOutput(rawText: String, request: AiSlideRequest): AiSlideResult {
        // Strip markdown fences if present
        var cleanJson = rawText.trim()
        if (cleanJson.startsWith("```json")) {
            cleanJson = cleanJson.removePrefix("```json")
        } else if (cleanJson.startsWith("```")) {
            cleanJson = cleanJson.removePrefix("```")
        }
        if (cleanJson.endsWith("```")) {
            cleanJson = cleanJson.removeSuffix("```")
        }
        cleanJson = cleanJson.trim()

        val json = JSONObject(cleanJson)

        val tag = json.optString("tag", "นวัตกรรมเพื่อชุมชน (Gemini AI)")
        val title = json.optString("title", "ข้อเสนอโครงการและแนวทางพัฒนา")
        val subtitle = json.optString("subtitle", request.summary.take(120))

        val bulletsJson = json.optJSONArray("bulletPoints")
        val bulletPoints = mutableListOf<String>()
        if (bulletsJson != null && bulletsJson.length() > 0) {
            for (i in 0 until bulletsJson.length()) {
                val b = bulletsJson.optString(i)
                if (b.isNotBlank()) bulletPoints.add(b)
            }
        }
        if (bulletPoints.isEmpty()) {
            bulletPoints.addAll(listOf(
                "ยกระดับการให้บริการซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการในระดับตำบล",
                "ประยุกต์ใช้เทคโนโลยีวิศวกรรมฟื้นฟูและระบบพลังงานหมุนเวียน",
                "สร้างงานและรายได้ที่มั่นคงตามสิทธิมาตรา 35",
                "วัดผลลัพธ์ทางสังคมและความคุ้มค่าในการลงทุน CSR อย่างโปร่งใส"
            ))
        }

        val calloutText = json.optString("calloutText").takeIf { it.isNotBlank() }
        val speakerNotes = json.optString("speakerNotes", "สไลด์นี้นำเสนอหัวใจสำคัญของโครงการตามที่ระบุในสรุป")

        val layoutStr = json.optString("suggestedLayout", "STANDARD")
        val suggestedLayout = try {
            SlideLayoutType.valueOf(layoutStr)
        } catch (_: Exception) {
            SlideLayoutType.STANDARD
        }

        val presetStr = json.optString("suggestedPreset", "WHEELCHAIR_CLINIC")
        val suggestedPreset = try {
            PlaceholderPreset.valueOf(presetStr)
        } catch (_: Exception) {
            PlaceholderPreset.WHEELCHAIR_CLINIC
        }

        return AiSlideResult(
            tag = tag,
            title = title,
            subtitle = subtitle,
            bulletPoints = bulletPoints,
            calloutText = calloutText,
            speakerNotes = speakerNotes,
            suggestedLayout = suggestedLayout,
            suggestedPreset = suggestedPreset,
            isLiveApiCall = true,
            modelName = "gemini-3.5-flash",
            promptSummary = request.summary
        )
    }

    /**
     * Intelligent local fallback synthesis tailored to the project domain.
     * Guarantees realistic, high-quality results even when API key is missing or offline.
     */
    fun generateIntelligentFallback(
        request: AiSlideRequest,
        isKeyMissing: Boolean = true,
        errorMessage: String? = null
    ): AiSlideResult {
        val s = request.summary.trim()
        val lower = s.lowercase()

        val isEnergy = lower.contains("soyo") || lower.contains("inverter") || lower.contains("พลังงาน") || lower.contains("ไฟ") || lower.contains("แผง")
        val isWheelchair = lower.contains("ซ่อม") || lower.contains("วีลแชร์") || lower.contains("รถเข็น") || lower.contains("คนพิการ")
        val isSewing = lower.contains("เย็บ") || lower.contains("เบาะ") || lower.contains("แผลกดทับ") || lower.contains("wfh")
        val isBudget = lower.contains("งบ") || lower.contains("เงิน") || lower.contains("บาท") || lower.contains("csr") || lower.contains("มาตรา")

        val tag: String
        val title: String
        val subtitle: String
        val bullets: List<String>
        val callout: String
        val notes: String
        val layout: SlideLayoutType
        val preset: PlaceholderPreset

        when {
            isEnergy -> {
                tag = "นวัตกรรมพลังงานหมุนเวียน (Microgrid)"
                title = if (s.length in 5..50) s else "ระบบไมโครกริดและอินเวอร์เตอร์ Soyosource GIT-1200W"
                subtitle = "จ่ายพลังงานสะอาด 220V จากแบตเตอรี่ฟื้นฟูเพื่อเครื่องมือช่างและสำรองไฟผู้ป่วยติดเตียง"
                bullets = listOf(
                    "ระบบ Grid-Tie Inverter 1200W พร้อม CT Sensor กันย้อน (Zero Export) ตอบสนองใน 200ms",
                    "ต่อพ่วงชุดแบตเตอรี่ LiFePO4 48V (16S) และโซลาร์เซลล์ ลดค่าไฟศูนย์ซ่อมได้มากกว่า 450 บาท/เดือน",
                    "สัญญาณขับแบบ Pure Sine Wave ความเพี้ยน THD <3% ปลอดภัยต่ออุปกรณ์เครื่องมือแพทย์และอุปกรณ์ช่าง",
                    "ความสามารถซ่อมบำรุงบอร์ดระดับคอมโพเนนต์ (MOSFET IRFP4668) โดยช่างคนพิการในศูนย์เอง"
                )
                callout = "ประสิทธิภาพแปลงพลังงาน >92% พร้อมตัดไฟอัตโนมัติป้องกันแบตเตอรี่โอเวอร์ดิสชาร์จ"
                notes = "เน้นย้ำกับผู้บริหารว่าอินเวอร์เตอร์ Soyosource 1200W ช่วยให้ศูนย์ซ่อมมีพลังงานสำรองฟรี และช่างมีทักษะซ่อมบอร์ดไฟฟ้าระดับสูง"
                layout = SlideLayoutType.MEDIA_SPLIT
                preset = PlaceholderPreset.SOYOSOURCE_GIT_1200W
            }
            isSewing -> {
                tag = "อาชีพคนพิการ & WFH"
                title = if (s.length in 5..50) s else "งานตัดเย็บเบาะพนักพิงลดแผลกดทับด้วยจักรอุตสาหกรรม"
                subtitle = "สร้างรายได้ประจำให้ผู้พิการและผู้ดูแล (มารดา) ทำงานจากที่บ้าน (Work From Home)"
                bullets = listOf(
                    "เบาะรองนั่ง 3 ชั้น (ฟองน้ำอัดแน่น + เมมโมรี่โฟมกระจายแรง + ชั้นลดแรงเฉือน) ป้องกันแผลกดทับเกรด 2-3",
                    "หุ้มหนัง Medical PVC กันน้ำ ทำความสะอาดง่าย ไม่อมกลิ่น ปลอดภัยต่อผิวหนังผู้ป่วยติดเตียง",
                    "จัดหาจักรอุตสาหกรรมตัดเย็บ 15,000 บาท ภายใต้งบอุปกรณ์ตั้งต้นมาตรา 35",
                    "กำลังการผลิต 15-20 ชุด/เดือน ส่งมอบให้ผู้ป่วยยากไร้ใน จ.นครนายก โดยไม่คิดค่าใช้จ่าย"
                )
                callout = "ประหยัดค่าเบาะนำเข้าให้ครอบครัวคนพิการชิ้นละ 2,500 - 4,500 บาท"
                notes = "ชี้ให้เห็นถึงความเกื้อกูลในครอบครัว ผู้พิการและแม่สามารถทำงานร่วมกันได้อย่างมีศักดิ์ศรีและมีรายได้ที่มั่นคง"
                layout = SlideLayoutType.TWO_COLUMN
                preset = PlaceholderPreset.BEDSORE_SEATING
            }
            isBudget -> {
                tag = "ความคุ้มค่าทางการเงิน & ภาษี"
                title = if (s.length in 5..50) s else "งบประมาณจัดหาอุปกรณ์ตั้งต้น 60,000 บาท (คืนทุนสังคม)"
                subtitle = "จัดสรรงบประมาณอย่างโปร่งใสตามมาตรา 35 สิทธิประโยชน์ทางภาษีนิติบุคคล 100%"
                bullets = listOf(
                    "เครื่องมือช่างอิเล็กทรอนิกส์และเครื่องวัดแบตเตอรี่ 16,500 บาท สำหรับงานช่างระดับวิศวกรรม",
                    "จักรอุตสาหกรรมและอุปกรณ์ตัดเย็บ 15,000 บาท สำหรับอาชีพ WFH ผู้พิการและมารดา",
                    "อะไหล่และชุดแบตเตอรี่สำรอง 28,500 บาท สำหรับหมุนเวียนให้บริการซ่อมฟรีในชุมชน",
                    "บริษัทผู้ให้สิทธิ์สามารถนำค่าใช้จ่ายทั้งหมดไปหักลดหย่อนภาษีได้ 100% เต็มตามกฎหมาย"
                )
                callout = "ผลตอบแทนทางสังคม (SROI): ทุก 1 บาทที่ลงทุน สร้างมูลค่าฟื้นฟูอุปกรณ์ 3.4 บาท"
                notes = "เน้นย้ำความโปร่งใสของบัญชีและเอกสารแนบตามแบบฟอร์ม พม. สนย.1 เพื่อให้คณะกรรมการอนุมัติได้ง่าย"
                layout = SlideLayoutType.HERO_STAT
                preset = PlaceholderPreset.BUDGET_CHART
            }
            else -> {
                tag = "ข้อเสนอโครงการ (มาตรา 35)"
                title = if (s.length in 5..50) s else "ศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการชุมชนนวัตกรรม"
                subtitle = if (s.isNotBlank()) s else "ยกระดับคุณภาพชีวิตคนพิการใน จ.นครนายก ด้วยทักษะช่างและวิศวกรรมฟื้นฟู"
                bullets = listOf(
                    "ซ่อมบำรุงรถวีลแชร์ไฟฟ้า รถสามล้อโยก และฟื้นฟูแบตเตอรี่ลิเธียมฟรีแก่คนพิการยากไร้",
                    "พัฒนาโดย นายศรายุทธ มูลผล ผู้มีทักษะวิศวกรรมมอเตอร์ไซค์ไฟฟ้าและระบบ BMS ออนไลน์",
                    "ส่งเสริมการจ้างเหมาบริการและสัมปทานตามมาตรา 35 พ.ร.บ. ส่งเสริมคุณภาพชีวิตคนพิการ",
                    "เป้าหมายซ่อมแซมและฟื้นฟูอุปกรณ์ไม่ต่ำกว่า 45-60 คันต่อปี ลดภาระค่าใช้จ่ายครอบครัว"
                )
                callout = "สร้างผลกระทบเชิงบวกต่อคนพิการกว่า 120 ครอบครัวในจังหวัดนครนายก"
                notes = "อธิบายภาพรวมโครงการที่ผสานทักษะช่างระดับสูงและการช่วยเหลือสังคมเข้าด้วยกันอย่างลงตัว"
                layout = SlideLayoutType.STANDARD
                preset = PlaceholderPreset.WHEELCHAIR_CLINIC
            }
        }

        return AiSlideResult(
            tag = tag,
            title = title,
            subtitle = subtitle,
            bulletPoints = bullets,
            calloutText = callout,
            speakerNotes = notes,
            suggestedLayout = layout,
            suggestedPreset = preset,
            isLiveApiCall = false,
            modelName = if (isKeyMissing) "gemini-3.5-flash (Demo Fallback)" else "gemini-3.5-flash (Offline Mode)",
            promptSummary = request.summary
        )
    }

    companion object {
        val samplePromptTemplates = listOf(
            PromptTemplate(
                chipLabel = "🛠️ ศูนย์ช่าง ม.35 นครนายก",
                promptText = "จัดตั้งศูนย์ซ่อมแซมและฟื้นฟูรถเข็นวีลแชร์ไฟฟ้าฟรีแก่คนพิการยากไร้ใน จ.นครนายก ภายใต้การให้สิทธิตามมาตรา 35",
                tone = AiSlideGenTone.CSR_FUNDING,
                suggestedPreset = PlaceholderPreset.WHEELCHAIR_CLINIC
            ),
            PromptTemplate(
                chipLabel = "⚡ อินเวอร์เตอร์ Soyosource 1200W",
                promptText = "ติดตั้งอินเวอร์เตอร์กันย้อน Soyosource GIT-1200W ต่อร่วมแบตเตอรี่ลิเธียมฟอสเฟต 48V เพื่อลดค่าไฟและสำรองไฟให้เตียงคนไข้",
                tone = AiSlideGenTone.TECHNICAL_ENGINEERING,
                suggestedPreset = PlaceholderPreset.SOYOSOURCE_GIT_1200W
            ),
            PromptTemplate(
                chipLabel = "🧵 ตัดเย็บเบาะแผลกดทับ WFH",
                promptText = "งานตัดเย็บเบาะรองนั่ง Anti-Bedsore 3 ชั้น กระจายแรงกดทับ ด้วยจักรอุตสาหกรรม โดยคนพิการและแม่ทำงานจากที่บ้าน (WFH)",
                tone = AiSlideGenTone.COMMUNITY_EMPOWERMENT,
                suggestedPreset = PlaceholderPreset.BEDSORE_SEATING
            ),
            PromptTemplate(
                chipLabel = "🔋 ฟื้นฟูแบตเตอรี่ & Smart BMS",
                promptText = "ฟื้นฟูเซลล์แบตเตอรี่ลิเธียม LiFePO4 ด้วยเครื่องบาลานซ์และติดตั้ง Smart BMS IoT เพื่อตรวจเช็กแรงดันเซลล์แบบเรียลไทม์",
                tone = AiSlideGenTone.TECHNICAL_ENGINEERING,
                suggestedPreset = PlaceholderPreset.BMS_TELEMETRY
            ),
            PromptTemplate(
                chipLabel = "📊 งบอุปกรณ์ตั้งต้น 60,000 บ.",
                promptText = "แผนงบประมาณอุปกรณ์เครื่องมือช่าง จักรอุตสาหกรรม และอะไหล่รวม 60,000 บาท ความคุ้มค่าทางภาษี 100% สำหรับบริษัทผู้สนับสนุน",
                tone = AiSlideGenTone.CSR_FUNDING,
                suggestedPreset = PlaceholderPreset.BUDGET_CHART
            )
        )
    }
}
