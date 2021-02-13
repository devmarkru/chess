package ru.devmark.chess.models

// todo в Turn добавить всю инфу, которая нужна для фиксации истории и отмены хода (исходная точка, тип атакуемой фигуры)
interface Turn {
//    val from: Point
    val to: Point
}

data class NormalTurn(
//    override val from: Point,
    override val to: Point
): Turn

data class PromotionTurn(
//    override val from: Point,
    override val to: Point,
    val toType: PieceType
): Turn
