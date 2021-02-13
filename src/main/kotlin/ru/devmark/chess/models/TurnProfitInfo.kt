package ru.devmark.chess.models

data class TurnProfitInfo(
    val from: Point,
    val turn: Turn,
    val ownProfit: Int,
    val enemyProfit: Int
)