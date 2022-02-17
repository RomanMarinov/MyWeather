package com.dev_marinov.myweathernow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity{

    TextView tvM, tvY, tvW, tvE1, tvA, tvT, tvH, tvE2, tvR;
    static final String url = "https://api.openweathermap.org/data/2.5/weather";
    static final String appId = "840d7fe8bd94345a170e94b449c68646";
    static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    Animation animation;
    LinearLayout llTvAnim;
    LinearLayout ll_frag_menu_weather, ll_frag_choose;
    //static String finalOutput;
    boolean flagCheckCorrectRespone;

    static MyInterFace myInterFace;


    String value = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.e("333MAIN_ACT", "-начало hashMap.size-" + hashMap.size());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);





        ll_frag_menu_weather = findViewById(R.id.ll_frag_menu_weather);
        ll_frag_choose = findViewById(R.id.ll_frag_choose);
        llTvAnim = findViewById(R.id.llTvAnim);
        tvM = findViewById(R.id.tvM);
        tvY = findViewById(R.id.tvY);
        tvW = findViewById(R.id.tvW);
        tvE1 = findViewById(R.id.tvE1);
        tvA = findViewById(R.id.tvA);
        tvT = findViewById(R.id.tvT);
        tvH = findViewById(R.id.tvH);
        tvE2 = findViewById(R.id.tvE2);
        tvR = findViewById(R.id.tvR);

        StartAnimation startAnimation = new StartAnimation(this);
        startAnimation.wordsAnimation(); // анимация букв при старте приложения
    }

    // получить данные погоды о городе. метод срабатывает только при нажатии кн get во FragmentMenuWeather
//    public String getWeatherDetail(String city) {
//
//        Log.e("333MAIN_ACT", "-city-" + city);
//        RequestWeather requestWeather = new RequestWeather();
//        requestWeather.setMyInterFaceString(new RequestWeather.MyInterFaceString() {
//            @Override
//            public void methodInterfaceString(String output) {
//        Log.e("output=","output1="+output);
//                value = output;
//            }
//        });
//        requestWeather.method(getApplicationContext(),city);
//
//        Log.e("output=","value="+value);
//        return  value;
//    }

    // метод только для myAlertDialog();
    @Override
    public void onBackPressed()
    {
        // как только будет ноль (последний экран) выполниться else
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.e("MAIN_ACT","getFragmentManager().getBackStackEntryCount()== "
                    + getSupportFragmentManager().getBackStackEntryCount() );
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack(); // удаление фрагментов из транзакции
            myAlertDialog(); // метод реализации диалога с пользователем закрыть приложение или нет
        }
    }

    // метод реализации диалога с пользователем закрыть приложение или нет
    public void myAlertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Do you wish to exit ?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // finish used for destroyed activity
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Nothing will be happened when clicked on no button
                // of Dialog
            }
        });
        alertDialog.show();
    }


    // интерфейс для передачи флага в другой фрагмент
    interface MyInterFace
    {
        void methodInterface(Boolean bool);
    }
    public void setMyInterFace(MyInterFace myInterFace)
    {
        this.myInterFace = myInterFace;
    }
}