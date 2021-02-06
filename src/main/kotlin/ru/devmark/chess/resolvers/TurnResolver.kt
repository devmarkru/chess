package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Piece

interface TurnResolver {
    /**
     * Определяет, какие поля доступны для хода, с учётом наличия фигур
     */
    fun getSpacesForTurn(current: Piece, pieces: Map<Point, Piece>): Set<Point>
}