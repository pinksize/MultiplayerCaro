package com.example.vedhn.multiplayercaro;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.vedhn.multiplayercaro.models.Move;
import com.example.vedhn.multiplayercaro.models.PlayerInfo;

public class GameScene extends View implements View.OnTouchListener {

    private int cellSize;
    private int numberOfColumn;
    private int numberOfRow;
    private Paint gridPaint;
    private String TAG = GameScene.this.getClass().getSimpleName();
    private Paint rectPaint;
    private List<Move> xMoves, oMoves;
    private PlayerInfo player1, player2, currentUser;
    private Bitmap xSign;
    private Bitmap oSign;
    private Matrix bitmapMatrix;
    private float sx;
    private float sy;
    private float delta;
    private GameSceneListener mListener;

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

    private boolean clickedCell(float clickedX, float clickedY) {
        int col = (int) (clickedX / cellSize);
        int row = (int) (clickedY / cellSize);

        boolean isCellAvailable = true;
        for (Move xMove : xMoves) {
            if (xMove.col == col && xMove.row == row) {
                isCellAvailable = false;
                break;
            }
        }
        for (Move oMove : oMoves) {
            if (oMove.col == col && oMove.row == row) {
                isCellAvailable = false;
                break;
            }
        }
        if (!isCellAvailable)
            return false;

        boolean isClicked = false;
        RectF rect = new RectF(col * cellSize, row * cellSize, col * cellSize + cellSize, row * cellSize + cellSize);
        if (rect.contains(clickedX, clickedY)) {
            Log.d(TAG, "clickedCell: " + col + ", " + row);
            isClicked = true;

            if (currentUser == player1) {
                xMoves.add(new Move(player1, "", col, row));
            } else if (currentUser == player2) {
                oMoves.add(new Move(player2, "", col, row));
            }

            invalidate();
        } else {
            isClicked = false;
        }
        return isClicked;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            boolean isClicked = clickedCell(event.getX(), event.getY());
            if (isClicked) {
                if (mListener != null)
                    mListener.onStepFinished(currentUser);
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
        void onStepFinished(PlayerInfo playerInfo);

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
