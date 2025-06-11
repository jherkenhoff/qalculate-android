package com.jherkenhoff.qalculate.ui.units

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.R

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
                    Text(text = "Units")
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }

                },
                actions = {
                    IconButton(onClick = {   }) {
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
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Add unit icon")
            }
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchString,
                        onQueryChange = onSearchInputUpdate,
                        onSearch = {},
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = { Text(stringResource(R.string.unit_search_placeholder)) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Search icon"
                            )
                        },
                    )
                }, expanded = false, onExpandedChange = {},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                windowInsets = WindowInsets(top = 0.dp)
            ) { }

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp),
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
                        }
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
                    }
                )
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Favorite") }
                )
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Custom") }
                )
            }

            LazyColumn {
                for (unitDefinition in unitList) {
                    item {
                        ListItem(
                            headlineContent = { Text(unitDefinition.title) },
                            supportingContent = { Text(unitDefinition.abbreviation) },
                        )
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
