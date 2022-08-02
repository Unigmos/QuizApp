package es.exsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MyAppAnswer extends AppCompatActivity {
    Intent it;
    String correct, incorrect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_app_answer);  //レイアウトのXMLファイル読み込み
        it = getIntent();
        correct = it.getStringExtra("correct");
        incorrect = it.getStringExtra("incorrect");

        String answer_symbol = it.getStringExtra("answer_symbol");
        String answer_statement = it.getStringExtra("answer_statement");
        String radio_text = it.getStringExtra("check_radio");

        TextView symbol = findViewById(R.id.answer_symbol);
        TextView statement = findViewById(R.id.answer_statement);
        TextView answer_flag = findViewById(R.id.answer_flag);

        Button next_question = (Button) findViewById(R.id.check_stat);  //ビューのidを用いてオブジェクトを関連付け
        Button finish_button = (Button) findViewById(R.id.finish_button);

        next_question.setOnClickListener(new MyAppAnswer.BackPlayScreen());  //ボタンクリック時のリスナー登録
        finish_button.setOnClickListener(new MyAppAnswer.CheckStat());  //ボタンクリック時のリスナー登録

        // 内部ストレージに書き込み
        rewrite_local_file();

        // 1文字目だけ切り出し、解答判定を行う
        if (radio_text.charAt(0) == answer_symbol.charAt(0)){
            answer_flag.setText("正解です！");

            // 型変換及び加算
            int int_correct = Integer.parseInt(correct);
            int_correct++;
            correct = String.valueOf(int_correct);

        } else {
            answer_flag.setText("不正解です");

            // 型変換及び加算
            int int_incorrect = Integer.parseInt(incorrect);
            int_incorrect++;
            incorrect = String.valueOf(int_incorrect);

        }

        symbol.setText("模範解答: " + answer_symbol);
        statement.setText(answer_statement);
    }

    private void rewrite_local_file(){
        String strings[] = read_local_file("num_question.txt", 1);
        write_local_file(strings[0]);
    }

    private String[] read_local_file(String file, int size) {
        InputStream is = null;
        BufferedReader br = null;
        String[] text_data = new String[size];
        int counter = 0;

        try {
            try {
                is = openFileInput(file);
                br = new BufferedReader(new InputStreamReader(is));

                String str;
                while ((str = br.readLine()) != null) {
                    text_data[counter] = str;
                    counter++;
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
            return text_data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void write_local_file(String data){
        try{
            // 再書き込み処理
            FileOutputStream outputStream;

            outputStream = openFileOutput("num_question.txt", Context.MODE_PRIVATE);
            int int_data = Integer.parseInt(data);
            int_data++;
            String str_data = String.valueOf(int_data);

            outputStream.write(str_data.getBytes());

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class BackPlayScreen implements OnClickListener{
        public void onClick(View v){
            Intent it = new Intent(getApplicationContext(), MyAppPlay.class);  //明示的なインテントで起動する他のアクティビティを設定
            it.putExtra("correct", correct);
            it.putExtra("incorrect", incorrect);
            startActivity(it);
        }
    }

    class CheckStat implements OnClickListener{
        public void onClick(View v){
            Intent it = new Intent(getApplicationContext(), MyAppCheckStat.class);  //明示的なインテントで起動する他のアクティビティを設定
            it.putExtra("correct", correct);
            it.putExtra("incorrect", incorrect);
            startActivity(it);
        }
    }
}