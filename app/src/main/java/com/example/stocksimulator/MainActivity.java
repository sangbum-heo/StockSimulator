package com.example.stocksimulator;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    ArrayList<Float> valueList = new ArrayList<Float>();
    
    int range = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_start = findViewById(R.id.btn_start);
        Button btn_exit = findViewById(R.id.btn_exit);
        Button btn_test = findViewById(R.id.btn_test);

        LineChart chart = findViewById(R.id.lineChart);

        ArrayList<Entry> list = new ArrayList<Entry>();

        // 시작 값은 1000으로 고정
        valueList.add((float) 1000);

        for (int i = 0; i < 9; i++) {
            addValueListItem();
        }
        refreshChart();

        Handler handler = new Handler();
        TextView textCount = findViewById(R.id.textCount);
        TextView textValue = findViewById(R.id.textValue);
        TextView textRange = findViewById(R.id.textRange);

        btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    boolean isRun = false;
                    int count = 0;

                    @Override
                    public void run() {
                        isRun = true;
                        //0.5초마다 벨류값 1씩 증가 / 차트 값 변경하는 쓰레드
                        while ((isRun)) {
                            count += 1;
                            //핸들러클래스로서 post로 던질수가있음.
                            //핸들러의 post 메소드를 호출하면 Runnable 객체를 전달할 수 있습니다.
                            //핸들러로 전달된 Runnable, 객체는 메인 스레드에서 실행될 수 있으며 따라서 UI를 접근하는 코드는 Runnable 객체 안에 넣어두면 됩니다.
                            //post 메소드 이외에도 지정된 시간에 실행하는 postAtTime 메소드와 지정된 시간만큼 딜레이된 시간후 실행되는 postDelayed 메소드가 있습니다.
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    float value = valueList.get(valueList.size()-1);

                                    textCount.setText("Count : " + count);
                                    textValue.setText("현재 값 : " + value);
                                    textRange.setText("range : ± " + (100 / range) + "%");

                                    addValueListItem();
                                    refreshChart();
                                }
                            });
                            try {
                                // 0.5초 주기로 반복하도록 딜레이 설정
                                Thread.sleep(500);
                            } catch (Exception e) {
                            }
                        }
                    }
                }).start(); //start()붙이면 바로실행시킨다.
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int nextRange =  ((int) (Math.random() * 16) + 4);
                range = nextRange;

            }
        });
    }

    // valueList 에 새로운 값을 넣는 함수
    void addValueListItem(){
        float lastValue = valueList.get(valueList.size()-1);

        // 현재의 오류 : 새로운 값은 마지막 값을 기준으로 ± 를 하기 때문에 새로운 값을 계속 add 하면 값이 점점 조금씩 작아진다.
        // ex) 10%로 가정) 100 -> 110 -> 99 / 100 -> 90 -> 99
        valueList.add(lastValue * ( 1 + (((float) Math.random() * 2 ) - 1 ) / range ));
    }

    // valueList 값을 이용해서 새로운 차트 생성해서 보여주는 함수
    void refreshChart(){
        LineChart chart = findViewById(R.id.lineChart);

        ArrayList<Entry> list = new ArrayList<Entry>();

        // 한 번에 표시할 아이템 수
        int k = 10;

        // valueList 에서 마지막 k개 값을 가져와서 list (ArrayList<Entry>) 에 add 한다
        for(int i = 1; i <= 10; i++){
            float nextValue = valueList.get(valueList.size()-k);
            list.add(new Entry(i, nextValue));
            k--;
        }

        // LineDataSet set1에  list를 Example A라는 이름으로 넣는다.
        LineDataSet set1;
        set1 = new LineDataSet(list, "Example A");

        // 색 변경
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // dataSets에 Example A (set1)을 add 한다
        // 하나의 차트에 그래프가 여러개 들어갈 수 있기 때문에 이렇게 한다.
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        // R.id.LineChart (chart) - 드디어 chart 에 값을 넣어준다
        LineData data = new LineData(dataSets);
        chart.setData(data);

        // invalidate 를 해주어야 변경된 차트가 정상적으로 보인다.
        chart.invalidate();
    }

}