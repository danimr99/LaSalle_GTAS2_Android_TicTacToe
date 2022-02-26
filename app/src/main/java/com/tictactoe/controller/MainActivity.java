package com.tictactoe.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tictactoe.R;
import com.tictactoe.model.Cell;
import com.tictactoe.model.PlayerTurn;

public class MainActivity extends AppCompatActivity {
    private TextView playerTurnText;
    private PlayerTurn playerTurn;
    private Cell[] cells;
    private Integer[] imageViewsIDs = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set first turn for the player X
        this.playerTurn = PlayerTurn.PLAYER_X;

        // Set text notifying of the initial player's turn
        this.playerTurnText = (TextView) findViewById(R.id.textView);
        this.playerTurnText.setText(R.string.turnPlayerX);

        // Get every ImageView as cell
        for(int i = 0; i < this.imageViewsIDs.length; i++) {
            this.cells[i] = new Cell((ImageView) findViewById(this.imageViewsIDs[i]));
        }

        // Make every cell clickable
        for(Cell cell : this.cells) {
            cell.getWidget().setOnClickListener(view -> this.clickCell(cell));
        }
    }

    private void clickCell(Cell cell) {
        if(cell.isEmptyCell()) {
            this.setPlayerCell(cell.getWidget());

            this.changePlayerTurn();
        } else {
            Toast.makeText(this, R.string.notEmptyCellError, Toast.LENGTH_SHORT).show();
        }
    }

    private void setPlayerCell(ImageView imageView) {
        switch (this.playerTurn) {
            case PLAYER_X: {
                imageView.setBackgroundResource(R.drawable.x);
                break;
            }
            case PLAYER_O: {
                imageView.setBackgroundResource(R.drawable.o);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + this.playerTurn);
        }
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
}