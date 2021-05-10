package com.example.stocksimulator;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ArrayList<Float> valueList = new ArrayList<Float>();

    LineDataSet set1;

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_start = findViewById(R.id.btn_start);

        LineChart chart = findViewById(R.id.lineChart);

        ArrayList<Entry> list = new ArrayList<Entry>();

        for (int i = 0; i < 15; i++) {
            valueList.add((float) Math.random() * 10);
            list.add(new Entry(i, valueList.get(i)));
        }

        set1 = new LineDataSet(list, "DataSet 1");

        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);

        TextView textView;

        Handler handler = new Handler();
        textView = findViewById(R.id.textView);

        btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    boolean isRun = false;
                    int value = 0;

                    @Override
                    public void run() {
                        isRun = true;
                        //1초마다 벨류값 1씩 증가시키는 스레드임
                        while ((isRun)) {
                            value += 1;
                            //핸들러클래스로서 post로 던질수가있음.
                            //핸들러의 post 메소드를 호출하면 Runnable 객체를 전달할 수 있습니다.
                            //핸들러로 전달된 Runnable, 객체는 메인 스레드에서 실행될 수 있으며 따라서 UI를 접근하는 코드는 Runnable 객체 안에 넣어두면 됩니다.
                            //post 메소드 이외에도 지정된 시간에 실행하는 postAtTime 메소드와 지정된 시간만큼 딜레이된 시간후 실행되는 postDelayed 메소드가 있습니다.
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("현재값 : " + value);
                                    addChartValue();
                                    refreshChart();
                                }
                            });
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                            }
                        }
                    }
                }).start(); //start()붙이면 바로실행시킨다.
            }
        });
    }

    void addChartValue(){
        valueList.add(((float) Math.random() * 10));
    }

    void refreshChart(){
        LineChart chart = findViewById(R.id.lineChart);

        ArrayList<Entry> list = new ArrayList<Entry>();
        
        int j = 0; // j : 값의 x축 위치(좌표) ?
        for(int i = 15; i >= 1; i--){
            list.add(new Entry(j, valueList.get(valueList.size()-i)));
            j++;
        }

        LineDataSet set1;
        set1 = new LineDataSet(list, "Example A");

        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }

}