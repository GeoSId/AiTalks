package com.geosid.aitalks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geosid.aitalks.R
import com.geosid.aitalks.models.ApiType
import com.geosid.aitalks.models.Message
import com.geosid.aitalks.ui.components.ScrollToBottomButton
import com.geosid.aitalks.ui.theme.ChatLiteTheme
import com.geosid.aitalks.ui.theme.Purple80
import com.geosid.aitalks.ui.theme.black
import com.geosid.aitalks.ui.theme.grayLight
import com.geosid.aitalks.ui.theme.transparent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = hiltViewModel(),
    ) {

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val groupedMessages = remember(messages) { groupMessages(messages) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        listState.animateScrollToItem(groupedMessages.keys.size)
    }

    LaunchedEffect(true) {
        delay(300)
        listState.animateScrollToItem(groupedMessages.keys.size)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        topBar = {
            ChatTopBar(
                stringResource(R.string.app_name),
                scrollBehavior
            )
        },
        bottomBar = {
            ChatInputBox {
                chatViewModel.sendToOpenAi("")
                focusManager.clearFocus()
            }
        },
        floatingActionButton = {
            if (listState.canScrollForward) {
                ScrollToBottomButton(itemsSize = messages.size -8, onClick = {
                    scope.launch {
                        listState.animateScrollToItem(messages.size)
                    }
                })
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding),
            state = listState
        ) {
            messages.forEach { message ->
                item {
                    MessageCard(
                        message = message,
                        isGemini = message.platformType == ApiType.GOOGLE,
                    )
                }
            }
        }
    }
}

@Composable
fun ChatInputBox(
    onSendButtonClick: (String) -> Unit = {}
) {
    var value by rememberSaveable { mutableStateOf("") }

    val localStyle = LocalTextStyle.current
    val mergedStyle = localStyle.merge(TextStyle(color = LocalContentColor.current))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
            .padding(BottomAppBarDefaults.ContentPadding)
            .background(color = transparent)
    ) {
        BasicTextField(
            modifier = Modifier.heightIn(max = 100.dp),
            value = value,
            enabled = true,
            textStyle = mergedStyle,
            cursorBrush = SolidColor(grayLight),
            onValueChange = { value = it},
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .background(
                            color = Purple80,
                            shape = RoundedCornerShape(size = 24.dp)
                        )
                        .padding(all = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                modifier = Modifier,
                                color = black,
                                text = value,
                            )
                        }
                        innerTextField()
                    }
                    IconButton(
                        onClick = { onSendButtonClick(value) }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            "",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        )
    }
}

private fun groupMessages(messages: List<Message>): HashMap<Int, MutableList<Message>> {
    val classifiedMessages = hashMapOf<Int, MutableList<Message>>()
    var counter = 0

    messages.sortedBy { it.createdAt }.forEach { message ->
        if (message.platformType == null) {
            if (classifiedMessages.containsKey(counter) || counter % 2 == 1) {
                counter++
            }

            classifiedMessages[counter] = mutableListOf(message)
            counter++
        } else {
            if (counter % 2 == 0) {
                counter++
            }

            if (classifiedMessages.containsKey(counter)) {
                classifiedMessages[counter]?.add(message)
            } else {
                classifiedMessages[counter] = mutableListOf(message)
            }
        }
    }
    return classifiedMessages
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ChatTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(modifier = Modifier.fillMaxWidth(), text = title, maxLines = 1) },
        navigationIcon = {
        },
        actions = {
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    ChatLiteTheme {}
}