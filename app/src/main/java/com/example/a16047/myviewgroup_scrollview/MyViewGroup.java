package com.example.a16047.myviewgroup_scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import static android.content.ContentValues.TAG;

/**
 * Created by 16047 on 2018/3/7.
 */

public class MyViewGroup extends ViewGroup {
    private int lastX;
    private int lastY;
    private Scroller mScroller;
    private int mStart;
    private int mScreenHeight;
    private int mEnd;

    public MyViewGroup(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mScroller=new Scroller(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count=getChildCount();

        for(int i=0;i<count;i++){
            View childView=getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();
        Log.i(TAG, "onLayout: childCount="+childCount);
        MarginLayoutParams mlp= (MarginLayoutParams) getLayoutParams();
        mScreenHeight=getResources().getDisplayMetrics().heightPixels;
        Log.i(TAG, "onLayout: mScreenHeight"+mScreenHeight);
        mlp.height=mScreenHeight*childCount;
        setLayoutParams(mlp);
        for(int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            if(childView.getVisibility()!=View.GONE){
                childView.layout(l,i*mScreenHeight,r,(i+1)*mScreenHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x= (int) event.getX();
        int y= (int) event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                lastY=y;
                mStart=getScrollY();
                Log.i(TAG, "onTouchEvent: lastY="+lastY);
                Log.i(TAG, "onTouchEvent: mStart="+mStart);
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy=lastY-y;
               if(getScrollY()<0){
                    dy=0;
                }
                if(getScrollY()>mScreenHeight*(getChildCount()-1)){
                    dy=0;
                }
                Log.i(TAG, "onTouchEvent: dy="+dy);
                scrollBy(0,dy);
                lastY=y;
                break;
            case MotionEvent.ACTION_UP:
                mEnd=getScrollY();
                int dScrollY=mEnd-mStart;
                Log.i(TAG, "onTouchEvent: dScrollY="+dScrollY);
                if(dScrollY>0){
                    if(dScrollY<mScreenHeight/3){
                        mScroller.startScroll(
                                0,getScrollY(),
                                0,-dScrollY);
                    }else{
                        mScroller.startScroll(
                                0,getScrollY(),
                                0,mScreenHeight-dScrollY
                        );
                    }
                }else{
                    if(-dScrollY<mScreenHeight/3){
                        mScroller.startScroll(
                                0,getScrollY(),
                                0,-dScrollY);
                    }else {
                        mScroller.startScroll(
                                0,getScrollY(),
                                0,-mScreenHeight-dScrollY
                        );
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(0,mScroller.getCurrY());
            postInvalidate();
        }
    }

}
