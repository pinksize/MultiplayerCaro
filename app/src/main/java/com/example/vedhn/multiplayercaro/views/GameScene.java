package com.example.vedhn.multiplayercaro.views;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.vedhn.multiplayercaro.R;
import com.example.vedhn.multiplayercaro.models.Move;
import com.example.vedhn.multiplayercaro.models.PlayerInfo;

public class GameScene extends View implements View.OnTouchListener {

    private String TAG = GameScene.this.getClass().getSimpleName();
    private int cellSize;
    private int numberOfColumn;
    private int numberOfRow;
    private Paint gridPaint;
    private Paint rectPaint;
    private List<Move> xMoves, oMoves;
    private PlayerInfo player1, player2, currentUser;
    private Bitmap xSign, oSign;
    private Matrix bitmapMatrix;
    private float sx, sy; // scale
    private float delta;
    private GameSceneListener mListener;
    private int[][] mark = new int[100][100];

    public GameScene(Context context) {
        super(context);
        init(context);
    }

    public GameScene(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameScene(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        xMoves = new ArrayList<>();
        oMoves = new ArrayList<>();

        xSign = BitmapFactory.decodeResource(getResources(), R.drawable.x_sign);
        oSign = BitmapFactory.decodeResource(getResources(), R.drawable.o_sign);

        bitmapMatrix = new Matrix();

        setBackgroundColor(Color.parseColor("#0097A7"));
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.RED);
        rectPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * check if game is finished
     *
     * @param row last checked row
     * @param col last check col
     * @return true if game is finished, false otherwise
     */
    public boolean isWon(int row, int col) {
        int total = 1;
        int value = mark[row][col];
        int i = 1, j = 1;
        // horizontal
        while (col - i >= 0 && mark[row][col - i] == value) {
            total++;
            i++;
            if (total == 5)
                break;
        }
        if (total == 5)
            return true;
        while (col + j < numberOfColumn && mark[row][col + j] == value) {
            total++;
            j++;
            if (total == 5)
                break;
        }
        if (total == 5)
            return true;

        i = 1;
        j = 1;
        total = 1;
        // vertical
        while (row - i >= 0 && mark[row - i][col] == value) {
            total++;
            i++;
            if (total == 5)
                break;
        }
        if (total == 5)
            return true;
        while (row + j < numberOfRow && mark[row + j][col] == value) {
            total++;
            j++;
            if (total == 5)
                break;
        }
        if (total == 5)
            return true;

        i = 1;
        j = 1;
        total = 1;
        // diagonal
        while (row - i >= 0 && (col - i) >= 0 && mark[row - i][col - i] == value) {
            total++;
            i++;
            if (total == 5)
                break;
        }
        if (total == 5)
            return true;
        while (row + j < numberOfRow && col + j < numberOfColumn && mark[row + j][col + j] == value) {
            total++;
            j++;
            if (total == 5)
                break;
        }
        if (total == 5)
            return true;

        i = 1;
        j = 1;
        total = 1;
        while (row + i < numberOfRow && col - i >= 0 && mark[row + i][col - i] == value) {
            total++;
            i--;
            if (total == 5)
                break;
        }
        if (total == 5)
            return true;
        while (row - j >= 0 && col + j < numberOfColumn && mark[row - j][col + j] == value) {
            total++;
            j++;
            if (total == 5)
                break;
        }
        return total == 5;

    }

    private boolean isVerticalWon(int row, int col){
        int value = mark[row][col];
        int total = 1;

        return total == 5;
    }

    private boolean isHorizontalWon(int row, int col){
        int value = mark[row][col];
        int total = 0;

        return total == 5;
    }

    private boolean isDiagonalWon(int row, int col){
        int value = mark[row][col];
        int total = 0;

        return total == 5;
    }

    public void startGame() {
        setOnTouchListener(this);
        if (mListener != null)
            mListener.onGameStarted(player1, player2);
    }

    public void endGame() {
        setOnTouchListener(null);
        if (mListener != null)
            mListener.onGameFinished(currentUser);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        cellSize = width / 10;
        numberOfColumn = width / cellSize;
        numberOfRow = height / cellSize;
        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                mark[i][j] = 0;
            }
        }

        sx = cellSize * 0.75f / xSign.getWidth();
        sy = cellSize * 0.75f / xSign.getHeight();

        delta = cellSize - sx * xSign.getWidth();
        delta /= 2;

        setMeasuredDimension(numberOfColumn * cellSize, numberOfRow * cellSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw x user steps
        rectPaint.setColor(Color.RED);
        for (Move xMove : xMoves) {
            bitmapMatrix.setScale(sx, sy);
            bitmapMatrix.postTranslate(xMove.col * cellSize + delta, xMove.row * cellSize + delta);
            canvas.drawBitmap(xSign, bitmapMatrix, null);
        }

        // draw o user steps
        rectPaint.setColor(Color.BLUE);
        for (Move oMove : oMoves) {
            bitmapMatrix.setScale(sx, sy);
            bitmapMatrix.postTranslate(oMove.col * cellSize + delta, oMove.row * cellSize + delta);
            canvas.drawBitmap(oSign, bitmapMatrix, null);
        }

        // draw grid
        for (int i = 0; i < numberOfColumn; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, numberOfRow * cellSize, gridPaint);
        }
        for (int i = 0; i < numberOfRow; i++) {
            canvas.drawLine(0, i * cellSize, numberOfColumn * cellSize, i * cellSize, gridPaint);
        }
    }

    public void setPlayers(PlayerInfo player1, PlayerInfo player2, GameSceneListener listener) {
        this.player1 = player1;
        this.player2 = player2;
        this.mListener = listener;

        this.currentUser = this.player1;
    }

    private boolean clickedCell(int row, int col) {
        if (mark[row][col] != 0)
            return false;
        else {
            // click to an available cell
            if (currentUser == player1) {
                mark[row][col] = 1;
                xMoves.add(new Move(player1, "", col, row));
            } else if (currentUser == player2) {
                mark[row][col] = 2;
                oMoves.add(new Move(player2, "", col, row));
            }

            invalidate();

            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            int col = (int) (event.getX() / cellSize);
            int row = (int) (event.getY() / cellSize);
            boolean isClicked = clickedCell(row, col);
            if (isClicked) {
                Log.d(TAG, "isWon: " + isWon(row, col));
                if (mListener != null)
                    mListener.onStepFinished(currentUser, row, col);
                currentUser = currentUser == player1 ? player2 : player1;
            }
        }
        return false;
    }

    public interface GameSceneListener {

        /**
         * call when a step is completed, should switch to next player
         *
         * @param playerInfo
         */
        void onStepFinished(PlayerInfo playerInfo, int row, int column);

        /**
         * call when game is finished
         *
         * @param winner
         */
        void onGameFinished(PlayerInfo winner);

        /**
         * call when game is starting
         *
         * @param player1
         * @param player2
         */
        void onGameStarted(PlayerInfo player1, PlayerInfo player2);
    }

}
