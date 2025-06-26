package org.expensetrackerui.util.preview_dummies

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.daysUntil
import org.expensetrackerui.data.model.BudgetSummary
import org.expensetrackerui.data.model.CategorySpending
import org.expensetrackerui.data.model.Currency
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.data.model.ExpenseTag
import org.expensetrackerui.data.model.FinancialTip
import org.expensetrackerui.data.model.PaymentMethod
import org.expensetrackerui.data.model.PaymentMethodSpending
import org.expensetrackerui.data.model.SpendingItem
import org.expensetrackerui.data.model.TagWithColor
import org.expensetrackerui.data.model.Transaction
import org.expensetrackerui.data.repository.BudgetRepository
import org.expensetrackerui.data.repository.ExpenseRepository
import org.expensetrackerui.data.repository.FinancialTipsRepository
import org.expensetrackerui.data.repository.GetExpenseCategoriesRepository
import org.expensetrackerui.data.repository.GetExpenseTagsRepository
import org.expensetrackerui.data.repository.GetPaymentMethodsRepository
import org.expensetrackerui.data.repository.SaveExpenseRepository
import org.expensetrackerui.data.repository.SpendingRepository
import org.expensetrackerui.data.repository.TransactionRepository
import kotlin.random.Random
import kotlinx.datetime.Clock as KtxClock

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

val sampleFinancialTips = listOf(
    FinancialTip("Ahorra a tiempo", "Consejos para el supermercado.", null),
    FinancialTip("Haz un presupuesto", "Guía paso a paso.", null),
    FinancialTip("Invierte Inteligente", "Primeros pasos.", null),
    FinancialTip("Reduce Gastos", "Identifica fugas de dinero.", null)
)

class DummyBudgetRepository : BudgetRepository {
    override fun getBudgetSummary(): BudgetSummary = sampleBudget
}

class DummySpendingRepository : SpendingRepository {
    override fun getPaymentMethodSpending(): List<PaymentMethodSpending> = samplePaymentMethodsSpending
    override fun getMappedPaymentMethodSpending(): List<SpendingItem> = mappedPaymentMethodsSpending
    override fun getMappedCategorySpending(): List<SpendingItem> = mappedCategoriesSpending
    override fun getCategorySpending(): List<CategorySpending> = sampleCategoriesSpending
}

class DummyTransactionRepository : TransactionRepository {
    override fun getRecentTransactions(): List<Transaction> = sampleRecentTransactions
}

class DummyFinancialTipsRepository : FinancialTipsRepository {
    override fun getFinancialTips(): List<FinancialTip> = sampleFinancialTips
}

class DummyLocalExpenseDataSource {
    fun saveExpense(expense: Expense) {
        println("Dummy: Saving expense: ${expense.name}")
    }
}

class DummySaveExpenseUseCase(private val dummyDataSource: DummyLocalExpenseDataSource = DummyLocalExpenseDataSource()) : SaveExpenseRepository {
    override suspend fun invoke(expense: Expense) {
        dummyDataSource.saveExpense(expense)
    }
}

class DummyGetExpenseCategoriesUseCase : GetExpenseCategoriesRepository {
    override fun invoke(): List<ExpenseCategory> {
        return listOf(
            ExpenseCategory.NECESIDAD,
            ExpenseCategory.GASTO_NO_PLANEADO,
            ExpenseCategory.GUSTO,
            ExpenseCategory.DEUDA
        )
    }
}

class DummyGetExpenseTagsUseCase : GetExpenseTagsRepository {
    override fun invoke(): List<ExpenseTag> {
        return listOf(
            ExpenseTag.ESCUELA,
            ExpenseTag.VACACIONES,
            ExpenseTag.WALMART,
            ExpenseTag.VIAJE_CDMX,
            ExpenseTag.NAVIDAD_2024,
            ExpenseTag.SUPER,
            ExpenseTag.NOVIA,
            ExpenseTag.SUSCRIPCION
        )
    }
}

class DummyGetPaymentMethodsUseCase : GetPaymentMethodsRepository {
    override fun invoke(): List<PaymentMethod> {
        return listOf(
            PaymentMethod.BANAMEX_ORO,
            PaymentMethod.BANAMEX_AZUL,
            PaymentMethod.VALES_DE_DESPENSA,
            PaymentMethod.HEY_BANCO,
            PaymentMethod.NU_CREDITO,
            PaymentMethod.NU_DEBITO,
            PaymentMethod.EFECTIVO,
            PaymentMethod.BANAMEX_DEBITO,
            PaymentMethod.OTRO
        )
    }
}

val allExpenseCategories = ExpenseCategory.entries // Get all enum values
val allPaymentMethods = PaymentMethod.entries // Get all enum values
val allExpenseTags = ExpenseTag.entries // Get all enum values

val commonExpenseNames = listOf(
    "Café", "Alquiler", "Electricidad", "Agua", "Gas", "Internet",
    "Comestibles", "Cena fuera", "Transporte público", "Gasolina",
    "Ropa", "Entretenimiento", "Gimnasio", "Farmacia", "Médico",
    "Reparación coche", "Regalo", "Libros", "Música", "Software"
)

val randomTagColorsList = listOf(
    Color(0xFFE57373),
    Color(0xFF81C784),
    Color(0xFF64B5F6),
    Color(0xFFFFD54F),
    Color(0xFFBA68C8),
    Color(0xFF90A4AE),
    Color(0xFFFFB74D)
)

fun generateRandomDate(): LocalDate {
    val today = KtxClock.System.todayIn(TimeZone.currentSystemDefault())
    val sixMonthsAgo = today.minus(6, DateTimeUnit.MONTH)
    val diffDays = sixMonthsAgo.daysUntil(today).toLong()

    if (diffDays <= 0) {
        return sixMonthsAgo
    }

    val randomDays = Random.nextLong(0, diffDays)

    return sixMonthsAgo.plus(randomDays.toInt(), DateTimeUnit.DAY)
}

val sampleAllExpenses: List<Expense> = (1..100).map { id ->
    val randomName = commonExpenseNames.random()
    val randomAmount = Random.nextDouble(5.0, 500.0) // Random amount between 5 and 500
    val randomCategory = allExpenseCategories.random()
    val randomPaymentMethod = allPaymentMethods.random()
    val randomDate = generateRandomDate()

    val numberOfTags = Random.nextInt(allExpenseTags.size + 1) // 0 to max tags
    val uniqueRandomTags =
        allExpenseTags.shuffled(Random).take(numberOfTags) // Get unique ExpenseTag enums
            .map { tag ->
                val color = randomTagColorsList.random()
                TagWithColor(tag.name, color)
            }

    Expense(
        id = id.toString(),
        name = randomName,
        amount = randomAmount * -1,
        currency = Currency.MXN,
        date = randomDate,
        paymentMethod = randomPaymentMethod,
        category = randomCategory,
        tags = uniqueRandomTags
    )
}


class DummyExpenseRepository : ExpenseRepository {

    private val _expenses = MutableStateFlow(sampleAllExpenses)

    override fun getExpenses(): Flow<List<Expense>> {
        return _expenses.asStateFlow()
    }

    override suspend fun saveExpense(expense: Expense) {
        _expenses.value += expense
        println("DummyExpenseRepository: Saved expense: ${expense.name}. Total expenses: ${_expenses.value.size}")
    }
}