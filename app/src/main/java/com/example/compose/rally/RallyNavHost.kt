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

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.overview.OverviewScreen

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Log.d("RallyApp TAG", "RallyNavHost Render ")
    // 1.NavHost需要定义一个初始路由页
    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) {
        // 2.路由的跳转集中在路由配置模块处理，让组件不需要关心如何进行路由跳转，只关注组件
        // 本身需要的功能
        composable(route = Overview.route) {
            // 组件要分离关注点，以下是将导航分离出来
            OverviewScreen(
                onClickSeeAllAccounts = {
                    // 3. 可以给导航模块添加扩展函数，跳转到所有账户页面
                    navController.navigateSingleTopTo(Accounts.route)
                },
                onClickSeeAllBills = {
                    navController.navigateSingleTopTo(Bills.route)
                },
                onAccountClick = { accountType ->
                    // navigateToSingleAccount是NavHostController类的扩展函数
                    // 跳转到指定账户页，这里就涉及到路由传参，要传账户类型
                    // this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
                    // 跳转到查看单个账户类型，那么这个类型参数在SingleAccount页面如接收呢？
                    // 其中一个简单方法是在带参数的路由配置内部接收即可
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }
        composable(route = Accounts.route) {
            Log.d("RallyApp TAG", "Accounts.route Render ")
            // 这是账户列表，选中某个账户时，需要带参数跳转到单账户页面
            // 涉及到动态路由传参
            AccountsScreen(
                onAccountClick = { accountType ->
                    //  navigateToSingleAccount是类NavHostController的扩展函数
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }
        composable(route = Bills.route) {
            Log.d("RallyApp TAG", "Bills.route Render ")
            BillsScreen()
        }

        // SingleAccount参数路由配置，在这个路由配置组件内接收路由参数值
        // 通过路由配置中的navBackStackEntry对象进行解析这个参数值
        // 然后把动态路由参数值传递给组件即可
        // 增加带参数路由配置
        // this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
        composable(
            route = SingleAccount.routeWithArgs,
            arguments = SingleAccount.arguments,
            deepLinks = SingleAccount.deepLinks
        ) { navBackStackEntry ->
            // 读取动态路由参数值
            val accountType =
                navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)

            SingleAccountScreen(accountType)
        }
    }
}

// 单个路由导航，带参数路由，选择一个项目
// 返回会回到首页
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        // 重新选择上一次选中的项目，则恢复状态
        restoreState = true
    }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    // $accountType变量插值风格，$accountType改为${accountType}等效
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}
