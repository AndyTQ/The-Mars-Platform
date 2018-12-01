package fall2018.csc2017.gamecentre.games.game2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.GridLayout;

import fall2018.csc2017.gamecentre.User;

/**
 * This class is frequently called, but actually contains minimal amount of logic,
 * hence should not be separately tested on.
 *
 * The board of the game
 */
public class Game2048Layout extends GridLayout {
    /**
     * Fields for use by setting the layout
     */
    private int childWidth;
    private final int padding;
    private int childRow = 4;
    private final int margin = 10;
    /**
     * The gesture detector
     */
    private final GestureDetector gestureDetector;
    /**
     * The current user playing the game
     */
    private User user;
    /**
     * The board manager responsible for managing the game logic
     */
    private Game2048BoardManager game2048BoardManager;
    /**
     * boolean flag for use by onLayout, to avoid multiple loading
     */
    private boolean isLayout = false;

    /**
     * Required Constructor
     */
    public Game2048Layout(Context context) {
        this(context, null);
    }

    /**
     * Required Constructor
     */
    public Game2048Layout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Required Constructor
     */
    public Game2048Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        padding = Math.min(getPaddingBottom(), getPaddingTop());
        gestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Height and width should be the same, the minimum of width and height
        int length = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        childWidth = (length - 2 * padding - (childRow - 1) * margin) / childRow;
        setMeasuredDimension(length, length);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Avoid multiple loading
        if (!isLayout) {
            if (game2048BoardManager.game2048ItemIsNull()) {
                game2048BoardManager.createNewEmptyBoard();
            }
            for (int i = 0; i < childRow; i++) {
                for (int j = 0; j < childRow; j++) {
                    Game2048Item child = new Game2048Item(getContext());
                    Spec row = GridLayout.spec(i);
                    Spec column = GridLayout.spec(j);
                    LayoutParams layoutParams = new LayoutParams(row, column);
                    layoutParams.height = layoutParams.width = childWidth;
                    if ((j + 1) != childRow) {
                        layoutParams.rightMargin = margin;
                    }
                    if (i > 0) {
                        layoutParams.topMargin = margin;
                    }
                    layoutParams.setGravity(Gravity.FILL);
                    addView(child, layoutParams);
                    game2048BoardManager.setGame2048ItemWithIndex(child, i, j);
                }
            }
            game2048BoardManager.generateViewAuto();
        }
        isLayout = true;
    }

    /**
     * Set the size for the map. Update the size in score manager.
     *
     * @param size the size passed in for the map
     */
    public void setSize(int size) {
        this.childRow = size;
        this.game2048BoardManager = new Game2048BoardManager(user, childRow, getContext());
        this.game2048BoardManager.getGame2048ScoreManager().
                setComplexity(String.valueOf(size) + " X " + String.valueOf(size));
    }

    /**
     * Set the current user to...
     *
     * @param user the user playing the game
     */
    public void setCurrentUser(User user) {
        this.user = user;
    }

    public void onUndo() {
        game2048BoardManager.undo(getContext());
    }

    /**
     * @param event the event to be processed
     * @return boolean, always true in this case, as required
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * Initialize the on Game listener interface
     *
     * @param onGame2048Listener the listener, according to the onGame2048Listener interface
     */
    public void setOnGame2048Listener(OnGame2048Listener onGame2048Listener) {
        game2048BoardManager.setGame2048Listener(onGame2048Listener);
    }

    public void reStart() {
        game2048BoardManager.reStart();
    }

    /**
     * Enumeration for the possible movements, this saves us from writing 'default' when switching
     */
    public enum Action {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * The gesture listener that captures the users gesture
     * Performs the action as required
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        final int MIN_DISTANCE = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            float absX = Math.abs(x);
            float absY = Math.abs(y);

            if (x > MIN_DISTANCE && absX > absY) {
                game2048BoardManager.action(Action.RIGHT);
            }
            if (x < -MIN_DISTANCE && absX > absY) {
                game2048BoardManager.action(Action.LEFT);
            }
            if (y > MIN_DISTANCE && absY > absX) {
                game2048BoardManager.action(Action.DOWN);
            }
            if (y < -MIN_DISTANCE && absY > absX) {
                game2048BoardManager.action(Action.UP);
            }
            return true;
        }
    }

}
