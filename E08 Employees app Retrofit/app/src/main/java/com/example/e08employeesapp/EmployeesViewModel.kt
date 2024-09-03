package com.example.e08employeesapp

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.Exception

class EmployeesViewModel: ViewModel() {
    private val _employeesState = mutableStateOf(EmployeesState())
    val employeesState: State<EmployeesState> = _employeesState

    data class EmployeesState(
        val isLoading: Boolean = true,
        val allEmployees: List<EmployeeData> = emptyList(),
        val currentEmployee: EmployeeData? = null,
        val error: String? = null
    )

    fun setCurrentEmployee(employee: EmployeeData){
        _employeesState.value = _employeesState.value.copy(currentEmployee = employee)
    }

    init {
        if (_employeesState.value.allEmployees.isEmpty()) fetchEmployees()
    }

    private fun fetchEmployees(){
        viewModelScope.launch {
            try {
                val response = employeesService.getEmployees() // fetch data
                // update the state
                _employeesState.value = _employeesState.value.copy(
                    isLoading = false,
                    allEmployees = response.employees,
                    error = null)
            } catch (e: Exception){
                // update and set the error message
                _employeesState.value = _employeesState.value.copy(
                    isLoading = false,
                    allEmployees = emptyList(),
                    error="Error occurred while fetching data: ${e.message}")
            }
        }
    }
}