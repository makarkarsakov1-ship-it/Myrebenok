package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = AppRepository(database.appDao())

    // --- SYSTEM STATE ---
    private val _languageCode = MutableStateFlow("RU")
    val languageCode: StateFlow<String> = _languageCode.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _premiumTrialExpiry = MutableStateFlow<Long?>(null)
    val premiumTrialExpiry: StateFlow<Long?> = _premiumTrialExpiry.asStateFlow()

    // --- AUTH STATE ---
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _authMethod = MutableStateFlow<String?>(null) // "Email", "Google", "Apple", "Phone"
    val authMethod: StateFlow<String?> = _authMethod.asStateFlow()

    // --- DATA STATE ---
    val children: StateFlow<List<Child>> = repository.allChildren.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedChildId = MutableStateFlow<Long?>(null)
    val selectedChildId: StateFlow<Long?> = _selectedChildId.asStateFlow()

    // Selected child reactive flows
    val selectedChild: Flow<Child?> = combine(children, _selectedChildId) { list, id ->
        if (id == null) list.firstOrNull() else list.find { it.id == id }
    }

    val selectedChildVaccinations: Flow<List<VaccinationRecord>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getVaccinations(id) else flowOf(emptyList())
    }

    val selectedChildGrowthRecords: Flow<List<GrowthRecord>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getGrowthRecords(id) else flowOf(emptyList())
    }

    val selectedChildDiaryEntries: Flow<List<DiaryEntry>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getDiaryEntries(id) else flowOf(emptyList())
    }

    val selectedChildMedicalDocuments: Flow<List<MedicalDocument>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getMedicalDocuments(id) else flowOf(emptyList())
    }

    val selectedChildReminders: Flow<List<Reminder>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getReminders(id) else flowOf(emptyList())
    }

    init {
        // Automatically hook first child in list when kids load if none selected
        viewModelScope.launch {
            children.collect { list ->
                if (_selectedChildId.value == null && list.isNotEmpty()) {
                    _selectedChildId.value = list.first().id
                }
            }
        }
    }

    // --- SYSTEM ACTIONS ---
    fun setLanguage(code: String) {
        _languageCode.value = code
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun activatePremium(isYearly: Boolean = false) {
        _isPremium.value = true
        _premiumTrialExpiry.value = null
    }

    fun activateTrial() {
        _isPremium.value = true
        _premiumTrialExpiry.value = System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000)
    }

    fun endPremium() {
        _isPremium.value = false
        _premiumTrialExpiry.value = null
    }

    // --- AUTH ACTIONS ---
    fun login(emailOrPhone: String, name: String, method: String) {
        _userEmail.value = if (emailOrPhone.contains("@")) emailOrPhone else null
        _userName.value = name.ifEmpty { "Родитель" }
        _authMethod.value = method
    }

    fun logout() {
        _userEmail.value = null
        _userName.value = null
        _authMethod.value = null
    }

    // --- CHILDREN CRUD ---
    fun selectChild(id: Long) {
        _selectedChildId.value = id
    }

    fun addChild(
        name: String,
        gender: String,
        dobMillis: Long,
        height: Float,
        weight: Float,
        bloodGroup: String,
        allergies: String,
        chronicDiseases: String,
        healthNotes: String,
        countryCode: String,
        onLimitReached: () -> Unit
    ) {
        viewModelScope.launch {
            val count = children.value.size
            if (count >= 1 && !_isPremium.value) {
                // Free level kid limit
                onLimitReached()
                return@launch
            }

            val newChild = Child(
                name = name,
                gender = gender,
                dateOfBirth = dobMillis,
                height = height,
                weight = weight,
                bloodGroup = bloodGroup,
                allergies = allergies,
                chronicDiseases = chronicDiseases,
                healthNotes = healthNotes
            )
            val newId = repository.insertChild(newChild)
            _selectedChildId.value = newId

            // Pre-seed vaccination template
            repository.reseedVaccinations(newId, countryCode, dobMillis)

            // Seed initial growth records
            repository.insertGrowthRecord(GrowthRecord(childId = newId, type = "height", value = height, dateMillis = dobMillis))
            repository.insertGrowthRecord(GrowthRecord(childId = newId, type = "weight", value = weight, dateMillis = dobMillis))
        }
    }

    fun updateChildInfo(
        childId: Long,
        name: String,
        gender: String,
        dobMillis: Long,
        height: Float,
        weight: Float,
        bloodGroup: String,
        allergies: String,
        chronicDiseases: String,
        healthNotes: String,
        countryCode: String
    ) {
        viewModelScope.launch {
            val updated = Child(
                id = childId,
                name = name,
                gender = gender,
                dateOfBirth = dobMillis,
                height = height,
                weight = weight,
                bloodGroup = bloodGroup,
                allergies = allergies,
                chronicDiseases = chronicDiseases,
                healthNotes = healthNotes
            )
            repository.updateChild(updated)
        }
    }

    fun deleteChildProfile(child: Child) {
        viewModelScope.launch {
            repository.deleteChild(child)
            if (_selectedChildId.value == child.id) {
                _selectedChildId.value = children.value.firstOrNull { it.id != child.id }?.id
            }
        }
    }

    // --- GROWTH RECORDS CRUD ---
    fun addGrowthRecord(childId: Long, type: String, value: Float, dateMillis: Long) {
        viewModelScope.launch {
            repository.insertGrowthRecord(GrowthRecord(childId = childId, type = type, value = value, dateMillis = dateMillis))
        }
    }

    fun deleteGrowthRecordItem(id: Long) {
        viewModelScope.launch {
            repository.deleteGrowthRecord(id)
        }
    }

    // --- VACCINE STATUS ACTIONS ---
    fun toggleVaccineDone(record: VaccinationRecord, doneDateMillis: Long?) {
        viewModelScope.launch {
            val updated = record.copy(
                isCompleted = doneDateMillis != null,
                dateDoneMillis = doneDateMillis
            )
            repository.updateVaccination(updated)
        }
    }

    fun reseedChildVaccines(childId: Long, countryCode: String, dobMillis: Long) {
        viewModelScope.launch {
            repository.reseedVaccinations(childId, countryCode, dobMillis)
        }
    }

    // --- HEALTH DIARY CRUD ---
    fun addDiaryEntryLog(childId: Long, temperature: Float, symptoms: String, medicineTaken: String, notes: String, dateMillis: Long) {
        viewModelScope.launch {
            repository.insertDiaryEntry(
                DiaryEntry(
                    childId = childId,
                    temperature = temperature,
                    symptoms = symptoms,
                    notes = notes,
                    dateMillis = dateMillis,
                    medicineTaken = medicineTaken
                )
            )
        }
    }

    fun deleteDiaryEntryLog(entry: DiaryEntry) {
        viewModelScope.launch {
            repository.deleteDiaryEntry(entry)
        }
    }

    // --- MEDICAL DOCUMENTS CRUD ---
    fun addMedicalDocumentRecord(childId: Long, title: String, doctorName: String, type: String, notes: String, photoPath: String?) {
        viewModelScope.launch {
            repository.insertMedicalDocument(
                MedicalDocument(
                    childId = childId,
                    title = title,
                    doctorName = doctorName,
                    dateMillis = System.currentTimeMillis(),
                    documentType = type,
                    notes = notes,
                    photoPath = photoPath
                )
            )
        }
    }

    fun deleteMedicalDoc(doc: MedicalDocument) {
        viewModelScope.launch {
            repository.deleteMedicalDocument(doc)
        }
    }

    // --- REMINDERS CRUD ---
    fun addReminderItem(childId: Long, title: String, type: String, dateTimeMillis: Long, notes: String) {
        viewModelScope.launch {
            repository.insertReminder(
                Reminder(
                    childId = childId,
                    title = title,
                    type = type,
                    dateTimeMillis = dateTimeMillis,
                    notes = notes
                )
            )
        }
    }

    fun toggleReminderCompleted(reminder: Reminder) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isCompleted = !reminder.isCompleted))
        }
    }

    fun deleteReminderItem(reminder: Reminder) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }
}
