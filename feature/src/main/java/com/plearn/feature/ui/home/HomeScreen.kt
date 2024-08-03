package com.plearn.feature.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.plearn.core.R

@Composable
fun HomeScreen(username: String, onLogout: () -> Unit) {
    BackHandler { }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = stringResource(R.string.txt_welcome_use, username))
        Button(
            onClick = onLogout,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_logout))
        }
    }
}