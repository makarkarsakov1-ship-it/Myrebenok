package com.example.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.*
import java.text.SimpleDateFormat
import java.util.*

// Dynamic custom visual shades matching user's pinkish-orange baby theme
val CoralRed = Color(0xFFFF5252)
val SoftPeach = Color(0xFFFFECEF)
val WarmSand = Color(0xFFFAF6F0)

val DarkGrayBackground = Color(0xFF121212)
val DarkSurfaceCard = Color(0xFF1E1E1E)

@Composable
fun ChildAppMain(viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val language by viewModel.languageCode.collectAsStateWithLifecycle()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "onboarding"

    // Colors mapping based on selected theme
    val customColorScheme = if (isDark) {
        darkColorScheme(
            primary = CoralRed,
            secondary = Color(0xFFFF8A80),
            background = DarkGrayBackground,
            surface = DarkSurfaceCard,
            onPrimary = Color.White,
            onBackground = Color(0xFFE0E0E0),
            onSurface = Color(0xFFEEEEEE)
        )
    } else {
        lightColorScheme(
            primary = CoralRed,
            secondary = Color(0xFFFF7043),
            background = WarmSand,
            surface = Color.White,
            onPrimary = Color.White,
            onBackground = Color(0xFF3E2723),
            onSurface = Color(0xFF4E342E)
        )
    }

    MaterialTheme(
        colorScheme = customColorScheme,
        typography = com.example.ui.theme.Typography
    ) {
        // Full Edge to Edge App Shell
        Scaffold(
            bottomBar = {
                // Show bottom navigation on main views only
                val showBottomBar = currentRoute in listOf("home", "vaccines", "diary", "more")
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text(Translations.get(language, "home"), fontSize = 11.sp) },
                            selected = currentRoute == "home",
                            onClick = { navController.navigate("home") { popUpTo(0) } }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
                            label = { Text(Translations.get(language, "calendar"), fontSize = 11.sp) },
                            selected = currentRoute == "vaccines",
                            onClick = { navController.navigate("vaccines") { popUpTo(0) } }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Assignment, contentDescription = "Diary") },
                            label = { Text(Translations.get(language, "diary"), fontSize = 11.sp) },
                            selected = currentRoute == "diary",
                            onClick = { navController.navigate("diary") { popUpTo(0) } }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Menu, contentDescription = "More") },
                            label = { Text(Translations.get(language, "more"), fontSize = 11.sp) },
                            selected = currentRoute == "more",
                            onClick = { navController.navigate("more") { popUpTo(0) } }
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavHost(
                    navController = navController,
                    startDestination = "onboarding"
                ) {
                    composable("onboarding") {
                        OnboardingScreen(
                            onFinish = {
                                navController.navigate("auth") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("auth") {
                        AuthScreen(
                            viewModel = viewModel,
                            onAuthSuccess = {
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("home") {
                        HomeDashboardScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("profile") {
                        ChildProfileScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("vaccines") {
                        VaccineCalendarScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("diary") {
                        HealthDiaryScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("more") {
                        MoreOptionsScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("development") {
                        DevelopmentScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("nutrition") {
                        NutritionScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("medicines") {
                        MedicineDirectoryScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("medcard") {
                        MedicalCardScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("reminders") {
                        RemindersScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("emergency") {
                        EmergencyCardScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("subscription") {
                        SubscriptionScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 1. ONBOARDING SCREEN
// ---------------------------------------------------------------------
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }

    val slides = listOf(
        Triple(
            "Забота. Здоровье. Будущее.",
            "Следите за здоровьем, развитием и важными событиями вашего ребёнка в одном приложении.",
            "" // Visual spacer / mock of images
        ),
        Triple(
            "Все важное под рукой",
            "Прививки, рост, вес, питание, лекарства и напоминания всегда с вами.",
            ""
        ),
        Triple(
            "Напоминания и забота",
            "Мы напомним о прививках, лекарствах и визитах к врачу, чтобы вы ничего не забыли.",
            ""
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SoftPeach, Color.White)
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo and Title Block
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(CoralRed)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo Logo",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Мой Ребёнок",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = CoralRed
            )
            Text(
                text = "Забота о здоровье и будущем вашего ребёнка",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 4.dp)
            )
        }

        // Active State slide body
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Text(
                text = slides[step].first,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = slides[step].second,
                fontSize = 15.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            // Step dots indicator
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                slides.forEachIndexed { idx, _ ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (step == idx) CoralRed else Color.LightGray)
                    )
                }
            }
        }

        // Lower Navigation
        Button(
            onClick = {
                if (step < 2) {
                    step++
                } else {
                    onFinish()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("onboarding_next_button")
        ) {
            Text(
                text = if (step < 2) "Далее" else "Начать",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

// ---------------------------------------------------------------------
// 2. AUTH SCREEN & CHANNELS
// ---------------------------------------------------------------------
@Composable
fun AuthScreen(viewModel: AppViewModel, onAuthSuccess: () -> Unit) {
    var isRegisterState by remember { mutableStateOf(false) }
    var parentsName by remember { mutableStateOf("") }
    var emailOrPhoneInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val language by viewModel.languageCode.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    colors = listOf(SoftPeach, Color.White)
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Small decorative logo
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(CoralRed),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Favorite, "Heart icon", tint = Color.White, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isRegisterState) Translations.get(language, "auth_register") else Translations.get(language, "auth_welcome"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isRegisterState) {
            OutlinedTextField(
                value = parentsName,
                onValueChange = { parentsName = it },
                label = { Text("Имя родителя / Parent name") },
                leadingIcon = { Icon(Icons.Default.Person, "Name") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("auth_parents_name")
            )
        }

        OutlinedTextField(
            value = emailOrPhoneInput,
            onValueChange = { emailOrPhoneInput = it },
            label = { Text(Translations.get(language, "auth_email")) },
            leadingIcon = { Icon(Icons.Default.Email, "Email") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("auth_email_field")
        )

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text(Translations.get(language, "auth_password")) },
            leadingIcon = { Icon(Icons.Default.Lock, "Lock") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .testTag("auth_password_field")
        )

        Button(
            onClick = {
                if (emailOrPhoneInput.isBlank()) {
                    Toast.makeText(context, "Заполните поля / Fill coordinates", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(
                        emailOrPhone = emailOrPhoneInput,
                        name = parentsName,
                        method = "Email"
                    )
                    onAuthSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("auth_action_submit")
        ) {
            Text(
                text = if (isRegisterState) Translations.get(language, "auth_signup_btn") else Translations.get(language, "auth_login_btn"),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Federated Auth options
        Text(text = "или войдите через / or fast sign", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.login("google@aistudio.com", "Google User", "Google")
                    onAuthSuccess()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PlayArrow, "Google logo", tint = CoralRed)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Google")
            }

            OutlinedButton(
                onClick = {
                    viewModel.login("apple@aistudio.com", "Apple User", "Apple")
                    onAuthSuccess()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Face, "Apple logo", tint = Color.DarkGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Apple ID")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = { isRegisterState = !isRegisterState }
        ) {
            Text(
                text = if (isRegisterState) Translations.get(language, "auth_already_have") else Translations.get(language, "auth_dont_have"),
                color = CoralRed,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------------------------------------------------------------------
// 3. HOME DASHBOARD SCREEN
// ---------------------------------------------------------------------
@Composable
fun HomeDashboardScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val childrenList by viewModel.children.collectAsStateWithLifecycle()
    val selectedChildId by viewModel.selectedChildId.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)
    val vaccinations by viewModel.selectedChildVaccinations.collectAsState(initial = emptyList())
    val reminders by viewModel.selectedChildReminders.collectAsState(initial = emptyList())
    val diaries by viewModel.selectedChildDiaryEntries.collectAsState(initial = emptyList())

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header with Kid Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = Translations.get(language, "app_title"),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = CoralRed
                )
                Text(
                    text = if (currentChild != null) "${currentChild?.name}, ${getAgeString(currentChild!!.dateOfBirth, language)}" else Translations.get(language, "no_children"),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Quick child switches
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (childrenList.size > 1) {
                    childrenList.forEach { kid ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (selectedChildId == kid.id) CoralRed else Color.LightGray)
                                .clickable { viewModel.selectChild(kid.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = kid.name.take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.testTag("add_kid_button")
                ) {
                    Icon(Icons.Default.Add, "Add/Edit profile", tint = CoralRed)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (currentChild == null) {
            // Empty State
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ChildCare,
                        contentDescription = "No children",
                        tint = CoralRed,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Translations.get(language, "no_children"),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("profile") },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                    ) {
                        Text(Translations.get(language, "add_child"))
                    }
                }
            }
            return
        }

        // QUICK METRICS & EMERGENCY CARD EXPOSE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedCard(
                colors = CardDefaults.cardColors(containerColor = CoralRed),
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("emergency") },
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Icon(Icons.Default.Warning, "Emergency", tint = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Translations.get(language, "emergency_card"),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text("Быстрый доступ", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                }
            }

            ElevatedCard(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("development") },
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Icon(Icons.Default.TrendingUp, "Development", tint = CoralRed)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Translations.get(language, "growth_weight"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${currentChild?.weight} кг / ${currentChild?.height} см",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NEAREST VACCINE ALERTS
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            val nextVaccines = vaccinations.filter { !it.isCompleted }.take(1)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("vaccines") }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SoftPeach),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.MedicalServices, "vaccine", tint = CoralRed)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Translations.get(language, "near_vaccine"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (nextVaccines.isNotEmpty()) {
                        val v = nextVaccines.first()
                        val vName = if (language == "RU" || language == "UA") v.vaccineNameRu else v.vaccineNameEn
                        Text("$vName (${Translations.get(language, "status_planned")})", fontSize = 12.sp, color = Color.DarkGray)
                    } else {
                        Text("Нет ближайших прививок / None planned", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Icon(Icons.Default.ChevronRight, "View vaccine list", tint = Color.Gray)
            }
        }

        // REMINDERS BLOCK
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                Translations.get(language, "active_reminders"),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            TextButton(onClick = { navController.navigate("reminders") }) {
                Text("Все / Manage", color = CoralRed)
            }
        }

        val activeReminders = reminders.filter { !it.isCompleted }
        if (activeReminders.isEmpty()) {
            Text(
                "Нет активных напоминаний / No active reminders",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            activeReminders.take(3).forEach { r ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val rIcon = when (r.type) {
                                "medicine" -> Icons.Default.MedicalServices
                                "vaccine" -> Icons.Default.DateRange
                                "doctor" -> Icons.Default.Person
                                else -> Icons.Default.Description
                            }
                            Icon(rIcon, "reminder type", tint = CoralRed, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(r.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(formatTime(r.dateTimeMillis), fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        IconButton(onClick = { viewModel.toggleReminderCompleted(r) }) {
                            Icon(Icons.Default.Check, "Done", tint = Color.Green)
                        }
                    }
                }
            }
        }

        // HEALTH DIARY COMPACT
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                Translations.get(language, "recent_records"),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            TextButton(onClick = { navController.navigate("diary") }) {
                Text("Весь дневник / All logs", color = CoralRed)
            }
        }

        if (diaries.isEmpty()) {
            Text(
                "Дневник пуст. Добавьте первую запись / Diary is empty",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            diaries.take(2).forEach { entry ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(formatDate(entry.dateMillis), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CoralRed)
                            Text("${entry.temperature}°C", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        if (entry.symptoms.isNotEmpty()) {
                            Text("Симптомы: ${entry.symptoms}", fontSize = 12.sp, color = Color.Gray)
                        }
                        if (entry.notes.isNotEmpty()) {
                            Text(entry.notes, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 4. CHILD PROFILE EDIT SCREEN
// ---------------------------------------------------------------------
@Composable
fun ChildProfileScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val childrenList by viewModel.children.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // Fields
    var isNewState by remember { mutableStateOf(currentChild == null) }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var dobMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("A (I)") }
    var allergies by remember { mutableStateOf("") }
    var chronicDiseases by remember { mutableStateOf("") }
    var healthNotes by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("RU") }

    // Init values
    LaunchedEffect(currentChild, isNewState) {
        if (!isNewState && currentChild != null) {
            val c = currentChild!!
            name = c.name
            gender = c.gender
            dobMillis = c.dateOfBirth
            height = c.height.toString()
            weight = c.weight.toString()
            bloodGroup = c.bloodGroup
            allergies = c.allergies
            chronicDiseases = c.chronicDiseases
            healthNotes = c.healthNotes
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                text = if (isNewState) Translations.get(language, "add_child") else Translations.get(language, "child_profile"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle creation selector if has existing children
        if (childrenList.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { isNewState = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (!isNewState) SoftPeach else Color.Transparent
                    )
                ) {
                    Text("Редактировать текущего")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        isNewState = true
                        name = ""
                        height = ""
                        weight = ""
                        allergies = ""
                        chronicDiseases = ""
                        healthNotes = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isNewState) CoralRed else Color.LightGray
                    )
                ) {
                    Text("Добавить ребенка")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(Translations.get(language, "name")) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("profile_name_field")
        )

        // Gender selector
        Text("Пол / Gender", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { gender = "Male" }
            ) {
                RadioButton(selected = gender == "Male", onClick = { gender = "Male" })
                Text("Мальчик / Boy")
            }
            Spacer(modifier = Modifier.width(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { gender = "Female" }
            ) {
                RadioButton(selected = gender == "Female", onClick = { gender = "Female" })
                Text("Девочка / Girl")
            }
        }

        // DOB Picker simple simulation
        Text("Дата рождения / Birthday (дд/мм/гггг)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        var dateText by remember { mutableStateOf("") }
        LaunchedEffect(dobMillis) {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateText = format.format(Date(dobMillis))
        }
        OutlinedTextField(
            value = dateText,
            onValueChange = { input ->
                dateText = input
                // Parse date if complete
                try {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val parsed = sdf.parse(input)
                    if (parsed != null) dobMillis = parsed.time
                } catch (e: Exception) {}
            },
            placeholder = { Text("Example: 20/03/2024") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("profile_dob_field")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text(Translations.get(language, "height")) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 12.dp)
                    .testTag("profile_height_field")
            )
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text(Translations.get(language, "weight")) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 12.dp)
                    .testTag("profile_weight_field")
            )
        }

        // Vaccine Country Setting
        Text(Translations.get(language, "vaccine_country"), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { countryCode = "RU" }) {
                RadioButton(selected = countryCode == "RU", onClick = { countryCode = "RU" })
                Text("Россия")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { countryCode = "US" }) {
                RadioButton(selected = countryCode == "US", onClick = { countryCode = "US" })
                Text("США / ВОЗ")
            }
        }

        OutlinedTextField(
            value = bloodGroup,
            onValueChange = { bloodGroup = it },
            label = { Text(Translations.get(language, "blood_group")) },
            placeholder = { Text("A (I) Rh+, B (II) Rh-, etc.") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = allergies,
            onValueChange = { allergies = it },
            label = { Text(Translations.get(language, "allergies")) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = chronicDiseases,
            onValueChange = { chronicDiseases = it },
            label = { Text(Translations.get(language, "chronic_diseases")) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = healthNotes,
            onValueChange = { healthNotes = it },
            label = { Text(Translations.get(language, "health_notes")) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                val parsedHeight = height.toFloatOrNull() ?: 50f
                val parsedWeight = weight.toFloatOrNull() ?: 3.5f

                if (name.isBlank()) {
                    Toast.makeText(context, "Заполните имя / Set child name", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (isNewState) {
                    viewModel.addChild(
                        name = name,
                        gender = gender,
                        dobMillis = dobMillis,
                        height = parsedHeight,
                        weight = parsedWeight,
                        bloodGroup = bloodGroup,
                        allergies = allergies,
                        chronicDiseases = chronicDiseases,
                        healthNotes = healthNotes,
                        countryCode = countryCode,
                        onLimitReached = {
                            Toast.makeText(context, Translations.get(language, "select_kids_limit"), Toast.LENGTH_LONG).show()
                            navController.navigate("subscription")
                        }
                    )
                } else {
                    currentChild?.let { c ->
                        viewModel.updateChildInfo(
                            childId = c.id,
                            name = name,
                            gender = gender,
                            dobMillis = dobMillis,
                            height = parsedHeight,
                            weight = parsedWeight,
                            bloodGroup = bloodGroup,
                            allergies = allergies,
                            chronicDiseases = chronicDiseases,
                            healthNotes = healthNotes,
                            countryCode = countryCode
                        )
                    }
                }
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_profile_button")
        ) {
            Text(Translations.get(language, "save"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        if (!isNewState && currentChild != null) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    currentChild?.let { viewModel.deleteChildProfile(it) }
                    navController.popBackStack()
                },
                border = BorderStroke(1.dp, Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(Translations.get(language, "delete"), color = Color.Red)
            }
        }
    }
}

// ---------------------------------------------------------------------
// 5. VACCINATION CALENDAR SCREEN
// ---------------------------------------------------------------------
@Composable
fun VaccineCalendarScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)
    val vaccinations by viewModel.selectedChildVaccinations.collectAsState(initial = emptyList())

    var activeTab by remember { mutableIntStateOf(0) } // 0 = Calendar, 1 = History

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    Translations.get(language, "vaccination_calendar"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CoralRed
                )
                Text(currentChild?.name ?: "No kid", fontSize = 13.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentChild == null) {
            Text("Пожалуйста, сначала добавьте ребенка в разделе Главная.", color = Color.Gray)
            return
        }

        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.Transparent,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Tab(selected = activeTab == 0, onClick = { activeTab = 0 }) {
                Text("Календарь", fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
            }
            Tab(selected = activeTab == 1, onClick = { activeTab = 1 }) {
                Text(Translations.get(language, "vaccination_history"), fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
            }
        }

        val listToDisplay = if (activeTab == 0) {
            vaccinations.filter { !it.isCompleted }
        } else {
            vaccinations.filter { it.isCompleted }
        }

        if (listToDisplay.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Список пуст / List empty", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listToDisplay) { vaccine ->
                    val nameText = if (language == "RU" || language == "UA") vaccine.vaccineNameRu else vaccine.vaccineNameEn
                    val descText = if (language == "RU" || language == "UA") vaccine.infoRu else vaccine.infoEn

                    ElevatedCard(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(nameText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Планируется в ${vaccine.ageIntervalMonths} мес.", fontSize = 12.sp, color = Color.Gray)
                                Text(descText, fontSize = 11.sp, color = Color.DarkGray)
                                if (vaccine.isCompleted && vaccine.dateDoneMillis != null) {
                                    Text("Сделано: ${formatDate(vaccine.dateDoneMillis)}", color = Color.Green, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                                }
                            }
                            Checkbox(
                                checked = vaccine.isCompleted,
                                onCheckedChange = { done ->
                                    val doneTime = if (done) System.currentTimeMillis() else null
                                    viewModel.toggleVaccineDone(vaccine, doneTime)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 6. HEALTH DIARY SCREEN (TEMPERATURE, SYMPTOMS, NOTES)
// ---------------------------------------------------------------------
@Composable
fun HealthDiaryScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)
    val diaries by viewModel.selectedChildDiaryEntries.collectAsState(initial = emptyList())

    val context = LocalContext.current

    var showAddForm by remember { mutableStateOf(false) }

    // Form states
    var temperatureInput by remember { mutableStateOf("36.6") }
    var symptomsInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }
    var medicineInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    Translations.get(language, "diary"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CoralRed
                )
                Text(currentChild?.name ?: "No kid selected", fontSize = 13.sp, color = Color.Gray)
            }

            Button(
                onClick = { showAddForm = !showAddForm },
                colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
            ) {
                Text(if (showAddForm) "Скрыть / Hide" else Translations.get(language, "add_entry"))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentChild == null) {
            Text("Пожалуйста, сначала добавьте ребенка.", color = Color.Gray)
            return
        }

        if (showAddForm) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Новая запись", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoralRed)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = temperatureInput,
                        onValueChange = { temperatureInput = it },
                        label = { Text(Translations.get(language, "temperature")) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("diary_temp_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = symptomsInput,
                        onValueChange = { symptomsInput = it },
                        label = { Text("Симптомы (насморк, кашель и т.д.) / Symptoms") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = medicineInput,
                        onValueChange = { medicineInput = it },
                        label = { Text("Прием лекарств / Meds taken") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        label = { Text(Translations.get(language, "notes")) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val temp = temperatureInput.toFloatOrNull() ?: 36.6f
                            viewModel.addDiaryEntryLog(
                                childId = currentChild!!.id,
                                temperature = temp,
                                symptoms = symptomsInput,
                                medicineTaken = medicineInput,
                                notes = noteInput,
                                dateMillis = System.currentTimeMillis()
                            )
                            // Clear fields and close
                            temperatureInput = "36.6"
                            symptomsInput = ""
                            noteInput = ""
                            medicineInput = ""
                            showAddForm = false
                            Toast.makeText(context, "Запись сохранена!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(Translations.get(language, "save"))
                    }
                }
            }
        }

        // List
        if (diaries.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Дневник пуст. Записывайте состояние здоровья ребенка каждый день!", textAlign = TextAlign.Center, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(diaries) { entry ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(formatDate(entry.dateMillis), fontWeight = FontWeight.Bold, color = CoralRed, fontSize = 13.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${entry.temperature}°C", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    IconButton(
                                        onClick = { viewModel.deleteDiaryEntryLog(entry) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, "Delete item", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                            if (entry.symptoms.isNotEmpty()) {
                                Text("Симптомы: ${entry.symptoms}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                            if (entry.medicineTaken.isNotEmpty()) {
                                Text("Прием лекарств: ${entry.medicineTaken}", fontSize = 12.sp, color = Color.DarkGray)
                            }
                            if (entry.notes.isNotEmpty()) {
                                Text(entry.notes, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 7. EXPANDED MORE OPTIONS & APP SECTIONS SCREEN
// ---------------------------------------------------------------------
@Composable
fun MoreOptionsScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            Translations.get(language, "more"),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = CoralRed,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Subscription Ad banner inside More screen
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isPremium) Color(0xFFFFD54F) else CoralRed
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .clickable { navController.navigate("subscription") }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Subscription badge",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = if (isPremium) Translations.get(language, "active_premium") else "Попробовать Premium бесплатно",
                        fontWeight = FontWeight.Bold,
                        color = if (isPremium) Color.Black else Color.White,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (isPremium) "Вам доступны все 12 продвинутых функций" else "Множество детей, графики ВОЗ, PDF экспорты",
                        color = if (isPremium) Color.DarkGray else Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Section Cards List
        val sections = listOf(
            Triple(Translations.get(language, "growth_charts"), Icons.Default.TrendingUp, "development"),
            Triple(Translations.get(language, "nutrition"), Icons.Default.Restaurant, "nutrition"),
            Triple(Translations.get(language, "med_directory"), Icons.Default.LocalPharmacy, "medicines"),
            Triple(Translations.get(language, "med_card"), Icons.Default.FolderShared, "medcard"),
            Triple(Translations.get(language, "reminders"), Icons.Default.Notifications, "reminders"),
            Triple(Translations.get(language, "emergency_card"), Icons.Default.Warning, "emergency"),
            Triple(Translations.get(language, "settings"), Icons.Default.Settings, "settings")
        )

        sections.forEach { (title, icon, route) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navController.navigate(route) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftPeach),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = title, tint = CoralRed)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Icon(Icons.Default.ChevronRight, "Arrow", tint = Color.LightGray)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 8. DEVELOPMENT PROGRESS SCREEN (CUSTOM CHARTS & INTERACTIVE SLIDERS)
// ---------------------------------------------------------------------
@Composable
fun DevelopmentScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)
    val growthRecords by viewModel.selectedChildGrowthRecords.collectAsState(initial = emptyList())

    val context = LocalContext.current

    var isWeightMode by remember { mutableStateOf(true) } // true for weight chart, false for height
    var valueInput by remember { mutableStateOf("") }
    var showExplanation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                Translations.get(language, "growth_charts"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentChild == null) {
            Text("Сначала добавьте ребенка.")
            return
        }

        // Toggle switcher height vs weight
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = { isWeightMode = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isWeightMode) SoftPeach else Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Scale, "Weight")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Вес (кг) / Weight")
            }
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedButton(
                onClick = { isWeightMode = false },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (!isWeightMode) SoftPeach else Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Straighten, "Height")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Рост (см) / Height")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // CUSTOM GRAPH CANVAS (WHO guidelines limits)
        Text(
            text = if (isWeightMode) "График веса ВОЗ" else "График роста ВОЗ",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = CoralRed
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(vertical = 8.dp)
        ) {
            val typeStr = if (isWeightMode) "weight" else "height"
            val points = growthRecords.filter { it.type == typeStr }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw reference WHO guide limits shading (Normal corridor)
                    val yWHOBottom = h * 0.7f
                    val yWHOTop = h * 0.3f
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = androidx.compose.ui.geometry.Offset(0f, yWHOBottom),
                        end = androidx.compose.ui.geometry.Offset(w, yWHOBottom - 20f),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = androidx.compose.ui.geometry.Offset(0f, yWHOTop),
                        end = androidx.compose.ui.geometry.Offset(w, yWHOTop - 20f),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )

                    // Draw curve line of child's measured records
                    if (points.isNotEmpty()) {
                        val maxVal = points.maxOf { it.value }.coerceAtLeast(10f)
                        val minVal = points.minOf { it.value }.coerceAtMost(maxVal - 1f)
                        val valRange = (maxVal - minVal).coerceAtLeast(1f)

                        val coordPoints = points.mapIndexed { idx, item ->
                            val x = if (points.size <= 1) w / 2f else (idx.toFloat() / (points.size - 1)) * w
                            val normalized = (item.value - minVal) / valRange
                            val y = h - (normalized * h * 0.7f + h * 0.15f)
                            androidx.compose.ui.geometry.Offset(x, y)
                        }

                        for (i in 0 until coordPoints.size - 1) {
                            drawLine(
                                color = CoralRed,
                                start = coordPoints[i],
                                end = coordPoints[i + 1],
                                strokeWidth = 8f,
                                cap = StrokeCap.Round
                            )
                        }

                        // Draw dots for markers
                        coordPoints.forEach { pt ->
                            drawCircle(
                                color = CoralRed,
                                radius = 10f,
                                center = pt
                            )
                        }
                    }
                }

                // Normal WHO tag watermark
                Text(
                    text = Translations.get(language, "norm_who"),
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Actions: Add new growth/weight value
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(Translations.get(language, "add_record"), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = valueInput,
                        onValueChange = { valueInput = it },
                        label = { Text(if (isWeightMode) "Вес (кг)" else "Рост (см)") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("growth_record_input")
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val v = valueInput.toFloatOrNull()
                            if (v != null) {
                                viewModel.addGrowthRecord(
                                    childId = currentChild!!.id,
                                    type = if (isWeightMode) "weight" else "height",
                                    value = v,
                                    dateMillis = System.currentTimeMillis()
                                )
                                valueInput = ""
                                Toast.makeText(context, "Замер сохранен!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Введите числовое значение", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                    ) {
                        Text("ОК")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History logs
        Text("История замеров / Measurement History", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        val activeRecords = growthRecords.filter { it.type == if (isWeightMode) "weight" else "height" }.reversed()

        if (activeRecords.isEmpty()) {
            Text("Пока нет замеров.", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
        } else {
            activeRecords.forEach { r ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(formatDate(r.dateMillis), fontSize = 13.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${r.value} ${if (isWeightMode) "кг" else "см"}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = { viewModel.deleteGrowthRecordItem(r.id) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                        }
                    }
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}

// ---------------------------------------------------------------------
// 9. NUTRITION SCREEN (UNDERSTANDING DEVELOPMENT, ALLERGIES CHECK)
// ---------------------------------------------------------------------
@Composable
fun NutritionScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)

    var currentAgeInterval by remember { mutableStateOf("6-8") } // "6-8", "9-12", "1-3", "3+"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                Translations.get(language, "nutrition"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interval Tabs Picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("6-8", "9-12", "1-3", "3+").forEach { range ->
                val label = when (range) {
                    "6-8" -> "6-8 мес."
                    "9-12" -> "9-12 мес."
                    "1-3" -> "1-3 года"
                    else -> "3+ года"
                }
                OutlinedButton(
                    onClick = { currentAgeInterval = range },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (currentAgeInterval == range) SoftPeach else Color.Transparent
                    ),
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text(label, fontSize = 11.sp, maxLines = 1)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val catData = StaticCatalogData.nutritionCategories.find { it.ageInterval == currentAgeInterval }
            ?: StaticCatalogData.nutritionCategories.first()

        // Recommended / Allowed
        Text(Translations.get(language, "allowed_food"), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2E7D32))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(catData.recommended) { item ->
                FoodNutritionRow(item = item, currentChild = currentChild, isAllowed = true, language = language)
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(Translations.get(language, "forbidden_food"), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Red)
            }

            items(catData.forbidden) { item ->
                FoodNutritionRow(item = item, currentChild = currentChild, isAllowed = false, language = language)
            }
        }
    }
}

@Composable
fun FoodNutritionRow(item: FoodRecommendItem, currentChild: Child?, isAllowed: Boolean, language: String) {
    val nameText = if (language == "RU" || language == "UA") item.ruName else item.enName

    // Run custom allergy trigger check
    val isAllergic = currentChild != null && item.triggerAllergyKeywords.any { keyword ->
        currentChild.allergies.lowercase().contains(keyword) ||
                currentChild.chronicDiseases.lowercase().contains(keyword)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isAllergic) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isAllowed) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = "Status indicator",
                        tint = if (isAllergic) Color.Red else (if (isAllowed) Color(0xFF2E7D32) else Color.Red)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(nameText, fontWeight = FontWeight.SemiBold)
                }
                if (isAllergic) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Red)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Опасно! / Alert", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isAllergic) {
                Text(
                    text = "⚠️ Внимание! У ребенка аллергия или заболевание, связанное с этим продуктом/ингредиентом: '${currentChild?.allergies ?: ""}'",
                    color = Color.Red,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------
// 10. MEDICINE GUIDE & REFERENCE SCREEN
// ---------------------------------------------------------------------
@Composable
fun MedicineDirectoryScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                Translations.get(language, "med_directory"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CRITICAL DISCLAIMER CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Icon(Icons.Default.Warning, "Pediatric warning", tint = Color(0xFFE65100))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = Translations.get(language, "med_consultation_warning"),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Meds List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(StaticCatalogData.medicines) { med ->
                val categoryName = if (language == "RU" || language == "UA") med.categoryRu else med.categoryEn
                val limitLabel = if (language == "RU" || language == "UA") med.ageLimitRu else med.ageLimitEn
                val dosageLabel = if (language == "RU" || language == "UA") med.dosageRu else med.dosageEn
                val contraLabel = if (language == "RU" || language == "UA") med.contraindicationsRu else med.contraindicationsEn

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(med.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoralRed)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SoftPeach)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(limitLabel, fontSize = 10.sp, color = CoralRed, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(categoryName, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 2.dp))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text("Рекомендуемая справочная дозировка:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(dosageLabel, fontSize = 12.sp, color = Color.DarkGray)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Противопоказания:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Red)
                        Text(contraLabel, fontSize = 11.sp, color = Color.Red.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 11. MEDICAL CARD (DOCUMENTS CLOUD OR SCANS STORAGE)
// ---------------------------------------------------------------------
@Composable
fun MedicalCardScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)
    val docs by viewModel.selectedChildMedicalDocuments.collectAsState(initial = emptyList())

    val context = LocalContext.current

    var showAddDoc by remember { mutableStateOf(false) }

    // Add states
    var titleInput by remember { mutableStateOf("") }
    var doctorNameInput by remember { mutableStateOf("") }
    var notesInput by remember { mutableStateOf("") }
    var docType by remember { mutableStateOf("analysis") } // "analysis", "prescription", "discharge"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    Translations.get(language, "med_card"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CoralRed
                )
                Text(currentChild?.name ?: "No kid selected", fontSize = 13.sp, color = Color.Gray)
            }

            Button(
                onClick = { showAddDoc = !showAddDoc },
                colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
            ) {
                Text(if (showAddDoc) "Скрыть / Hide" else Translations.get(language, "add_doc"))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentChild == null) {
            Text("Пожалуйста, сначала добавьте ребенка.")
            return
        }

        if (showAddDoc) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Загрузка/Добавление документа", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CoralRed)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text(Translations.get(language, "doc_title")) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("doc_title_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = doctorNameInput,
                        onValueChange = { doctorNameInput = it },
                        label = { Text(Translations.get(language, "doctor")) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Тип документа", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { docType = "analysis" }) {
                            RadioButton(selected = docType == "analysis", onClick = { docType = "analysis" })
                            Text("Анализ")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { docType = "prescription" }) {
                            RadioButton(selected = docType == "prescription", onClick = { docType = "prescription" })
                            Text("Рецепт")
                        }
                    }

                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text(Translations.get(language, "notes")) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (titleInput.isBlank()) {
                                Toast.makeText(context, "Укажите название документа", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.addMedicalDocumentRecord(
                                childId = currentChild!!.id,
                                title = titleInput,
                                doctorName = doctorNameInput,
                                type = docType,
                                notes = notesInput,
                                photoPath = null
                            )
                            titleInput = ""
                            doctorNameInput = ""
                            notesInput = ""
                            showAddDoc = false
                            Toast.makeText(context, "Документ успешно прикреплен!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Прикрепить / Save document")
                    }
                }
            }
        }

        // List
        if (docs.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Медицинская карта пуста. Сохраняйте фото анализов, рецепты и выписки!", textAlign = TextAlign.Center, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(docs) { doc ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val icon = when (doc.documentType) {
                                        "analysis" -> Icons.Default.Science
                                        "prescription" -> Icons.Default.ReceiptLong
                                        else -> Icons.Default.Description
                                    }
                                    Icon(icon, "doc icon", tint = CoralRed)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(doc.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }

                                IconButton(onClick = { viewModel.deleteMedicalDoc(doc) }) {
                                    Icon(Icons.Default.Delete, "Delete doc", tint = Color.LightGray)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Врач / Специалист: ${doc.doctorName.ifEmpty { "Не указан" }}", fontSize = 12.sp, color = Color.Gray)
                            Text("Дата добавления: ${formatDate(doc.dateMillis)}", fontSize = 11.sp, color = Color.Gray)
                            if (doc.notes.isNotEmpty()) {
                                Text(doc.notes, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 12. CUSTOM REMINDERS SETUP SCREEN
// ---------------------------------------------------------------------
@Composable
fun RemindersScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)
    val reminders by viewModel.selectedChildReminders.collectAsState(initial = emptyList())

    val context = LocalContext.current

    var showForm by remember { mutableStateOf(false) }

    // Form states
    var titleInput by remember { mutableStateOf("") }
    var typeSelection by remember { mutableStateOf("medicine") } // "medicine", "vaccine", "doctor", "analysis"
    var dateHourInput by remember { mutableStateOf("") } // simple text date

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    Translations.get(language, "reminders"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CoralRed
                )
                Text(currentChild?.name ?: "No child", fontSize = 13.sp, color = Color.Gray)
            }

            Button(
                onClick = { showForm = !showForm },
                colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
            ) {
                Text(if (showForm) "Скрыть / Hide" else Translations.get(language, "add_reminder"))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentChild == null) {
            Text("Пожалуйста, сначала добавьте ребенка.")
            return
        }

        if (showForm) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Новое напоминание", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CoralRed)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Название (например: Дать витамин D3) / Reminder note") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("reminder_title_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Категория / Type", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "medicine" to "Лекарство",
                            "vaccine" to "Прививка",
                            "doctor" to "Врач",
                            "analysis" to "Анализы"
                        ).forEach { (slug, rLabel) ->
                            FilterChip(
                                selected = typeSelection == slug,
                                onClick = { typeSelection = slug },
                                label = { Text(rLabel) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = dateHourInput,
                        onValueChange = { dateHourInput = it },
                        label = { Text("Дата и Время (Пример: 10:00 или 12/03 15:00) / Time") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (titleInput.isBlank()) {
                                Toast.makeText(context, "Введите название напоминания", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.addReminderItem(
                                childId = currentChild!!.id,
                                title = titleInput,
                                type = typeSelection,
                                dateTimeMillis = System.currentTimeMillis() + (3600 * 1000L), // mock time range 1hr ahead
                                notes = dateHourInput
                            )
                            titleInput = ""
                            dateHourInput = ""
                            showForm = false
                            Toast.makeText(context, "Напоминание успешно создано!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(Translations.get(language, "save"))
                    }
                }
            }
        }

        // List
        if (reminders.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Список напоминаний пуст.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reminders) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.NotificationsActive,
                                    contentDescription = "bell",
                                    tint = if (item.isCompleted) Color.Green else CoralRed
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        item.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        textDecoration = if (item.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                    )
                                    Text("Время: ${item.notes.ifEmpty { formatTime(item.dateTimeMillis) }}", fontSize = 12.sp, color = Color.Gray)
                                }
                            }

                            Row {
                                IconButton(onClick = { viewModel.toggleReminderCompleted(item) }) {
                                    Icon(Icons.Default.Done, "Check complete", tint = if (item.isCompleted) Color.Gray else Color.Green)
                                }
                                IconButton(onClick = { viewModel.deleteReminderItem(item) }) {
                                    Icon(Icons.Default.Delete, "Delete reminder", tint = Color.Red.copy(alpha = 0.5f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// 13. EMERGENCY CARD SCREEN
// ---------------------------------------------------------------------
@Composable
fun EmergencyCardScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val currentChild by viewModel.selectedChild.collectAsState(initial = null)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                Translations.get(language, "emergency_card"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentChild == null) {
            Text("Пожалуйста, сначала создайте профиль ребенка на вкладке Главная.")
            return
        }

        // RED CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.MedicalServices, "Medical", tint = Color.White, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = currentChild!!.name.uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = Color.White
                )
                Text(
                    text = "Дата рождения: ${formatDate(currentChild!!.dateOfBirth)}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
                Text(
                    text = "Группа крови: ${currentChild!!.bloodGroup.ifEmpty { "Не указана" }}",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Allergies Alert
        Text(Translations.get(language, "allergies"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(
                text = currentChild!!.allergies.ifEmpty { "Нет известных аллергий / No registered allergies" },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentChild!!.allergies.isNotEmpty()) Color.Red else Color.DarkGray,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chronic disease
        Text(Translations.get(language, "chronic_diseases"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(
                text = currentChild!!.chronicDiseases.ifEmpty { "Нет / None" },
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Contacts list
        Text(Translations.get(language, "contacts_parent"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                EmergencyContactRow("Мама", "+7 (999) 111-22-33", context)
                Spacer(modifier = Modifier.height(12.dp))
                EmergencyContactRow("Папа", "+7 (999) 444-55-66", context)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pediatrist contacts
        Text(Translations.get(language, "contacts_doctor"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                EmergencyContactRow(Translations.get(language, "pediatrician"), "+7 (999) 777-88-99", context)
            }
        }
    }
}

@Composable
fun EmergencyContactRow(role: String, phoneStr: String, context: android.content.Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(role, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(phoneStr, fontSize = 13.sp, color = Color.Gray)
        }

        IconButton(onClick = {
            // Trigger Phone Call Activity directly
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneStr"))
            context.startActivity(intent)
        }) {
            Icon(Icons.Default.Call, "Call emergency target", tint = Color.Green)
        }
    }
}

// ---------------------------------------------------------------------
// 14. APPLICATION SETTINGS SCREEN (LANGS & THEMES SHIFTERS)
// ---------------------------------------------------------------------
@Composable
fun SettingsScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    var showLanguagesSelector by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                Translations.get(language, "settings"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Change languages block
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, "Language", tint = CoralRed)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(Translations.get(language, "language"), fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { showLanguagesSelector = !showLanguagesSelector },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                    ) {
                        val activeLangName = Translations.languages.find { it.first == language }?.second ?: "Русский"
                        Text(activeLangName)
                    }
                }

                // Inner Dropdown
                if (showLanguagesSelector) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Translations.languages.forEach { (code, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLanguage(code)
                                    showLanguagesSelector = false
                                }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(label, fontSize = 14.sp)
                            if (code == language) {
                                Icon(Icons.Default.Check, "Selected", tint = CoralRed)
                            }
                        }
                    }
                }
            }
        }

        // Toggle Dark theme block
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Theme status",
                        tint = CoralRed
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(Translations.get(language, "dark_theme"), fontWeight = FontWeight.Bold)
                }

                Switch(
                    checked = isDark,
                    onCheckedChange = { viewModel.toggleTheme() },
                    modifier = Modifier.testTag("dark_theme_switch")
                )
            }
        }

        // Subscription Manager quick entry
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("subscription") }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WorkspacePremium, "Premium badge", tint = CoralRed)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(Translations.get(language, "subscription"), fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.ChevronRight, "GOTO")
            }
        }
    }
}

// ---------------------------------------------------------------------
// 15. PREMIUM SUBSCRIPTION PRICING SCREEN
// ---------------------------------------------------------------------
@Composable
fun SubscriptionScreen(viewModel: AppViewModel, navController: NavHostController) {
    val language by viewModel.languageCode.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val trialExpiry by viewModel.premiumTrialExpiry.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                Translations.get(language, "subscription"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Subscription status banner
        if (isPremium) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Verified, "Verified logo", tint = Color(0xFF2E7D32), modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(Translations.get(language, "active_premium"), fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    if (trialExpiry != null) {
                        Text("Пробный период до ${formatDate(trialExpiry!!)}", fontSize = 12.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { viewModel.endPremium() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = BorderStroke(1.dp, Color.Red)
                    ) {
                        Text("Отменить пробную / Отключить Premium")
                    }
                }
            }
        } else {
            // General Sales pitch info
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftPeach),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(CoralRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.WorkspacePremium, "Premium shield", tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Разблокировать все 12 функций", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = CoralRed)
                    Text("Полный контроль над развитием ребенка на всех устройствах", fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(20.dp))

                    val benefits = listOf(
                        Translations.get(language, "unlimited_children"),
                        Translations.get(language, "expanded_med_card"),
                        Translations.get(language, "export_pdf"),
                        Translations.get(language, "backup"),
                        Translations.get(language, "family_sharing")
                    )

                    benefits.forEach { benefit ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, "checked", tint = Color.Green, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(benefit, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CTA BUY BUTTONS
            Button(
                onClick = {
                    viewModel.activateTrial()
                    Toast.makeText(context, "14 дней бесплатного доступа активировано!", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("activate_trial_button")
            ) {
                Text(Translations.get(language, "try_free"), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.activatePremium(false)
                    Toast.makeText(context, "Подписка за 299 ₽ успешно оформлена!", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(Translations.get(language, "subscribe_monthly"), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.activatePremium(true)
                    Toast.makeText(context, "Годовая подписка за 2 490 ₽ оформлена!", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(Translations.get(language, "subscribe_yearly"), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// ---------------------------------------------------------------------
// HELPER CONVERTERS AND STRING PARSERS
// ---------------------------------------------------------------------
fun getAgeString(birthMillis: Long, currentLang: String): String {
    val diff = System.currentTimeMillis() - birthMillis
    val months = (diff / (30L * 24 * 60 * 60 * 1000)).toInt()

    if (months <= 0) {
        return if (currentLang == "RU") "Меньше месяца" else "Under a month"
    }

    val years = months / 12
    val remainingMonths = months % 12

    return when {
        years == 0 -> {
            if (currentLang == "RU") "$months мес." else "$months mos."
        }
        else -> {
            if (currentLang == "RU") "$years г. $remainingMonths мес." else "$years yr. $remainingMonths mos."
        }
    }
}

fun formatDate(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(date)
}

fun formatTime(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}
