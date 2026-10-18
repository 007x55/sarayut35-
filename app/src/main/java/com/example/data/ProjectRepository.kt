package com.example.data

import com.example.model.BudgetItem
import com.example.model.CsrImpactBenefit
import com.example.model.InverterComponentInfo
import com.example.model.PresentationSlide
import com.example.model.ReferencePerson
import com.example.model.ScopeItem
import com.example.model.ScopeSection
import com.example.model.ServiceRepairRecord

object ProjectRepository {
    const val PROJECT_TITLE = "ศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการชุมชนนวัตกรรม"
    const val PROJECT_SUBTITLE = "Community Wheelchair Repair & Battery Clinic"
    const val LEGAL_BASIS = "โครงการเสนอขอรับสิทธิสัมปทานและจ้างเหมาบริการ ตามมาตรา 35 พ.ร.บ.ส่งเสริมและพัฒนาคุณภาพชีวิตคนพิการ"
    const val TARGET_AREA = "จังหวัดนครนายก"
    const val PROPOSER_NAME = "นายสรายุทธ มูลผล และครอบครัว"
    const val PROPOSER_GROUP = "เครือข่ายช่างชุมชนจิตอาสาจังหวัดนครนายก"
    const val CONTACT_PHONE = "081-xxx-xxxx"
    const val CONTACT_EMAIL = "sarayutm2016@gmail.com"
    const val REQUESTED_RIGHTS = 2 // 2 สิทธิ (นายสรายุทธ 1 สิทธิ + น้องผู้พิการ 1 สิทธิ)

    val references = listOf(
        ReferencePerson(
            id = "ref1",
            name = "อาจารย์สุพร ทำมงคลสวัสดิ์",
            title = "อาจารย์ใหญ่",
            organization = "โรงเรียนอาชีวพระมหาไถ่ พัทยา",
            endorsement = "รับรองประวัติการศึกษา ทักษะวิชาชีพช่างเฉพาะทาง และความมุ่งมั่นในการทำงานอุทิศตนเพื่อสังคมและคนพิการ",
            badge = "ผู้รับรองประวัติและทักษะวิชาชีพ"
        ),
        ReferencePerson(
            id = "ref2",
            name = "ดร. อภิรักษ์ จันทร",
            title = "ผู้มอบหมายงานและที่ปรึกษาโครงการ",
            organization = "ผู้เชี่ยวชาญด้านระบบวิศวกรรมยานยนต์ไฟฟ้าและพลังงานทดแทน",
            endorsement = "รับรองความเชี่ยวชาญระดับสูงด้านการดัดแปลงยานยนต์ไฟฟ้า (EV Conversion) เช่น โปรเจกต์รถจักรยานยนต์ไฟฟ้าฮายาบูซะ (Hayabusa), การประกอบและฟื้นฟูแบตเตอรี่ลิเธียม, อินเวอร์เตอร์ Soyosource 1200W/Grid-tie และการเขียนโค้ดพัฒนาระบบ IoT ตรวจสอบ BMS ออนไลน์",
            badge = "ผู้รับรองความเชี่ยวชาญวิศวกรรม EV & IoT"
        ),
        ReferencePerson(
            id = "ref3",
            name = "อาจารย์ปรีดา นันตคุณ",
            title = "ผู้ทรงคุณวุฒิด้านการพัฒนาโครงการ",
            organization = "สถาบันพัฒนาศักยภาพชุมชน",
            endorsement = "รับรองการผ่านการอบรมหลักสูตรการเขียน บริหารจัดการ และการประเมินผลโครงการพัฒนาคุณภาพชีวิตคนพิการ",
            badge = "ผู้รับรองการบริหารจัดการโครงการ"
        ),
        ReferencePerson(
            id = "ref4",
            name = "คุณอุดมโชค ชูรัตน์",
            title = "ผู้ประสานงานเครือข่ายภาคประชาสังคม",
            organization = "เครือข่ายส่งเสริมโอกาสคนพิการ",
            endorsement = "รับรองทักษะการปฏิบัติงานภาคสนาม การเข้าถึงชุมชน และความสามารถในการทำงานร่วมกับเครือข่ายคนพิการในพื้นที่จริง",
            badge = "ผู้รับรองทักษะปฏิบัติงานชุมชน"
        )
    )

    val objectives = listOf(
        "เพื่อขอรับการสนับสนุนตามมาตรา 35 จำนวน 2 สิทธิ (สำหรับนายสรายุทธ 1 สิทธิ และน้องผู้พิการ 1 สิทธิ) ในรูปแบบการจ้างเหมาบริการ หรือสนับสนุนการประกอบอาชีพ",
        "เพื่อจัดตั้งศูนย์ซ่อมวีลแชร์และฟื้นฟูแบตเตอรี่รถไฟฟ้าในชุมชนนครนายก โดยให้บริการตรวจเช็กและซ่อมบำรุงฟรีแก่คนพิการและผู้สูงอายุในพื้นที่",
        "เพื่อนำทักษะด้านงานตัดเย็บด้วยจักรอุตสาหกรรมมาผลิตและซ่อมแซมเบาะพนักพิงวีลแชร์แบบลดแรงกดทับ โดยทำงานจากที่บ้าน (Work from Home)"
    )

    val scopeSections = listOf(
        ScopeSection(
            id = "scope1",
            title = "ส่วนที่ 1: งานวิศวกรรมยานยนต์ไฟฟ้า แบตเตอรี่ และ IoT",
            leader = "นายสรายุทธ มูลผล",
            role = "หัวหน้าช่างนวัตกรรมวิศวกรรม (สิทธิที่ 1)",
            iconName = "electric_bolt",
            items = listOf(
                ScopeItem(
                    title = "EV Conversion & Battery Rebuild",
                    detail = "ดัดแปลงและซ่อมบำรุงระบบไฟฟ้ายานยนต์ (ผ่านประสบการณ์ระดับสูงจากบิ๊กไบค์ไฟฟ้า Hayabusa), ซ่อมระบบอินเวอร์เตอร์ รวมถึงการประยุกต์ใช้ Grid-Tie Inverter เช่น Soyosource 1200W",
                    highlight = "ความเชี่ยวชาญระดับแปลง Hayabusa EV"
                ),
                ScopeItem(
                    title = "ประกอบและบาลานซ์เซลล์ลิเธียม (LiFePO4 & Li-ion)",
                    detail = "ฟื้นฟูและประกอบแพ็กแบตเตอรี่คุณภาพสูงสำหรับรถวีลแชร์ไฟฟ้า รถสามล้อคนพิการ และระบบกักเก็บพลังงานโซล่าเซลล์",
                    highlight = "Active Balance + Cell Match สเปกโรงงาน"
                ),
                ScopeItem(
                    title = "IoT & Smart BMS Cloud Monitoring",
                    detail = "เขียนโค้ดและพัฒนาระบบหน้าจอตรวจสอบสถานะแบตเตอรี่ (BMS) แบบออนไลน์ แจ้งเตือนแรงดันเซลล์ อุณหภูมิ และระดับพลังงานผ่านแอป/คลาวด์",
                    highlight = "เทคโนโลยีติดตามสถานะแบตเตอรี่ทางไกล"
                ),
                ScopeItem(
                    title = "ตรวจซ่อมบำรุงกลไกวีลแชร์รอบด้าน",
                    detail = "ตรวจเช็กมอเตอร์ขับเคลื่อน, ชุดเกียร์, เบรกไฟฟ้า, จอยสติ๊กควบคุม, โครงสร้างเหล็กและอะลูมิเนียม พร้อมเปลี่ยนยางและลูกปืน",
                    highlight = "ซ่อมบำรุงฟรีแก่คนพิการในชุมชน"
                )
            )
        ),
        ScopeSection(
            id = "scope2",
            title = "ส่วนที่ 2: งานตัดเย็บและประกอบเบาะรองนั่งป้องกันแผลกดทับ",
            leader = "น้องผู้พิการ และมารดา (ผู้ดูแล)",
            role = "ช่างหัตถกรรมจักรอุตสาหกรรม WFH (สิทธิที่ 2)",
            iconName = "content_cut",
            items = listOf(
                ScopeItem(
                    title = "ตัดเย็บเบาะพนักพิงวีลแชร์เฉพาะบุคคล",
                    detail = "ใช้จักรอุตสาหกรรมตัดเย็บเบาะรองนั่งและพนักพิงจากหนังเทียมเกรดทางการแพทย์ (Medical PVC) และผ้าตาข่ายระบายอากาศ",
                    highlight = "ตรงตามสรีระผู้ใช้งานแต่ละคน"
                ),
                ScopeItem(
                    title = "ชั้นฟองน้ำอัดแน่นกระจายแรงกด (Anti-Bedsore)",
                    detail = "ประกอบชั้นฟองน้ำวิทยาศาสตร์และฟองน้ำอัดแน่นความหนาแน่นสูง ช่วยกระจายน้ำหนัก ลดจุดกดทับ ป้องกันการเกิดแผลกดทับที่เป็นอันตราย",
                    highlight = "ป้องกันแผลกดทับ ยกระดับสุขอนามัย"
                ),
                ScopeItem(
                    title = "รูปแบบการทำงานจากที่บ้าน (Work from Home)",
                    detail = "เปิดโอกาสให้น้องผู้พิการและแม่ผู้ดูแลได้สร้างผลงานและมีรายได้ที่มั่นคงจากที่พักอาศัย โดยใช้อุปกรณ์จักรอุตสาหกรรมที่ได้รับการสนับสนุน",
                    highlight = "การจ้างงานที่เคารพศักดิ์ศรีความเป็นมนุษย์"
                )
            )
        )
    )

    val csrBenefits = listOf(
        CsrImpactBenefit(
            number = 1,
            title = "ปฏิบัติตามกฎหมายครบถ้วน 100%",
            description = "เป็นไปตาม พ.ร.บ. ส่งเสริมและพัฒนาคุณภาพชีวิตคนพิการ พ.ศ. 2550 มาตรา 35 ถูกต้องตามระเบียบกรมส่งเสริมและพัฒนาคุณภาพชีวิตคนพิการ (พม.)",
            tangibleOutcome = "ลดภาระค่าปรับ/เงินส่งเข้ากองทุนฯ ได้สิทธิประโยชน์ทางภาษียกเว้นภาษีเงินได้นิติบุคคลตามเกณฑ์ที่กฎหมายกำหนด",
            sdgTarget = "SDG 8: Decent Work & Economic Growth"
        ),
        CsrImpactBenefit(
            number = 2,
            title = "ผลงานช่วยเหลือสังคมที่จับต้องได้จริง (Tangible CSR)",
            description = "เปลี่ยนเงินงบประมาณเป็นการ 'ซ่อมวีลแชร์และรถไฟฟ้าฟรี' ให้คนพิการและผู้สูงอายุในจังหวัดนครนายก มีรายงานภาพถ่ายสรุปผลงานจริง",
            tangibleOutcome = "ติดตราสัญลักษณ์และชื่อบริษัทร่วมกับป้ายศูนย์ช่าง เสื้อปฏิบัติงาน และใบรับรองงานซ่อมเพื่อการประชาสัมพันธ์เชิงบวก",
            sdgTarget = "SDG 10: Reduced Inequalities"
        ),
        CsrImpactBenefit(
            number = 3,
            title = "ส่งเสริมครอบครัวคนพิการพึ่งพาตนเองอย่างยั่งยืน",
            description = "สนับสนุนการสร้างรายได้ที่มั่นคงให้กับทั้งนายสรายุทธและน้องผู้พิการ พร้อมแม่ผู้ดูแล เกิดเป็นโมเดลต้นแบบช่างนวัตกรรมชุมชน",
            tangibleOutcome = "สร้างคุณค่าทางสังคม (Social ROI) สูงกว่าการส่งเงินเข้ากองทุนฯ ที่ไม่เห็นผลลัพธ์โดยตรงในพื้นที่",
            sdgTarget = "SDG 11: Sustainable Communities"
        )
    )

    val budgetItems = listOf(
        BudgetItem(
            id = "b1",
            category = "เครื่องมือช่างและอุปกรณ์อิเล็กทรอนิกส์",
            title = "ชุดเครื่องมือซ่อมและทดสอบแบตเตอรี่ EV/BMS",
            description = "ตู้เชื่อม TIG, เครื่องสปอตแบตเตอรี่ลิเธียม (Battery Spot Welder), เครื่องบาลานซ์เซลล์แรงดันสูง (Active Balancer), เครื่องมือทดสอบ BMS",
            amountThb = 16500L,
            iconName = "hardware"
        ),
        BudgetItem(
            id = "b2",
            category = "หมวดจักรเย็บอุตสาหกรรม",
            title = "จักรอุตสาหกรรมและอุปกรณ์ตัดเย็บเบาะ",
            description = "จักรเย็บผ้าอุตสาหกรรมสำหรับงานหนังและฟองน้ำหนา สำหรับงานเย็บเบาะพนักพิงวีลแชร์ลดแรงกดทับ",
            amountThb = 15000L,
            iconName = "content_cut"
        ),
        BudgetItem(
            id = "b3",
            category = "หมวดอะไหล่และวัสดุสำรองซ่อมฟรี",
            title = "ชุดอะไหล่หมุนเวียนบริการชุมชนฟรี",
            description = "ยางนอก ยางใน วีลแชร์, เซลล์แบตเตอรี่ลิเธียมสำรอง, ผ้าหนัง PVC ทางการแพทย์, ลูกปืนล้อ และสายไฟฉนวนกันความร้อน",
            amountThb = 28500L,
            iconName = "inventory_2"
        )
    )

    val totalEquipmentBudgetThb: Long = budgetItems.sumOf { it.amountThb } // 60,000 บาท

    val presentationSlides = listOf(
        PresentationSlide(
            id = 1,
            tag = "หน้าปก & สรุปสาระสำคัญ",
            title = "ศูนย์ซ่อมแซมและฟื้นฟูอุปกรณ์คนพิการชุมชนนวัตกรรม",
            subtitle = "ข้อเสนอขอรับสิทธิสัมปทานและจ้างเหมาบริการตามมาตรา 35 (2 สิทธิ) | จังหวัดนครนายก",
            keyPoints = listOf(
                "ผู้เสนอโครงการ: นายสรายุทธ มูลผล และครอบครัว เครือข่ายช่างชุมชนจิตอาสา จ.นครนายก",
                "เป้าหมาย: ขอรับสนับสนุน ม.35 จำนวน 2 สิทธิ เพื่อจัดตั้งศูนย์ซ่อมวีลแชร์และแบตเตอรี่ฟรีแก่ชุมชน",
                "ผสาน 2 ขีดความสามารถ: นวัตกรรมวิศวกรรมยานยนต์ไฟฟ้า + งานตัดเย็บเบาะพนักพิงลดแผลกดทับ"
            ),
            speakerNotes = "กราบเรียนคณะกรรมการและผู้บริหาร วันนี้ครอบครัวกระผมขอเสนอโครงการศูนย์ช่างนวัตกรรมชุมชน ที่ไม่เพียงตอบโจทย์กฎหมาย ม.35 แต่ยังสร้างผลงาน CSR ที่จับต้องได้จริง โดยนำทักษะระดับสูงมาซ่อมวีลแชร์ฟรีให้คนพิการในจังหวัดนครนายกครับ",
            calloutText = "2 สิทธิ ม.35 = 1 วิศวกรไฟฟ้าชุมชน + 1 ช่างตัดเย็บ WFH ผู้พิการและมารดา"
        ),
        PresentationSlide(
            id = 2,
            tag = "หลักการ เหตุผล & กฎหมาย ม.35",
            title = "ความจำเป็นในพื้นที่ & โอกาสตามมาตรา 35",
            subtitle = "เปลี่ยนภาระเงินส่งกองทุน ให้กลายเป็นผลลัพธ์ CSR ที่คนนครนายกสัมผัสได้",
            keyPoints = listOf(
                "ปัญหาในพื้นที่: จ.นครนายกมีผู้พิการและผู้สูงอายุใช้วีลแชร์และรถไฟฟ้าจำนวนมาก แต่ขาดช่างในพื้นที่ ค่าส่งซ่อมเข้า กทม. สูงมาก",
                "กฎหมายเปิดทาง: ม.35 ให้สิทธิสถานประกอบการจ้างเหมาบริการหรือสนับสนุนการประกอบอาชีพแทนการส่งเงินเข้ากองทุน",
                "คุ้มค่าสูงสุด: เงินสนับสนุนเปลี่ยนเป็นบริการซ่อมฟรี บริษัทได้ผลงาน CSR ที่เห็นตัวตนชัดเจน พร้อมภาพรายงานส่ง พม."
            ),
            speakerNotes = "ในนครนายก รถเข็นหรือแบตเตอรี่เสียครั้งหนึ่ง ผู้พิการต้องจอดทิ้งไว้นานเพราะไม่มีช่างท้องถิ่น หรือต้องเสียค่าส่งซ่อมแพงมาก โครงการนี้จึงเข้ามาอุดช่องว่างด้วยความเชี่ยวชาญจริงของครอบครัวเราครับ",
            calloutText = "ซ่อมฟรีให้ผู้พิการในชุมชน = ภาพลักษณ์องค์กรที่ดีเยี่ยม & เอกสารส่ง พม. ถูกต้อง 100%"
        ),
        PresentationSlide(
            id = 3,
            tag = "ความเชี่ยวชาญที่ 1 (วิศวกรรม EV & IoT)",
            title = "ทักษะวิศวกรรมขั้นสูง: EV, แบตเตอรี่ลิเธียม & IoT",
            subtitle = "นำทักษะระดับดัดแปลงบิ๊กไบค์ไฟฟ้า Hayabusa มารับใช้ผู้พิการ",
            keyPoints = listOf(
                "การันตีผลงาน: ผ่านการดัดแปลงมอเตอร์ไซค์ไฟฟ้า Hayabusa EV และซ่อมบำรุงอินเวอร์เตอร์ (Soyosource 1200W)",
                "คลินิกแบตเตอรี่: เชี่ยวชาญการสปอตและฟื้นฟูเซลล์ลิเธียม Active Balancer เพิ่มอายุการใช้งานแบตเตอรี่วีลแชร์ 2-3 เท่า",
                "ระบบ IoT ล้ำสมัย: พัฒนาระบบมอนิเตอร์ BMS ออนไลน์ ดูแรงดันไฟและแจ้งเตือนผ่านมือถือได้ทันที"
            ),
            speakerNotes = "จุดแข็งที่สุดของผมคือความเชี่ยวชาญด้านอิเล็กทรอนิกส์กำลังและแบตเตอรี่ลิเธียม ซึ่งได้รับการรับรองโดย ดร.อภิรักษ์ จันทร ผมสามารถซ่อมและฟื้นฟูแบตเตอรี่วีลแชร์ที่เสื่อมสภาพให้กลับมาใช้งานได้อย่างปลอดภัยครับ",
            calloutText = "มีโมเดล Interactive BMS Simulator ในแอป ให้ทดลองตรวจสอบสถานะเซลล์ได้แบบสดๆ"
        ),
        PresentationSlide(
            id = 4,
            tag = "นวัตกรรมอินเวอร์เตอร์ (Soyosource GIT 1200W)",
            title = "ระบบพลังงานหมุนเวียน & อินเวอร์เตอร์ Soyosource GIT 1200W",
            subtitle = "ระบบ Grid-Tie พร้อมระบบกันย้อน (Zero Export) ผสานแบตเตอรี่วีลแชร์รีไซเคิล",
            keyPoints = listOf(
                "Soyosource GIT-1200W (48V to 220V AC Pure Sine Wave): ประสิทธิภาพสูง 93% พร้อม CT Clamp กันย้อนมิเตอร์ กฟภ. แบบเรียลไทม์",
                "ไมโครกริดพลังงานสะอาดสำหรับศูนย์ช่าง: จ่ายไฟขับเคลื่อนตู้เชื่อมสปอตแบตเตอรี่และแท่นชาร์จวีลแชร์โดยลดต้นทุนค่าไฟฟ้า 100%",
                "สำรองไฟฉุกเฉินช่วยชีวิต: ดึงพลังงานจากแบตเตอรี่ลิเธียมแปลงเป็นไฟ 220V จ่ายเครื่องผลิตออกซิเจนและที่นอนลมให้ผู้ป่วยติดเตียงยามไฟดับ",
                "ทักษะซ่อมบำรุงบอร์ดระดับชิ้นส่วน: สามารถตรวจเช็กและเปลี่ยนมอสเฟต IRFP4668, คาปาซิเตอร์, และจูนเซนเซอร์ CT ได้เองในพื้นที่"
            ),
            speakerNotes = "อินเวอร์เตอร์ Soyosource GIT 1200W คือหัวใจสำคัญของระบบพลังงานในศูนย์ช่างของเราครับ เราสามารถเปลี่ยนพลังงานจากแสงแดดหรือแบตเตอรี่ลิเธียม 48V จ่ายกลับมาเป็นไฟฟ้ากระแสสลับ 220V ขับเคลื่อนเครื่องมือช่างและสำรองไฟให้คนพิการติดเตียงในยามฉุกเฉินได้ครับ",
            calloutText = "ระบบกันย้อน (Zero Export) อัจฉริยะ ตอบสนองโหลดใน 200ms ไม่ไหลย้อนมิเตอร์ กฟภ.",
            imagePlaceholder = com.example.model.SlideImagePlaceholder(
                preset = com.example.model.PlaceholderPreset.SOYOSOURCE_GIT_1200W,
                title = "อินเวอร์เตอร์ Soyosource GIT-1200W & CT Sensor",
                caption = "การทดสอบระบบแปลงพลังงาน 48V สู่ 220V AC Pure Sine Wave พร้อมระบบกันย้อน"
            )
        ),
        PresentationSlide(
            id = 5,
            tag = "ความเชี่ยวชาญที่ 2 (งานตัดเย็บ WFH)",
            title = "งานตัดเย็บเบาะพนักพิง ป้องกันแผลกดทับ",
            subtitle = "สร้างงาน สร้างรายได้จากที่บ้าน (WFH) สู่ผลิตภัณฑ์เพื่อสุขภาพ",
            keyPoints = listOf(
                "รับผิดชอบโดย: น้องผู้พิการและมารดา ใช้จักรอุตสาหกรรมตัดเย็บงานประณีตจากที่พัก",
                "แก้วิกฤตแผลกดทับ: ผลิตเบาะรองนั่งด้วยฟองน้ำอัดแน่นพิเศษและหนัง Medical PVC ช่วยกระจายแรงกดทับอย่างมีประสิทธิภาพ",
                "เปลี่ยนให้ฟรี: สับเปลี่ยนเบาะและพนักพิงใหม่ให้วีลแชร์ที่ขาด ชำรุด หรือสะสมเชื้อโรคแก่คนพิการในชุมชน"
            ),
            speakerNotes = "แผลกดทับคืออันตรายถึงชีวิตของคนนั่งวีลแชร์ แม่และน้องผู้พิการของผมมีความชำนาญในการใช้จักรอุตสาหกรรมเย็บเบาะเกรดแพทย์ นี่คือการรวมพลังของครอบครัวคนพิการที่ช่วยเหลือคนพิการด้วยกันครับ",
            calloutText = "ลดอัตราการเกิดแผลกดทับ ยกระดับสุขอนามัยของผู้ป่วยติดเตียงและผู้ใช้วีลแชร์"
        ),
        PresentationSlide(
            id = 6,
            tag = "ผู้รับรองความสามารถ (Endorsements)",
            title = "บุคคลอ้างอิงและผู้ทรงคุณวุฒิรับรอง",
            subtitle = "ความน่าเชื่อถือระดับประเทศจากสถาบันการศึกษาและผู้เชี่ยวชาญ",
            keyPoints = listOf(
                "1. อ.สุพร ทำมงคลสวัสดิ์ (อาจารย์ใหญ่ รร.อาชีวพระมหาไถ่ พัทยา) รับรองทักษะและประวัติวิชาชีพ",
                "2. ดร. อภิรักษ์ จันทร (ผู้เชี่ยวชาญ EV) รับรองความสามารถด้าน Hayabusa EV, แบตเตอรี่, อินเวอร์เตอร์ และ IoT BMS",
                "3. อ.ปรีดา นันตคุณ รับรองหลักสูตรการบริหารจัดการและประเมินผลโครงการ",
                "4. คุณอุดมโชค ชูรัตน์ รับรองทักษะการปฏิบัติงานและการเข้าถึงเครือข่ายคนพิการภาคสนาม"
            ),
            speakerNotes = "ท่านผู้บริหารสามารถมั่นใจในศักยภาพและความซื่อสัตย์สุจริตได้ 100% เพราะโครงการนี้มีผู้ทรงคุณวุฒิระดับประเทศทั้ง 4 ท่าน ให้การรับรองเป็นลายลักษณ์อักษรครับ",
            calloutText = "ตรวจสอบและติดต่อบุคคลอ้างอิงได้ทุกท่านเพื่อยืนยันความพร้อมของโครงการ"
        ),
        PresentationSlide(
            id = 7,
            tag = "ผลลัพธ์ CSR & ประโยชน์ต่อองค์กร",
            title = "ทำไมสถานประกอบการควรเลือกร่วมงานกับเรา?",
            subtitle = "CSR ที่เห็นผลจริง + สิทธิประโยชน์ทางภาษี + ภาพลักษณ์แบรนด์ที่น่าประทับใจ",
            keyPoints = listOf(
                "ประโยชน์ที่ 1: ถูกต้องตามกฎหมาย 100% มีสัญญาจ้างเหมาบริการและเอกสารครบถ้วนสำหรับยื่นกรม พม.",
                "ประโยชน์ที่ 2: Tangible CSR ติดโลโก้บริษัทที่ศูนย์ช่าง เสื้อช่าง และใบเสร็จส่งมอบงานซ่อมฟรี ถ่ายรูปประชาสัมพันธ์ได้ตลอดทั้งปี",
                "ประโยชน์ที่ 3: ยกเว้นภาษีเงินได้นิติบุคคลจากค่าใช้จ่ายตามเกณฑ์กฎหมาย ช่วยประหยัดต้นทุนของบริษัท"
            ),
            speakerNotes = "เมื่อเทียบกับการส่งเงินเข้ากองทุนฯ ไปเฉยๆ การให้สิทธิโครงการนี้ทำให้เงินของท่านกลายเป็นรอยยิ้มของคนพิการในชุมชน และแบรนด์ของท่านจะถูกพูดถึงในฐานะผู้สนับสนุนนวัตกรรมคนพิการครับ",
            calloutText = "Social ROI สูงกว่าการจ่ายเงินเข้ากองทุนทั่วไปอย่างเทียบไม่ได้"
        ),
        PresentationSlide(
            id = 8,
            tag = "งบประมาณเริ่มต้น (Budget Plan)",
            title = "งบประมาณจัดหาอุปกรณ์เริ่มต้นศูนย์ช่าง",
            subtitle = "งบลงทุนอุปกรณ์เพียง 60,000 บาท เพื่อการดำเนินงานตลอดทั้งปี",
            keyPoints = listOf(
                "1. ชุดเครื่องมือช่างและอิเล็กทรอนิกส์ (TIG, Spot Welder, Active Balancer, BMS Tester): 16,500 บาท",
                "2. จักรเย็บอุตสาหกรรมและอุปกรณ์ตัดเย็บเบาะพนักพิง: 15,000 บาท",
                "3. อะไหล่สำรองซ่อมฟรีชุมชน (ยางนอก/ใน, เซลล์ลิเธียม, หนัง PVC, ลูกปืน): 28,500 บาท",
                "รวมงบประมาณเริ่มต้น: 60,000 บาท (ไม่รวมค่าจ้างเหมาบริการรายปีตามเกณฑ์ขั้นต่ำ ม.35)"
            ),
            speakerNotes = "งบตั้งต้น 60,000 บาทนี้คุ้มค่ามาก เพราะเป็นเครื่องมือถาวรที่จะใช้ดูแลวีลแชร์ได้นับร้อยคันตลอดระยะเวลาของสัญญาครับ",
            calloutText = "โปร่งใส ชัดเจน มีรายการเครื่องมือและใบเสนอราคาตรวจสอบได้ทุกรายการ"
        ),
        PresentationSlide(
            id = 9,
            tag = "บทสรุป & ช่องทางติดต่อ",
            title = "ก้าวแรกสู่การเปลี่ยนชีวิตคนพิการในชุมชน",
            subtitle = "นายสรายุทธ มูลผล และครอบครัว พร้อมเริ่มงานและส่งมอบความภาคภูมิใจให้ท่านทันที",
            keyPoints = listOf(
                "พร้อมลงนามสัญญาจ้างเหมาบริการตามมาตรา 35 ทันที",
                "ที่ตั้งศูนย์ช่าง: จังหวัดนครนายก (พร้อมประสานงานและตรวจเยี่ยมพื้นที่)",
                "อีเมล: sarayutm2016@gmail.com | โทรศัพท์: 081-xxx-xxxx",
                "ขอขอบพระคุณคณะกรรมการและผู้บริหารทุกท่านเป็นอย่างสูง"
            ),
            speakerNotes = "พวกเราขอโอกาสในการพิสูจน์ศักยภาพ และพร้อมที่จะเป็นตัวแทนของบริษัทท่านในการส่งต่อความช่วยเหลือแก่สังคมครับ ขอขอบพระคุณครับ",
            calloutText = "กดปุ่มติดต่อหรือแชร์สรุปโครงการด้านล่างได้ทันที"
        )
    )

    val sampleCommunityRepairs = listOf(
        ServiceRepairRecord(
            id = "REP-001",
            beneficiaryName = "ลุงสมศักดิ์ สุขใจ (อายุ 68 ปี)",
            disabilityType = "ผู้พิการทางการเคลื่อนไหว (อัมพาตครึ่งท่อน)",
            location = "อ.เมือง จ.นครนายก",
            deviceType = "รถวีลแชร์ไฟฟ้า 24V",
            problemDescription = "แบตเตอรี่ตะกั่วกรดเดิมเสื่อม วิ่งได้เพียง 500 เมตรแล้วดับ จอยสติ๊กมีอาการกระตุก",
            solutionApplied = "เปลี่ยนเป็นแพ็กแบตเตอรี่ลิเธียม LiFePO4 24V 20Ah พร้อม Active Balancer & Smart BMS ตรวจซ่อมสายสัญญาณจอยสติ๊ก",
            savedCostForUserThb = 6500L,
            date = "ตรวจรับเข้าศูนย์",
            status = "พร้อมทดสอบส่งมอบฟรี"
        ),
        ServiceRepairRecord(
            id = "REP-002",
            beneficiaryName = "ป้ามาลี วงษ์ทอง (อายุ 72 ปี)",
            disabilityType = "ผู้สูงอายุและผู้พิการทางการเคลื่อนไหว",
            location = "อ.บ้านนา จ.นครนายก",
            deviceType = "รถวีลแชร์แบบเข็นมาตรฐาน",
            problemDescription = "เบาะหนังและพนักพิงเดิมเปื่อยขาด ฟองน้ำยุบตัว ทำให้มีอาการปวดหลังและเริ่มมีรอยแดงกดทับที่สะโพก",
            solutionApplied = "ตัดเย็บเบาะรองนั่งป้องกันแผลกดทับ 2 ชั้น ด้วยหนัง Medical PVC สีน้ำเงินเข้มและฟองน้ำอัดแน่นความยืดหยุ่นสูง",
            savedCostForUserThb = 2200L,
            date = "กำลังตัดเย็บ WFH",
            status = "งานตัดเย็บเสร็จ 90%"
        ),
        ServiceRepairRecord(
            id = "REP-003",
            beneficiaryName = "พี่ประเสริฐ ชัยมงคล (อายุ 45 ปี)",
            disabilityType = "ผู้พิการขาขาดทั้งสองข้าง",
            location = "อ.ปากพลี จ.นครนายก",
            deviceType = "รถสามล้อโยกคนพิการ",
            problemDescription = "โซ่ขับเคลื่อนหย่อน ลูกปืนล้อหน้าแตก ยางนอกสึกจนเห็นผ้าใบ เสี่ยงต่อการเกิดอุบัติเหตุบนท้องถนน",
            solutionApplied = "เปลี่ยนยางนอก ยางใน และตลับลูกปืนความเร็วสูงใหม่ทั้งสองล้อ ล้างทำความสะอาดและตั้งโซ่ขับเคลื่อนใหม่",
            savedCostForUserThb = 1800L,
            date = "ให้บริการนอกสถานที่",
            status = "ส่งมอบเรียบร้อยแล้ว"
        )
    )

    val defaultAttachments = listOf(
        com.example.model.AttachmentFile(
            id = "DOC-001",
            fileName = "เล่มข้อเสนอโครงการ_ม35_ศูนย์ซ่อมวีลแชร์นวัตกรรม_นครนายก.pdf",
            fileType = com.example.model.AttachmentType.PDF,
            fileSizeBytes = 3984588L, // ~3.8 MB
            uploadedDate = "14 ต.ค. 2569 • 10:30",
            category = com.example.model.AttachmentCategory.PROPOSAL_DOC,
            description = "เอกสารเสนอโครงการฉบับสมบูรณ์ 18 หน้า พร้อมแผนดำเนินงาน 12 เดือน และกรอบงบประมาณ",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-002",
            fileName = "หนังสือรับรองความเชี่ยวชาญ_ดร_อภิรักษ์_จันทร.pdf",
            fileType = com.example.model.AttachmentType.PDF,
            fileSizeBytes = 1258291L, // ~1.2 MB
            uploadedDate = "14 ต.ค. 2569 • 11:15",
            category = com.example.model.AttachmentCategory.CERTIFICATE,
            description = "หนังสือรับรองความเชี่ยวชาญ Hayabusa EV, Soyosource Inverter และการเขียนโค้ด IoT BMS",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-003",
            fileName = "หนังสือรับรองประวัติและทักษะ_อ_สุพร_พระมหาไถ่.pdf",
            fileType = com.example.model.AttachmentType.PDF,
            fileSizeBytes = 972800L, // ~950 KB
            uploadedDate = "14 ต.ค. 2569 • 11:20",
            category = com.example.model.AttachmentCategory.CERTIFICATE,
            description = "หนังสือรับรองประวัติการศึกษา ทักษะวิชาชีพช่างคนพิการ และความมุ่งมั่นทำงานเพื่อสังคม",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-004",
            fileName = "ใบเสนอราคาชุดเครื่องมือช่างและจักรเย็บ_60000บาท.docx",
            fileType = com.example.model.AttachmentType.DOCX,
            fileSizeBytes = 491520L, // ~480 KB
            uploadedDate = "14 ต.ค. 2569 • 13:00",
            category = com.example.model.AttachmentCategory.QUOTATION,
            description = "ใบเสนอราคา ตู้เชื่อม TIG, เครื่องสปอตแบตเตอรี่, Active Balancer, และจักรอุตสาหกรรม WFH",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-005",
            fileName = "ภาพถ่ายผลงาน_รถจักรยานยนต์ไฟฟ้า_Hayabusa_EV.jpg",
            fileType = com.example.model.AttachmentType.IMAGE,
            fileSizeBytes = 4404019L, // ~4.2 MB
            uploadedDate = "15 ต.ค. 2569 • 09:40",
            category = com.example.model.AttachmentCategory.TECH_PHOTO,
            description = "ภาพถ่ายความละเอียดสูง ตัวรถ Hayabusa EV, แบตเตอรี่ลิเธียมแรงสูง และชุดมอเตอร์ขับเคลื่อน",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-006",
            fileName = "ภาพถ่ายโครงสร้างเบาะพนักพิง_AntiBedsore_3Layers.jpg",
            fileType = com.example.model.AttachmentType.IMAGE,
            fileSizeBytes = 2726297L, // ~2.6 MB
            uploadedDate = "15 ต.ค. 2569 • 10:05",
            category = com.example.model.AttachmentCategory.TECH_PHOTO,
            description = "ภาพถ่ายชิ้นงานจริง เบาะหนัง Medical PVC ฟองน้ำอัดแน่น 3 ชั้น ลดแรงกดทับของสะโพก",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-007",
            fileName = "วิดีโอสาธิตระบบ_Smart_BMS_IoT_Online_Monitor.mp4",
            fileType = com.example.model.AttachmentType.VIDEO,
            fileSizeBytes = 19398656L, // ~18.5 MB
            uploadedDate = "15 ต.ค. 2569 • 14:22",
            category = com.example.model.AttachmentCategory.DEMO_VIDEO,
            description = "วิดีโอคลิปสาธิตการทำงานจริงของระบบ IoT BMS เชื่อมต่อออนไลน์ แสดงผลแรงดันเซลล์และ Soyosource 1200W",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-008",
            fileName = "แบบฟอร์มคำขอใช้สิทธิตามมาตรา35_พม_สนย1.pdf",
            fileType = com.example.model.AttachmentType.PDF,
            fileSizeBytes = 839680L, // ~820 KB
            uploadedDate = "16 ต.ค. 2569 • 08:30",
            category = com.example.model.AttachmentCategory.GOV_FORM,
            description = "แบบฟอร์มทางการสำหรับการยื่นขอใช้สิทธิต่อสำนักงานจัดหางานจังหวัดนครนายก (กรม พม.)",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-009",
            fileName = "คู่มือสเปกทางเทคนิค_Soyosource_GIT_1200W_GridTie_Limiter.pdf",
            fileType = com.example.model.AttachmentType.PDF,
            fileSizeBytes = 2516582L, // ~2.4 MB
            uploadedDate = "16 ต.ค. 2569 • 11:00",
            category = com.example.model.AttachmentCategory.PROPOSAL_DOC,
            description = "คู่มือวงจรทางเทคนิค Soyosource GIT-1200W (48V-90V DC to 220V AC Pure Sine Wave) พร้อมระบบกันย้อน CT Sensor",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-010",
            fileName = "ผังวงจรและการเปลี่ยนมอสเฟต_Soyosource_IRFP4668.pdf",
            fileType = com.example.model.AttachmentType.PDF,
            fileSizeBytes = 1887436L, // ~1.8 MB
            uploadedDate = "16 ต.ค. 2569 • 11:30",
            category = com.example.model.AttachmentCategory.PROPOSAL_DOC,
            description = "คู่มือและขั้นตอนการซ่อมบำรุงบอร์ดอินเวอร์เตอร์ การวัดสัญญาณขับเกต 50kHz และการเปลี่ยนชุด MOSFET กำลังสูง",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-011",
            fileName = "ภาพถ่ายชุดทดสอบบอร์ด_Soyosource_GIT1200W_คู่แบตลิเธียม.jpg",
            fileType = com.example.model.AttachmentType.IMAGE,
            fileSizeBytes = 3250585L, // ~3.1 MB
            uploadedDate = "16 ต.ค. 2569 • 12:15",
            category = com.example.model.AttachmentCategory.TECH_PHOTO,
            description = "ภาพถ่ายชุดทดสอบอินเวอร์เตอร์ Soyosource GIT-1200W ต่อร่วมกับแบตเตอรี่ LiFePO4 48V และโหลดตู้เชื่อมสปอต",
            isPreloaded = true
        ),
        com.example.model.AttachmentFile(
            id = "DOC-012",
            fileName = "ESP32_Robot_Face_OLED_SSD1306_Cpp_Source.zip",
            fileType = com.example.model.AttachmentType.OTHER,
            fileSizeBytes = 458752L, // ~448 KB
            uploadedDate = "17 ต.ค. 2569 • 15:45",
            category = com.example.model.AttachmentCategory.PROPOSAL_DOC,
            description = "ซอร์สโค้ด C++ (oled_display.h, oled_display.cpp, robot_face.ino) แอนิเมชันใบหน้าหุ่นยนต์ ESP32 I2C GPIO4/GPIO5",
            isPreloaded = true
        )
    )

    val soyosourceComponents = listOf(
        InverterComponentInfo(
            name = "DC-DC Power MOSFET Stage",
            partNumber = "IRFP4668 / HY4008 (100V 200A TO-247 x 8 ตัว)",
            role = "แปลงไฟกระแสตรง 48V-90V เป็นพัลส์ความถี่สูง 50kHz ขับหม้อแปลงเฟอร์ไรต์",
            repairSkill = "ตรวจวัดพิกัด Vds, Rds(on) และเช็กสัญญาณขับเกต Gate Pulse 50kHz ด้วยสโคปก่อนสลับเปลี่ยน",
            status = "ปกติ (Gate Waveform 50kHz สมบูรณ์)"
        ),
        InverterComponentInfo(
            name = "Ferrite High-Frequency Transformers",
            partNumber = "Dual E55 Ferrite Transformer Core",
            role = "ยกระดับแรงดันไฟ DC Bus จาก 48V ขึ้นเป็น High Voltage DC 360V - 380V พร้อมความเป็นฉนวนกัลวานิก",
            repairSkill = "ตรวจสอบฉนวนขดลวด ป้องกันการลัดวงจรระหว่างรอบ (Turn-to-turn short) และทดสอบความร้อน",
            status = "ปกติ (ไร้เสียงหวีดฮัม แปลงไฟเสถียร)"
        ),
        InverterComponentInfo(
            name = "Pure Sine Wave H-Bridge & SPWM Driver",
            partNumber = "EG8010 SPWM Controller + IR2110 Isolated Driver",
            role = "แปลงไฟ DC 380V เป็น AC 220V Pure Sine Wave ความถี่ 50.0Hz ความเพี้ยนฮาร์มอนิกต่ำ THD < 2.5%",
            repairSkill = "วิเคราะห์สัญญาณ SPWM Modulation และตรวจเช็กวงจร Zero-Crossing Detector สอดคล้องกับเฟสไฟ กฟภ.",
            status = "ปกติ (THD 2.1% Pure Sine Wave)"
        ),
        InverterComponentInfo(
            name = "RS485 Limiter Decoder & CT Sensor (กันย้อน)",
            partNumber = "Split-Core Current Transformer (CT Clamp) + RS485 Interface",
            role = "ตรวจวัดกระแสไฟฟ้าที่ไหลผ่านมิเตอร์หลัก กฟภ. และส่งคำสั่งควบคุมวัตต์ให้อินเวอร์เตอร์ไม่จ่ายไฟย้อนมิเตอร์",
            repairSkill = "คาลิเบรตค่า Current Ratio, ตรวจสอบสายชีลด์ป้องกันสัญญาณรบกวน และโปรโตคอล Modbus RTU 9600 bps",
            status = "ปกติ (หน่วงเวลาตอบสนอง < 200ms)"
        ),
        InverterComponentInfo(
            name = "Intelligent Dual Ball-Bearing Fans",
            partNumber = "Brushless DC 12V High-CFM Dual Cooling Fan",
            role = "ระบายความร้อนจากอะลูมิเนียมฮีตซิงก์ ปรับรอบตามอุณหภูมิแบบ PWM (ทำงานเมื่อแกนอุณหภูมิเกิน 45°C)",
            repairSkill = "ทำความสะอาดใบพัด ตรวจสอบท่อระบายอากาศ และทาซิลิโคนเกรดความนำความร้อนสูง (Thermal Grease 6.0 W/mK)",
            status = "ปกติ (ทำงานเงียบ 42.5°C)"
        ),
        InverterComponentInfo(
            name = "Anti-Islanding & Safety Protection Relay",
            partNumber = "High-Voltage Relay 250V 16A + Protection MCU",
            role = "ตัดการจ่ายไฟเข้าสายส่งทันทีภายใน 0.05 วินาที เมื่อไฟฟ้า กฟภ. ดับ ตามมาตรฐานความปลอดภัย IEEE 1547",
            repairSkill = "ทดสอบฟังก์ชัน Anti-Islanding Trip Time และตรวจสอบคอนแทกรีเลย์ว่าไม่มีคราบออกไซด์เกาะ",
            status = "ปกติ (ตัดต่อรวดเร็ว ปลอดภัยต่อช่าง กฟภ.)"
        )
    )
}
