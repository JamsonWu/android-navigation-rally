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

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 * 活动窗口
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 内容渲染
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    Log.d("RallyApp TAG", "RallyApp Render ")
    // 主题作为根组件
    RallyTheme {
        // 导航控制器
        val navController = rememberNavController()
        // 当前返回栈，可变状态，by是委托
        val currentBackStack by navController.currentBackStackEntryAsState()
        // 当前导航目标
        val currentDestination = currentBackStack?.destination
        // 当前导航的唯一路由 currentDestination?.route
        // rallyTabRowScreens包含自定义的路由
        val currentScreen =
            rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview

        Scaffold(
            topBar = {
                // 构建头部菜单栏
                RallyTabRow(
                    // 头部菜单传入自定义页面
                    allScreens = rallyTabRowScreens,
                    // 传入Tab选中事件跳转到选中的页面
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    // 记录当前选中的屏幕，即当前路由
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            // 导航控制与路由映射
            // 控制只显示当前选中路由及所有路由配置
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
