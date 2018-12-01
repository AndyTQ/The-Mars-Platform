package fall2018.csc2017.gamecentre.games.game2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Partially adapted from https://blog.csdn.net/lmj623565791/article/details/40020137
 * This class is frequently called, but actually contains minimal amount of logic,
 * hence should not be separately tested on.
 * <p>
 * Tiles (Views) in game 2048
 */
public class Game2048Item extends View {
    /**
     * Initialize the fields, parameters used in the class
     */
    private final Paint paint;
    private int number;
    private Rect textRect;
    private String backColor;
    private String numberValue;

    /**
     * Constructor taking in context
     *
     * @param context the context passed in
     */
    public Game2048Item(Context context) {
        this(context, null);
    }

    /**
     * Constructor taking in context and attributes
     *
     * @param context the context passes in
     * @param attrs   the attribute passed in
     */
    public Game2048Item(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor using context, attribute and the style
     *
     * @param context      the context
     * @param attrs        the attribute
     * @param defStyleAttr the style
     */
    public Game2048Item(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    /**
     * Generate the appropriate tile number according to the number on the tile
     */
    void getBackGroundColor() {
        switch (number) {
            case 0:
                backColor = "#CCC0B3";
                break;
            case 2:
                backColor = "#EEE4DA";
                break;
            case 4:
                backColor = "#EDE0C8";
                break;
            case 8:
                backColor = "#F2B179";
                break;
            case 16:
                backColor = "#F49563";
                break;
            case 32:
                backColor = "#F5794D";
                break;
            case 64:
                backColor = "#F55D37";
                break;
            case 128:
                backColor = "#EEE863";
                break;
            case 256:
                backColor = "#EDB04D";
                break;
            case 512:
                backColor = "#ECB04D";
                break;
            case 1024:
                backColor = "#EB9437";
                break;
            case 2048:
                backColor = "#EA7821";
                break;
            default:
                backColor = "#EA7821";
                break;
        }
    }

    /**
     * Getter for the number on the tile
     *
     * @return int number on the tile
     */
    public int getNumber() {
        return number;
    }

    /**
     * Set the number
     *
     * @param number the number to be set-ed
     */
    public void setNumber(int number) {
        this.number = number;
        numberValue = "" + number;
        paint.setTextSize(50);
        textRect = new Rect();
        paint.getTextBounds(numberValue, 0, numberValue.length(), textRect);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setMinimumWidth(249);
        setMinimumHeight(249);
        getBackGroundColor();
        paint.setColor(Color.parseColor(backColor));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
        if (number != 0) {
            paint.setColor(Color.BLACK);
            float x = (getMeasuredWidth() - textRect.width()) / 2;
            float y = getMeasuredHeight() / 2 + textRect.width() / 2;
            canvas.drawText(numberValue, x, y, paint);
        }
    }

    /**
     * Clone method for the Game2048Item
     *
     * @param context the context that the tile is in
     * @return Game2048Item
     */
    public Game2048Item clone(Context context) {
        Game2048Item game2048Item = new Game2048Item(context);
        game2048Item.setNumber(this.getNumber());
        game2048Item.getBackGroundColor();
        return game2048Item;
    }
}
