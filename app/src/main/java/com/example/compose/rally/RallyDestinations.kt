/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

// 路由元数据定义

/**
 * Contract for information needed on every Rally navigation destination
 * 导航目标定义约定，有两个参数，子类要实现这个参数
 */

sealed interface RallyDestination {
    val icon: ImageVector
    val route: String
}

/**
 * Rally app navigation destinations
 *
 */
data object Overview : RallyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "overview"
}

data object Accounts : RallyDestination {
    override val icon = Icons.Filled.AttachMoney
    override val route = "accounts"
}

data object Bills : RallyDestination {
    override val icon = Icons.Filled.MoneyOff
    override val route = "bills"
}

data object SingleAccount : RallyDestination {
    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
    // part of the RallyTabRow selection
    override val icon = Icons.Filled.Money
    override val route = "single_account"
    const val accountTypeArg = "account_type"
    // 使用/{参数}带参数路由
    val routeWithArgs = "$route/{$accountTypeArg}"
    // 指定动态路由参数为 account_type
    val arguments = listOf(
        // 定义导航参数
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
    // 深度链接具体做什么的？
    val deepLinks = listOf(
        navDeepLink { uriPattern = "rally://$route/{$accountTypeArg}" }
    )
}

// Screens to be displayed in the top RallyTabRow
val rallyTabRowScreens = listOf(Overview, Accounts, Bills)
