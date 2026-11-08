package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class LoyaltyReward(
    @DocumentId val id: String = "",
    val title: String = "",
    val pointsRequired: Long = 0L,
    val description: String = "",
    val discountAmount: Double = 0.0,
    val isAvailable: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class LoyaltyTransaction(
    @DocumentId val id: String = "",
    val userId: String = "",
    val points: Long = 0L,
    val type: String = "EARNED", // EARNED, REDEEMED
    val orderId: String = "",
    val description: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
