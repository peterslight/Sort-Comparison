package com.peterstev.sortcomparison.screens

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.peterstev.sortcomparison.contracts.AlgoType
import com.peterstev.sortcomparison.contracts.Algorithm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmPicker(
    algorithms: List<Algorithm>,
    onSelect: (AlgoType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(algorithms[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selected.value,
            onValueChange = { selected.value },
            readOnly = true,
            label = { Text("Select Algorithm") },
            trailingIcon = { TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            algorithms.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.value) },
                    onClick = {
                        expanded = false
                        selected = option
                        onSelect(option.type)
                    }
                )
            }
        }
    }
}