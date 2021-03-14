package ru.devmark.chess.models

enum class PieceType(
    val nameEn: String,
    val nameRu: String,
    val price: Int,
    val notation: String,
    val useForPromotion: Boolean
) {
    PAWN("pawn", "пешка", 100, "", false),
    KNIGHT("knight", "конь", 320, "N", true),
    BISHOP("bishop", "слон", 330, "B", true),
    ROOK("rook", "ладья", 500, "R", true),
    QUEEN("queen", "ферзь", 900, "Q", true),
    KING("king", "король", 20_000, "K", false)
}