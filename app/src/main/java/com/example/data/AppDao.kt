package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- CHILDREN ---
    @Query("SELECT * FROM children ORDER BY id ASC")
    fun getAllChildren(): Flow<List<Child>>

    @Query("SELECT * FROM children WHERE id = :childId LIMIT 1")
    suspend fun getChildById(childId: Long): Child?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: Child): Long

    @Delete
    suspend fun deleteChild(child: Child)

    @Update
    suspend fun updateChild(child: Child)

    // --- GROWTH RECORDS ---
    @Query("SELECT * FROM growth_records WHERE childId = :childId ORDER BY dateMillis ASC")
    fun getGrowthRecordsForChild(childId: Long): Flow<List<GrowthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrowthRecord(record: GrowthRecord): Long

    @Query("DELETE FROM growth_records WHERE id = :id")
    suspend fun deleteGrowthRecord(id: Long)

    // --- VACCINATIONS ---
    @Query("SELECT * FROM vaccinations WHERE childId = :childId ORDER BY ageIntervalMonths ASC, id ASC")
    fun getVaccinationsForChild(childId: Long): Flow<List<VaccinationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccination(record: VaccinationRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccinations(records: List<VaccinationRecord>)

    @Update
    suspend fun updateVaccination(record: VaccinationRecord)

    @Query("DELETE FROM vaccinations WHERE childId = :childId")
    suspend fun clearVaccinationsForChild(childId: Long)

    // --- DIARY ENTRIES ---
    @Query("SELECT * FROM diary_entries WHERE childId = :childId ORDER BY dateMillis DESC")
    fun getDiaryEntriesForChild(childId: Long): Flow<List<DiaryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaryEntry(entry: DiaryEntry): Long

    @Delete
    suspend fun deleteDiaryEntry(entry: DiaryEntry)

    // --- MEDICAL DOCUMENTS ---
    @Query("SELECT * FROM medical_documents WHERE childId = :childId ORDER BY dateMillis DESC")
    fun getMedicalDocumentsForChild(childId: Long): Flow<List<MedicalDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalDocument(doc: MedicalDocument): Long

    @Delete
    suspend fun deleteMedicalDocument(doc: MedicalDocument)

    // --- REMINDERS ---
    @Query("SELECT * FROM reminders WHERE childId = :childId ORDER BY dateTimeMillis ASC")
    fun getRemindersForChild(childId: Long): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders ORDER BY dateTimeMillis ASC")
    fun getAllReminders(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)
}
