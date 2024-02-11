package com.kelvinquantic.danamon.ui.photolist.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kelvinquantic.danamon.ui.confirmpassword.ConfirmPasswordActivity
import com.kelvinquantic.danamon.ui.photolist.viewmodel.HomeViewModel
import com.kelvinquantic.danamon.ui.register.RegisterActivity
import com.kelvinquantic.danamon.utils.bigTextStyle
import com.kelvinquantic.danamon.utils.mediumTextStyle

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun UserListContent(vm: HomeViewModel = hiltViewModel()) {
    val ctx = LocalContext.current

    OnLifecycleEvent { owner, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                vm.getUser()
            }

            else -> {}
        }
    }

    val userState = vm.userState.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "User List",
            style = bigTextStyle
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            items(userState.value.size) {
                Card(
                    elevation = CardDefaults.cardElevation(),
                    border = BorderStroke(width = 2.dp, Color.LightGray),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                        .background(Color.White)

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            if (userState.value[it].username != vm.userSessionData.username) {
                                Row(
                                    horizontalArrangement = Arrangement.Absolute.Right,
                                    verticalAlignment = Alignment.Top,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        "Edit Profile",
                                        Modifier
                                            .size(25.dp)
                                            .clickable {
                                                val i = Intent(ctx, RegisterActivity::class.java)
                                                i.putExtra("isEdit", true)
                                                i.putExtra("data", userState.value[it])
                                                ctx.startActivity(i)
                                            },
                                        tint = Color.Gray
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        "Delete Profile",
                                        Modifier
                                            .size(25.dp)
                                            .clickable {
                                                val i = Intent(ctx, ConfirmPasswordActivity::class.java)
                                                i.putExtra("data", userState.value[it])
                                                ctx.startActivity(i)
                                            },
                                        tint = Color.Gray

                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                userState.value[it].username?.let { it1 ->
                                    Text(
                                        text = it1,
                                        style = mediumTextStyle
                                    )
                                }

                                userState.value[it].email?.let { it1 ->
                                    Text(
                                        text = it1,
                                        style = mediumTextStyle
                                    )
                                }
                                userState.value[it].role?.let { it1 ->
                                    Text(
                                        text = it1,
                                        style = mediumTextStyle
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
