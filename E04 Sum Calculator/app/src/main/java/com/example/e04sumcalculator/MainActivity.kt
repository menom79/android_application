package com.example.e04sumcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e04sumcalculator.ui.theme.E04SumCalculatorTheme
import com.notkamui.keval.keval
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.coroutines.CoroutineContext


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            E04SumCalculatorTheme {
                val windowSize = calculateWindowSizeClass(this)
                val calcModel: CalculatorViewModel = viewModel()
                CalculatorApp(windowSize, calcModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("App is closing")
    }
}


@Composable
fun CalculatorApp(windowSize: WindowSizeClass, calcViewModel: CalculatorViewModel = viewModel()) {
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CalculatorPortrait(calcViewModel)
        }
//        WindowWidthSizeClass.Medium -> {}
//        WindowWidthSizeClass.Expanded -> {}
        else -> {
            CalculatorLandscape(calcViewModel)
        }
    }
}

@Composable
fun CalculatorPortrait(
    calcViewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by calcViewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .then(modifier)
    ) {
        Banner(imageSrc = R.drawable.math, modifier = Modifier.fillMaxHeight(1f))

        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Expressions
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Show previous expressions
                for (expression in state.previousExpressions) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${expression.expression} = ${expression.result}",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 8.dp)
                        )
                        Divider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }

            // Show current expression
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    // text = "${expression.expression} = ${expression.result}",
                    text = state.currentExpression.expression,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                )
                Divider(thickness = 1.dp, color = Color.Gray)
            }

            // Keys
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(16.dp, 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { calcViewModel.onKeyPressed("7") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("7")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("8") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("8")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("9") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("9")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("*") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("*")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { calcViewModel.onKeyPressed("4") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("4")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("5") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("5")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("6") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("6")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("-") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("-")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { calcViewModel.onKeyPressed("1") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("1")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("2") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("2")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("3") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("3")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("+") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { calcViewModel.onKeyPressed("0") }) {
                            Text("0")
                        }
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { calcViewModel.onKeyPressed("C") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("C")
                        }
                        Button(
                            onClick = {
                                calcViewModel.onKeyPressed("=")
                                scope.launch {
                                    while (scrollState.canScrollForward) {
                                        scrollState.scrollBy(20f)
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("=")
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun CalculatorLandscape(
    calcViewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by calcViewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .then(modifier)
    ) {
        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Expressions
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Show previous expressions
                for (expression in state.previousExpressions) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${expression.expression} = ${expression.result}",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 8.dp)
                        )
                        Divider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }

            // Show current expression
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = state.currentExpression.expression,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                )
                Divider(thickness = 1.dp, color = Color.Gray)
            }

            // Keys
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(16.dp, 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { calcViewModel.onKeyPressed("(") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("(")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed(")") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(")")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("<=") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("<=")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("7") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("7")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("8") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("8")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("9") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("9")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("*") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("*")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = { calcViewModel.onKeyPressed("SIN") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("SIN")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("COS") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("COS")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("e") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("e")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("4") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("4")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("5") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("5")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("6") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("6")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("-") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("-")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { calcViewModel.onKeyPressed("SINH") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("SINH")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("COSH") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("COSH")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("^") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("^")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("1") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("1")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("2") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("2")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("3") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("3")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("+") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { calcViewModel.onKeyPressed("C") },
                        modifier = Modifier.weight(2f)
                    ) {
                        Text("C")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { calcViewModel.onKeyPressed("PI") }) {
                        Text("PI")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { calcViewModel.onKeyPressed("0") }) {
                        Text("0")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { calcViewModel.onKeyPressed(".") }) {
                        Text(".")
                    }
                    Button(
                        onClick = { calcViewModel.onKeyPressed("/") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("/")
                    }
                    Button(
                        onClick = {
                            calcViewModel.onKeyPressed("=")
                            scope.launch {
                                while (scrollState.canScrollForward) {
                                    scrollState.scrollBy(20f)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("=")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPortraitPreview() {
    E04SumCalculatorTheme {
        CalculatorPortrait()
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun CalculatorLandscapePreview() {
    E04SumCalculatorTheme {
        CalculatorLandscape()
    }
}

@Composable
fun Banner(@DrawableRes imageSrc: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = imageSrc),
        contentDescription = stringResource(R.string.banner_content_description),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(16f / 9f)
            .fillMaxWidth()
            .then(modifier)
    )
}

@Preview
@Composable
fun BannerPreview() {
    Banner(R.drawable.math)
}