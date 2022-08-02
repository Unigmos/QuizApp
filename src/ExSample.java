package es.exsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;

public class ExSample extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_app_top_menu);  //レイアウトのXMLファイル読み込み

        Button start_button = (Button) findViewById(R.id.start_button);  //ビューのidを用いてオブジェクトを関連付け　ビューの数が増える時にはViewBindingを使う方法もある
        Button stat_button = (Button) findViewById(R.id.stat_button);
        Button setting_button = (Button) findViewById(R.id.setting_button);
        start_button.setOnClickListener(new ButtonClickListener());  //ボタンクリック時のリスナー登録
        stat_button.setOnClickListener(new ButtonClickListener_stat());  //ボタンクリック時のリスナー登録
        //setting_button.setOnClickListener(new ButtonClickListener_setting());  //ボタンクリック時のリスナー登録(時間の問題で実装できなかった)
        // 問題数の初期化
        init_write_local_file();
    }

    // 出題数をリセットし、テキストファイルに書き込む処理
    private void init_write_local_file(){
        try{
            // 再書き込み処理
            FileOutputStream outputStream;

            outputStream = openFileOutput("num_question.txt", Context.MODE_PRIVATE);

            outputStream.write("1".getBytes());

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 問題のルールページに移動
    class ButtonClickListener implements OnClickListener{
        public void onClick(View v){
            Intent it = new Intent(getApplicationContext(), MyAppRule.class);  //明示的なインテントで起動する他のアクティビティを設定
            startActivity(it); //インテントの起動
        }
    }

    // 統計画面移動
    class ButtonClickListener_stat implements OnClickListener{
        public void onClick(View v){
            Intent it = new Intent(getApplicationContext(), MyAppStat.class);  //明示的なインテントで起動する他のアクティビティを設定
            startActivity(it);
        }
    }
}