package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "children")
data class Child(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val photoUri: String? = null,
    val dateOfBirth: Long, // milliseconds
    val gender: String, // "Male", "Female"
    val height: Float, // init height in cm
    val weight: Float, // init weight in kg
    val bloodGroup: String, // "A (I)", "B (II)", "AB (III)", "O (IV)", etc.
    val allergies: String,
    val chronicDiseases: String,
    val healthNotes: String
)

@Entity(tableName = "growth_records")
data class GrowthRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val childId: Long,
    val type: String, // "height" or "weight"
    val value: Float, // cm or kg
    val dateMillis: Long
)

@Entity(tableName = "vaccinations")
data class VaccinationRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val childId: Long,
    val vaccineNameRu: String,
    val vaccineNameEn: String,
    val ageIntervalMonths: Int,
    val datePlannedMillis: Long,
    val dateDoneMillis: Long? = null,
    val isCompleted: Boolean = false,
    val infoRu: String,
    val infoEn: String
)

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val childId: Long,
    val temperature: Float,
    val symptoms: String,
    val notes: String,
    val dateMillis: Long,
    val medicineTaken: String
)

@Entity(tableName = "medical_documents")
data class MedicalDocument(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val childId: Long,
    val title: String,
    val doctorName: String,
    val dateMillis: Long,
    val documentType: String, // "analysis", "prescription", "discharge", "photo"
    val notes: String,
    val photoPath: String? = null
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val childId: Long,
    val title: String,
    val type: String, // "medicine", "vaccine", "doctor", "analysis"
    val dateTimeMillis: Long,
    val isCompleted: Boolean = false,
    val notes: String
)
