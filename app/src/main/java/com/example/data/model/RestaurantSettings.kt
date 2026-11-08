package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class Review(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val orderId: String = "",
    val foodRating: Int = 5,
    val presentationRating: Int = 5,
    val serviceRating: Int = 5,
    val deliveryRating: Int = 5,
    val overallRating: Int = 5,
    val comment: String = "",
    val reply: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class RestaurantSettings(
    @DocumentId val id: String = "general",
    val restaurantName: String = "Pedro's Snack",
    val phone: String = "+212 6 56 45 70 14",
    val address: String = "91 Boulevard Allal Elfassi, Nahda, Khouribga, Morocco",
    val mapsUrl: String = "https://maps.app.goo.gl/wHPDpPaDUtSqqu2V8",
    val instagramHandle: String = "@pedros.khouribga",
    val instagramUrl: String = "https://instagram.com/pedros.khouribga",
    val deliveryAvailable: Boolean = true,
    val deliveryHours: String = "7/7 Delivery (12:00 – 00:00)",
    val deliveryFee: Double = 10.0,
    val minOrderDelivery: Double = 50.0,
    val openingHours: String = "12:00 – 00:00 (7/7)",
    @ServerTimestamp val updatedAt: Timestamp? = null
)
