package ru.devmark.chess.engine.impl

import ru.devmark.chess.engine.PieceFactory
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType

abstract class AbstractPieceFactory : PieceFactory {

    override fun createPawn(): Piece = Piece(PieceType.PAWN, getColor())

    override fun createKnight(): Piece = Piece(PieceType.KNIGHT, getColor())

    override fun createBishop(): Piece = Piece(PieceType.BISHOP, getColor())

    override fun createRook(): Piece = Piece(PieceType.ROOK, getColor())

    override fun createQueen(): Piece = Piece(PieceType.QUEEN, getColor())

    override fun createKing(): Piece = Piece(PieceType.KING, getColor())

    protected abstract fun getColor(): PieceColor
}