package com.jherkenhoff.qalculate.ui.units

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.outlined.ArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

data class UnitDefinition(
    val title: String,
    val name: String,
    val abbreviation: String
)

@Composable
fun UnitsScreen(
    viewModel: UnitsViewModel = viewModel(),
    openDrawer: () -> Unit = {}
) {

    UnitsScreenContent(
        openDrawer = openDrawer,
        unitList = viewModel.unitList.value,
        onSearchInputUpdate = { viewModel.setSearchString(it) },
        searchString = viewModel.searchString.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitsScreenContent(
    openDrawer: () -> Unit = {  },
    unitList: List<UnitDefinition> = emptyList(),
    onSearchInputUpdate: (String) -> Unit = {},
    searchString: String = ""
) {

    val scrollState = rememberScrollState()


    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Units", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Open navigation drawer"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort icon"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Filled.Add, "Add unit icon")
            }
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(6.dp)
            ) {
                // All composable content must be inside the Card's content lambda
                Column {
                    SearchBar(
                        query = searchString,
                        onQueryChange = onSearchInputUpdate,
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        placeholder = { Text("search units") },
                        trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {}
                    Spacer(modifier = Modifier.size(8.dp))
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.wrapContentSize(Alignment.BottomStart)) {
                            var expanded by remember { mutableStateOf(false) }
                            FilterChip(
                                selected = true,
                                onClick = { expanded = true },
                                label = { Text("Light") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = "Unit system icon"
                                    )
                                },
                                shape = MaterialTheme.shapes.medium
                            )
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(
                                    text = { Text("Up") },
                                    onClick = { /* Handle edit! */ },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Outlined.ArrowLeft, contentDescription = null) }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = { /* Handle settings! */ },
                                )
                                DropdownMenuItem(
                                    text = { Text("Radiance") },
                                    onClick = { /* Handle send feedback! */ },
                                )
                            }
                        }
                        FilterChip(
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = { Text("System") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Unit system icon"
                                )
                            },
                            shape = MaterialTheme.shapes.medium
                        )
                        FilterChip(
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = { Text("Favorite") },
                            shape = MaterialTheme.shapes.medium
                        )
                        FilterChip(
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = { Text("Custom") },
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = MaterialTheme.shapes.large,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    for (unitDefinition in unitList) {
                        item {
                            ListItem(
                                headlineContent = { Text(unitDefinition.title, style = MaterialTheme.typography.bodyLarge) },
                                supportingContent = { Text(unitDefinition.abbreviation, style = MaterialTheme.typography.labelLarge) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    UnitsScreenContent(
    )
}
