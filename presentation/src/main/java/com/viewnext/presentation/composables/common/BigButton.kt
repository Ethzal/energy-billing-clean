package com.viewnext.presentation.composables.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.presentation.ui.theme.HoloGreenLight

@Composable
fun BigButtonSection(
    title: String,
    buttonText: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(200.dp, 180.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HoloGreenLight)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = buttonText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}
