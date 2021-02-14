package ru.devmark.chess.resolvers

import ru.devmark.chess.models.NormalTurn
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

fun MutableSet<Turn>.addPointIfCan(
    position: Point,
    pieces: Map<Point, Piece>,
    deltaX: Int,
    deltaY: Int
): Boolean {
    val current = pieces.getValue(position)
    val from = position
    val to = Point(from.x + deltaX, from.y + deltaY)
    val otherPiece = pieces[to]
    return to.takeIf { it.x in 0..7 && it.y in 0..7 }
        ?.let {
            when {
                otherPiece == null -> { // нет фигуры
                    this += NormalTurn(from = from, to = to)
                    true
                }
                otherPiece.color != current.color -> { // вражеская фигура
                    this += NormalTurn(from = from, to = to, enemyPiece = otherPiece)
                    false
                }
                else -> { // своя фигура
                    false
                }
            }
        } ?: false
}

fun generateRectangularTurns(position: Point, pieces: Map<Point, Piece>, maxRange: Int): Set<Turn> {
    val spaces = mutableSetOf<Turn>()
    var left = true
    var right = true
    var top = true
    var bottom = true
    for (i in 1..maxRange) {
        if (left) {
            left = spaces.addPointIfCan(position, pieces, -i, 0)
        }
        if (right) {
            right = spaces.addPointIfCan(position, pieces, i, 0)
        }
        if (top) {
            top = spaces.addPointIfCan(position, pieces, 0, i)
        }
        if (bottom) {
            bottom = spaces.addPointIfCan(position, pieces, 0, -i)
        }
    }
    return spaces
}

fun generateDiagonalTurns(position: Point, pieces: Map<Point, Piece>, maxRange: Int): Set<Turn> {
    val spaces = mutableSetOf<Turn>()
    var leftTop = true
    var rightTop = true
    var leftBottom = true
    var rightBottom = true
    for (i in 1..maxRange) {
        if (leftTop) {
            leftTop = spaces.addPointIfCan(position, pieces, -i, i)
        }
        if (rightTop) {
            rightTop = spaces.addPointIfCan(position, pieces, i, i)
        }
        if (leftBottom) {
            leftBottom = spaces.addPointIfCan(position, pieces, -i, -i)
        }
        if (rightBottom) {
            rightBottom = spaces.addPointIfCan(position, pieces, i, -i)
        }
    }
    return spaces
}