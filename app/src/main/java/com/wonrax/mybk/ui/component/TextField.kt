package com.wonrax.mybk.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.Color

@Composable
fun TextField(
    placeHolder: String,
    value: String,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Default,
    password: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val mod by remember { mutableStateOf(modifier.fillMaxWidth()) }
    var showPassword by remember { mutableStateOf(false) }
    val visualTransformation =
        if (password) {
            if (!showPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        } else {
            VisualTransformation.None
        }

    OutlinedTextField(
        modifier = mod,
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(placeHolder, color = Color.Grey50) },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = if (password) KeyboardType.Password else KeyboardType.Text,
            autoCorrect = false
        ),
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Dark,
            backgroundColor = Color.Light,
            cursorColor = Color.Primary,
            disabledLabelColor = Color.Grey30,
            focusedIndicatorColor = Color.Primary,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            placeholderColor = Color.Grey30,
            trailingIconColor = Color.Dark
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (password && value != "") {
                Icon(
                    Icons.EyeOff,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) {
                            showPassword = !showPassword
                        }
                )
            }
        }
    )
}
