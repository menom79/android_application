package com.example.e04sumcalculator


import androidx.lifecycle.ViewModel
import com.notkamui.keval.keval
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.RoundingMode


data class MathExpression(
    val expression: String,
    val result: String
)

const val EXAMPLE_EXPRESSION = "(e*PI)^2 * ((10-2.5+3)/2)^2"

data class CalculatorState(
    val previousExpressions: MutableList<MathExpression> = mutableListOf(
        MathExpression(
            EXAMPLE_EXPRESSION,
            EXAMPLE_EXPRESSION.keval().toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
        )
    ),
    val currentExpression: MathExpression = MathExpression("", "")
)

class CalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()
    private val validDigits = setOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")
    private val validKevalOperators = setOf("+", "-", "*", "/", "%", "^", "neg", "PI", "e", "(", ")")
    private val validActions = setOf("C", "<=", "=")

    fun onKeyPressed(data: String) {
        val expr = _state.value.currentExpression.expression
        if (isValidDigit(data) || isValidOperator(data)) {
            concatCurrentExpression(data)
        } else if (isValidAction(data)) {
            when (data) {
                "C" -> clearCurrentExpression()
                "=" -> evaluateCurrentExpression()
                "<=" -> eraseLastCharacter()
            }
        } else {
            println("Have no idea what you are doing.")
        }
    }

    private fun evaluateCurrentExpression() {
        val expr = _state.value.currentExpression.expression
        if (expr.isEmpty()) return
        var exprRes = "Error"
        try {
            exprRes = expr
                .keval()
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_EVEN)
                .toString()
        } catch (error: Exception) {
            println("Error curred")
        }
        _state.update {
            var prevExpressions = it.previousExpressions
            prevExpressions.add(MathExpression(expr, exprRes))
            CalculatorState(
                previousExpressions = prevExpressions,
                currentExpression = MathExpression("", "")
            )
        }
    }

    private fun eraseLastCharacter() {
        val expr = state.value.currentExpression.expression
        if (expr.isNotEmpty()) {
            _state.update {
                it.copy(currentExpression = MathExpression(expr.dropLast(1), ""))
            }
        } else if (_state.value.previousExpressions.size > 0) {
            _state.update {
                var prevExpressions = it.previousExpressions
                var lastExpr = prevExpressions.removeLast()
                it.copy(previousExpressions = prevExpressions, currentExpression = lastExpr)
            }
        }
    }

    private fun clearCurrentExpression() {
        _state.update {
            it.copy(currentExpression = MathExpression("", ""))
        }
    }

    private fun concatCurrentExpression(str: String) {
        _state.update {
            it.copy(currentExpression = MathExpression(it.currentExpression.expression + str, ""))
        }
    }

    private fun isValidDigit(str: String): Boolean {
        return validDigits.contains(str)
    }

    private fun isValidOperator(str: String): Boolean {
        return validKevalOperators.contains(str)
    }

    private fun isValidAction(str: String): Boolean {
        return validActions.contains(str)
    }
}
