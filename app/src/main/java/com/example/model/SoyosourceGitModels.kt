package com.example.model

enum class SoyosourceWorkingMode(val label: String, val shortDesc: String) {
    LIMITER_CT("Limiter Mode (กันย้อน)", "ปรับกำลังไฟตามโหลดคลินิกอัตโนมัติ ไม่ไหลย้อนมิเตอร์ กฟภ."),
    BATTERY_DISCHARGE("Battery Mode (แบตเตอรี่)", "ดึงไฟจาก 48V LiFePO4 จ่ายเข้าคลินิก พร้อมระบบตัดไฟต่ำ LVD"),
    PV_SOLAR("PV / Solar MPPT", "ต่อตรงแผงโซล่าเซลล์ ดึงพลังงานแสงอาทิตย์ประสิทธิภาพสูงสุด")
}

enum class InverterFanState(val label: String) {
    STANDBY("หยุดหมุน (อุณหภูมิต่ำกว่า 40°C)"),
    LOW("ความเร็วต่ำ (40°C - 50°C)"),
    HIGH("ความเร็วสูง (50°C - 65°C)"),
    TURBO("เทอร์โบคูลลิ่ง (ระบายความร้อนสูงสุด)")
}

data class InverterComponentInfo(
    val name: String,
    val partNumber: String,
    val role: String,
    val repairSkill: String,
    val status: String = "สมบูรณ์"
)

data class SoyosourceInverterState(
    val modelName: String = "Soyosource GIT-1200W (GTIL-1200)",
    val isRunning: Boolean = true,
    val workingMode: SoyosourceWorkingMode = SoyosourceWorkingMode.LIMITER_CT,
    val dcInputVoltage: Double = 53.4,
    val dcInputCurrent: Double = 18.6,
    val acOutputWatts: Int = 945,
    val acOutputVoltage: Double = 226.4,
    val acFrequency: Double = 50.0,
    val maxPowerLimitWatts: Int = 1000,
    val batteryCutoffVoltage: Double = 44.0,
    val batteryRecoveryVoltage: Double = 48.0,
    val clinicLoadWatts: Int = 945,
    val gridExportWatts: Int = 0,
    val coreTemperatureC: Double = 42.5,
    val fanSpeedPercent: Int = 65,
    val ctSensorConnected: Boolean = true,
    val rs485Active: Boolean = true,
    val pureSineWaveThd: Double = 2.1,
    val isDiagnosticRunning: Boolean = false,
    val diagnosticStep: Int = 0,
    val diagnosticMessage: String = "ระบบปกติ: ผ่านเกณฑ์ความปลอดภัยและมาตรฐาน กฟภ."
) {
    val efficiencyPercent: Double
        get() {
            val dcInWatts = dcInputVoltage * dcInputCurrent
            if (dcInWatts <= 0) return 0.0
            return ((acOutputWatts / dcInWatts) * 100.0).coerceIn(88.0, 94.8)
        }

    val fanState: InverterFanState
        get() = when {
            coreTemperatureC < 38.0 -> InverterFanState.STANDBY
            coreTemperatureC < 48.0 -> InverterFanState.LOW
            coreTemperatureC < 60.0 -> InverterFanState.HIGH
            else -> InverterFanState.TURBO
        }
}
