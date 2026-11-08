package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class UserRole {
    CUSTOMER, STAFF, OWNER
}

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val phone: String = "",
    val address: String = "",
    val role: String = "customer",
    val loyaltyPoints: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun isOwner(): Boolean = role.equals("owner", ignoreCase = true) || email.equals("chahidahmed80@gmail.com", ignoreCase = true)
    fun isStaff(): Boolean = role.equals("staff", ignoreCase = true) || isOwner()
}
