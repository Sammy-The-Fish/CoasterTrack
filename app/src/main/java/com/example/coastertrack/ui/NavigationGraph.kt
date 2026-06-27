package com.example.coastertrack.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coastertrack.ui.screens.MainScreen
import com.example.coastertrack.ui.screens.NewDetailsScreen
import com.example.coastertrack.ui.screens.ParkDetailsScreen
import com.example.coastertrack.ui.screens.RollercoasterDetailsScreen
import com.example.coastertrack.ui.screens.RollercoasterRankingScreen
import com.example.coastertrack.ui.screens.TotalParksScreen
import com.example.coastertrack.ui.screens.TotalRollercoasterScreen
import com.example.coastertrack.ui.screens.VisitCreationParkSelectionScreen
import com.example.coastertrack.ui.screens.VisitCreationScreen
import com.example.coastertrack.ui.screens.VisitScreen
import com.example.coastertrack.ui.screens.intro.IntroScreenOne
import com.example.coastertrack.ui.screens.intro.IntroScreenThree
import com.example.coastertrack.ui.screens.intro.IntroScreenTwo
import com.example.coastertrack.ui.viewmodel.CheckFirstTImeViewModel

// navigation tree which connects screen together
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()


    val viewModel = hiltViewModel<CheckFirstTImeViewModel>()

    val isFirsTime by viewModel.isFirstTIme

    LaunchedEffect(isFirsTime) {
        val destination = if (isFirsTime) "intro_1" else "main"
        navController.navigate(destination) {
            popUpTo(0) // clear backstack
        }
    }


    NavHost(navController = navController, startDestination = "main", modifier = modifier) {
        composable(
            route = "main",
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
        ) {
            MainScreen(navController)
        }
        composable(
            route = "rollercoaster/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },

            ) { backStackEntry ->
//            RollercoasterDetailsScreen(
//                navController = navController
//            )
            NewDetailsScreen(
                navController
            )
        }

        composable(
            route = "park/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                when (initialState.destination.route) {
                    "rollercoaster/{id}" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "rollercoaster/{id}" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }

        ) { backStackEntry ->
            ParkDetailsScreen(navController = navController)
        }
        composable(
            route = "total_rollercoaster",
            enterTransition = {
                when (initialState.destination.route) {
                    "rollercoaster/{id}" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "rollercoaster/{id}" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }

        ) {
            TotalRollercoasterScreen(10, navController = navController)
        }
        composable(
            route = "ranking_rollercoaster",
            enterTransition = {
                when (initialState.destination.route) {
                    "rollercoaster/{id}" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "rollercoaster/{id}" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            RollercoasterRankingScreen(navController)
        }
        composable(
            route = "total_parks",
            enterTransition = {
                when (initialState.destination.route) {
                    "park/{id}" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "park/{id}" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            TotalParksScreen(navController = navController)
        }

        composable(
            route = "visit_creation_park_selection",
            enterTransition = {
                when (initialState.destination.route) {
                    "visit_creation_date_selection/{id}" -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right
                    )

                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "visit_creation_date_selection/{id}" -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left
                    )

                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            VisitCreationParkSelectionScreen(navController = navController)
//            VisitCreationScreen(navController = navController)

        }

        composable(
            route = "visit_creation_date_selection/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                when (initialState.destination.route) {
                    "park/{id}" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "visit/{id}" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            VisitCreationScreen(navController = navController)

        }

        composable(
            route = "visit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                when (initialState.destination.route) {
                    "park/{id}" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "park/{id}" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            VisitScreen(navController = navController)
        }
        composable(
            route = "intro_1",
            enterTransition = {
                when (initialState.destination.route) {
                    "intro_2" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "intro_2" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            IntroScreenOne(navController)
        }
        composable(
            route = "intro_2",
            enterTransition = {
                when (initialState.destination.route) {
                    "intro_3" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "intro_3" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            IntroScreenTwo(navController = navController)
        }

        composable(
            route = "intro_3",
            enterTransition = {
                when (initialState.destination.route) {
                    "main" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            IntroScreenThree(navController)
        }

        composable(
            route = "test/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                when (initialState.destination.route) {
                    "main" -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "" -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                }
            }
        ) {
            NewDetailsScreen(navController)
        }

    }
}


