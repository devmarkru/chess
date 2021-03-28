package ru.devmark.chess

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ChoiceDialog
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import ru.devmark.chess.engine.Board
import ru.devmark.chess.engine.impl.BoardImpl
import ru.devmark.chess.models.HistoryItem
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.PromotionTurn
import java.util.concurrent.TimeUnit

class ChessApp : Application() {

    private val images = initImages()
    private val board: Board = BoardImpl()

    private var selectedPiecePoint: Point? = null

    private val ai = Ai(PieceColor.BLACK)

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Шахматы"
        val initialPieces = READER.readFileFromResource("/boards/initial.txt")
        board.load(initialPieces)
        initImages()
        val root = Group()
        val canvas = Canvas(
            (FIELD_SIZE * 8).toDouble(),
            (FIELD_SIZE * 9).toDouble()
        )
        val gc = canvas.graphicsContext2D
        drawBoard(gc, null, emptySet())
        drawPieces(gc)
        drawText(gc, "Первый ход белых")

        canvas.addEventHandler(
            MouseEvent.MOUSE_CLICKED
        ) { event: MouseEvent ->
            val x: Int = event.x.toInt() / FIELD_SIZE
            val y: Int = event.y.toInt() / FIELD_SIZE
            val selectedPoint = Point(x, 7 - y)
            if (x in 0..7 && y in 0..7) {
                if (event.clickCount == 1) {
                    when (event.button) {
                        MouseButton.PRIMARY -> processLeftMouseButton(selectedPoint, gc)
                        MouseButton.SECONDARY -> processRightMouseButton(selectedPoint, gc)
                    }
                }
            }
        }

        root.children.add(canvas)
        val scene = Scene(root)
        scene.onKeyPressed = EventHandler { event: KeyEvent ->
            when (event.code) {
                KeyCode.ESCAPE -> primaryStage.close()
            }
        }
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun processLeftMouseButton(selectedPoint: Point, gc: GraphicsContext) {
        board.getPieces()[selectedPoint]?.let { piece ->
            selectedPiecePoint = selectedPoint
            val availableSpaces = board.getTurnsForPiece(selectedPoint).map { it.to }.toMutableSet()
            drawBoard(gc, selectedPoint, availableSpaces)
            drawPieces(gc)
        }
    }

    private fun processRightMouseButton(selectedPoint: Point, gc: GraphicsContext) {
        if (selectedPiecePoint != null) {
            val turns = board.getTurnsForPiece(selectedPiecePoint!!)
            val selectedTurns = turns.filter { it.to == selectedPoint }
            val selectedTurn = if (selectedTurns.size > 1) {
                val newPieceType = showPromotionDialog()
                selectedTurns.first {
                    (it as PromotionTurn).toType == newPieceType
                }
            } else {
                selectedTurns.first()
            }
            val gameState = board.executeTurn(selectedTurn)
            selectedPiecePoint = null
            displayLastTurn(gc, board.getHistory().last())
            drawBoard(gc, null, emptySet())
            drawPieces(gc)

            if (!gameState.isFinal) {
                Thread {
                    TimeUnit.MILLISECONDS.sleep(500)
                    val aiTurn = ai.nextTurn(board)
                    board.executeTurn(aiTurn)
                    selectedPiecePoint = null
                    displayLastTurn(gc, board.getHistory().last())
                    drawBoard(gc, null, emptySet())
                    drawPieces(gc)
                }.start()
            }
        }
    }

    private fun showPromotionDialog(): PieceType {
        val piecesForPromotion = PieceType.values()
            .filter { it.useForPromotion }
        val choices = piecesForPromotion
            .map { it.nameRu }
        val dialog: ChoiceDialog<String> = ChoiceDialog<String>(
            choices.last(),
            choices
        )
        dialog.headerText = "В какую фигуру превратить пешку?"
        dialog.title = "Превращение пешки"
        dialog.contentText = "Выберите фигуру:"

        val choice: String = dialog.showAndWait().orElse(choices.first())
        return piecesForPromotion[choices.indexOf(choice)]
    }

    private fun drawBoard(gc: GraphicsContext, selectedPoint: Point?, availableSpaces: Set<Point>) {
        for (j in 7 downTo 0) {
            var color = if (j % 2 == 0) {
                DARK
            } else {
                LIGHT
            }
            for (i in 0..7) {
                gc.fill = color
                gc.fillRect(
                    (i * FIELD_SIZE).toDouble(),
                    ((7 - j) * FIELD_SIZE).toDouble(),
                    FIELD_SIZE.toDouble(),
                    FIELD_SIZE.toDouble()
                )
                color = if (color == DARK) {
                    LIGHT
                } else {
                    DARK
                }
            }
        }

        val borderSize = 4

        selectedPoint?.let { point ->
            gc.stroke = SELECTED
            gc.lineWidth = borderSize.toDouble()
            gc.strokeRect(
                (point.x * FIELD_SIZE + borderSize).toDouble(),
                ((7 - point.y) * FIELD_SIZE + borderSize).toDouble(),
                (FIELD_SIZE - borderSize * 2).toDouble(),
                (FIELD_SIZE - borderSize * 2).toDouble()
            )
        }

        availableSpaces.forEach { point ->
            gc.fill = SELECTED
            gc.fillRect(
                (point.x * FIELD_SIZE + borderSize).toDouble(),
                ((7 - point.y) * FIELD_SIZE + borderSize).toDouble(),
                (FIELD_SIZE - borderSize * 2).toDouble(),
                (FIELD_SIZE - borderSize * 2).toDouble()
            )
        }
    }

    private fun displayLastTurn(gc: GraphicsContext, turn: HistoryItem) {
        var text = "${turn.turnNumber}. ${turn.white}"
        turn.black?.let { text += "  $it" }
        drawText(gc, text)
    }

    private fun drawText(gc: GraphicsContext, text: String) {
        gc.fill = Color.BLACK
        gc.fillRect(
            0.toDouble(),
            (8 * FIELD_SIZE).toDouble(),
            (8 * FIELD_SIZE).toDouble(),
            FIELD_SIZE.toDouble()
        )
        gc.fill = Color.YELLOW
        gc.font = Font.font("Monospace", 30.toDouble())
        gc.fillText(text, (2 * FIELD_SIZE + 50).toDouble(), (8 * FIELD_SIZE + 50).toDouble())
    }

    private fun drawPieces(gc: GraphicsContext) {
        board.getPieces().forEach { (point, piece) ->
            gc.drawImage(
                images.getValue(piece.getImageName()),
                (point.x * FIELD_SIZE).toDouble(),
                ((7 - point.y) * FIELD_SIZE).toDouble(),
                FIELD_SIZE.toDouble(),
                FIELD_SIZE.toDouble()
            )
        }
    }

    private fun Piece.getImageName(): String =
        "${this.color.text}-${this.type.nameEn}"

    private fun initImages(): Map<String, Image> {
        return mapOf(
            "black-pawn" to Image(javaClass.getResourceAsStream("/chess/black-pawn.png")),
            "white-pawn" to Image(javaClass.getResourceAsStream("/chess/white-pawn.png")),
            "black-rook" to Image(javaClass.getResourceAsStream("/chess/black-rook.png")),
            "white-rook" to Image(javaClass.getResourceAsStream("/chess/white-rook.png")),
            "black-bishop" to Image(javaClass.getResourceAsStream("/chess/black-bishop.png")),
            "white-bishop" to Image(javaClass.getResourceAsStream("/chess/white-bishop.png")),
            "black-knight" to Image(javaClass.getResourceAsStream("/chess/black-knight.png")),
            "white-knight" to Image(javaClass.getResourceAsStream("/chess/white-knight.png")),
            "black-queen" to Image(javaClass.getResourceAsStream("/chess/black-queen.png")),
            "white-queen" to Image(javaClass.getResourceAsStream("/chess/white-queen.png")),
            "black-king" to Image(javaClass.getResourceAsStream("/chess/black-king.png")),
            "white-king" to Image(javaClass.getResourceAsStream("/chess/white-king.png")),
        )
    }

    companion object {
        private const val FIELD_SIZE = 100

        private val DARK = Color.DARKCYAN
        private val LIGHT = Color.LIGHTGRAY
        private val SELECTED = Color.ORANGE
        private val READER = BoardReader()

        @JvmStatic
        fun main(args: Array<String>) {
            launch(ChessApp::class.java)
        }
    }
}