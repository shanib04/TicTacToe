package com.colman.tictactoe.game

data class WinResult(val player: Player, val winningIndices: List<Int>) // Holds the result of a win, including the player and the indices of the winning cells

// Handles the game logic for a Tic Tac Toe game
class TicTacToeGame {
    private val board = Array(3) { Array<Player?>(3) { null } }

    var currentPlayer: Player = Player.X
        private set

    // Makes a move at the given position and returns if the move was successful
    fun makeMove(row: Int, col: Int): Boolean {
        if (board[row][col] != null) {
            return false
        }

        board[row][col] = currentPlayer
        switchPlayer()
        return true
    }

    // Checks if there is a winner
    fun checkWinner(): WinResult? {
        // Checks rows
        for (i in 0..2) {
            val player = board[i][0]
            if (player != null &&
                player == board[i][1] &&
                player == board[i][2]
            ) {
                return WinResult(player, listOf(i * 3, i * 3 + 1, i * 3 + 2))
            }
        }
        // Checks columns
        for (i in 0..2) {
            val player = board[0][i]
            if (player != null &&
                player == board[1][i] &&
                player == board[2][i]
            ) {
                return WinResult(player, listOf(i, i + 3, i + 6))
            }
        }
        // Checks diagonals
        val center = board[1][1]
        if (center != null) {
            if (center == board[0][0] && center == board[2][2]) {
                return WinResult(center, listOf(0, 4, 8))
            }
            if (center == board[0][2] && center == board[2][0]) {
                return WinResult(center, listOf(2, 4, 6))
            }
        }
        return null
    }

    // Checks if the game ended in a draw
    fun isDraw(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == null) {
                    return false
                }
            }
        }
        return checkWinner() == null
    }

    // Resets the game to the initial state
    fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = null
            }
        }
        currentPlayer = Player.X
    }

    // Gets the player at the specified cell
    fun getCell(row: Int, col: Int): Player? {
        return board[row][col]
    }

    // Switches the current player
    private fun switchPlayer() {
        currentPlayer =
            if (currentPlayer == Player.X) Player.O else Player.X
    }
}