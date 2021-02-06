package ru.devmark.chess.models

enum class PieceType(val imageName: String, val price: Int, val notation: String) {
    PAWN("pawn", 1, ""),         // пешка
    KNIGHT("knight", 3, "N"),    // конь
    BISHOP("bishop", 3, "B"),    // слон
    ROOK("rook", 5, "R"),        // ладья
    QUEEN("queen", 9, "Q"),      // ферзь
    KING("king", 1_000_000, "K") // король
}