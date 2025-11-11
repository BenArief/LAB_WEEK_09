package com.example.lab_week_09.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import org.w3c.dom.Text

// UI Element for displaying title
@Composable
fun OnBackgroundTitleText(text: String) {
    TitleText(text = text, color =
        MaterialTheme.colorScheme.onBackground)
}

//use the titleLarge style from typography
@Composable
fun TitleText(text: String, color: Color){
    Text(text = text, style =
        MaterialTheme.typography.titleLarge, color = color)
}

//UI Element for displaying an item list
@Composable
fun OnBackgroundItemText(text: String){
    ItemText(text = text, color =
        MaterialTheme.colorScheme.onBackground)
}

//use the bodySmall style from the typography
@Composable
fun ItemText(text: String, color: Color){
    Text(text = text, style =
        MaterialTheme.typography.bodySmall, color = color)
}

//UI Element for displaying a button
@Composable
fun PrimaryTextButton(text: String, onClick: () -> Unit){
    TextButton(text = text,
        textColor = Color.White,
        onClick = onClick)
}

//Use the LabelMedium style from the typography
@Composable
fun TextButton(text: String, textColor: Color, onClick: () -> Unit){
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray,
            contentColor = textColor
        )
    ) {
        Text(text = text, style =
        MaterialTheme.typography.labelMedium)
    }
}