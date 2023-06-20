package com.adoyo.diaryapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adoyo.diaryapp.R
import com.adoyo.diaryapp.model.Diary
import java.time.LocalDate

@Composable
fun HomeContent(
    diaryNotes: Map<LocalDate, List<Diary>>,
    onClick: (String) -> Unit
) {

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onMenuClicked: () -> Unit,
    navigateToWrite: () -> Unit,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit
) {
    //val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NavigationDrawer(drawerState = drawerState, onSignOutClicked = onSignOutClicked) {
        Scaffold(
            topBar = { HomeTopBar(onMenuClicked = onMenuClicked) },
            content = {
            },
            floatingActionButton = {
                FloatingActionButton(onClick = navigateToWrite) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "New diary")
                }
            }
        )

    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App logo"
                    )
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.google_logo),
                                    contentDescription = "Google Logo"
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Sign Out", color = MaterialTheme.colorScheme.onSurface)

                            }
                        }, selected = false, onClick = onSignOutClicked
                    )
                }
            )
        },
        content = content
    )
}

@Composable
fun EmptyPage(
    title: String = "Empty Diary",
    subtitle: String = "Write Something"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Normal
            )
        )
    }
}