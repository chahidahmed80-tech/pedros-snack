package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class OrderStatus(val label: String) {
    NEW("New Order"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY("Ready"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

enum class OrderType(val label: String) {
    PICKUP("Pickup"),
    DELIVERY("Delivery")
}

enum class PaymentMethod(val label: String) {
    CASH_ON_DELIVERY("Cash on Delivery"),
    CASH_AT_PICKUP("Cash at Pickup")
}

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0,
    val selectedOption: String = "", // e.g. "Normal", "Mghamess", "Seul", "+Frite", "Gratiné"
    val selectedExtras: List<String> = emptyList(),
    val itemNotes: String = "",
    val totalPrice: Double = 0.0
)

@IgnoreExtraProperties
data class Order(
    @DocumentId val id: String = "",
    val userId: String = "",
    val orderNumber: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val orderType: String = OrderType.PICKUP.name,
    val deliveryAddress: String = "",
    val items: List<OrderItem> = emptyList(),
    val subtotalAmount: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val discountAmount: Double = 0.0,
    val totalAmount: Double = 0.0,
    val status: String = OrderStatus.NEW.name,
    val notes: String = "",
    val cancellationReason: String = "",
    val paymentMethod: String = PaymentMethod.CASH_AT_PICKUP.name,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
