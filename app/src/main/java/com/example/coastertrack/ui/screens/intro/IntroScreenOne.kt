package com.example.coastertrack.ui.screens.intro


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.rotationMatrix
import androidx.graphics.shapes.transformed
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.coastertrack.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(
    device = "spec:width=1080px,height=2340px,dpi=440", apiLevel = 35
)
@Composable
fun IntroScreenOne(navController: NavController = rememberNavController()) {
    val image = painterResource(R.drawable.stealth_up_close_2024_05_08_02)

    val infiniteTransition = rememberInfiniteTransition("polygon_rotation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(20000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ),
        label = "rotationDegrees"
    )


    val shape = MaterialShapes.Cookie12Sided.transformed(matrix = rotationMatrix(rotation, ))

    val buttonSize = ButtonDefaults.LargeContainerHeight



    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = image,
                contentDescription = "picture of the rollercoaster stealth",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .aspectRatio(1f)
//                    .size(1000.dp)
                    .offset(100.dp, (0).dp)
//                    .background(color = Color(0.5f, 0.5f, 0.5f))
                    .scale(1.3f)
                    .clip(shape.toShape())
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .offset(0.dp, 100.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .width(400.dp)
                        .padding(10.dp)
                        .clip(
                            RoundedCornerShape(40.dp)
                        )
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(15.dp)
                        .align(Alignment.Start),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            "Coaster Track",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "This is the one app you need for the queue times at all of your favourite parks, as well as providing information about rollercoasters " +
                                    "at those parks",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(75.dp))
                    }
                }
                Button(
                    onClick = { navController.navigate("intro_2") },
                    modifier = Modifier
                        .align(Alignment.End)
                        .heightIn(buttonSize)
                        .offset((-40).dp, (-buttonSize)),
                    contentPadding = ButtonDefaults.contentPaddingFor(buttonSize)


                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        "arrow forward",
                        modifier = Modifier.size(ButtonDefaults.iconSizeFor(buttonSize))
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.iconSizeFor(buttonSize)))
                    Text("Get Started", style = ButtonDefaults.textStyleFor(buttonSize))
                }
            }
        }
    }
}


//@Composable
//fun IntroScreenOne(navController: NavController = rememberNavController()) {
//    Button(onClick = {
//        navController.navigate("intro_2")
//    }, modifier = Modifier.padding(20.dp)) { }
//}