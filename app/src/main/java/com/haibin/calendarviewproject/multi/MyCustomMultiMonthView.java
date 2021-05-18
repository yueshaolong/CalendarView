package com.haibin.calendarviewproject.multi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MultiMonthView;

public class MyCustomMultiMonthView extends MultiMonthView {
    private Context context;
    private int mRadius;
    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();
    /**
     * 周末画笔
     */
    private Paint mWeekEndTextPaint = new Paint();
    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    public MyCustomMultiMonthView(Context context) {
        super(context);
        this.context = context;

        mWeekEndTextPaint.setColor(0xff666666);
        mWeekEndTextPaint.setTextSize(dipToPx(getContext(), 18));
        mWeekEndTextPaint.setAntiAlias(true);
        mWeekEndTextPaint.setTextAlign(Paint.Align.CENTER);

        mSolarTermTextPaint.setColor(0xff489dff);
        mSolarTermTextPaint.setTextSize(dipToPx(getContext(), 10));
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);
    }


    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme,
                                     boolean isSelectedPre, boolean isSelectedNext) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        if (isSelectedPre) {
            if (isSelectedNext) {
                canvas.drawRect(x, cy - mRadius, x + mItemWidth, cy + mRadius, mSelectedPaint);
            } else {//最后一个，the last
                canvas.drawRect(x, cy - mRadius, cx, cy + mRadius, mSelectedPaint);
                canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
            }
        } else {
            if(isSelectedNext){
                canvas.drawRect(cx, cy - mRadius, x + mItemWidth, cy + mRadius, mSelectedPaint);
            }
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        }

        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {
//        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
//        float baselineY = mTextBaseLine + y;
//        int cx = x + mItemWidth / 2;

//        boolean isInRange = isInRange(calendar);
//        boolean isEnable = !onCalendarIntercept(calendar);
//
//        if (isSelected) {
//            canvas.drawText(String.valueOf(calendar.getDay()),
//                    cx,
//                    baselineY,
//                    mSelectTextPaint);
//        } else if (hasScheme) {
//            canvas.drawText(String.valueOf(calendar.getDay()),
//                    cx,
//                    baselineY,
//                    calendar.isCurrentDay() ? mCurDayTextPaint :
//                            calendar.isCurrentMonth() && isInRange && isEnable? mSchemeTextPaint : mOtherMonthTextPaint);
//
//        } else {
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
//                    calendar.isCurrentDay() ? mCurDayTextPaint :
//                            calendar.isCurrentMonth() && isInRange && isEnable? mCurMonthTextPaint : mOtherMonthTextPaint);
//        }

        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;

        //画日期数字
        canvas.drawText(String.valueOf(calendar.getDay()), cx, cy,
                isSelected ? mSelectTextPaint :
                        calendar.isCurrentDay() ? mCurDayTextPaint :
                                !calendar.isCurrentMonth() ? mOtherMonthTextPaint :
                                        calendar.isWeekend() ? mWeekEndTextPaint : mCurMonthTextPaint);

        //画农历
        canvas.drawText(calendar.getLunar(), cx, cy + mItemHeight / 4,
                isSelected ? mSelectedLunarTextPaint :
                        calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                (!TextUtils.isEmpty(calendar.getSolarTerm()) ||
                                        !TextUtils.isEmpty(calendar.getGregorianFestival()) ||
                                        !TextUtils.isEmpty(calendar.getTraditionFestival())) ? mSolarTermTextPaint :
                                        mCurMonthLunarTextPaint);

        if (hasScheme) {
            //画右上角
            //画右上角标记的圆形背景
//            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2,
//                    y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicPaint);
            //画右上角文字
            if(isSelected){
                mSchemeTextPaint.setColor(0xffffffff);
            }else {
                switch (calendar.getScheme()) {
                    case "班":
                        mSchemeTextPaint.setColor(0xffff0000);
                        break;
                    default:
                        mSchemeTextPaint.setColor(0xff489dff);
                        break;
                }
            }
            mSchemeTextPaint.setTextSize(dipToPx(context, 8));
            canvas.drawText(calendar.getScheme(), x + mItemWidth - mItemWidth / 4,
                    y + mItemHeight / 3, mSchemeTextPaint);

//            canvas.drawCircle(cx ,
//                    y + mItemHeight - mItemHeight / 7, dipToPx(getContext(), 2), mPointPaint);
        }
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
