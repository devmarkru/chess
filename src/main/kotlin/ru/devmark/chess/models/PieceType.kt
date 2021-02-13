package ru.devmark.chess.models

enum class PieceType(
    val nameEn: String,
    val nameRu: String,
    val price: Int,
    val notation: String
) {
    PAWN("pawn", "пешка", 100, ""),
    KNIGHT("knight", "конь", 320, "N"),
    BISHOP("bishop", "слон", 330, "B"),
    ROOK("rook", "ладья", 500, "R"),
    QUEEN("queen", "ферзь", 900, "Q"),
    KING("king", "король", 20_000, "K")
}