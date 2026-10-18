package com.example.model

enum class SlideLayoutType(val label: String, val description: String) {
    STANDARD("มาตรฐาน (Bullets)", "หัวข้อประเด็นสำคัญพร้อมกล่องไฮไลต์"),
    TWO_COLUMN("คอลัมน์คู่ (Split)", "แบ่ง 2 คอลัมน์ ข้อความและกล่องสรุปเปรียบเทียบ"),
    HERO_STAT("จุดเด่น & ผลลัพธ์", "เน้นข้อความใหญ่พร้อมการ์ดตัวเลขสรุป"),
    MEDIA_SPLIT("เน้นภาพนวัตกรรม", "ข้อความพร้อมการ์ดรูปภาพหรือผังวงจร")
}

enum class PlaceholderPreset(val label: String, val defaultTitle: String, val defaultCaption: String) {
    HAYABUSA_EV("ภาพ Hayabusa EV", "บิ๊กไบค์ไฟฟ้า Hayabusa EV", "โครงการแปลงมอเตอร์ไซค์ไฟฟ้าแรงสูง รับรองโดย ดร.อภิรักษ์"),
    BMS_TELEMETRY("ภาพวงจร IoT BMS", "ผังวงจร Smart IoT BMS & Soyosource 1200W", "ระบบตรวจสอบแรงดันเซลล์ LiFePO4 ออนไลน์"),
    WHEELCHAIR_CLINIC("ภาพศูนย์ซ่อมชุมชน", "ศูนย์ซ่อมอุปกรณ์คนพิการ จ.นครนายก", "ศูนย์บริการซ่อมฟรีและฟื้นฟูแบตเตอรี่ในพื้นที่"),
    BEDSORE_SEATING("ภาพเบาะ Anti-Bedsore", "เบาะรองนั่งกระจายแรงกดทับ 3 ชั้น", "งานตัดเย็บจักรอุตสาหกรรม WFH โดยน้องผู้พิการและมารดา"),
    BUDGET_CHART("ภาพผังงบประมาณ", "งบจัดหาอุปกรณ์ตั้งต้น 60,000 บาท", "เครื่องมือช่าง 16,500 บ., จักร 15,000 บ., อะไหล่สำรอง 28,500 บ."),
    SOYOSOURCE_GIT_1200W("ภาพ Soyosource GIT-1200W", "อินเวอร์เตอร์ Soyosource GIT-1200W", "ระบบ Grid-Tie Inverter กันย้อน (Zero Export) และไมโครกริดชุมชน"),
    CUSTOM("เลือกภาพจากเครื่อง", "รูปภาพประกอบสไลด์", "ภาพประกอบการนำเสนอสำหรับคณะกรรมการ")
}

data class SlideImagePlaceholder(
    val preset: PlaceholderPreset = PlaceholderPreset.WHEELCHAIR_CLINIC,
    val title: String = "",
    val caption: String = "",
    val uriString: String? = null
)
