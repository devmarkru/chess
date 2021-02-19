package ru.devmark.chess.engine.impl

import ru.devmark.chess.models.PieceColor

class BlackPieceFactory : AbstractPieceFactory() {
    override fun getColor(): PieceColor = PieceColor.BLACK
}