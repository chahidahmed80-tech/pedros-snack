package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.MenuCategory
import com.example.data.model.Order
import com.example.data.model.OrderItem
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.RestaurantSettings
import com.example.data.model.Review
import com.example.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class PedroRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing authenticated resources.")
    }

    fun getCurrentUserUid(): String? = auth.currentUser?.uid
    fun getCurrentUserEmail(): String? = auth.currentUser?.email
    fun getCurrentUserDisplayName(): String? = auth.currentUser?.displayName

    // ==========================================
    // INITIAL ORIGINAL MENU DATA SEEDING
    // ==========================================
    val originalInitialProducts: List<Product> = listOf(
        // CATEGORY A — BURGERS (Normal & Mghamess)
        Product(id = "b1", name = "L'Original", category = "Burgers", price = 37.0, mghamessPrice = 52.0, description = "Signature Pedro's burger with gourmet sauce & fresh toppings", isFeatured = true, isBestSeller = true),
        Product(id = "b2", name = "Cheese Burger", category = "Burgers", price = 37.0, mghamessPrice = 52.0, description = "Classic beef burger with melted cheddar & Pedro's relish", isBestSeller = true),
        Product(id = "b3", name = "Chicken Burger", category = "Burgers", price = 37.0, mghamessPrice = 52.0, description = "Crispy golden fried chicken breast burger"),
        Product(id = "b4", name = "Double Cheese", category = "Burgers", price = 43.0, mghamessPrice = 58.0, description = "Double juicy beef patty loaded with double cheddar"),
        Product(id = "b5", name = "Monster Pedro's", category = "Burgers", price = 50.0, mghamessPrice = 65.0, description = "Massive multi-patty specialty burger with smoked beef & melted cheeses", isFeatured = true),

        // CATEGORY B — CHAWARMA (Accompaniment: fries)
        Product(id = "ch1", name = "Chawarma Normal", category = "Chawarma", price = 32.0, description = "Authentic marinated chicken chawarma served with fries", accompaniment = "Fries"),
        Product(id = "ch2", name = "Chawarma Double", category = "Chawarma", price = 39.0, description = "Double portion marinated chicken chawarma served with fries", accompaniment = "Fries"),
        Product(id = "ch3", name = "Chawarma Extra (Sauce Fromagère)", category = "Chawarma", price = 45.0, description = "Chawarma loaded with Pedro's rich warm cheese sauce & fries", accompaniment = "Fries", isBestSeller = true),

        // CATEGORY C — PASTICCIO (Accompaniment: fries or pasta)
        Product(id = "pa1", name = "Pasticcio Viande Hachée Poulet", category = "Pasticcio", price = 27.0, description = "Baked pasta or fries gratiné with spiced minced chicken", accompaniment = "Fries or Pasta"),
        Product(id = "pa2", name = "Pasticcio Poulet", category = "Pasticcio", price = 30.0, description = "Creamy baked dish with tender chicken cubes & mozzarella", accompaniment = "Fries or Pasta"),
        Product(id = "pa3", name = "Pasticcio Saucisse", category = "Pasticcio", price = 30.0, description = "Oven baked pasticcio with sliced savory sausages", accompaniment = "Fries or Pasta"),
        Product(id = "pa4", name = "Pasticcio Charcuterie", category = "Pasticcio", price = 30.0, description = "Oven baked pasticcio with deli charcuterie & melted cheese", accompaniment = "Fries or Pasta"),
        Product(id = "pa5", name = "Pasticcio Viande Hachée Boeuf", category = "Pasticcio", price = 35.0, description = "Rich gratiné with seasoned minced beef", accompaniment = "Fries or Pasta"),
        Product(id = "pa6", name = "Pasticcio Mixte", category = "Pasticcio", price = 37.0, description = "Chef's combination of meats baked in golden cheese", accompaniment = "Fries or Pasta", isBestSeller = true),

        // CATEGORY D — SALADS
        Product(id = "sa1", name = "Salade Marocain", category = "Salads", price = 15.0, description = "Crisp diced tomatoes, cucumber, sweet onions and Moroccan herbs"),
        Product(id = "sa2", name = "Salade Italiano", category = "Salads", price = 25.0, description = "Italian salad with garden greens, mozzarella, olives & vinaigrette"),

        // CATEGORY E — PIZZA (26 cm / 29 cm)
        Product(id = "pi1", name = "Pizza Margarita", category = "Pizza", price = 24.0, description = "San Marzano tomato sauce, mozzarella & fresh basil (26 cm)"),
        Product(id = "pi2", name = "Pizza Végétarienne", category = "Pizza", price = 24.0, description = "Fresh bell peppers, mushrooms, onions, olives & mozzarella"),
        Product(id = "pi3", name = "Pizza Thon", category = "Pizza", price = 30.0, description = "Flaked tuna, onions, olives & melted mozzarella"),
        Product(id = "pi4", name = "Pizza Poulet", category = "Pizza", price = 30.0, description = "Herb grilled chicken, mozzarella & tomato sauce"),
        Product(id = "pi5", name = "Pizza Charcuterie", category = "Pizza", price = 30.0, description = "Smoked charcuterie, mozzarella & herbs"),
        Product(id = "pi6", name = "Pizza Viande Hachée", category = "Pizza", price = 30.0, description = "Spiced minced beef, tomato sauce & melted cheese"),
        Product(id = "pi7", name = "Pizza Chawarma", category = "Pizza", price = 30.0, description = "Chawarma chicken slices, white sauce & mozzarella"),
        Product(id = "pi8", name = "Pizza Pepperoni", category = "Pizza", price = 37.0, description = "Generous beef pepperoni & melted mozzarella"),
        Product(id = "pi9", name = "Pizza 4 Fromage", category = "Pizza", price = 37.0, description = "Mozzarella, cheddar, blue cheese & emmental blend"),
        Product(id = "pi10", name = "Pizza Quatre Saisons", category = "Pizza", price = 39.0, description = "Four distinct quarters with mushrooms, artichoke/peppers, meats"),
        Product(id = "pi11", name = "Pizza Pedro's — Taille 29", category = "Pizza", price = 60.0, description = "Large 29 cm signature pizza loaded with house toppings & crust cheese", isFeatured = true, needsConfirmation = true, confirmationNote = "Please confirm +5 DH optional marking from original menu photo"),

        // CATEGORY F — BAGUETTINA
        Product(id = "bg1", name = "Baguettina Viande Hachée Poulet", category = "Baguettina", price = 36.0, description = "Crispy French mini-baguette stuffed with seasoned minced chicken"),
        Product(id = "bg2", name = "Baguettina Saucisse", category = "Baguettina", price = 40.0, description = "Baguettina filled with sautéed sausages & sauces"),
        Product(id = "bg3", name = "Baguettina Poulet", category = "Baguettina", price = 40.0, description = "Baguettina filled with marinated chicken breast"),
        Product(id = "bg4", name = "Baguettina Charcuterie", category = "Baguettina", price = 40.0, description = "Baguettina filled with sliced fine charcuterie"),
        Product(id = "bg5", name = "Baguettina Viande Hachée Boeuf", category = "Baguettina", price = 42.0, description = "Baguettina filled with savory ground beef & onions"),
        Product(id = "bg6", name = "Baguettina Nuggets", category = "Baguettina", price = 42.0, description = "Baguettina with crunchy chicken nuggets & melted cheese"),
        Product(id = "bg7", name = "Baguettina Crispy Chicken", category = "Baguettina", price = 42.0, description = "Baguettina with seasoned crunchy crispy chicken strips"),
        Product(id = "bg8", name = "Baguettina Mixte", category = "Baguettina", price = 42.0, description = "Baguettina with combination of meats & cheese"),
        Product(id = "bg9", name = "Baguettina Pedro's", category = "Baguettina", price = 50.0, description = "Chef Pedro's loaded specialty baguettina", isFeatured = true),

        // CATEGORY G — TACOS (Prepared au feu de bois. Options: Seul, +Frite, Gratiné)
        Product(id = "tc1", name = "Tacos Viande Hachée de Poulet", category = "Tacos au Feu de Bois", price = 35.0, alonePrice = 32.0, withFriesPrice = 35.0, gratinePrice = 45.0, description = "Wood-fired folded tacos with seasoned chicken mince & cheese sauce"),
        Product(id = "tc2", name = "Tacos Poulet", category = "Tacos au Feu de Bois", price = 38.0, alonePrice = 35.0, withFriesPrice = 38.0, gratinePrice = 48.0, description = "Wood-fired folded tacos with marinated chicken cubes & cheese sauce", isBestSeller = true),
        Product(id = "tc3", name = "Tacos Saucisse", category = "Tacos au Feu de Bois", price = 38.0, alonePrice = 35.0, withFriesPrice = 38.0, gratinePrice = 48.0, description = "Wood-fired folded tacos with tasty sliced sausages"),
        Product(id = "tc4", name = "Tacos Nuggets", category = "Tacos au Feu de Bois", price = 38.0, alonePrice = 35.0, withFriesPrice = 38.0, gratinePrice = 48.0, description = "Wood-fired tacos with crunchy chicken nuggets inside"),
        Product(id = "tc5", name = "Tacos Cordon Bleu", category = "Tacos au Feu de Bois", price = 38.0, alonePrice = 35.0, withFriesPrice = 38.0, gratinePrice = 48.0, description = "Wood-fired tacos with crispy cordon bleu slices"),
        Product(id = "tc6", name = "Tacos Chawarma", category = "Tacos au Feu de Bois", price = 38.0, alonePrice = 35.0, withFriesPrice = 38.0, gratinePrice = 48.0, description = "Wood-fired tacos with spiced chawarma chicken"),
        Product(id = "tc7", name = "Tacos Chicken Crispy", category = "Tacos au Feu de Bois", price = 40.0, alonePrice = 37.0, withFriesPrice = 40.0, gratinePrice = 50.0, description = "Wood-fired tacos with extra crunchy crispy chicken"),
        Product(id = "tc8", name = "Tacos Viande Hachée Boeuf", category = "Tacos au Feu de Bois", price = 40.0, alonePrice = 37.0, withFriesPrice = 40.0, gratinePrice = 50.0, description = "Wood-fired tacos with savory seasoned beef mince"),
        Product(id = "tc9", name = "Tacos Mixte", category = "Tacos au Feu de Bois", price = 40.0, alonePrice = 37.0, withFriesPrice = 40.0, gratinePrice = 50.0, description = "Wood-fired tacos with two choices of meats & cheese"),
        Product(id = "tc10", name = "Tacos XL Mixte", category = "Tacos au Feu de Bois", price = 49.0, alonePrice = 46.0, withFriesPrice = 49.0, gratinePrice = 59.0, description = "Giant XL wood-fired double tortilla tacos with mixed fillings"),
        Product(id = "tc11", name = "Tacos Pedro's", category = "Tacos au Feu de Bois", price = 50.0, alonePrice = 47.0, withFriesPrice = 50.0, gratinePrice = 60.0, description = "The Pedro's house special wood-fired tacos", isFeatured = true),
        Product(id = "tc12", name = "Tacos Pedro's Royal", category = "Tacos au Feu de Bois", price = 58.0, alonePrice = 55.0, withFriesPrice = 58.0, gratinePrice = 68.0, description = "Royal oversized wood-fired tacos loaded with meats & cheeses", needsConfirmation = true, confirmationNote = "Flagged for owner confirmation before publishing"),
        Product(id = "tc13", name = "Tacos Extreme", category = "Tacos au Feu de Bois", price = 145.0, alonePrice = 135.0, withFriesPrice = 145.0, gratinePrice = 150.0, description = "Mega multi-person extreme tacos (Feu de bois)", needsConfirmation = true, confirmationNote = "Flagged for owner confirmation before publishing"),

        // CATEGORY H — PASTICOS
        Product(id = "pt1", name = "Pasticos", category = "Pasticos", price = 55.0, description = "Pedro's specialty baked pasticos gratiné with deluxe fillings", isFeatured = true),

        // CATEGORY I — NUGGETS
        Product(id = "ng1", name = "Nuggets 4 Pieces", category = "Nuggets", price = 20.0, description = "4 pieces of golden crunchy chicken nuggets with dip"),
        Product(id = "ng2", name = "Nuggets 6 Pieces", category = "Nuggets", price = 25.0, description = "6 pieces of golden crunchy chicken nuggets with dip"),
        Product(id = "ng3", name = "Nuggets 8 Pieces", category = "Nuggets", price = 35.0, description = "8 pieces of golden crunchy chicken nuggets with dip"),

        // CATEGORY J — WRAPS
        Product(id = "wr1", name = "Twister Crispy", category = "Wraps", price = 37.0, description = "Crispy chicken strips wrapped in warm tortilla with lettuce & garlic sauce"),
        Product(id = "wr2", name = "Mega Twister", category = "Wraps", price = 43.0, description = "Double portion crispy chicken twister wrap"),
        Product(id = "wr3", name = "Wrap au Choix", category = "Wraps", price = 35.0, description = "Tortilla wrap with your choice of filling & house sauces"),
        Product(id = "wr4", name = "Pedro's L", category = "Wraps", price = 39.0, description = "Large Pedro's style wrap with golden fries", accompaniment = "Fries"),
        Product(id = "wr5", name = "Pedro's XL", category = "Wraps", price = 49.0, description = "Extra large specialty wrap with fries and dipping sauces", accompaniment = "Fries"),

        // CATEGORY K — PLATS (12:00–20:00, with pasta, fries and salad)
        Product(id = "pl1", name = "Plat Saucisse", category = "Plats (12:00–20:00)", price = 50.0, description = "Grilled sausages served with seasoned pasta, crispy fries, and fresh salad", accompaniment = "Pasta, fries & salad"),
        Product(id = "pl2", name = "Plat Poulet", category = "Plats (12:00–20:00)", price = 50.0, description = "Tender grilled chicken plate with pasta, fries, and salad", accompaniment = "Pasta, fries & salad"),
        Product(id = "pl3", name = "Plat Nuggets", category = "Plats (12:00–20:00)", price = 55.0, description = "Golden nuggets plate with pasta, fries, and salad", accompaniment = "Pasta, fries & salad"),
        Product(id = "pl4", name = "Plat Viande Hachée", category = "Plats (12:00–20:00)", price = 55.0, description = "Spiced minced beef steak with pasta, fries, and salad", accompaniment = "Pasta, fries & salad"),
        Product(id = "pl5", name = "Plat Chicken Crispy", category = "Plats (12:00–20:00)", price = 55.0, description = "Crunchy crispy chicken strips served with pasta, fries, and salad", accompaniment = "Pasta, fries & salad"),
        Product(id = "pl6", name = "Plat Emincé de Poulet", category = "Plats (12:00–20:00)", price = 55.0, description = "Sautéed sliced chicken with creamy sauce, pasta, fries, and salad", accompaniment = "Pasta, fries & salad"),
        Product(id = "pl7", name = "Plat Mixte", category = "Plats (12:00–20:00)", price = 60.0, description = "Assortment of grilled meats with pasta, fries, and salad", accompaniment = "Pasta, fries & salad", isBestSeller = true),

        // CATEGORY L — MENU KIDS
        Product(id = "kd1", name = "Menu Kids 1", category = "Menu Kids", price = 40.0, description = "Cheese Burger, 2 pieces nuggets, fries and a drink"),
        Product(id = "kd2", name = "Menu Kids 2", category = "Menu Kids", price = 37.0, description = "Cheesy fries, chicken nuggets, cheese sauce and a drink"),

        // CATEGORY M — DRINKS AND DESSERTS
        Product(id = "dr1", name = "Eau Minérale", category = "Drinks & Desserts", price = 2.0, description = "Fresh mineral water 50cl"),
        Product(id = "dr2", name = "Jus de Citron", category = "Drinks & Desserts", price = 6.0, description = "Chilled freshly squeezed lemon juice"),
        Product(id = "dr3", name = "Boisson Gazeuse 25CL", category = "Drinks & Desserts", price = 6.0, description = "Refreshing soda can (Coca-Cola, Fanta, Sprite, Hawai, Pom's)"),
        Product(id = "dr4", name = "Boisson Gazeuse 33CL", category = "Drinks & Desserts", price = 10.0, description = "Refreshing soda bottle 33CL"),
        Product(id = "dr5", name = "Boissons Spéciales", category = "Drinks & Desserts", price = 15.0, description = "Special iced drinks & smoothies"),
        Product(id = "dr6", name = "Mojito", category = "Drinks & Desserts", price = 15.0, description = "Virgin fresh mint & lime mojito"),
        Product(id = "dr7", name = "Tiramisu", category = "Drinks & Desserts", price = 17.0, description = "Homemade Italian coffee & cocoa tiramisu"),

        // CATEGORY N — SIDES AND EXTRAS
        Product(id = "sd1", name = "Cheesy Fries", category = "Sides & Extras", price = 25.0, description = "Crispy fries drizzled with melted warm cheddar cheese sauce", isBestSeller = true),
        Product(id = "sd2", name = "Barketa", category = "Sides & Extras", price = 35.0, description = "Barketa cheesy boat filled with meats and sauces"),
        Product(id = "sd3", name = "Sauce", category = "Sides & Extras", price = 2.0, description = "Choice of sauce: Algérienne, Samouraï, Biggy, Barbecue, Mayo"),
        Product(id = "sd4", name = "Double Fromage", category = "Sides & Extras", price = 4.0, description = "Extra slice of melted cheddar or mozzarella"),
        Product(id = "sd5", name = "Extra Sauce", category = "Sides & Extras", price = 5.0, description = "Generous portion of house gourmet sauce"),
        Product(id = "sd6", name = "Gratiné", category = "Sides & Extras", price = 10.0, description = "Oven baked golden melted mozzarella crust on any dish"),
        Product(id = "sd7", name = "Double Extra Sauce", category = "Sides & Extras", price = 10.0, description = "Double portion of warm cheese sauce"),

        // CATEGORY O — LUNCH OFFERS (12:00–18:00)
        Product(id = "lo1", name = "Lunch Menu 1", category = "Lunch Offers (12:00–18:00)", price = 50.0, description = "Tacos au choix + Cheesy Fries + Jus de Citron (12:00–18:00)", isFeatured = true),
        Product(id = "lo2", name = "Lunch Menu 2", category = "Lunch Offers (12:00–18:00)", price = 50.0, description = "Burger au choix + Cheesy Fries + Jus de Citron (12:00–18:00)", isFeatured = true),
        Product(id = "lo3", name = "Lunch Menu 3", category = "Lunch Offers (12:00–18:00)", price = 50.0, description = "Baguettina au choix + Cheesy Fries + Jus de Citron (12:00–18:00)", isFeatured = true)
    )

    // ==========================================
    // PRODUCTS OBSERVER
    // ==========================================
    fun observeProducts(): Flow<List<Product>> = flow {
        val path = "products"
        emitAll(
            db.collection(path)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.isEmpty) {
                        originalInitialProducts
                    } else {
                        snapshot.toObjects(Product::class.java)
                    }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    emit(originalInitialProducts)
                }
        )
    }

    suspend fun saveProduct(product: Product) {
        val path = "products/${product.id.ifBlank { db.collection("products").document().id }}"
        val docRef = db.document(path)
        val payload = mapOf(
            "name" to product.name,
            "category" to product.category,
            "price" to product.price,
            "mghamessPrice" to product.mghamessPrice,
            "alonePrice" to product.alonePrice,
            "withFriesPrice" to product.withFriesPrice,
            "gratinePrice" to product.gratinePrice,
            "description" to product.description,
            "isAvailable" to product.isAvailable,
            "isFeatured" to product.isFeatured,
            "isBestSeller" to product.isBestSeller,
            "options" to product.options,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            throw e
        }
    }

    // ==========================================
    // USER PROFILE
    // ==========================================
    fun observeUserProfile(userId: String): Flow<UserProfile?> = flow {
        val path = "users/$userId"
        emitAll(
            db.collection("users").document(userId)
                .snapshots()
                .map { it.toObject(UserProfile::class.java) }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null) {
                        handleFirestoreError(exceptionToLog, OperationType.GET, path)
                    }
                    emit(null)
                }
        )
    }

    suspend fun createOrUpdateProfile(profile: UserProfile) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val payload = mapOf(
            "userId" to uid,
            "email" to profile.email.ifBlank { auth.currentUser?.email.orEmpty() },
            "displayName" to profile.displayName.ifBlank { auth.currentUser?.displayName ?: "Pedro Fan" },
            "phone" to profile.phone,
            "address" to profile.address,
            "role" to if (profile.email.equals("chahidahmed80@gmail.com", ignoreCase = true)) "owner" else profile.role,
            "loyaltyPoints" to profile.loyaltyPoints,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    // ==========================================
    // ORDERS
    // ==========================================
    fun observeUserOrders(userId: String): Flow<List<Order>> = flow {
        val path = "orders"
        emitAll(
            db.collection(path)
                .whereEqualTo("userId", userId)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Order::class.java).sortedByDescending { it.createdAt } }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    emit(emptyList())
                }
        )
    }

    fun observeAllOrders(): Flow<List<Order>> = flow {
        val path = "orders"
        emitAll(
            db.collection(path)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Order::class.java).sortedByDescending { it.createdAt } }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    emit(emptyList())
                }
        )
    }

    suspend fun placeOrder(
        customerName: String,
        customerPhone: String,
        orderType: String,
        deliveryAddress: String,
        notes: String,
        paymentMethod: String,
        items: List<OrderItem>,
        subtotal: Double,
        deliveryFee: Double,
        discount: Double,
        total: Double
    ): Order {
        val uid = requireUserId()
        val docRef = db.collection("orders").document()
        val orderNum = "PEDRO-${Random.nextInt(1000, 9999)}"

        val payload = mapOf(
            "userId" to uid,
            "orderNumber" to orderNum,
            "customerName" to customerName,
            "customerPhone" to customerPhone,
            "orderType" to orderType,
            "deliveryAddress" to deliveryAddress,
            "totalAmount" to total,
            "status" to OrderStatus.NEW.name,
            "notes" to notes,
            "paymentMethod" to paymentMethod,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            docRef.set(payload).await()
            // Record points in user loyalty subcollection
            val pointsEarned = (total * 0.1).toLong().coerceAtLeast(1L)
            val loyaltyRef = db.collection("users").document(uid)
                .collection("loyaltyTransactions").document()
            loyaltyRef.set(
                mapOf(
                    "userId" to uid,
                    "points" to pointsEarned,
                    "type" to "EARNED",
                    "orderId" to docRef.id,
                    "description" to "Points for Order $orderNum",
                    "createdAt" to FieldValue.serverTimestamp()
                )
            ).await()

            return Order(
                id = docRef.id,
                userId = uid,
                orderNumber = orderNum,
                customerName = customerName,
                customerPhone = customerPhone,
                orderType = orderType,
                deliveryAddress = deliveryAddress,
                items = items,
                subtotalAmount = subtotal,
                deliveryFee = deliveryFee,
                discountAmount = discount,
                totalAmount = total,
                status = OrderStatus.NEW.name,
                notes = notes,
                paymentMethod = paymentMethod
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String, cancellationReason: String = "") {
        val docRef = db.collection("orders").document(orderId)
        val payload = mutableMapOf<String, Any>(
            "status" to newStatus,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (cancellationReason.isNotBlank()) {
            payload["cancellationReason"] = cancellationReason
        }
        try {
            docRef.update(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    // ==========================================
    // REVIEWS
    // ==========================================
    fun observeReviews(): Flow<List<Review>> = flow {
        val path = "reviews"
        emitAll(
            db.collection(path)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Review::class.java).sortedByDescending { it.createdAt } }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    emit(emptyList())
                }
        )
    }

    suspend fun submitReview(
        userName: String,
        orderId: String,
        foodRating: Int,
        presentationRating: Int,
        serviceRating: Int,
        deliveryRating: Int,
        overallRating: Int,
        comment: String
    ) {
        val uid = requireUserId()
        val docRef = db.collection("reviews").document()
        val payload = mapOf(
            "userId" to uid,
            "userName" to userName,
            "orderId" to orderId,
            "foodRating" to foodRating,
            "presentationRating" to presentationRating,
            "serviceRating" to serviceRating,
            "deliveryRating" to deliveryRating,
            "overallRating" to overallRating,
            "comment" to comment,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    // ==========================================
    // RESTAURANT SETTINGS
    // ==========================================
    fun observeSettings(): Flow<RestaurantSettings> = flow {
        val path = "restaurantSettings/general"
        emitAll(
            db.document(path)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObject(RestaurantSettings::class.java) ?: RestaurantSettings()
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null) {
                        handleFirestoreError(exceptionToLog, OperationType.GET, path)
                    }
                    emit(RestaurantSettings())
                }
        )
    }

    suspend fun updateSettings(settings: RestaurantSettings) {
        val path = "restaurantSettings/general"
        val docRef = db.document(path)
        val payload = mapOf(
            "restaurantName" to settings.restaurantName,
            "phone" to settings.phone,
            "address" to settings.address,
            "mapsUrl" to settings.mapsUrl,
            "instagramHandle" to settings.instagramHandle,
            "deliveryAvailable" to settings.deliveryAvailable,
            "deliveryFee" to settings.deliveryFee,
            "minOrderDelivery" to settings.minOrderDelivery,
            "openingHours" to settings.openingHours,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            throw e
        }
    }
}
