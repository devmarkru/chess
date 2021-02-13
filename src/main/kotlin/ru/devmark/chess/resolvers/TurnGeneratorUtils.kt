package ru.devmark.chess.resolvers

import ru.devmark.chess.models.NormalTurn
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

fun MutableSet<Turn>.addPointIfCan(
    current: Piece,
    pieces: Map<Point, Piece>,
    deltaX: Int,
    deltaY: Int
): Boolean {
    val position = current.position
    val point = Point(position.x + deltaX, position.y + deltaY)
    val otherPiece = pieces[point]
    return point.takeIf { it.x in 0..7 && it.y in 0..7 }
        ?.let {
            when {
                otherPiece == null -> { // нет фигуры
                    this += NormalTurn(to = point)
                    true
                }
                otherPiece.color != current.color -> { // вражеская фигура
                    this += NormalTurn(to = point)
                    false
                }
                else -> { // своя фигура
                    false
                }
            }
        } ?: false
}

fun generateRectangularTurns(current: Piece, pieces: Map<Point, Piece>, maxRange: Int): Set<Turn> {
    val spaces = mutableSetOf<Turn>()
    var left = true
    var right = true
    var top = true
    var bottom = true
    for (i in 1..maxRange) {
        if (left) {
            left = spaces.addPointIfCan(current, pieces, -i, 0)
        }
        if (right) {
            right = spaces.addPointIfCan(current, pieces, i, 0)
        }
        if (top) {
            top = spaces.addPointIfCan(current, pieces, 0, i)
        }
        if (bottom) {
            bottom = spaces.addPointIfCan(current, pieces, 0, -i)
        }
    }
    return spaces
}

fun generateDiagonalTurns(current: Piece, pieces: Map<Point, Piece>, maxRange: Int): Set<Turn> {
    val spaces = mutableSetOf<Turn>()
    var leftTop = true
    var rightTop = true
    var leftBottom = true
    var rightBottom = true
    for (i in 1..maxRange) {
        if (leftTop) {
            leftTop = spaces.addPointIfCan(current, pieces, -i, i)
        }
        if (rightTop) {
            rightTop = spaces.addPointIfCan(current, pieces, i, i)
        }
        if (leftBottom) {
            leftBottom = spaces.addPointIfCan(current, pieces, -i, -i)
        }
        if (rightBottom) {
            rightBottom = spaces.addPointIfCan(current, pieces, i, -i)
        }
    }
    return spaces
}