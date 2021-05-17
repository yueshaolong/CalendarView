package com.haibin.calendarviewproject.simple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.Article;
import com.haibin.calendarviewproject.ArticleAdapter;
import com.haibin.calendarviewproject.R;
import com.haibin.calendarviewproject.base.activity.BaseActivity;
import com.haibin.calendarviewproject.colorful.ColorfulActivity;
import com.haibin.calendarviewproject.group.GroupItemDecoration;
import com.haibin.calendarviewproject.group.GroupRecyclerView;
import com.haibin.calendarviewproject.index.IndexActivity;
import com.haibin.calendarviewproject.meizu.MeiZuActivity;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MySimpleActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnMonthChangeListener,
        View.OnClickListener {

    CalendarView mCalendarView;
    CalendarLayout mCalendarLayout;
    TextView tv_time;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MySimpleActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_my;
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

        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnCalendarInterceptListener(this);
    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        tv_time.setText(year+"年"+month+"月");
        int day = mCalendarView.getCurDay();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 23, 0xFFff0000, "事").toString(),
                getSchemeCalendar(year, month, 23, 0xFFff0000, "事"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
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
            default:
                break;
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTimeInMillis());
        Toast.makeText(this,time,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return calendar.getTimeInMillis() < System.currentTimeMillis();
    }
    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this,"整改时限至少一天",
//                calendar.toString() + (isClick ? "拦截不可点击" : "拦截设定为无效日期"),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChange(int year, int month) {
        tv_time.setText(year+"年"+month+"月");
    }
}
