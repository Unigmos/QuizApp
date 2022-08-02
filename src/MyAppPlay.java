package es.exsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MyAppPlay extends AppCompatActivity {
    Intent it;
    String[] str_data;
    // ファイルを格納する配列(ここからランダム抽出)
    String quiz_files[] = {
            "question_1.txt", "question_2.txt", "question_3.txt", "question_4.txt", "question_5.txt",
            "question_6.txt", "question_7.txt", "question_8.txt", "question_9.txt", "question_10.txt"
                          };
    String correct, incorrect;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_app_playing);  //レイアウトのXMLファイル読み込み
        it = getIntent();
        correct = it.getStringExtra("correct");
        incorrect = it.getStringExtra("incorrect");

        // ランダム抽出したファイルの読み込み
        Random random = new Random();
        int quiz_num = (int)(Math.random()*(quiz_files.length));
        str_data = read_text(quiz_files[quiz_num]);

        TextView problem_statement = findViewById(R.id.problem_statement);
        TextView num_question = findViewById(R.id.num_question);
        TextView date_time = findViewById(R.id.date_time);
        Button check_answer = (Button) findViewById(R.id.check_answer);

        RadioButton answer_a = (RadioButton) findViewById(R.id.answer_a);
        RadioButton answer_b = (RadioButton) findViewById(R.id.answer_b);
        RadioButton answer_c = (RadioButton) findViewById(R.id.answer_c);
        RadioButton answer_d = (RadioButton) findViewById(R.id.answer_d);

        check_answer.setOnClickListener(new MyAppPlay.ToCheckAnswer());

        num_question.setText("問 " + read_local_file());

        if(str_data != null){
            problem_statement.setText(str_data[0]);
            date_time.setText(str_data[1]);
            answer_a.setText("ア　" + str_data[2]);
            answer_b.setText("イ　" + str_data[3]);
            answer_c.setText("ウ　" + str_data[4]);
            answer_d.setText("エ　" + str_data[5]);
        } else {
            problem_statement.setText("問題が参照できません");
        }
    }

    // テキストファイルの入力を配列にして返す関数
    private String[] read_text(String text_file) {
        InputStream is = null;
        BufferedReader br = null;
        String[] text_data = new String[8];
        int counter = 0;

        try {
            try {
                is = this.getAssets().open(text_file);
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

    // 問題数を読み取る処理
    private String read_local_file() {
        try {
            // 既存のファイル読み込み
            FileInputStream inputStream;
            byte[] buffer = new byte[256];

            inputStream = openFileInput("num_question.txt");
            inputStream.read(buffer);

            String data = new String(buffer, "UTF-8");
            inputStream.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class ToCheckAnswer implements OnClickListener{
        RadioGroup radio_group = findViewById(R.id.radio_group);
        RadioButton radio;
        String radio_text;
        public void onClick(View v){
            Intent it = new Intent(getApplicationContext(), MyAppAnswer.class); //明示的なインテントで起動する他のアクティビティを設定
            String answer_symbol = str_data[6];
            String answer_statement = str_data[7];
            it.putExtra("answer_symbol", answer_symbol);
            it.putExtra("answer_statement", answer_statement);

            int checkedId = radio_group.getCheckedRadioButtonId();

            if (checkedId != -1) {
                // ID取得
                radio = (RadioButton) findViewById(checkedId);
                // IDから文字取得
                radio_text = radio.getText().toString();
            } else {
                radio_text = "未選択";
            }
            it.putExtra("check_radio", radio_text);
            it.putExtra("correct", correct);
            it.putExtra("incorrect", incorrect);
            startActivity(it); //インテントの起動
        }
    }
}