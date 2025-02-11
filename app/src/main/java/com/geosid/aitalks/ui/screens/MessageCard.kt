package com.geosid.aitalks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geosid.aitalks.R
import com.geosid.aitalks.models.Message
import com.geosid.aitalks.ui.theme.BackGroundMessageGPT
import com.geosid.aitalks.ui.theme.BackGroundMessageGemini
import com.geosid.aitalks.ui.theme.ChatLiteTheme
import com.geosid.aitalks.ui.theme.ColorTextGPT
import com.geosid.aitalks.ui.theme.PrimaryColor
import com.geosid.aitalks.ui.theme.black
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.CodeBlockStyle
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText

@Composable
fun MessageCard(message: Message, isGemini: Boolean = false) {
    Column(
        horizontalAlignment = if (isGemini) Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isGemini) BackGroundMessageGemini else BackGroundMessageGPT,
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            if (message.content.isNotEmpty()) {
                if (isGemini) {
                    GeminiMessageCard(message = message)
                } else {
                    GPTMessageCard(message = message)
                }
            }
        }
    }
}

@Composable
fun GeminiMessageCard(message: Message) {

    Column (
        verticalArrangement = Arrangement.Center){
        Text(
            text = message.content,
            fontSize = 14.sp,
            color = black,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            textAlign = TextAlign.Justify,
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.power_by_gemini),
                fontSize = 11.sp,
                color = PrimaryColor,
                modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
fun GPTMessageCard(message: Message) {
    Column (
        verticalArrangement = Arrangement.Center) {
        ChatLiteTheme {
            SetupMaterial3RichText {
                RichText(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    style = RichTextStyle(
                        codeBlockStyle = CodeBlockStyle(
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = ColorTextGPT,
                            ),
                            wordWrap = true,
                            modifier = Modifier.background(
                                color = Color.Black,
                                shape = RoundedCornerShape(6.dp)
                            )
                        )
                    )
                ) {
                    Markdown(
                        message.content.trimIndent()
                    )
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.power_by_openai),
                fontSize = 11.sp,
                color = black,
                modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
                textAlign = TextAlign.End,
            )
        }
    }
}