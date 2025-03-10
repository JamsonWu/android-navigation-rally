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

package com.example.compose.rally.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.compose.rally.R
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.components.AccountRow
import com.example.compose.rally.ui.components.StatementBody

/**
 * The Accounts screen.
 */
@Composable
fun AccountsScreen(
    onAccountClick: (String) -> Unit = {},
) {
    val amountsTotal = remember { UserData.accounts.map { account -> account.balance }.sum() }
    StatementBody(
        modifier = Modifier.semantics { contentDescription = "Accounts Screen" },
        items = UserData.accounts,
        // 函数
        amounts = { account -> account.balance },
        colors = { account -> account.color },
        amountsTotal = amountsTotal,
        circleLabel = stringResource(R.string.total)
    ) {
        // 这个写法要求函数StatementBody最后一个参数是Lamda表达式
        account ->
        AccountRow(
            modifier = Modifier.clickable {
                onAccountClick(account.name)
            },
            name = account.name,
            number = account.number,
            amount = account.balance,
            color = account.color
        )
    }
}

/**
 * Detail screen for a single account.
 */
@Composable
fun SingleAccountScreen(
    accountType: String? = UserData.accounts.first().name
) {
    // 性能优化
    // remember 属于计算字段 达到性能优化目的，只要accountType不变
    // SingleAccountScreen 组件重新渲染时也不会去调用getAccount的方法去重新拉取值
    // 这样account就不变，StatementBody组件就不会重新渲染
    // remember 计算字段对值进行记忆，当accountType发生变化时才会重新求值
    val account = remember(accountType) { UserData.getAccount(accountType) }
    StatementBody(
        items = listOf(account),
        colors = { account.color },
        amounts = { account.balance },
        amountsTotal = account.balance,
        circleLabel = account.name,
    ) { row ->
        AccountRow(
            name = row.name,
            number = row.number,
            amount = row.balance,
            color = row.color
        )
    }
}
