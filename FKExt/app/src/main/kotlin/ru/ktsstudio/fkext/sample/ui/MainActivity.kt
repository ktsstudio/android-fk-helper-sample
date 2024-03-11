package ru.ktsstudio.fkext.sample.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.koin.androidx.compose.koinViewModel
import ru.ktsstudio.fkext.sample.presentation.FinanceViewModel
import ru.ktsstudio.fkext.sample.ui.theme.ForeignKeyHandlerTheme
import ru.ktsstudio.fkext.sample.utils.common.VisibleStateParams
import ru.ktsstudio.fkext.sample.utils.common.rememberVisibleStateParams
import ru.ktsstudio.fkext.sample.utils.eventbus.EventBus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForeignKeyHandlerTheme {
                val visibleState = rememberVisibleStateParams<String>()
                val viewModel = koinViewModel<FinanceViewModel>()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = viewModel::insert
                        ) {
                            Text(text = "INSERT")
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = viewModel::delete
                        ) {
                            Text(text = "DELETE")
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = viewModel::update
                        ) {
                            Text(text = "UPDATE")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }


                    MessageDialog(
                        visibleState = visibleState,
                    )
                }

                LaunchedEffect(key1 = true) {
                    EventBus.event.collect(visibleState::show)
                }
            }
        }
    }
}

@Composable
fun MessageDialog(
    visibleState: VisibleStateParams<String>,
) {
    if (visibleState.shouldShow) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(0.70f))
        ) {
            Dialog(
                onDismissRequest = visibleState::hide,
                content = {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(16.dp),
                            )
                            .padding(
                                top = 32.dp,
                                bottom = 24.dp,
                                start = 16.dp,
                                end = 16.dp,
                            )
                            .animateContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape,
                                )
                                .padding(18.dp),
                            painter = painterResource(android.R.drawable.stat_sys_warning),
                            contentDescription = null,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            textAlign = TextAlign.Center,
                            text = visibleState.getParams(),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.W400,
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            onClick = visibleState::hide
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            )
        }
    }
}




