package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Component
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

// Botones de acción del filtro: "Aplicar" (botón principal) y "Eliminar filtros" (enlace subrayado)
@Composable
fun FilterActionButtons(
    onApply: () -> Unit,
    onClear: () -> Unit
) {
    val colors = IberdrolaTheme.colors

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp).padding(horizontal = Spacing.dp24),
            shape = RoundedCornerShape(Radius.dp50),

            colors = ButtonDefaults.buttonColors(containerColor = colors.iberdrolaDarkGreen)
        ) {
            Text(
                text = stringResource(R.string.filter_button_apply),
                color = colors.white,
                fontSize = TextSize.sp12,
                fontWeight = FontWeight.Bold,
                fontFamily = IberFontBold
            )
        }

        Spacer(modifier = Modifier.height(Spacing.dp16))

        Button(
            onClick = onClear,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = Spacing.dp24),
            shape = RoundedCornerShape(Radius.dp50),
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = colors.iberdrolaDarkGreen
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_button_clear),
                textDecoration = TextDecoration.Underline,
                fontSize = TextSize.sp12,
                color = colors.iberdrolaDarkGreen,
                fontWeight = FontWeight.Bold,
                fontFamily = IberFontBold
            )
        }
    }
}
