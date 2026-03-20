package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.InvoiceFilterUIState
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafeDatePickerDialog(
    initialDate: LocalDate?,
    statistics: InvoiceFilterUIState.FilterStatistics,
    otherDate: LocalDate?,
    isStartDate: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = IberdrolaTheme.colors

    val minDateAllowed = statistics.oldestDateMillis
    val maxDateAllowed = statistics.newestDateMillis

    val validationMin = if (!isStartDate && otherDate != null) {
        otherDate.toEpochMilli().coerceAtLeast(minDateAllowed)
    } else minDateAllowed

    val validationMax = if (isStartDate && otherDate != null) {
        otherDate.toEpochMilli().coerceAtMost(maxDateAllowed)
    } else maxDateAllowed

    val rawSelection = initialDate?.toEpochMilli()
    val safeSelection = if (rawSelection != null && validationMin > 0 && rawSelection in validationMin..validationMax) {
        rawSelection
    } else {
        if (isStartDate && minDateAllowed > 0) minDateAllowed
        else if (!isStartDate && maxDateAllowed > 0) maxDateAllowed
        else null
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = safeSelection,
        selectableDates = if (minDateAllowed > 0 && maxDateAllowed > 0) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis in validationMin..validationMax
                }

                override fun isSelectableYear(year: Int): Boolean {
                    val yearStartMillis = LocalDate.of(year, 1, 1).toEpochMilli()
                    val yearEndMillis = LocalDate.of(year, 12, 31).toEpochMilli()
                    return !(yearEndMillis < validationMin || yearStartMillis > validationMax)
                }
            }
        } else {
            object : SelectableDates {}
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    if (minDateAllowed <= 0 || maxDateAllowed <= 0 || millis in validationMin..validationMax) {
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        onDateSelected(date)
                    }
                }
                onDismiss()
            }) {
                Text("OK", color = colors.iberdrolaDarkGreen, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = colors.iberdrolaDarkGreen)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                todayContentColor = colors.iberdrolaDarkGreen,
                todayDateBorderColor = colors.iberdrolaDarkGreen,
                selectedDayContainerColor = colors.iberdrolaDarkGreen,
                selectedDayContentColor = Color.White
            )
        )
    }
}

private fun LocalDate.toEpochMilli(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
