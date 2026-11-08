package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.data.model.OrderItem
import com.example.data.model.Product

data class CartLineItem(
    val id: String,
    val product: Product,
    val selectedOption: String,
    val selectedExtras: List<String>,
    val notes: String,
    val unitPrice: Double,
    var quantity: Int
) {
    val totalPrice: Double get() = unitPrice * quantity

    fun toOrderItem(): OrderItem = OrderItem(
        productId = product.id,
        productName = product.name,
        quantity = quantity,
        unitPrice = unitPrice,
        selectedOption = selectedOption,
        selectedExtras = selectedExtras,
        itemNotes = notes,
        totalPrice = totalPrice
    )
}

class CartManager {
    val items = mutableStateListOf<CartLineItem>()
    var deliveryFee by mutableStateOf(10.0)
    var isDelivery by mutableStateOf(false)
    var appliedRewardDiscount by mutableStateOf(0.0)

    val itemCount: Int get() = items.sumOf { it.quantity }
    val subtotal: Double get() = items.sumOf { it.totalPrice }
    val finalDeliveryFee: Double get() = if (isDelivery && subtotal > 0) deliveryFee else 0.0
    val total: Double get() = (subtotal + finalDeliveryFee - appliedRewardDiscount).coerceAtLeast(0.0)

    fun addItem(product: Product, option: String, extras: List<String>, notes: String, unitPrice: Double, quantity: Int = 1) {
        val existingIndex = items.indexOfFirst {
            it.product.id == product.id && it.selectedOption == option && it.selectedExtras == extras && it.notes == notes
        }
        if (existingIndex >= 0) {
            val item = items[existingIndex]
            items[existingIndex] = item.copy(quantity = item.quantity + quantity)
        } else {
            items.add(
                CartLineItem(
                    id = "${product.id}_${System.currentTimeMillis()}",
                    product = product,
                    selectedOption = option,
                    selectedExtras = extras,
                    notes = notes,
                    unitPrice = unitPrice,
                    quantity = quantity
                )
            )
        }
    }

    fun updateQuantity(itemId: String, newQty: Int) {
        val index = items.indexOfFirst { it.id == itemId }
        if (index >= 0) {
            if (newQty <= 0) {
                items.removeAt(index)
            } else {
                val item = items[index]
                items[index] = item.copy(quantity = newQty)
            }
        }
    }

    fun removeItem(itemId: String) {
        items.removeAll { it.id == itemId }
    }

    fun clear() {
        items.clear()
        appliedRewardDiscount = 0.0
    }
}

val LocalCartManager = compositionLocalOf { CartManager() }
