package es.exsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MyAppRule extends AppCompatActivity {
    Intent it;
    // correct:正解数, incorrect:不正解数
    String correct = "0", incorrect = "0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_app_play_rule);  //レイアウトのXMLファイル読み込み
        it = getIntent();

        Button play_button = (Button) findViewById(R.id.play_button);  //ビューのidを用いてオブジェクトを関連付け
        play_button.setOnClickListener(new MyAppRule.ToPlayScreen());  //ボタンクリック時のリスナー登録
    }

    class ToPlayScreen implements OnClickListener{
        public void onClick(View v){
            Intent it = new Intent(getApplicationContext(), MyAppPlay.class);  //明示的なインテントで起動する他のアクティビティを設定
            it.putExtra("correct", correct);
            it.putExtra("incorrect", incorrect);
            startActivity(it);
        }
    }
}