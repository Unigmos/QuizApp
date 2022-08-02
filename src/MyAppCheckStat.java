package es.exsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyAppCheckStat extends AppCompatActivity {
    Intent it;
    String stat_name[] = {"正解","不正解"};
    String correct, incorrect;
    int stat_data[] = new int[2];
    int new_correct;
    int new_incorrect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_app_check_stat);  //レイアウトのXMLファイル読み込み

        Button to_top_button = (Button) findViewById(R.id.to_top_button);
        it = getIntent();
        correct = it.getStringExtra("correct");
        incorrect = it.getStringExtra("incorrect");
        stat_data[0] = Integer.parseInt(correct);
        stat_data[1] = Integer.parseInt(incorrect);

        TextView correct_answers = findViewById(R.id.correct_answers);
        TextView incorrect_answers = findViewById(R.id.incorrect_answers);
        TextView percentage = findViewById(R.id.percentage);

        double correct_double = Double.parseDouble(correct);
        double incorrect_double = Double.parseDouble(incorrect);

        // 正答率計算
        double percent_data = (correct_double / (correct_double + incorrect_double)) * 100;
        double round_percent = Math.round(percent_data * 100.0) / 100.0;

        correct_answers.setText("正解数　:"+ correct);
        incorrect_answers.setText("不正解数:"+ incorrect);
        percentage.setText("正答率　:"+ round_percent + "%");

        to_top_button.setOnClickListener(new ToTopListener());  //ボタンクリック時のリスナー登録
        DrawPieChart();
    }

    // 円グラフ描画
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

    class ToTopListener implements View.OnClickListener {
        public void onClick(View v){
            String[] before_data = new String[2];
            try {
                before_data = read_file();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            write_data(before_data[0], before_data[1]);

            Intent it = new Intent(getApplicationContext(), ExSample.class);  //明示的なインテントで起動する他のアクティビティを設定
            startActivity(it); //インテントの起動

        }

        private String[] read_file() throws FileNotFoundException {
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
            }
            return list_result;
        }

        private void write_data(String correct_data, String incorrect_data){
            try{
                // 再書き込み処理
                FileOutputStream outputStream;

                outputStream = openFileOutput("point_data.txt", Context.MODE_PRIVATE);
                int before_correct_1 = Integer.parseInt(correct_data);
                int before_correct_2 = Integer.parseInt(correct);
                new_correct = before_correct_1 + before_correct_2;

                int before_incorrect_1 = Integer.parseInt(incorrect_data);
                int before_incorrect_2 = Integer.parseInt(incorrect);
                new_incorrect = before_incorrect_1 + before_incorrect_2;

                String str_correct = String.valueOf(new_correct);
                String str_incorrect = String.valueOf(new_incorrect);

                String str_data;
                str_data = str_correct + "," + str_incorrect;

                outputStream.write(str_data.getBytes());

                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}