package com.example.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(val code: String, val displayName: String, val layoutDirection: LayoutDirection) {
    FRENCH("fr", "Français", LayoutDirection.Ltr),
    ARABIC("ar", "العربية", LayoutDirection.Rtl),
    ENGLISH("en", "English", LayoutDirection.Ltr)
}

val LocalAppLanguage = compositionLocalOf { AppLanguage.FRENCH }

object Strings {
    @Composable
    fun get(key: String, lang: AppLanguage = LocalAppLanguage.current): String {
        return translations[key]?.get(lang) ?: key
    }

    private val translations = mapOf(
        "app_title" to mapOf(
            AppLanguage.FRENCH to "Pedro's Snack",
            AppLanguage.ARABIC to "بيدروس سناك",
            AppLanguage.ENGLISH to "Pedro's Snack"
        ),
        "tagline" to mapOf(
            AppLanguage.FRENCH to "Restaurant Premium & Tacos au Feu de Bois | Khouribga",
            AppLanguage.ARABIC to "مطعم بريميوم وطاكوس على الحطب | خريبكة",
            AppLanguage.ENGLISH to "Premium Restaurant & Wood-Fired Tacos | Khouribga"
        ),
        "nav_home" to mapOf(
            AppLanguage.FRENCH to "Accueil",
            AppLanguage.ARABIC to "الرئيسية",
            AppLanguage.ENGLISH to "Home"
        ),
        "nav_menu" to mapOf(
            AppLanguage.FRENCH to "Menu",
            AppLanguage.ARABIC to "قائمة الطعام",
            AppLanguage.ENGLISH to "Menu"
        ),
        "nav_offers" to mapOf(
            AppLanguage.FRENCH to "Offres",
            AppLanguage.ARABIC to "العروض",
            AppLanguage.ENGLISH to "Offers"
        ),
        "nav_orders" to mapOf(
            AppLanguage.FRENCH to "Mes Commandes",
            AppLanguage.ARABIC to "طلباتي",
            AppLanguage.ENGLISH to "My Orders"
        ),
        "nav_account" to mapOf(
            AppLanguage.FRENCH to "Compte",
            AppLanguage.ARABIC to "حسابي",
            AppLanguage.ENGLISH to "Account"
        ),
        "nav_admin" to mapOf(
            AppLanguage.FRENCH to "Administration",
            AppLanguage.ARABIC to "لوحة الإدارة",
            AppLanguage.ENGLISH to "Admin"
        ),
        "order_now" to mapOf(
            AppLanguage.FRENCH to "Commander Maintenant",
            AppLanguage.ARABIC to "اطلب الآن",
            AppLanguage.ENGLISH to "Order Now"
        ),
        "view_menu" to mapOf(
            AppLanguage.FRENCH to "Découvrir le Menu",
            AppLanguage.ARABIC to "استكشف القائمة",
            AppLanguage.ENGLISH to "View Menu"
        ),
        "delivery_banner" to mapOf(
            AppLanguage.FRENCH to "Livraison 7j/7 sur Khouribga (12h00 - 00h00)",
            AppLanguage.ARABIC to "توصيل 7/7 بخريبكة (12:00 - 00:00)",
            AppLanguage.ENGLISH to "7/7 Delivery in Khouribga (12:00 - 00:00)"
        ),
        "featured_specialties" to mapOf(
            AppLanguage.FRENCH to "Spécialités Vedettes",
            AppLanguage.ARABIC to "الأطباق المميزة",
            AppLanguage.ENGLISH to "Featured Specialties"
        ),
        "best_sellers" to mapOf(
            AppLanguage.FRENCH to "Nos Meilleurs Succès",
            AppLanguage.ARABIC to "الأكثر طلباً",
            AppLanguage.ENGLISH to "Best Sellers"
        ),
        "wood_fired_tag" to mapOf(
            AppLanguage.FRENCH to "Au Feu de Bois",
            AppLanguage.ARABIC to "على الحطب",
            AppLanguage.ENGLISH to "Wood-Fired"
        ),
        "currency" to mapOf(
            AppLanguage.FRENCH to "DH",
            AppLanguage.ARABIC to "درهم",
            AppLanguage.ENGLISH to "DH"
        ),
        "cart" to mapOf(
            AppLanguage.FRENCH to "Panier",
            AppLanguage.ARABIC to "سلة الطلبات",
            AppLanguage.ENGLISH to "Cart"
        ),
        "add_to_cart" to mapOf(
            AppLanguage.FRENCH to "Ajouter au Panier",
            AppLanguage.ARABIC to "أضف إلى السلة",
            AppLanguage.ENGLISH to "Add to Cart"
        ),
        "cart_empty" to mapOf(
            AppLanguage.FRENCH to "Votre panier est vide",
            AppLanguage.ARABIC to "سلتك فارغة حالياً",
            AppLanguage.ENGLISH to "Your cart is empty"
        ),
        "checkout" to mapOf(
            AppLanguage.FRENCH to "Valider la Commande",
            AppLanguage.ARABIC to "تأكيد الطلب",
            AppLanguage.ENGLISH to "Checkout"
        ),
        "pickup" to mapOf(
            AppLanguage.FRENCH to "À Emporter (Snack)",
            AppLanguage.ARABIC to "استلام من المطعم",
            AppLanguage.ENGLISH to "Pickup"
        ),
        "delivery" to mapOf(
            AppLanguage.FRENCH to "Livraison à Domicile",
            AppLanguage.ARABIC to "توصيل للمنزل",
            AppLanguage.ENGLISH to "Delivery"
        ),
        "full_name" to mapOf(
            AppLanguage.FRENCH to "Nom & Prénom",
            AppLanguage.ARABIC to "الاسم الكامل",
            AppLanguage.ENGLISH to "Full Name"
        ),
        "phone_number" to mapOf(
            AppLanguage.FRENCH to "Numéro de Téléphone",
            AppLanguage.ARABIC to "رقم الهاتف",
            AppLanguage.ENGLISH to "Phone Number"
        ),
        "delivery_address" to mapOf(
            AppLanguage.FRENCH to "Adresse de Livraison (Khouribga)",
            AppLanguage.ARABIC to "عنوان التوصيل (خريبكة)",
            AppLanguage.ENGLISH to "Delivery Address (Khouribga)"
        ),
        "order_notes" to mapOf(
            AppLanguage.FRENCH to "Instructions spéciales / Sauces",
            AppLanguage.ARABIC to "ملاحظات إضافية أو الصلصات",
            AppLanguage.ENGLISH to "Special instructions / Sauces"
        ),
        "whatsapp_order" to mapOf(
            AppLanguage.FRENCH to "Commander via WhatsApp (+212 6 56 45 70 14)",
            AppLanguage.ARABIC to "طلب عبر الواتساب (+212 6 56 45 70 14)",
            AppLanguage.ENGLISH to "Order via WhatsApp (+212 6 56 45 70 14)"
        ),
        "loyalty_points" to mapOf(
            AppLanguage.FRENCH to "Points Fidélité Pedro's",
            AppLanguage.ARABIC to "نقاط الولاء بيدروس",
            AppLanguage.ENGLISH to "Pedro's Loyalty Points"
        ),
        "points_earned_desc" to mapOf(
            AppLanguage.FRENCH to "Gagnez 10% de points sur chaque commande validée",
            AppLanguage.ARABIC to "اكسب 10% نقاط مع كل طلب مؤكد",
            AppLanguage.ENGLISH to "Earn 10% points on every confirmed order"
        ),
        "restaurant_address" to mapOf(
            AppLanguage.FRENCH to "91 Boulevard Allal Elfassi, Nahda, Khouribga, Maroc",
            AppLanguage.ARABIC to "91 شارع علال الفاسي، النهضة، خريبكة، المغرب",
            AppLanguage.ENGLISH to "91 Boulevard Allal Elfassi, Nahda, Khouribga, Morocco"
        ),
        "open_in_maps" to mapOf(
            AppLanguage.FRENCH to "Ouvrir dans Google Maps",
            AppLanguage.ARABIC to "فتح في خرائط جوجل",
            AppLanguage.ENGLISH to "Open in Google Maps"
        ),
        "call_us" to mapOf(
            AppLanguage.FRENCH to "Appeler le Restaurant",
            AppLanguage.ARABIC to "اتصل بالمطعم",
            AppLanguage.ENGLISH to "Call Restaurant"
        ),
        "customization" to mapOf(
            AppLanguage.FRENCH to "Personnalisation",
            AppLanguage.ARABIC to "خيارات الطلب",
            AppLanguage.ENGLISH to "Customization"
        ),
        "reviews" to mapOf(
            AppLanguage.FRENCH to "Avis Clients",
            AppLanguage.ARABIC to "آراء الزبائن",
            AppLanguage.ENGLISH to "Customer Reviews"
        ),
        "theme_customizer" to mapOf(
            AppLanguage.FRENCH to "Personnalisation du Thème",
            AppLanguage.ARABIC to "تخصيص المظهر",
            AppLanguage.ENGLISH to "Theme Customizer"
        )
    )
}
