package org.expensetrackerui.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import expensetrackerui.composeapp.generated.resources.Res
import expensetrackerui.composeapp.generated.resources.fondo_1
import expensetrackerui.composeapp.generated.resources.fondo_2
import expensetrackerui.composeapp.generated.resources.fondo_3
import expensetrackerui.composeapp.generated.resources.fondo_4
import expensetrackerui.composeapp.generated.resources.fondo_5
import expensetrackerui.composeapp.generated.resources.fondo_6
import expensetrackerui.composeapp.generated.resources.profile_pic
import expensetrackerui.composeapp.generated.resources.tip_ahorro
import expensetrackerui.composeapp.generated.resources.tip_gastos
import expensetrackerui.composeapp.generated.resources.tip_inversion
import expensetrackerui.composeapp.generated.resources.tip_presupuesto
import org.expensetrackerui.data.model.BudgetSummary
import org.expensetrackerui.data.model.CategorySpending
import org.expensetrackerui.data.model.FinancialTip
import org.expensetrackerui.data.model.PaymentMethodSpending
import org.expensetrackerui.data.model.SpendingItem
import org.expensetrackerui.data.model.Transaction
import org.expensetrackerui.util.CurrencyFormatter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

val sampleBudget = BudgetSummary(totalSpent = 1523.56, totalIncome = 1523.00)
val samplePaymentMethodsSpending = listOf(
    PaymentMethodSpending("Banamex Oro", 500.00, 1534.56),
    PaymentMethodSpending("BBVA Azul", 400.00, 1534.56),
    PaymentMethodSpending("Banamex Débito (priority)", 334.56, 1534.56),
    PaymentMethodSpending("Efectivo", 300.00, 1534.56),
    PaymentMethodSpending("Otro", 0.0, 1534.56)
)

val paymentMethodColors = listOf(
    Color(0xFF57B8FF),
    Color(0xFF81B29A),
    Color(0xFF7B506F),
    Color(0xFFE07A5F),
    Color(0xFF8B008B),
    Color(0xFFECD444)
)

val mappedPaymentMethodsSpending = samplePaymentMethodsSpending.mapIndexed { index, method ->
    SpendingItem(
        name = method.name,
        amount = method.amount,
        totalMonthSpending = method.totalMonthSpending,
        color = paymentMethodColors.getOrElse(index) { Color.Gray }
    )
}

val sampleCategoriesSpending = listOf(
    CategorySpending("Comida", 450.00, Icons.Default.Fastfood, 1534.56),
    CategorySpending("Compras", 300.00, Icons.Default.ShoppingCart, 1534.56),
    CategorySpending("Transporte", 200.00, Icons.Default.SportsMotorsports, 1534.56),
    CategorySpending("Entretenimiento", 150.00, Icons.Default.Movie, 1534.56),
    CategorySpending("Otros", 134.56, Icons.Default.OtherHouses, 1534.56)
)

val categoryColors = listOf(
    Color(0xFFFB4B4E),
    Color(0xFFFFAD05),
    Color(0xFFF4BFDB),
    Color(0xFF7B506F),
    Color(0xFFA5E6BA),
    Color(0xFF9AC6C5)
)

val mappedCategoriesSpending = sampleCategoriesSpending.mapIndexed { index, category ->
    SpendingItem(
        name = category.name,
        amount = category.amount,
        totalMonthSpending = category.totalMonthSpending,
        color = categoryColors.getOrElse(index) { Color.Gray }
    )
}

val sampleRecentTransactions = listOf(
    Transaction(Icons.Default.ShoppingCart, "Supermercado XYZ", "Abarrotes", 50.00),
    Transaction(Icons.Default.LocalMall, "Tienda de Ropa Z", "Vestimenta", -120.00),
    Transaction(Icons.Default.SportsMotorsports, "Gasolinera Pemex", "Transporte", -35.00),
    Transaction(Icons.Default.Movie, "Cinepolis", "Entretenimiento", -25.00),
    Transaction(Icons.Default.Fastfood, "Restaurante ABC", "Comida Fuera", -40.00)
)

val tipBackgroundColors = listOf(
    Color(0xFFE0F7FA),
    Color(0xFFE8F5E9),
    Color(0xFFF3E5F5),
    Color(0xFFFFFDE7)
)

val sampleFinancialTips = listOf(
    FinancialTip("Ahorra a tiempo", "Consejos para el supermercado.", Res.drawable.tip_ahorro),
    FinancialTip("Haz un presupuesto", "Guía paso a paso.", Res.drawable.tip_presupuesto),
    FinancialTip("Invierte Inteligente", "Primeros pasos.", Res.drawable.tip_inversion),
    FinancialTip("Reduce Gastos", "Identifica fugas de dinero.", Res.drawable.tip_gastos)
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(Res.drawable.profile_pic),
                    contentDescription = "Avatar del usuario",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.weight(0.7f),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "¡Hola, John Doe!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "USD/MXN",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "1 USD = ${CurrencyFormatter.formatAmount(19.50)} MXN",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "25 de junio del 2025 -- 25 de julio del 2025",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color(0XFF5e5b5b),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        BudgetCard(sampleBudget)
        Spacer(Modifier.height(24.dp))

        SpendingSection(
            title = "Gastos por método de pago",
            totalAmount = sampleBudget.totalSpent,
            items = mappedPaymentMethodsSpending,
            showTotalBar = true
        )
        Spacer(Modifier.height(24.dp))

        SpendingSection(
            title = "Gastos por categoría",
            totalAmount = sampleBudget.totalSpent,
            items = mappedCategoriesSpending,
            showTotalBar = true
        )
        Spacer(Modifier.height(24.dp))

        Text(
            text = "Puede interesarte",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1E),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sampleFinancialTips.forEachIndexed { index, tip ->
                val backgroundColor =
                    tipBackgroundColors.getOrElse(index % tipBackgroundColors.size) { Color.LightGray }
                FinancialTipCard(tip = tip, backgroundColor = backgroundColor) {
                    println("Tip clicked: ${tip.title}")
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        Text(
            text = "Transacciones Recientes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1E),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            sampleRecentTransactions.forEachIndexed { index, transaction ->
                TransactionRow(transaction)
                if (index < sampleRecentTransactions.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun BudgetCard(budget: BudgetSummary) {

    val backgroundImages = remember {
        listOf(
            Res.drawable.fondo_1,
            Res.drawable.fondo_2,
            Res.drawable.fondo_3,
            Res.drawable.fondo_4,
            Res.drawable.fondo_5,
            Res.drawable.fondo_6
        )
    }

    val randomBackgroundImage: DrawableResource =
        remember {
            backgroundImages.random()
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(randomBackgroundImage),
                contentDescription = "Fondo del presupuesto mensual",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Gastos", fontSize = 14.sp, color = Color.White)
                    Text(
                        CurrencyFormatter.formatAmount(budget.totalSpent),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ingresos", fontSize = 14.sp, color = Color.White)
                    Text(
                        CurrencyFormatter.formatAmount(budget.totalIncome),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SpendingSection(
    title: String,
    totalAmount: Double,
    items: List<SpendingItem>,
    showTotalBar: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1E),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = CurrencyFormatter.formatAmount(totalAmount),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(item.name, fontSize = 16.sp, color = Color.Black)
                }
                Spacer(Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(item.color)
                    )
                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = CurrencyFormatter.formatAmount(item.amount),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        if (showTotalBar) {
            Spacer(Modifier.height(16.dp))
            TotalSpendingBar(items = items, totalAmount = totalAmount)
        }
    }
}

@Composable
fun TotalSpendingBar(items: List<SpendingItem>, totalAmount: Double) {
    if (totalAmount <= 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
        )
        return
    }

    val segments = mutableListOf<Pair<Color, Float>>()
    var currentProgress = 0f
    items.forEach { item ->
        val proportion = (item.amount / totalAmount).toFloat()
        if (proportion > 0) {
            segments.add(Pair(item.color, proportion))
            currentProgress += proportion
        }
    }

    val finalSegments = if (currentProgress > 1.0f) {
        val factor = 1.0f / currentProgress
        segments.map { (color, proportion) -> Pair(color, proportion * factor) }
    } else {
        segments
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE0E0E0))
    ) {
        finalSegments.forEach { (color, proportion) ->
            val minWidth = if (proportion > 0 && proportion < 0.01f) 1.dp else 0.dp

            Spacer(
                modifier = Modifier
                    .weight(proportion)
                    .fillMaxHeight()
                    .background(color)
                    .widthIn(min = minWidth)
            )
        }
    }
}

@Composable
fun FinancialTipCard(tip: FinancialTip, backgroundColor: Color, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            tip.imageRes?.let { imageRes ->
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Default.Fastfood,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = tip.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1E),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = tip.subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6E6E73),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TransactionRow(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transaction.icon,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = transaction.storeName,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = transaction.category,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Text(
            text = CurrencyFormatter.formatAmount(transaction.amount),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = if (transaction.amount < 0) Color(0XFFbf100a) else Color(0XFF2d8c3a),
            textAlign = TextAlign.End
        )
    }
}