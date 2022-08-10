package com.example.gamepuzzle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ViewGroup mainView;
    private board board;
    private boardView boardView;
    private TextView moves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (ViewGroup) findViewById(R.id.linearlayout);
        moves = (TextView) findViewById(R.id.textView);
        this.moves.setText("Number of movements: 0");
        this.newGame();
    }

    private board.boardListeners boardChangeListener = new board.boardListeners() {
        public void tileSlid(place from, place to, int numOfMoves) {
            moves.setText("Number of movements: " + Integer.toString(numOfMoves));
        }

        public void solved(int numOfMoves) {
            moves.setText("Solved in " + Integer.toString(numOfMoves) + " moves!");
            Toast.makeText(getApplicationContext(), "You won!",
                    Toast.LENGTH_LONG).show();
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set, menu);
        return true;
    }
    private void newGame() {
        moves = (TextView) findViewById(R.id.textView);
        this.board = new board(4);
        this.board.addBoardChangeListener(boardChangeListener);
        this.board.rearrange();
        this.mainView.removeView(boardView);
        this.boardView = new boardView(this, board);
        this.mainView.addView(boardView);
        this.moves.setText("Number of movements: 0");
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.shuffle_menu:
                new AlertDialog.Builder(this)
                        .setTitle("New Game")
                        .setMessage("Are you sure you want to begin a new game?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        board.rearrange();
                                        moves.setText("Number of movements: 0");
                                        boardView.invalidate();
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // do nothing
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.exit_game_menu:
                new AlertDialog.Builder(this)
                        .setTitle("Quit Game")
                        .setMessage("Are you sure you want to quit the game?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        System.exit(0);
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // do nothing
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}