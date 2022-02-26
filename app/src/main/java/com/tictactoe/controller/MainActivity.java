package com.tictactoe.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tictactoe.R;
import com.tictactoe.model.Cell;
import com.tictactoe.model.CellStatus;
import com.tictactoe.model.GameStatus;
import com.tictactoe.model.PlayerTurn;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int MAX_MOVES = 9;
    private TextView playerTurnText;
    private PlayerTurn playerTurn;
    private final List<Cell> cells = new LinkedList<>();
    private final Integer[] imageViewsIDs = {
            R.id.imageView0,
            R.id.imageView1,
            R.id.imageView2,
            R.id.imageView3,
            R.id.imageView4,
            R.id.imageView5,
            R.id.imageView6,
            R.id.imageView7,
            R.id.imageView8
    };
    private final Integer[][] winningCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    private int movesCounter = 0;
    private GameStatus gameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set first turn for the player X
        this.playerTurn = PlayerTurn.PLAYER_X;

        // Set text notifying of the initial player's turn
        this.playerTurnText = findViewById(R.id.textView);
        this.playerTurnText.setText(R.string.turnPlayerX);

        // Get every ImageView as cell
        for (Integer imageViewsID : this.imageViewsIDs) {
            this.cells.add(new Cell(findViewById(imageViewsID)));
        }

        // Make every cell clickable
        for (Cell cell : this.cells) {
            cell.getWidget().setOnClickListener(view -> this.clickCell(cell));
        }
    }

    private void clickCell(Cell cell) {

        if (cell.isEmptyCell()) {
            this.movesCounter++;
            this.setPlayerCell(cell);
            this.gameStatus = this.checkForWinner();

            if (gameStatus.equals(GameStatus.PLAYING)) {
                this.changePlayerTurn();
            } else {
                // Display a dialog informing of the result of the game
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.endGameDialog);

                // Set a TextView for displaying the game result on the dialog
                final TextView dialogTextView = new TextView(this);
                dialogTextView.setGravity(Gravity.CENTER);
                dialogTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                dialogTextView.setTextSize(20);

                switch (this.gameStatus) {
                    case DRAW: {
                        dialogTextView.setText(R.string.draw);
                        break;
                    }
                    case WINS_X: {
                        dialogTextView.setText(R.string.playerXWins);
                        break;
                    }
                    case WINS_O: {
                        dialogTextView.setText(R.string.playerOWins);
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + this.gameStatus);
                }

                // Add TextView to dialog
                alertDialog.setView(dialogTextView);

                // Add reset button to dialog
                alertDialog.setPositiveButton(R.string.playAgain, (dialog, which) -> this.resetGame());

                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        } else {
            Toast.makeText(this, R.string.notEmptyCellError, Toast.LENGTH_SHORT).show();
        }
    }

    private void setPlayerCell(Cell cell) {
        switch (this.playerTurn) {
            case PLAYER_X: {
                cell.getWidget().setBackgroundResource(R.drawable.x);
                cell.setStatus(CellStatus.X_CELL);
                break;
            }
            case PLAYER_O: {
                cell.getWidget().setBackgroundResource(R.drawable.o);
                cell.setStatus(CellStatus.O_CELL);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + this.playerTurn);
        }
    }

    private GameStatus checkForWinner() {
        CellStatus winnerCell = null;

        // Check for every combination
        for (Integer[] winPosition : this.winningCombinations) {
            if (this.cells.get(winPosition[0]).getStatus() == this.cells.get(winPosition[1]).getStatus() &&
                    this.cells.get(winPosition[1]).getStatus() == this.cells.get(winPosition[2]).getStatus() &&
                    !this.cells.get(winPosition[0]).isEmptyCell()) {
                winnerCell = this.cells.get(winPosition[0]).getStatus();
            }
        }

        // Determine which is the winner
        if (winnerCell != null) {
            if (winnerCell.equals(CellStatus.X_CELL)) {
                return GameStatus.WINS_X;
            } else {
                return GameStatus.WINS_O;
            }
        }

        // Check if players have reached max moves
        if (this.movesCounter == MAX_MOVES) {
            return GameStatus.DRAW;
        }

        return GameStatus.PLAYING;
    }

    private void changePlayerTurn() {
        switch (this.playerTurn) {
            case PLAYER_X: {
                this.playerTurn = PlayerTurn.PLAYER_O;
                this.playerTurnText.setText(R.string.turnPlayerO);
                break;
            }
            case PLAYER_O: {
                this.playerTurn = PlayerTurn.PLAYER_X;
                this.playerTurnText.setText(R.string.turnPlayerX);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + this.playerTurn);
        }
    }

    private void resetGame() {
        this.gameStatus = null;
        this.movesCounter = 0;

        for (Cell cell : this.cells) {
            cell.getWidget().setBackgroundResource(0);
            cell.setStatus(CellStatus.EMPTY_CELL);
        }
    }
}