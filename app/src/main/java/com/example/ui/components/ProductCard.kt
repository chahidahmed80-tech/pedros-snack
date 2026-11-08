package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Product
import com.example.ui.i18n.Strings
import com.example.ui.theme.BrandRedBucket
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary

@Composable
fun ProductCard(
    product: Product,
    onAddToCartClicked: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDetailModal by remember { mutableStateOf(false) }
    val cartManager = LocalCartManager.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
            .clickable { showDetailModal = true },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Food image banner / placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                val drawableId = when {
                    product.category.contains("Tacos", ignoreCase = true) -> R.drawable.img_pedros_tacos
                    product.category.contains("Burger", ignoreCase = true) || product.isFeatured -> R.drawable.img_pedros_hero
                    else -> R.drawable.img_pedros_logo
                }
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentScale = ContentScale.Crop
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                // Badges
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (product.category.contains("Tacos", ignoreCase = true)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandRedBucket,
                            contentColor = Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Feu de Bois", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    if (product.isBestSeller) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldPrimary,
                            contentColor = Color.Black
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Top Seller", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Price pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldPrimary,
                    contentColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    val basePrice = if (product.alonePrice > 0) product.alonePrice else product.price
                    Text(
                        text = "${basePrice.toInt()} DH",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Info section
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (product.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (product.accompaniment.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Accompagnement: ${product.accompaniment}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.mghamessPrice > 0) {
                        Text(
                            text = "Mghamess: ${product.mghamessPrice.toInt()} DH",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else if (product.gratinePrice > 0) {
                        Text(
                            text = "Gratiné: ${product.gratinePrice.toInt()} DH",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Button(
                        onClick = { showDetailModal = true },
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("add_button_${product.id}"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = Strings.get("add_to_cart"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showDetailModal) {
        ProductCustomizationModal(
            product = product,
            onDismiss = { showDetailModal = false },
            onConfirmed = { option, extras, notes, unitPrice, qty ->
                cartManager.addItem(product, option, extras, notes, unitPrice, qty)
                showDetailModal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCustomizationModal(
    product: Product,
    onDismiss: () -> Unit,
    onConfirmed: (option: String, extras: List<String>, notes: String, unitPrice: Double, qty: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Option state
    var selectedOption by remember {
        mutableStateOf(
            when {
                product.category.contains("Burgers", ignoreCase = true) -> "Normal"
                product.category.contains("Tacos", ignoreCase = true) -> if (product.withFriesPrice > 0) "+Frite" else "Seul"
                product.accompaniment.contains("fries or pasta", ignoreCase = true) -> "Frites"
                else -> "Standard"
            }
        )
    }

    val selectedExtras = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }

    // Dynamic price calculation
    val basePrice = when {
        selectedOption == "Mghamess" && product.mghamessPrice > 0 -> product.mghamessPrice
        selectedOption == "Seul" && product.alonePrice > 0 -> product.alonePrice
        selectedOption == "+Frite" && product.withFriesPrice > 0 -> product.withFriesPrice
        selectedOption == "Gratiné" && product.gratinePrice > 0 -> product.gratinePrice
        else -> product.price
    }

    val extrasTotal = selectedExtras.sumOf { extra ->
        when (extra) {
            "Sauce Fromagère" -> 5.0
            "Double Fromage" -> 4.0
            "Gratiné Mozzarella" -> 10.0
            "Extra Sauce" -> 5.0
            else -> 0.0
        }
    }

    val unitPrice = basePrice + extrasTotal
    val totalPrice = unitPrice * quantity

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${product.category} | Pedro's Snack",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Options: Burgers
            if (product.category.contains("Burgers", ignoreCase = true) && product.mghamessPrice > 0) {
                Text("Options de Burger:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOption == "Normal",
                        onClick = { selectedOption = "Normal" },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                    )
                    Text("Normal (${product.price.toInt()} DH)")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = selectedOption == "Mghamess",
                        onClick = { selectedOption = "Mghamess" },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                    )
                    Text("Mghamess (${product.mghamessPrice.toInt()} DH)")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Options: Tacos
            if (product.category.contains("Tacos", ignoreCase = true) && (product.alonePrice > 0 || product.withFriesPrice > 0)) {
                Text("Préparation au Feu de Bois:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "Seul",
                            onClick = { selectedOption = "Seul" },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                        )
                        Text("Seul (${product.alonePrice.toInt()} DH)")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "+Frite",
                            onClick = { selectedOption = "+Frite" },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                        )
                        Text("+ Frite (${product.withFriesPrice.toInt()} DH)")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "Gratiné",
                            onClick = { selectedOption = "Gratiné" },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                        )
                        Text("Gratiné au Four (${product.gratinePrice.toInt()} DH)")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Accompaniment choice if Pasticcio
            if (product.accompaniment.contains("fries or pasta", ignoreCase = true)) {
                Text("Accompagnement au Choix:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOption == "Frites",
                        onClick = { selectedOption = "Frites" },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                    )
                    Text("Frites")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = selectedOption == "Pâtes",
                        onClick = { selectedOption = "Pâtes" },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                    )
                    Text("Pâtes")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Extras & Sauces
            Text("Suppléments & Sauces:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            val availableExtras = listOf(
                "Sauce Fromagère" to 5.0,
                "Double Fromage" to 4.0,
                "Gratiné Mozzarella" to 10.0,
                "Extra Sauce" to 5.0
            )
            availableExtras.forEach { (extraName, extraPrice) ->
                val isChecked = selectedExtras.contains(extraName)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        if (isChecked) selectedExtras.remove(extraName) else selectedExtras.add(extraName)
                    }
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) selectedExtras.add(extraName) else selectedExtras.remove(extraName)
                        },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary)
                    )
                    Text("$extraName (+${extraPrice.toInt()} DH)")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Special notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Instructions spéciales (sans oignon, sauce algérienne...)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity & Confirmation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "$quantity",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Add button with total
                Button(
                    onClick = {
                        onConfirmed(selectedOption, selectedExtras.toList(), notes, unitPrice, quantity)
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("confirm_add_to_cart_button")
                ) {
                    Text(
                        text = "Ajouter • ${totalPrice.toInt()} DH",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
