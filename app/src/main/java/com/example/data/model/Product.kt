package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class MenuCategory(val displayName: String, val iconKey: String) {
    BURGERS("Burgers", "fastfood"),
    CHAWARMA("Chawarma", "kebab_dining"),
    PASTICCIO("Pasticcio", "dinner_dining"),
    SALADS("Salads", "eco"),
    PIZZA("Pizza", "local_pizza"),
    BAGUETTINA("Baguettina", "bakery_dining"),
    TACOS("Tacos au Feu de Bois", "lunch_dining"),
    PASTICOS("Pasticos", "restaurant"),
    NUGGETS("Nuggets", "set_meal"),
    WRAPS("Wraps", "wrap_text"),
    PLATS("Plats (12:00–20:00)", "flatware"),
    MENU_KIDS("Menu Kids", "child_care"),
    DRINKS_DESSERTS("Drinks & Desserts", "local_bar"),
    SIDES_EXTRAS("Sides & Extras", "add_circle_outline"),
    LUNCH_OFFERS("Lunch Offers (12:00–18:00)", "local_offer")
}

@IgnoreExtraProperties
data class Product(
    @DocumentId val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val mghamessPrice: Double = 0.0,
    val alonePrice: Double = 0.0,
    val withFriesPrice: Double = 0.0,
    val gratinePrice: Double = 0.0,
    val description: String = "",
    val isAvailable: Boolean = true,
    val isFeatured: Boolean = false,
    val isBestSeller: Boolean = false,
    val needsConfirmation: Boolean = false, // Flagged for owner confirmation before publishing
    val confirmationNote: String = "",
    val options: List<String> = emptyList(),
    val accompaniment: String = "", // e.g. "fries", "fries or pasta", "pasta, fries & salad"
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
