
package com.nexusai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexusai.app.ui.theme.NexusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NexusTheme {
                NexusApp()
            }
        }
    }
}

@Composable
fun NexusApp() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf("Home", "Business", "Finance", "More")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Business,
        Icons.Default.AccountBalanceWallet,
        Icons.Default.MoreHoriz
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = title
                            )
                        },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    Text(
                        text = "Welcome to NEXUS AI",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text("Your intelligent business operating system")

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "My Workspace",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text("Your business overview will appear here.")
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabs[selectedTab],
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}
