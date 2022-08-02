package es.exsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyAppStat extends AppCompatActivity {
    Intent it;
    int[] stat_data = new int[2];
    int[] int_list = new int[2];
    int int_data_1, int_data_2;
    String stat_name[] = {"正解", "不正解"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_app_stat_menu);  //レイアウトのXMLファイル読み込み
        try {
            int_list = read_file();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        stat_data[0] = int_list[0];
        stat_data[1] = int_list[1];

        Button to_top_button = (Button) findViewById(R.id.to_top_button);
        it = getIntent();

        TextView correct_answers = findViewById(R.id.correct_answers);
        TextView incorrect_answers = findViewById(R.id.incorrect_answers);
        TextView percentage = findViewById(R.id.percentage);

        double correct_double = (double)(int_list[0]);
        double incorrect_double = (double)(int_list[1]);

        double percent_data = (correct_double / (correct_double + incorrect_double)) * 100;
        double round_percent = Math.round(percent_data * 100.0) / 100.0;

        correct_answers.setText("正解数　:"+ String.valueOf(int_list[0]));
        incorrect_answers.setText("不正解数:"+ String.valueOf(int_list[1]));
        percentage.setText("正答率　:"+ round_percent + "%");

        to_top_button.setOnClickListener(new ToTopListener());  //ボタンクリック時のリスナー登録
        DrawPieChart();
    }

    private int[] read_file() throws FileNotFoundException {
        String result = "";
        String[] list_result = new String[2];
        InputStream inputStream = openFileInput("point_data.txt");

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String tempString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while (true) {
                try {
                    if (!((tempString = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(tempString);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = stringBuilder.toString();
            list_result = result.split(",");

            int_data_1 = Integer.parseInt(list_result[0]);
            int_data_2 = Integer.parseInt(list_result[1]);
            int_list[0] = int_data_1;
            int_list[1] = int_data_2;
        }
        return int_list;
    }

    private void DrawPieChart() {
        //PieEntriesのリストを作成する:
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < stat_data.length; i++) {
            pieEntries.add(new PieEntry(stat_data[i], stat_name[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Test");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        //PieChartの取得:
        PieChart pie_chart = (PieChart) findViewById(R.id.pie_chart);
        pie_chart.setData(data);

        // 説明・凡例の非表示
        pie_chart.getDescription().setEnabled(false);
        pie_chart.getLegend().setEnabled(false);
        pie_chart.invalidate();

        // アニメーション
        pie_chart.animateXY(1000, 1000);
    }
    class ToTopListener implements OnClickListener{
        public void onClick(View v){
            Intent it = new Intent(getApplicationContext(), ExSample.class);  //明示的なインテントで起動する他のアクティビティを設定
            startActivity(it);
        }
    }
}