package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun BackButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = IberdrolaTheme.colors.iberdrolaGreen,
    iconSize: Dp = Spacing.dp24,
    fontSize: TextUnit = TextSize.sp14
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.dp24))
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp8, vertical = Spacing.dp6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = Spacing.dp6),
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontFamily = IberFontBold,
            textDecoration = TextDecoration.Underline,
            fontSize = fontSize
        )
    }
}

@Preview(name = "BackButton - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "BackButton - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BackButtonPreview() {
    IberdrolaTheme {
        BackButton(text = "Atrás", onClick = {})
    }
}
