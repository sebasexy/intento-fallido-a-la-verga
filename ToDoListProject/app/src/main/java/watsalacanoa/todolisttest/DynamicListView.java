package watsalacanoa.todolisttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import watsalacanoa.todolisttest.adapters.ItemsArrayAdapter;

public class DynamicListView extends ListView {

    private final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 15;
    private final int MOVE_DURATION = 150;
    private final int LINE_THICKNESS = 15;
    private final int INVALID_ID = -1;
    private final int INVALID_POINTER_ID = -1;
    public ArrayList<String> mCheeseList;
    private int mLastEventY = -1;
    private int mDownY = -1;
    private int mDownX = -1;
    private int mTotalOffset = 0;
    private int mSmoothScrollAmountAtEdge = 0;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
    private boolean mCellIsMobile = false;
    private boolean mIsMobileScrolling = false;
    private boolean mIsWaitingForScrollFinish = false;
    private long mMobileItemId = INVALID_ID;
    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;

    public DynamicListView(Context context) {
        super(context);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    }


    /**
     * Retrieves the view in the list corresponding to itemID
     */
    public View getViewForID(long itemID) {
        int firstVisiblePosition = getFirstVisiblePosition();
        ItemsArrayAdapter adapter = ((ItemsArrayAdapter) getAdapter());
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int position = firstVisiblePosition + i;
            long id = adapter.getItemId(position);
            if (id == itemID) {
                return v;
            }
        }
        return null;
    }

    /**
     * dispatchDraw gets invoked when all the child views are about to be drawn.
     * By overriding this method, the hover cell (BitmapDrawable) can be drawn
     * over the listview's mItems whenever the listview is redrawn.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHoverCell != null) {
            mHoverCell.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER_ID) {
                    break;
                }
            case MotionEvent.ACTION_CANCEL:
                touchEventsCancelled();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * Resets all the appropriate fields to a default state.
     */
    private void touchEventsCancelled() {
        View mobileView = getViewForID(mMobileItemId);
        if (mCellIsMobile) {
            mMobileItemId = INVALID_ID;
            mobileView.setVisibility(VISIBLE);
            mHoverCell = null;
            invalidate();
        }
        mCellIsMobile = false;
        mActivePointerId = INVALID_POINTER_ID;
    }

    public void setCheeseList(ArrayList<String> cheeseList) {
        mCheeseList = cheeseList;
    }
}