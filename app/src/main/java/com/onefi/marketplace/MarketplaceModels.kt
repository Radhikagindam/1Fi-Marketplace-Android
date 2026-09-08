package com.onefi.marketplace

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Model for EMI Options & Plans
data class EmiPlan(
    val tenureMonths: Int,
    val monthlyInstallment: Double,
    val interestRate: Double,
    val isNoCost: Boolean = false
)

// Model for Product Details & Variants
data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val variants: List<String>,
    val emiPlans: List<EmiPlan>
)

// Dynamic Mock Repository (avoids hardcoding data in UI)
object MarketplaceRepository {
    private val productCatalog = listOf(
        Product(
            id = "p1",
            name = "Apple iPhone 15 Pro",
            brand = "Apple",
            price = 129900.0,
            imageUrl = "https://placehold.co/400x400/png?text=iPhone+15+Pro",
            description = "Titanium design, A17 Pro chip, and USB-C.",
            variants = listOf("128 GB", "256 GB", "512 GB"),
            emiPlans = listOf(
                EmiPlan(3, 43300.0, 0.0, true),
                EmiPlan(6, 21650.0, 0.0, true),
                EmiPlan(12, 11450.0, 13.5, false)
            )
        ),
        Product(
            id = "p2",
            name = "Samsung Galaxy S24 Ultra",
            brand = "Samsung",
            price = 119999.0,
            imageUrl = "https://placehold.co/400x400/png?text=S24+Ultra",
            description = "Galaxy AI, 200MP camera, built-in S Pen.",
            variants = listOf("256 GB", "512 GB"),
            emiPlans = listOf(
                EmiPlan(3, 39999.0, 0.0, true),
                EmiPlan(6, 20500.0, 5.0, false),
                EmiPlan(9, 14100.0, 9.5, false)
            )
        )
    )

    fun fetchProducts(): Flow<List<Product>> = flow {
        delay(500) // Simulates dynamic API retrieval
        emit(productCatalog)
    }
}