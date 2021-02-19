package ru.devmark.chess.engine

import ru.devmark.chess.models.Piece

interface PieceFactory {

    fun createPawn(): Piece

    fun createKnight(): Piece

    fun createBishop(): Piece

    fun createRook(): Piece

    fun createQueen(): Piece

    fun createKing(): Piece
}