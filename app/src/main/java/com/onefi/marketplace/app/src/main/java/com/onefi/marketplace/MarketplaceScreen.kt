package com.onefi.marketplace

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MarketplaceScreen() {
    var productList by remember { mutableStateOf<List<Product>?>(null) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    // Dynamic retrieval handling with loading state
    LaunchedEffect(Unit) {
        MarketplaceRepository.fetchProducts().collect {
            productList = it
        }
    }

    if (selectedProduct != null) {
        ProductDetailScreen(
            product = selectedProduct!!,
            onBack = { selectedProduct = null }
        )
    } else {
        if (productList == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productList!!) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedProduct = product },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(product.brand, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Price: ₹${product.price.toInt()}", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "Starting EMI: ₹${product.emiPlans.firstOrNull()?.monthlyInstallment?.toInt()}/mo",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(product: Product, onBack: () -> Unit) {
    var selectedVariant by remember { mutableStateOf(product.variants.first()) }
    var selectedPlan by remember { mutableStateOf(product.emiPlans.first()) }
    var planConfirmed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedButton(onClick = onBack) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(product.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(product.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Total Price: ₹${product.price.toInt()}", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Available Variants:", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 4.dp)) {
            product.variants.forEach { variant ->
                FilterChip(
                    selected = selectedVariant == variant,
                    onClick = { selectedVariant = variant },
                    label = { Text(variant) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select EMI Plan:", fontWeight = FontWeight.SemiBold)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
            product.emiPlans.forEach { plan ->
                val isSelected = selectedPlan == plan
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedPlan = plan }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = isSelected, onClick = { selectedPlan = plan })
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("₹${plan.monthlyInstallment.toInt()} / month (${plan.tenureMonths} Months)", fontWeight = FontWeight.Bold)
                            Text(
                                if (plan.isNoCost) "0% Interest (No Cost EMI)" else "${plan.interestRate}% Interest",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (planConfirmed) {
            Text(
                "Selected: ${selectedPlan.tenureMonths}-month EMI plan for $selectedVariant.",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = { planConfirmed = true },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Proceed with ${selectedPlan.tenureMonths}-Month Plan")
        }
    }
}