package com.colman.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.colman.tictactoe.game.Player
import com.colman.tictactoe.game.TicTacToeGame

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Button>
    private lateinit var statusText: TextView
    private lateinit var winningLineView: WinningLineView

    private val game = TicTacToeGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        winningLineView = findViewById(R.id.winningLineView)

        buttons = Array(9) { i ->
            findViewById(resources.getIdentifier("b$i", "id", packageName))
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                onCellClicked(button, index)
            }
        }

        findViewById<Button>(R.id.resetButton).setOnClickListener {
            resetGame()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onCellClicked(button: Button, index: Int) {
        val row = index / 3
        val col = index % 3

        if (!game.makeMove(row, col)) return

        val player = game.getCell(row, col)
        button.text = player.toString()

        // Set text color based on player
        val colorResId = if (player == Player.X) R.color.xColor else R.color.oColor
        button.setTextColor(ContextCompat.getColor(this, colorResId))

        val winResult = game.checkWinner()
        when {
            winResult != null -> {
                statusText.text = "Player ${winResult.player} wins!"
                disableBoard()
                drawWinningLine(winResult.winningIndices)
            }

            game.isDraw() -> {
                statusText.text = "It's a draw!"
                disableBoard()
            }

            else -> {
                statusText.text = "Player ${game.currentPlayer} turn"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resetGame() {
        game.resetGame()
        winningLineView.clear()
        buttons.forEach {
            it.text = ""
            it.isEnabled = true
        }
        statusText.text = "Player X turn"
    }

    private fun drawWinningLine(winningIndices: List<Int>) {
        val firstButton = buttons[winningIndices.first()]
        val lastButton = buttons[winningIndices.last()]

        val offsetX = winningLineView.x
        val offsetY = winningLineView.y

        val startCenterX = firstButton.x + firstButton.width / 2
        val startCenterY = firstButton.y + firstButton.height / 2
        val endCenterX = lastButton.x + lastButton.width / 2
        val endCenterY = lastButton.y + lastButton.height / 2

        val extensionPercentage = 0.5f
        val extensionX = (firstButton.width / 2) * extensionPercentage
        val extensionY = (firstButton.height / 2) * extensionPercentage

        val dirX = when {
            startCenterX < endCenterX -> 1f
            startCenterX > endCenterX -> -1f
            else -> 0f
        }
        val dirY = when {
            startCenterY < endCenterY -> 1f
            startCenterY > endCenterY -> -1f
            else -> 0f
        }

        val finalStartX = startCenterX - (dirX * extensionX) - offsetX
        val finalStartY = startCenterY - (dirY * extensionY) - offsetY

        val finalEndX = endCenterX + (dirX * extensionX) - offsetX
        val finalEndY = endCenterY + (dirY * extensionY) - offsetY

        winningLineView.drawWinningLine(finalStartX, finalStartY, finalEndX, finalEndY)
    }

    private fun disableBoard() {
        buttons.forEach { it.isEnabled = false }
    }
}