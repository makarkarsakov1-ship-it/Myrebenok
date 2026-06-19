package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    val allChildren: Flow<List<Child>> = appDao.getAllChildren()

    suspend fun getChildById(childId: Long): Child? = appDao.getChildById(childId)

    suspend fun insertChild(child: Child): Long = appDao.insertChild(child)

    suspend fun deleteChild(child: Child) = appDao.deleteChild(child)

    suspend fun updateChild(child: Child) = appDao.updateChild(child)

    fun getGrowthRecords(childId: Long): Flow<List<GrowthRecord>> = appDao.getGrowthRecordsForChild(childId)

    suspend fun insertGrowthRecord(record: GrowthRecord) = appDao.insertGrowthRecord(record)

    suspend fun deleteGrowthRecord(id: Long) = appDao.deleteGrowthRecord(id)

    fun getVaccinations(childId: Long): Flow<List<VaccinationRecord>> = appDao.getVaccinationsForChild(childId)

    suspend fun insertVaccinations(records: List<VaccinationRecord>) = appDao.insertVaccinations(records)

    suspend fun updateVaccination(record: VaccinationRecord) = appDao.updateVaccination(record)

    suspend fun reseedVaccinations(childId: Long, countryCode: String, birthDateMillis: Long) {
        appDao.clearVaccinationsForChild(childId)
        val seedList = getSeedVaccinations(childId, countryCode, birthDateMillis)
        appDao.insertVaccinations(seedList)
    }

    fun getDiaryEntries(childId: Long): Flow<List<DiaryEntry>> = appDao.getDiaryEntriesForChild(childId)

    suspend fun insertDiaryEntry(entry: DiaryEntry) = appDao.insertDiaryEntry(entry)

    suspend fun deleteDiaryEntry(entry: DiaryEntry) = appDao.deleteDiaryEntry(entry)

    fun getMedicalDocuments(childId: Long): Flow<List<MedicalDocument>> = appDao.getMedicalDocumentsForChild(childId)

    suspend fun insertMedicalDocument(doc: MedicalDocument) = appDao.insertMedicalDocument(doc)

    suspend fun deleteMedicalDocument(doc: MedicalDocument) = appDao.deleteMedicalDocument(doc)

    fun getReminders(childId: Long): Flow<List<Reminder>> = appDao.getRemindersForChild(childId)

    val allReminders: Flow<List<Reminder>> = appDao.getAllReminders()

    suspend fun insertReminder(reminder: Reminder) = appDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: Reminder) = appDao.updateReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) = appDao.deleteReminder(reminder)

    private fun getSeedVaccinations(childId: Long, countryCode: String, birthDateMillis: Long): List<VaccinationRecord> {
        val secondsInMonth = 30L * 24 * 60 * 60 * 1000
        val items = mutableListOf<VaccinationRecord>()

        fun addVaccine(ruName: String, enName: String, ageMonths: Int, ruInfo: String, enInfo: String) {
            val plannedDate = birthDateMillis + (ageMonths.toLong() * secondsInMonth)
            items.add(
                VaccinationRecord(
                    childId = childId,
                    vaccineNameRu = ruName,
                    vaccineNameEn = enName,
                    ageIntervalMonths = ageMonths,
                    datePlannedMillis = plannedDate,
                    infoRu = ruInfo,
                    infoEn = enInfo
                )
            )
        }

        if (countryCode == "RU") {
            addVaccine("Гепатит В #1", "Hepatitis B #1", 0, "В первые 24 часа жизни ребенка", "First 24 hours of life")
            addVaccine("Туберкулез (БЦЖ-М/БЦЖ)", "Tuberculosis (BCG)", 0, "На 3-7 день жизни ребенка", "On day 3-7 of life")
            addVaccine("Гепатит В #2", "Hepatitis B #2", 1, "В возрасте 1 месяца", "At age of 1 month")
            addVaccine("Пневмококковая инфекция #1", "Pneumococcal #1", 2, "В возрасте 2 месяцев", "At age of 2 months")
            addVaccine("Гепатит В #3 (группа риска)", "Hepatitis B #3 (risk groups)", 2, "В возрасте 2 месяцев", "At age of 2 months")
            addVaccine("Коклюш, дифтерия, столбняк (АКДС #1)", "DTaP #1", 3, "В возрасте 3 месяцев", "At age of 3 months")
            addVaccine("Полиомиелит #1", "Polio #1", 3, "В возрасте 3 месяцев", "At age of 3 months")
            addVaccine("Гемофильная инфекция #1", "Hib #1", 3, "В возрасте 3 месяцев", "At age of 3 months")
            addVaccine("Коклюш, дифтерия, столбняк (АКДС #2)", "DTaP #2", 4, "В возрасте 4.5 месяцев (4-5 мес)", "At age of 4.5 months")
            addVaccine("Полиомиелит #2", "Polio #2", 4, "В возрасте 4.5 месяцев", "At age of 4.5 months")
            addVaccine("Пневмококковая инфекция #2", "Pneumococcal #2", 4, "В возрасте 4.5 месяцев", "At age of 4.5 months")
            addVaccine("Коклюш, дифтерия, столбняк (АКДС #3)", "DTaP #3", 6, "В возрасте 6 месяцев", "At age of 6 months")
            addVaccine("Полиомиелит #3", "Polio #3", 6, "В возрасте 6 месяцев", "At age of 6 months")
            addVaccine("Гепатит В #3 (#4 для групп риска)", "Hepatitis B #3/4", 6, "В возрасте 6 месяцев", "At age of 6 months")
            addVaccine("Корь, краснуха, паротит (КПК #1)", "MMR #1", 12, "В возрасте 12 месяцев", "At age of 12 months")
            addVaccine("Пневмококковая инфекция (Ревакцинация)", "Pneumococcal booster", 15, "В возрасте 15 месяцев", "At age of 15 months")
            addVaccine("АКДС (Ревакцинация #1)", "DTaP booster", 18, "В возрасте 18 месяцев", "At age of 18 months")
            addVaccine("Полиомиелит (Ревакцинация #1)", "Polio booster #1", 18, "В возрасте 18 месяцев", "At age of 18 months")
            addVaccine("Полиомиелит (Ревакцинация #2)", "Polio booster #2", 20, "В возрасте 20 месяцев", "At age of 20 months")
        } else {
            // Default WHO/US Calendar
            addVaccine("Hepatitis B #1", "Hepatitis B #1", 0, "At birth", "At birth within 24 hours")
            addVaccine("Hepatitis B #2", "Hepatitis B #2", 1, "At 1-2 months", "At 1-2 months")
            addVaccine("Rotavirus #1", "Rotavirus #1", 2, "At age 2 months", "At age 2 months")
            addVaccine("DTaP #1", "DTaP #1", 2, "At age 2 months (Diphtheria, Tetanus, Pertussis)", "At age 2 months")
            addVaccine("Hib #1", "Hib #1", 2, "At age 2 months", "At age 2 months")
            addVaccine("Pneumococcal (PCV13) #1", "Pneumococcal #1", 2, "At age 2 months", "At age 2 months")
            addVaccine("Polio (IPV) #1", "Polio (IPV) #1", 2, "At age 2 months", "At age 2 months")
            addVaccine("DTaP #2", "DTaP #2", 4, "At age 4 months", "At age 4 months")
            addVaccine("Hib #2", "Hib #2", 4, "At age 4 months", "At age 4 months")
            addVaccine("Pneumococcal (PCV13) #2", "Pneumococcal #2", 4, "At age 4 months", "At age 4 months")
            addVaccine("Polio (IPV) #2", "Polio (IPV) #2", 4, "At age 4 months", "At age 4 months")
            addVaccine("DTaP #3", "DTaP #3", 6, "At age 6 months", "At age 6 months")
            addVaccine("Polio (IPV) #3", "Polio #3", 6, "At age 6 months", "At age 6 months")
            addVaccine("Hepatitis B #3", "Hepatitis B #3", 6, "At age 6-18 months", "At age 6-18 months")
            addVaccine("MMR #1", "MMR #1", 12, "At age 12-15 months (Measles, Mumps, Rubella)", "At age 12-15 months")
            addVaccine("Varicella #1", "Varicella #1", 12, "At age 12-15 months (Chickenpox)", "At age 12-15 months")
            addVaccine("DTaP Ревакцинация", "DTaP booster", 15, "At age 15-18 months", "At age 15-18 months")
        }
        return items
    }
}
