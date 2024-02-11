package com.kelvinquantic.danamon.ui.photolist.components

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kelvinquantic.danamon.R
import com.kelvinquantic.danamon.model.PhotoUIModel
import com.kelvinquantic.danamon.ui.common.InfiniteListHandler
import com.kelvinquantic.danamon.ui.common.ScrollButton
import com.kelvinquantic.danamon.ui.common.findActivity
import com.kelvinquantic.danamon.ui.common.freshStartActivity
import com.kelvinquantic.danamon.ui.login.LoginActivity
import com.kelvinquantic.danamon.ui.photolist.HomeActivity
import com.kelvinquantic.danamon.ui.photolist.state.HomeScreenState
import com.kelvinquantic.danamon.ui.photolist.viewmodel.HomeViewModel
import com.kelvinquantic.danamon.ui.register.viewmodel.Role
import com.kelvinquantic.danamon.utils.bigTextStyle
import com.kelvinquantic.danamon.utils.smallTextStyle


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun HomeScreen(vm: HomeViewModel = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val isAdmin = vm.userSessionData.role == Role.Admin.toString()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            if (!isAdmin) {
                FloatingActionButton(onClick = {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.putExtra("isFavorite", true)
                    context.startActivity(intent)
                }) {
                    Card(
                        modifier = Modifier.size(48.dp),
                    ) {
                        Column {
                            Image(
                                painterResource(R.drawable.ic_love_red),
                                contentDescription = "",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .size(20.dp)
                            )
                            Text(
                                text = "Favorite Page",
                                style = smallTextStyle
                            )
                        }

                    }
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Home", style = bigTextStyle)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        if (vm.logout()) {
                            context.freshStartActivity(LoginActivity::class.java)

                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null)
                    }
                },
            )
        },
        content = {
            if (isAdmin) {
                UserListContent()
            } else {
                PhotoListContent()
            }
        }
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PhotoListContent(viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val intent = activity?.intent


    viewModel.isFavorite = intent?.getBooleanExtra("isFavorite", false) ?: false

    if (viewModel.isFavorite) {
        viewModel.getRoomPhotoList()
    } else {
        viewModel.getPhotoList()
    }

    val state by viewModel.homeState.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val paginationState by viewModel.paginationState.collectAsState()
    val focusManger = LocalFocusManager.current
    val lazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = {
                    viewModel.refresh()
                }
            ) {
                Content(
                    modifier = Modifier
                        .pointerInteropFilter {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    focusManger.clearFocus()
                                }

                                MotionEvent.ACTION_UP -> {
                                    focusManger.clearFocus()
                                }
                            }
                            false
                        },
                    lazyListState = lazyListState,
                    data = state.data,
                    onItemFav = {
                        viewModel.insertPhoto(context, it)
                    },
                    isFavoritePage = viewModel.isFavorite
                )

                InfiniteListHandler(lazyListState = lazyListState) {
                    viewModel.getCoinsPaginated()
                }
            }

        }

        HomeScreenState()
    }

    if (paginationState.isLoading) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                CircularProgressIndicator()
            }
        }
    }

    ScrollButton(lazyListState = lazyListState)
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    data: List<PhotoUIModel>,
    isFavoritePage: Boolean = false,
    onItemFav: (PhotoUIModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        items(
            items = data,
        ) { movie ->
            PhotoListItem(
                data = movie,
                onItemClick = {

                },
                onItemFav = {
                    onItemFav(it)
                },
                isFavoritePage = isFavoritePage
            )
        }
    }
}
