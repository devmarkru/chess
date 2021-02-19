package ru.devmark.chess.engine.impl

import ru.devmark.chess.models.PieceColor

class WhitePieceFactory : AbstractPieceFactory() {
    override fun getColor(): PieceColor = PieceColor.WHITE
}