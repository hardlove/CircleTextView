package com.hokai.www.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CircleTextView extends View {
    private String mTopText;
    private String mCenterText;
    private String mBottomText;
    private String mStatusText;
    private int mTextColor = Color.RED;
    private int mBackgroundColor = 0x00ff0000;
    private float mTopTextSize = 40;
    private float mCenterTextSize = 40;
    private float mBottomTextSize = 40;
    private float mStatusTextSize = 38;
    private float mBorderSize = 25; // 边框宽度
    private float mTextPadding;//上下文字的间距

    private int mBorderColor = Color.RED;
    private TextPaint mBorderPaint;
    private TextPaint mTextPaint;
    private float mTopTextWidth;
    private float mCenterTextWidth;
    private float mStatusTextWidth;
    private float mTopTextHeight;
    private float mCenterTextHeight;
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
        mCenterText = a.getString( R.styleable.CircleTextView_centerText);
        mBottomText = a.getString(R.styleable.CircleTextView_bottomText);
        mStatusText = a.getString(R.styleable.CircleTextView_statusText);
        mTextColor = a.getColor(R.styleable.CircleTextView_textColor,mTextColor);
        mBackgroundColor = a.getColor(R.styleable.CircleTextView_backgroundColor,mBackgroundColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mTopTextSize = a.getDimension(R.styleable.CircleTextView_topTextSize, mTopTextSize);
        mCenterTextSize = a.getDimension(R.styleable.CircleTextView_centerTextSize, mCenterTextSize);
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
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);


        mBorderPaint = new TextPaint();
        mBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setTextAlign(Paint.Align.LEFT);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        setBackgroundResource(android.R.color.transparent);


        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }


    private void invalidateTextPaintAndMeasurements() {
        Rect mRect = new Rect();
        if (mTopText != null) {
            mTextPaint.setTextSize(mTopTextSize);
            mTextPaint.getTextBounds(mTopText, 0, mTopText.length(), mRect);
            mTopTextWidth = mRect.width();
            mTopTextHeight = mRect.height();
        }
        if (mCenterText != null) {
            mTextPaint.setTextSize(mCenterTextSize);
            mTextPaint.getTextBounds(mCenterText, 0, mCenterText.length(), mRect);
            mCenterTextWidth = mRect.width();
            mCenterTextHeight = mRect.height();
        }
        if (mBottomText != null) {
            mTextPaint.setTextSize(mBottomTextSize);
            mTextPaint.setColor(mTextColor);
            mBottomTextWidth = mTextPaint.measureText(mBottomText);
            Paint.FontMetrics fontMetrics2 = mTextPaint.getFontMetrics();
            mBottomTextHeight = fontMetrics2.descent - fontMetrics2.ascent;
        }
        if (mStatusText != null) {
            mTextPaint.setTextSize(mStatusTextSize);
            mTextPaint.getTextBounds(mStatusText, 0, mStatusText.length(), mRect);
            mStatusTextWidth = mRect.width();
            mStatusTextHeight = mRect.height();
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
        //Draw background
        mBorderPaint.setColor(mBackgroundColor);
        mBorderPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mViewSize / 2, mViewSize / 2, mViewSize / 2, mBorderPaint);

        drawText(canvas);

        //Draw border
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawCircle(mViewSize / 2, mViewSize / 2, (mViewSize - mBorderSize) / 2, mBorderPaint);

    }

    private void drawText(Canvas canvas) {
        mTextPaint.setColor(mTextColor);
        if (TextUtils.isEmpty(mCenterText)) {
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            boolean adjust = false;
            String topStr = mTopText;
            if (mTopText != null) {
                // Draw the top text.
                int end = mTopText.length();
                if (mTopTextWidth + 2 * mStatusTextWidth > mViewSize) {

                    end = (int) (mViewSize * 1.0f / mTopTextWidth * end) - 2;
                    topStr = mTopText.substring(0, end);
                    topStr = topStr + "..";
                    end = topStr.length();
                    adjust = true;
                }
                mTextPaint.setTextSize(mTopTextSize);
                canvas.drawText(topStr, 0, end, mViewSize / 2, (mViewSize - mTextPadding) / 2, mTextPaint);
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
                mTextPaint.setTextSize(mBottomTextSize);
                canvas.drawText(bottomStr, 0, end, mViewSize / 2, (mViewSize) / 2 + mBottomTextHeight, mTextPaint);
            }
            if (mStatusText != null) {
                mTextPaint.setTextSize(mStatusTextSize);
                float offset = mTextPaint.measureText("M");
                if (adjust) {
                    canvas.drawText(mStatusText, mViewSize - 1.3f * offset, (mViewSize - mTextPadding - (mTopTextHeight - mStatusTextHeight)) / 2, mTextPaint);
                } else {
                    canvas.drawText(mStatusText, (mViewSize + mTopTextWidth + mStatusTextWidth + 0.5f * offset) / 2, (mViewSize - mTextPadding - (mTopTextHeight - mStatusTextHeight)) / 2, mTextPaint);
                }
            }
        } else {
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(mCenterTextSize);
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(mCenterText, mViewSize / 2, baseline, mTextPaint);
        }
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
