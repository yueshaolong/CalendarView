package com.haibin.calendarviewproject.range;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.R;
import com.haibin.calendarviewproject.base.activity.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRangeActivity extends BaseActivity implements
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnCalendarRangeSelectListener,
        CalendarView.OnMonthChangeListener,
        View.OnClickListener {
    CalendarLayout mCalendarLayout;
    CalendarView mCalendarView;
    private TextView tv_time;
    private int mCalendarHeight;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MyRangeActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_range_my;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();

        tv_time = findViewById(R.id.tv_time);
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.iv_right).setOnClickListener(this);
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnCalendarRangeSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        //设置日期拦截事件，当前有效
        mCalendarView.setOnCalendarInterceptListener(this);

        findViewById(R.id.tv_commit).setOnClickListener(this);

        mCalendarHeight = dipToPx(this, 50);
        mCalendarView.post(new Runnable() {
            @Override
            public void run() {
                mCalendarView.scrollToCurrent();
            }
        });
    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        tv_time.setText(year+"年"+month+"月");
        int day = mCalendarView.getCurDay();
        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, day, 0xFFff0000, "今").toString(),
                getSchemeCalendar(year, month, day, 0xFFff0000, "今"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                mCalendarView.scrollToPre();
                break;
            case R.id.iv_right:
                mCalendarView.scrollToNext();
                break;
            case R.id.tv_commit:
                List<Calendar> calendars = mCalendarView.getSelectCalendarRange();
                if (calendars == null || calendars.size() == 0) {
                    Toast.makeText(this, "请选择开始时间和结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Calendar c : calendars) {
                    Log.e("SelectCalendarRange", c.toString()
                            + " -- " + c.getTimeInMillis()
                            + "  --  " + c.getLunar());
                }
                Toast.makeText(this, String.format("选择了%s个日期: %s —— %s", calendars.size(),
                        calendars.get(0).toString(), calendars.get(calendars.size() - 1).toString()),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 屏蔽某些不可点击的日期，可根据自己的业务自行修改
     * 如 calendar > 2018年1月1日 && calendar <= 2020年12月31日
     * @param calendar calendar
     * @return 是否屏蔽某些不可点击的日期，MonthView和WeekView有类似的API可调用
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return false;
        //return calendar.getTimeInMillis()<getCurrentDayMill() ;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this,
                calendar.toString() + (isClick ? "拦截不可点击" : "拦截设定为无效日期"),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
        tv_time.setText(year+"年"+month+"月");
    }

    @Override
    public void onCalendarSelectOutOfRange(Calendar calendar) {
    }

    @Override
    public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {
        Toast.makeText(this,
                calendar.toString() + (isOutOfMinRange ? "小于最小选择范围" : "超过最大选择范围"),
                Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
    }
}
