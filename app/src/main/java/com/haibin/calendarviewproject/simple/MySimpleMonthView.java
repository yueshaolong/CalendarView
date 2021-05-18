package com.haibin.calendarviewproject.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;


public class MySimpleMonthView extends MonthView {
    /**
     * 周末画笔
     */
    private Paint mWeekEndTextPaint = new Paint();
    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();
    /**
     * 自定义标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private Context context;
    public MySimpleMonthView(Context context) {
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

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);


        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight /  2;
        canvas.drawCircle(cx, cy, Math.min(mItemWidth, mItemHeight) / 20 * 9, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
//        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
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
//            canvas.drawCircle(x + mItemWidth ,
//                    y + mItemHeight / 4, dipToPx(getContext(), 7), mSchemeBasicPaint);
            //画右上角文字, mSchemeTextPaint右上角文本标记的画笔
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
