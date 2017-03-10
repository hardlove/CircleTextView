package com.hokai.circletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CircleTextView extends View {
    private String mTopText;
    private String mBottomText;
    private String mStatusText;
    private int mTextColor = Color.RED;
    private float mTopTextSize = 40;
    private float mBottomTextSize = 40;
    private float mStatusTextSize = 38;
    private float mBorderSize = 5; // 边框宽度
    private float mTextPadding;//上下文字的间距
    private int mBorderColor = Color.RED;
    private Drawable mExampleDrawable;

    private TextPaint mTopTextPaint;
    private TextPaint mStatusTextPaint;
    private TextPaint mBottomTextPaint;
    private TextPaint mBorderPaint;
    private float mTopTextWidth;
    private float mStatusTextWidth;
    private float mTopTextHeight;
    private float mBottomTextWidth;
    private float mBottomTextHeight;
    private float mStatusTextHeight;
    private int mViewSize;

    public CircleTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleTextView, defStyle, 0);

        mTopText = a.getString( R.styleable.CircleTextView_topText);
        mBottomText = a.getString(R.styleable.CircleTextView_bottomText);
        mStatusText = a.getString(R.styleable.CircleTextView_statusText);
        mTextColor = a.getColor(R.styleable.CircleTextView_textColor,mTextColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mTopTextSize = a.getDimension(R.styleable.CircleTextView_topTextSize, mTopTextSize);
        mBottomTextSize = a.getDimension(R.styleable.CircleTextView_bottomTextSize,mBottomTextSize);
        mStatusTextSize = a.getDimension(R.styleable.CircleTextView_statusTextSize, mStatusTextSize);
        mBorderSize = a.getDimension(R.styleable.CircleTextView_borderSize,mBorderSize);
        mBorderColor = a.getColor(R.styleable.CircleTextView_borderColor, mBorderColor);

//        if (a.hasValue(R.styleable.CircleTextView_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.CircleTextView_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }

        a.recycle();

        // Set up a default TextPaint object
        mTopTextPaint = new TextPaint();
        mTopTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //x默认是‘3’这个字符的左边在屏幕的位置，如果设置了paint.setTextAlign(Paint.Align.CENTER);那就是字符的中心，y是指定这个字符baseline在屏幕上的位置。
        mTopTextPaint.setTextAlign(Paint.Align.CENTER);

        mBottomTextPaint = new TextPaint();
        mBottomTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBottomTextPaint.setTextAlign(Paint.Align.CENTER);

        mStatusTextPaint = new TextPaint();
        mStatusTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mStatusTextPaint.setTextAlign(Paint.Align.LEFT);

        mBorderPaint = new TextPaint();
        mBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setTextAlign(Paint.Align.LEFT);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        setBackgroundResource(android.R.color.transparent);


        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        if (mTopText != null) {
            mTopTextPaint.setTextSize(mTopTextSize);
            mTopTextPaint.setColor(mTextColor);
            mTopTextWidth = mTopTextPaint.measureText(mTopText);
            Paint.FontMetrics fontMetrics = mTopTextPaint.getFontMetrics();
            mTopTextHeight = fontMetrics.descent - fontMetrics.ascent;
        }
        if (mBottomText != null) {
            mBottomTextPaint.setTextSize(mBottomTextSize);
            mBottomTextPaint.setColor(mTextColor);
            mBottomTextWidth = mBottomTextPaint.measureText(mBottomText);
            Paint.FontMetrics fontMetrics2 = mBottomTextPaint.getFontMetrics();
            mBottomTextHeight = fontMetrics2.descent - fontMetrics2.ascent;
        }
        if (mStatusText != null) {
            mStatusTextPaint.setTextSize(mStatusTextSize);
            mStatusTextPaint.setColor(mTextColor);
            mStatusTextWidth = mStatusTextPaint.measureText(mStatusText);
            Paint.FontMetrics fontMetrics3 = mStatusTextPaint.getFontMetrics();
            mStatusTextHeight = fontMetrics3.descent - fontMetrics3.ascent;
        }
        mBorderPaint.setStrokeWidth(mBorderSize);
        mBorderPaint.setColor(mBorderColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(mViewSize, mViewSize);
        mTextPadding = mViewSize / 10;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        boolean adjust = false;
        String topStr = mTopText;
        if (mTopText != null) {
            // Draw the top text.
            int end = mTopText.length();
            if (mTopTextWidth + 2*mStatusTextWidth > mViewSize) {
                end = (int) (mViewSize * 1.0f / mTopTextWidth * end) - 2;
                topStr = mTopText.substring(0, end);
                topStr = topStr + "..";
                end = topStr.length();
                adjust = true;
            }
            canvas.drawText(topStr, 0, end, mViewSize / 2, (mViewSize - mTextPadding) / 2, mTopTextPaint);
        }

        if (mBottomText != null) {
            // Draw the bottom text.
            int end = mBottomText.length();
            String bottomStr = mBottomText;
            if (mBottomTextWidth > mViewSize) {
                end = (int) (mViewSize * 1.0f / mBottomTextWidth * end) - 2;
                bottomStr = mBottomText.substring(0, end) + "..";
                end = end + "..".length();

            }
            canvas.drawText(bottomStr, 0, end, mViewSize / 2, (mViewSize) / 2 + mBottomTextHeight, mBottomTextPaint);
        }
        if (mStatusText != null) {
            if (adjust) {
                float offset = mTopTextPaint.measureText(topStr.substring(0, topStr.length() - 2))/2;
                canvas.drawText(mStatusText, (mViewSize) / 2 + offset, (mViewSize - mTextPadding - (mTopTextHeight - mStatusTextHeight)) / 2, mStatusTextPaint);
            } else {
                canvas.drawText(mStatusText, (mViewSize + mTopTextWidth) / 2, (mViewSize - mTextPadding - (mTopTextHeight - mStatusTextHeight)) / 2, mStatusTextPaint);
            }
        }

        //Draw border
        canvas.drawCircle(mViewSize / 2, mViewSize / 2, (mViewSize - mBorderSize) / 2, mBorderPaint);
        //Draw center line
//        canvas.drawLine(0,mViewSize/2,mViewSize,mViewSize/2,mBorderPaint);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getTopText() {
        return mTopText;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param topText The example string attribute value to use.
     */
    public CircleTextView setTopText(String topText) {
        mTopText = topText;
        return this;
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param textColor The example color attribute value to use.
     */
    public CircleTextView setTextColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getTopTextSize() {
        return mTopTextSize;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param topTextSize The example dimension attribute value to use.
     */
    public void setTopTextSize(float topTextSize) {
        mTopTextSize = topTextSize;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    public CircleTextView setBorderColor(int color) {
        mBorderColor = color;
        return this;
    }

    public CircleTextView setBottomText(String bottomText) {
        mBottomText = bottomText;
        return this;
    }

    public CircleTextView setStatusText(String statusText) {
        mStatusText = statusText;
        return this;
    }

    @Override
    public void invalidate() {
        invalidateTextPaintAndMeasurements();
        super.invalidate();

    }
}
