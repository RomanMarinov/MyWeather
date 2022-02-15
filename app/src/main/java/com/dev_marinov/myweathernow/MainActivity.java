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
    public void getWeatherDetail(String city) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestWeather requestWeather = new RequestWeather(MainActivity.this);
                requestWeather.method(city);
            }
        }).start();
    }

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

//    public String method(String city)
//    {
//        Log.e("333MAIN_ACT", "-сработал getWeatherDetail-");
//        String tempUrl =  "";
//        if(!city.equals(""))
//        {
//            tempUrl = url + "?q=" + city + "&appid=" + appId;
//
//            // сайт просмотра json http://jsonviewer.stack.hu/
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.e("333MAIN_ACT", "-response-" + response);
//
//
//                    String output = "";
//                    try {
//                        // строку response в JSONObject
//                        JSONObject jsonObjectResponce = new JSONObject(response);
//                        // получаем из jsonObjectResponce данные по ключу weather
//                        JSONArray jsonArrayWeather = jsonObjectResponce.getJSONArray("weather");
//                        // получаем из jsonArrayWeather получаем данные индекс 0
//                        JSONObject jsonObject_O = jsonArrayWeather.getJSONObject(0);
//                        // получаем из jsonObject_O строку description
//                        String description = jsonObject_O.getString("description"); // -----------------> данные для отображения=
//
//                        // получаем из jsonObjectResponce данные по ключу main
//                        JSONObject jsonObjectMain = jsonObjectResponce.getJSONObject("main");
//                        // получаем из jsonObjectMain double значение по ключу temp
//                        double temp = jsonObjectMain.getDouble("temp") - 273.15; // -----------------> данные для отображения=
//                        // получаем из jsonObjectMain double значение по ключу feels_like
//                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15; // -----------------> данные для отображения=
//                        // получаем из jsonObjectMain double значение по ключу temp_min
//                        double temp_min = jsonObjectMain.getDouble("temp_min") - 273.15; // -----------------> данные для отображения=
//                        // получаем из jsonObjectMain double значение по ключу temp_max
//                        double temp_max = jsonObjectMain.getDouble("temp_max") - 273.15; // -----------------> данные для отображения=
//                        // получаем из jsonObjectMain int значение по ключу pressure
//                        int pressure = jsonObjectMain.getInt("pressure"); // -----------------> данные для отображения=
//                        // получаем из jsonObjectMain int значение по ключу humidity
//                        int humidity = jsonObjectMain.getInt("humidity"); // -----------------> данные для отображения=
//
//                        // получаем из jsonObjectResponce данные int по ключу clouds
//                        JSONObject jsonObjectClouds = jsonObjectResponce.getJSONObject("clouds"); // -----------------> данные для отображения=
//                        int clouds = jsonObjectClouds.getInt("all");
//
//                        // получаем из jsonObjectResponce данные по ключу wind
//                        JSONObject jsonObjectWind = jsonObjectResponce.getJSONObject("wind");
//                        // получаем из jsonObjectWind строку speed
//                        String speed = jsonObjectWind.getString("speed"); // -----------------> данные для отображения=
//                        // получаем из jsonObjectWind строку deg
//                        String deg = jsonObjectWind.getString("deg"); // -----------------> данные для отображения=
//
//                        // получаем из jsonObjectResponce строку name
//                        String name = jsonObjectResponce.getString("name"); // -----------------> данные для отображения=
//
//                        output = "City: " + name
//                                + "\ndescription: " + description
//                                + "\ntemp: " + decimalFormat.format(temp)  + " ℃"
//                                + "\ntemp min: " + decimalFormat.format(temp_min) + " ℃"
//                                + "\ntemp max: " + decimalFormat.format(temp_max)  + " ℃"
//                                + "\nfeels like: " + decimalFormat.format(feelsLike) + " ℃"
//                                + "\npreassure: " + pressure + " hPa"
//                                + "\nhumidity: " + humidity + " %"
//                                + "\ncloudnes: " + clouds + " %"
//                                + "\nwind speed: " + speed + " m/s"
//                                + "\ndeg: " + deg;
//
//                        //finalOutput = output; // переменная для хранения данных о погоде
//                        //flagCheckCorrectRespone = true;
//
//                        myInterFace.methodInterface(true);
//
//                        // передача во врагмент данных
//                        FragmentMenuWeather fragmentMenuWeather = (FragmentMenuWeather) getSupportFragmentManager().findFragmentById(R.id.ll_frag_menu_weather);
//                        if(fragmentMenuWeather != null)
//                        {
//                            fragmentMenuWeather.getOutPut(output);
//                            Log.e("333MAIN_ACT", "-output-" + output);
//                        }
//
//                    } catch (JSONException e) {
//                        Log.e("333MAIN_ACT", "-try catch-" + e);
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    myInterFace.methodInterface(false);
//
//                    //flagCheckCorrectRespone = false;
//                    Toast.makeText(getApplicationContext(), "error searching\n" + error.toString().trim(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//            requestQueue.add(stringRequest);
//        }
//        return "333";
//    }

}