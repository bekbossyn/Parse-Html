package kz.systemx.parsehtml;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static android.graphics.Typeface.DEFAULT_BOLD;


public class MainActivity extends AppCompatActivity {

    Activity mActivity;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        TextView textView = (TextView) findViewById(R.id.textView2);
        
        new Thread(new Runnable() {

            final TextView tv = (TextView) findViewById(R.id.textView);
            final String website_url = "http://eng.ibk.co.kr/lang/en/ps/excRate.jsp";

            @Override
            public void run() {
                String str;
                try {
                    Log.i("info", "trying to parse website!");
                    Document doc = Jsoup.connect(website_url).get();
                    str = doc.outerHtml();
                    boolean first_time = true;
                    for (int i=0; i < str.length() - 3; i++){
                        if ((str.charAt(i) == 'U') && (str.charAt(i + 1) == 'S') && (str.charAt(i + 2) == 'D')) {
                            if (first_time) {
                                first_time = false;
                            } else {
                                String str_substring = str.substring(i, i + 200);
                                int td = 0;
                                int my_index = 0;
                                for (int j = 0; j < str_substring.length() - 2; j++) {
                                    if (str_substring.charAt(j) == 't' && str_substring.charAt(j + 1) == 'd') {
                                        td = td + 1;
                                    }
                                    if (td > 3) {
                                        my_index = j;
                                        break;
                                    }
                                }
                                String result = str_substring.substring(my_index + 3, my_index + 11);
//                                TextView tv = (TextView) findViewById(R.id.textView);
//                                tv.setText(result);
                                str = result;
                                System.out.print(str);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    str = "Нету Интернета";
                }
                final String finalStr = str;
//                tv.setText(finalStr);

                handler.post(new Runnable() {
                    public void run() {
                        Log.i("info", "successful parsing, Posting Result");
                        String result;
                        if (finalStr.length() == 14) {
                            result = finalStr;
                        } else if (finalStr.length() < 50) {
                            result = "Текущий курс продажи: " + finalStr + " ₩";
                        } else {
                            result = "Ошибка запроса. Повторите попытку";
                        }
                        tv.setText(result);
                        tv.setTypeface(DEFAULT_BOLD);
                        tv.setTextColor(Color.YELLOW);
                    }
                });

//                tv.post(new Runnable() {
////                    @Override
//                    public void run() {
//                        tv.setText(finalStr);
//                        System.out.print("Final Str" + finalStr);
//                    }
//                });
            }
        }).start();
    }
}
