package com.news.weezo.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    protected OnItemClickListener listener;

    private GestureDetector gestureDetector;

    private View childView;
    private View touchedView;

    private int childViewPosition;


    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent event) {
        childView = view.findChildViewUnder(event.getX(), event.getY());
        childViewPosition = view.getChildPosition(childView);
        if (childView != null) {
            final List<View> viewHierarchy = new ArrayList<View>();
            // Important: x and y are raw screen coordinates here
            getViewHierarchyUnderChild(view, event.getRawX(), event.getRawY(), viewHierarchy);

            if (viewHierarchy.size() > 0) {
                touchedView = viewHierarchy.get(0);
            }
        }
        return childView != null && gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {

        /**
         * Called when an item is clicked.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        public void onItemClick(View childView, int position);

        /**
         * Called when an item is long pressed.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        public void onItemLongPress(View childView, int position);

    }

    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (touchedView != null) {
                listener.onItemClick(touchedView, childViewPosition);
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if (childView != null) {

                listener.onItemLongPress(childView, childViewPosition);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            // Best practice to always return true here.
            // http://developer.android.com/training/gestures/detector.html#detect
            return true;
        }
    }

    public void getViewHierarchyUnderChild(ViewGroup root, float x, float y, List<View> viewHierarchy) {
        int[] location = new int[2];
        final int childCount = root.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            final View child = root.getChildAt(i);
            child.getLocationOnScreen(location);
            final int childLeft = location[0], childRight = childLeft + child.getWidth();
            final int childTop = location[1], childBottom = childTop + child.getHeight();

            if (child.isShown() && x >= childLeft && x <= childRight && y >= childTop && y <= childBottom) {
                viewHierarchy.add(0, child);
            }
            if (child instanceof ViewGroup) {
                getViewHierarchyUnderChild((ViewGroup) child, x, y, viewHierarchy);
            }
        }
    }

}