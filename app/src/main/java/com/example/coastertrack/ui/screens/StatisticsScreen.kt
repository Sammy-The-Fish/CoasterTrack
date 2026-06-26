package com.example.coastertrack.ui.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coastertrack.ui.viewmodel.StatisticsViewModel
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val squigglyCircleString =
        "M97.79,3.3C101.68,6.35 106.8,7.36 111.55,6.04C118.45,4.13 125.77,7.15 129.29,13.37C131.73,17.68 136.06,20.56 140.96,21.17C148.05,22.03 153.65,27.64 154.53,34.74C155.13,39.64 158.01,43.99 162.32,46.41C168.56,49.93 171.59,57.25 169.66,64.15C168.34,68.92 169.35,74.02 172.4,77.91C176.8,83.54 176.8,91.46 172.4,97.09C169.35,100.98 168.34,106.1 169.66,110.85C171.57,117.75 168.54,125.07 162.32,128.59C158.01,131.03 155.13,135.36 154.53,140.26C153.67,147.36 148.05,152.95 140.96,153.83C136.06,154.44 131.71,157.32 129.29,161.63C125.77,167.86 118.45,170.89 111.55,168.96C106.78,167.64 101.68,168.65 97.79,171.7C92.16,176.1 84.24,176.1 78.6,171.7C74.72,168.65 69.6,167.64 64.85,168.96C57.95,170.87 50.63,167.85 47.11,161.63C44.67,157.32 40.34,154.44 35.44,153.83C28.34,152.97 22.75,147.36 21.87,140.26C21.26,135.36 18.38,131.01 14.07,128.59C7.83,125.07 4.81,117.75 6.73,110.85C8.05,106.08 7.05,100.98 4,97.09C-0.4,91.46 -0.4,83.54 4,77.91C7.05,74.02 8.05,68.9 6.73,64.15C4.83,57.25 7.85,49.93 14.07,46.41C18.38,43.97 21.26,39.64 21.87,34.74C22.73,27.64 28.34,22.05 35.44,21.17C40.34,20.56 44.69,17.68 47.11,13.37C50.63,7.14 57.95,4.11 64.85,6.04C69.62,7.36 74.72,6.35 78.6,3.3C84.24,-1.1 92.16,-1.1 97.79,3.3Z"
    val squircleString =
        "M55.4,2.44L75.51,8.39C83.35,10.71 91.68,10.71 99.52,8.39L119.63,2.44C152.02,-7.15 182.09,23.26 172.6,56.02L166.72,76.36C164.43,84.29 164.43,92.71 166.72,100.64L172.6,120.98C182.09,153.74 152.02,184.15 119.63,174.56L99.52,168.61C91.68,166.29 83.35,166.29 75.51,168.61L55.4,174.56C23.01,184.15 -7.07,153.73 2.41,120.96L8.29,100.63C10.59,92.69 10.59,84.27 8.29,76.34L2.41,56.04C-7.07,23.28 23.01,-7.15 55.4,2.44Z"

    val squigglyCirclePath = remember {
        PathParser().parsePathString(squigglyCircleString).toPath()
    }
    val squirclePath = remember {
        PathParser().parsePathString(squircleString).toPath()
    }

    val squigglyCircleShape = remember(squigglyCirclePath) {
        PathShape(squigglyCircleString)
    }

    val squircleShape = remember(squirclePath) {
        PathShape(squircleString)
    }


    val viewModel: StatisticsViewModel = hiltViewModel()
    val coasterCount by viewModel.coasterCount.observeAsState()
    val parkCount by viewModel.parkCount.observeAsState()
    val statisticsList by viewModel.statistics
    val topRides by viewModel.topRides


    // cannot remember carousel state as the carousel should always be regenerated when the screen
    // is navigated as the topRides list may have changed and therefore the carousel may try tp access out of range indices
    val carouselState = CarouselState() {
        topRides.size
    }


    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            MainStatistic(
                modifier = Modifier
                    .weight(1f)
                    .padding(3.dp),
                shape = squigglyCircleShape,
                name = "Total Coasters:",
                statistic = if (coasterCount != null) "$coasterCount" else "",
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = {
                    navController.navigate("total_rollercoaster")
                }
            )
            MainStatistic(
                modifier = Modifier.weight(1f),
                shape = squircleShape,
                name = "Total Parks:",
                statistic = if (parkCount != null) "$parkCount" else "",
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = {
                    navController.navigate("total_parks")
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(10.dp)
        ) {
            statisticsList.forEach {
                ListStatistic(it.name, it.value, it.unit)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp, 10.dp, 10.dp, 2.dp)
        ) {
            Column {
                Text(
                    text = "Top Rides",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(5.dp)
                )
                if (topRides.isNotEmpty()) {
                    HorizontalMultiBrowseCarousel(
                        state = carouselState,
                        preferredItemWidth = 200.dp,
                        itemSpacing = 16.dp,
                        modifier = Modifier.height(300.dp)
                    ) { index ->
                        val rollercoaster = topRides[index]
                        CarouselItem(
                            text = {
                                Text(
                                    text = "${index + 1}: ${rollercoaster.name}",
                                    modifier = Modifier
                                        .graphicsLayer {
                                            alpha = max(
                                                lerp(
                                                    -0.5f,
                                                    1f,
                                                    max(
                                                        size.width - (carouselItemDrawInfo.maxSize) +
                                                                carouselItemDrawInfo.size,
                                                        0f
                                                    ) / size.width
                                                ), 0f
                                            )
                                        }
                                )
                            },
                            picture = {
                                AsyncImage(
                                    model = rollercoaster.picUrl,
                                    contentDescription = "picture of ${rollercoaster.name}",
                                    contentScale = ContentScale.Crop,
                                )
                            },
                            modifier = Modifier.maskClip(shape = MaterialTheme.shapes.extraLarge),
                            onClick = { navController.navigate("rollercoaster/${rollercoaster.id}") }
                        )
                    }
                    TextButton(
                        onClick = { navController.navigate("ranking_rollercoaster") },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "edit list")
                    }
                } else {
                    Box(
                        modifier = Modifier.height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "add a ride to ride list to see it here",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }


}


class PathShape(
    private val stringPath: String,
    private val matrix: Matrix = Matrix()
) : Shape {

    private var path: Path = Path()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        path.rewind()
        path = PathParser().parsePathString(stringPath).toPath()

        matrix.reset()
        val bounds = path.getBounds()
        val maxDimension = max(bounds.width, bounds.height)
        matrix.scale(size.width / maxDimension, size.height / maxDimension)
        matrix.translate(-bounds.left, -bounds.top)

        path.transform(matrix)
        return Outline.Generic(path)
    }
}

@Composable
fun MainStatistic(
    shape: Shape,
    name: String,
    statistic: String,
    contentColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        color = backgroundColor,
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    style = TextStyle(lineHeight = 100.sp),
                    text = name,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    text = statistic,
                    style = TextStyle(lineHeight = 10.sp),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Medium,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }

        }
    }

}

@Composable
fun ListStatistic(
    name: String,
    value: String,
    unit: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Text(
            text = value + (unit ?: ""),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CarouselItem(
    text: @Composable () -> Unit,
    picture: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var imageWidth by remember {
        mutableStateOf(0)
    }

    val titleAlpha by animateFloatAsState(
        targetValue = if (false) 1f else 0.5f
    )
    Surface(
        modifier = modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.extraLarge)
            .onSizeChanged {
                imageWidth = it.width
                Log.d("STATS SCREEN", "width: ${it.width}")
            },
        onClick = { onClick() }
    ) {
        picture()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0f, 0f, 0f, 0.5f)
                        )
                    )
                )
                .padding(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                CompositionLocalProvider(LocalContentColor provides Color.White) {
                    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleMedium) {
                        text()
                    }
                }
            }
        }

    }


}