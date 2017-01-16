package com.ch3d.android.quicktiles;

/**
 * Created by Dmitry on 6/20/2016.
 */
public class TileState {

    private int mDrawableId;
    private final int mPrimaryValue;
    private final int mSecondaryValue;
    private int mTitleResId;

    public TileState(int drawable, int primaryValue, int secondaryValue, int strResId) {
        mDrawableId = drawable;
        mPrimaryValue = primaryValue;
        mSecondaryValue = secondaryValue;
        mTitleResId = strResId;
    }

    public int getPrimaryValue() {
        return mPrimaryValue;
    }

    public int getSecondaryValue() {
        return mSecondaryValue;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getDrawableId() {
        return mDrawableId;
    }
}
