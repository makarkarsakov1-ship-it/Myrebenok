package com.example.ui

data class FoodRecommendItem(
    val ruName: String,
    val enName: String,
    val triggerAllergyKeywords: List<String>, // if child's allergies contains any, trigger alert
    val iconName: String // "apple", "milk", "honey", "meat", "fish", "cereal"
)

data class AgeNutritionData(
    val ageInterval: String, // "6-8", "9-12", "1-3", "3+"
    val recommended: List<FoodRecommendItem>,
    val forbidden: List<FoodRecommendItem>
)

data class MedicineItem(
    val name: String,
    val categoryRu: String,
    val categoryEn: String,
    val ageLimitRu: String,
    val ageLimitEn: String,
    val dosageRu: String,
    val dosageEn: String,
    val contraindicationsRu: String,
    val contraindicationsEn: String
)

object StaticCatalogData {
    val nutritionCategories = listOf(
        AgeNutritionData(
            ageInterval = "6-8",
            recommended = listOf(
                FoodRecommendItem("Кабачок", "Zucchini", listOf("кабачок", "zucchini"), "zucchini"),
                FoodRecommendItem("Брокколи", "Broccoli", listOf("брокколи", "broccoli"), "broccoli"),
                FoodRecommendItem("Пюре из индейки", "Turkey puree", listOf("индейка", "turkey"), "turkey"),
                FoodRecommendItem("Гречневая каша", "Buckwheat porridge", listOf("гречка", "buckwheat"), "cereal"),
                FoodRecommendItem("Яблочное пюре", "Apple puree", listOf("яблоко", "apple"), "apple")
            ),
            forbidden = listOf(
                FoodRecommendItem("Цельное молоко", "Whole milk", listOf("молоко", "milk", "лактоз", "lactos"), "milk"),
                FoodRecommendItem("Мёд", "Honey", listOf("мед", "мёд", "honey"), "honey"),
                FoodRecommendItem("Орехи", "Nuts", listOf("орех", "nuts", "арахис", "peanut"), "nuts"),
                FoodRecommendItem("Цитрусовые", "Citrus fruits", listOf("цитрус", "апельсин", "лимон", "citrus", "orange"), "orange")
            )
        ),
        AgeNutritionData(
            ageInterval = "9-12",
            recommended = listOf(
                FoodRecommendItem("Овсяная каша", "Oatmeal porridge", listOf("овес", "овсян", "oat"), "cereal"),
                FoodRecommendItem("Мясо кролика", "Rabbit meat", listOf("кролик", "rabbit"), "meat"),
                FoodRecommendItem("Пюре из груши", "Pear puree", listOf("груша", "pear"), "pear"),
                FoodRecommendItem("Творог детский", "Baby cottage cheese", listOf("творог", " cottage", "молоко", "milk"), "dairy"),
                FoodRecommendItem("Банан", "Banana", listOf("банан", "banana"), "banana")
            ),
            forbidden = listOf(
                FoodRecommendItem("Мёд", "Honey", listOf("мед", "мёд", "honey"), "honey"),
                FoodRecommendItem("Шоколад", "Chocolate", listOf("шоколад", "какао", "chocolate", "cocoa"), "chocolate"),
                FoodRecommendItem("Грибы", "Mushrooms", listOf("гриб", "mushroom"), "mushroom"),
                FoodRecommendItem("Копчёности", "Smoked meats", listOf("копч", "smoked"), "meat")
            )
        ),
        AgeNutritionData(
            ageInterval = "1-3",
            recommended = listOf(
                FoodRecommendItem("Куриный суп", "Chicken soup", listOf("куриц", "chicken"), "soup"),
                FoodRecommendItem("Паровые котлеты", "Steamed meatballs", listOf("мясо", "meat", "говяд", "beef"), "meat"),
                FoodRecommendItem("Йогурт без сахара", "Plain yogurt", listOf("йогурт", "yogurt", "молоко", "milk"), "dairy"),
                FoodRecommendItem("Фрикадельки из рыбы", "Fish meatballs", listOf("рыба", "fish"), "fish"),
                FoodRecommendItem("Ягоды сезона", "Seasonal berries", listOf("ягод", "berry", "клубник", "strawber"), "berry")
            ),
            forbidden = listOf(
                FoodRecommendItem("Кока-кола и газировки", "Soda drinks", listOf("кола", "газир", "soda", "coke"), "soda"),
                FoodRecommendItem("Фастфуд", "Fast food", listOf("бургер", "чипс", "картоф", "fastfood"), "fastfood"),
                FoodRecommendItem("Колбасы", "Sausages", listOf("колбас", "сосис", "sausage"), "meat"),
                FoodRecommendItem("Острые специи", "Spicy seasonings", listOf("остр", "перц", "spicy", "pepper"), "spice")
            )
        ),
        AgeNutritionData(
            ageInterval = "3+",
            recommended = listOf(
                FoodRecommendItem("Запеченная рыба", "Baked fish", listOf("рыба", "fish"), "fish"),
                FoodRecommendItem("Овощной салат", "Vegetable salad", listOf("овощ", "vegetable"), "salad"),
                FoodRecommendItem("Омлет", "Omelette", listOf("яйцо", "яйца", "egg"), "egg"),
                FoodRecommendItem("Суп-пюре из тыквы", "Pumpkin soup", listOf("тыкв", "pumpkin"), "soup"),
                FoodRecommendItem("Творожная запеканка", "Cottage cheese bake", listOf("творог", "cottage", "молоко", "milk"), "dairy")
            ),
            forbidden = listOf(
                FoodRecommendItem("Чипсы и сухарики", "Chips & crackers", listOf("чипс", "chips", "сухар"), "snacks"),
                FoodRecommendItem("Энергетические напитки", "Energy drinks", listOf("энерг", "energy"), "soda"),
                FoodRecommendItem("Майонез", "Mayonnaise", listOf("майонез", "mayo"), "sauce"),
                FoodRecommendItem("Полуфабрикаты", "Processed food", listOf("полуфабр", "processed"), "fastfood")
            )
        )
    )

    val medicines = listOf(
        MedicineItem(
            name = "Парацетамол (Paracetamol)",
            categoryRu = "Жаропонижающее, анальгетик",
            categoryEn = "Antipyretic, analgesic",
            ageLimitRu = "С 3 месяцев",
            ageLimitEn = "From 3 months",
            dosageRu = "10-15 мг/кг массы тела ребенка за один прием. Интервал между приемами не менее 4-6 часов.",
            dosageEn = "10-15 mg/kg body weight per single dose. Minimum interval between doses is 4-6 hours.",
            contraindicationsRu = "Гиперчувствительность, возраст до 3 месяцев, тяжелые нарушения функции печени или почек.",
            contraindicationsEn = "Hypersensitivity, age under 3 months, severe liver or kidney impairment."
        ),
        MedicineItem(
            name = "Ибупрофен (Ibuprofen / Нурофен)",
            categoryRu = "Жаропонижающее, противовоспалительное",
            categoryEn = "Antipyretic, anti-inflammatory",
            ageLimitRu = "С 6 месяцев (вес > 5 кг)",
            ageLimitEn = "From 6 months (weight > 5 kg)",
            dosageRu = "5-10 мг/кг разово. Не более 3-4 раз в сутки. Суточная доза не должна превышать 30 мг/кг.",
            dosageEn = "5-10 mg/kg per dose. Max 3-4 times per day. Absolute daily dosage limit is 30 mg/kg.",
            contraindicationsRu = "Бронхиальная астма, язва желудка, возраст до 6 месяцев, тяжелая почечная или сердечная недостаточность.",
            contraindicationsEn = "Bronchial asthma, peptic ulcers, age under 6 months, severe kidney or heart failure."
        ),
        MedicineItem(
            name = "Витамин D3 (Cholecalciferol)",
            categoryRu = "Витамины, профилактика рахита",
            categoryEn = "Vitamins, rickets prevention",
            ageLimitRu = "С рождения (с 2-4 недель)",
            ageLimitEn = "From birth (from 2-4 weeks)",
            dosageRu = "Профилактическая доза: 500-1000 МЕ ежедневно (1-2 капли). В лечебных дозах строго по назначению врача.",
            dosageEn = "Preventative dose: 500-1000 IU daily (1-2 drops). Therapeutic dosages only on doctor's prescription.",
            contraindicationsRu = "Гиперкальциемия, гиперкальциурия, почечная остеодистрофия с гиперфосфатемией.",
            contraindicationsEn = "Hypercalcemia, hypercalciuria, renal osteodystrophy with hyperphosphatemia."
        ),
        MedicineItem(
            name = "Аква Марис (Физиологический раствор / Салин)",
            categoryRu = "Средство от насморка, гигиена полости носа",
            categoryEn = "Nasal hygiene, saline spray",
            ageLimitRu = "С рождения",
            ageLimitEn = "From birth",
            dosageRu = "Детям до 1 года - по 1-2 капли в каждый носовой ход 2-4 раза в день (для размягчения корочек и гигиены).",
            dosageEn = "Children under 1 year - 1-2 drops in each sinus 2-4 times a day (to soften crusts and maintain hygiene).",
            contraindicationsRu = "Повышенная чувствительность к морской воде (для некоторых разновидностей).",
            contraindicationsEn = "Hypersensitivity to natural seawater (for specific variations)."
        ),
        MedicineItem(
            name = "Фенистил (Dimetindene)",
            categoryRu = "Противоаллергическое, антигистаминное",
            categoryEn = "Antiallergic, antihistamine",
            ageLimitRu = "С 1 месяца",
            ageLimitEn = "From 1 month",
            dosageRu = "Детям от 1 мес до 1 года по 3-10 капель 3 раза в день. По назначению врача.",
            dosageEn = "Children from 1 month to 1 year 3-10 drops 3 times a day. Only as directed by physician.",
            contraindicationsRu = "Закрытоугольная глаукома, возраст до 1 месяца (особенно недоношенные), гиперчувствительность.",
            contraindicationsEn = "Angle-closure glaucoma, age under 1 month (especially premature babies), hypersensitivity."
        )
    )
}
