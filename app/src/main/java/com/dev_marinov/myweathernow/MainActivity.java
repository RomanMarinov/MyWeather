package com.dev_marinov.myweathernow;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

    static final String url = "https://api.openweathermap.org/data/2.5/weather";
    static final String appId = "840d7fe8bd94345a170e94b449c68646";
    static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    // макеты для управления backstack
    LinearLayout ll_frag_start_anim, ll_frag_menu_weather, ll_frag_choose;
    static MyInterFaceSuccessfulResponse myInterFaceSuccessfulResponse; // удачный ответ по api или нет
    static MyInterFacePositionClickCode myInterFacePositionClickCode; // передача code из адаптера клки по item
    static MyInterFacePositionClickName myInterFacePositionClickName; // передача name из адаптера клки по item
    String myPositionName = ""; // сюда сохраняю название страны из списка, на которую кликнул в адаптере
    int myPositionCode; // // сюда сохраняю позицию страны у Code из списка, на которую кликнул в адаптере
    // булева для разделения (if-else) помещения переменной int в scrollToPositionWithOffset
    // если пользовался живым поиском во frgamentChoose и выбрал страну, то помещу ее позицию в scrollToPositionWithOffset
    // иначе в scrollToPositionWithOffset поместиться переменная из адаптера от findFirstVisibleItemPosition
    boolean flag = false;
    int lastVisibleItemPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll_frag_start_anim = findViewById(R.id.ll_frag_start_anim);
        ll_frag_menu_weather = findViewById(R.id.ll_frag_menu_weather);
        ll_frag_choose = findViewById(R.id.ll_frag_choose);


        setWindow();

        FragmentStartAnimation fragmentStartAnimation = new FragmentStartAnimation();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_frag_start_anim, fragmentStartAnimation);
        fragmentTransaction.commit();

        setMyInterFacePositionClickName(new MainActivity.MyInterFacePositionClickName() {
            @Override
            public void methodInterfacePosClick(String posName) {
                myPositionName = posName;
                Log.e("333", "myPositionName = " + myPositionName);
            }
        });

        setMyInterFacePositionClickCode(new MyInterFacePositionClickCode() {
            @Override
            public void methodInterfacePosClickCode(int CodePos) {
                myPositionCode = CodePos;
            }
        });

    }

    public void setWindow()
    {
        Window window = getWindow();
        // установка градиента анимации на toolbar
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Drawable background = getResources().getDrawable(R.drawable.nebo_ver_1);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS Флаг, указывающий, что это Окно отвечает за отрисовку фона для системных полос.
        // Если установлено, системные панели отображаются с прозрачным фоном, а соответствующие области в этом окне заполняются цветами,
        // указанными в Window#getStatusBarColor()и Window#getNavigationBarColor().
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(getResources().getColor(android.R.color.black));
        window.setBackgroundDrawable(background);
    }

    // метод только для myAlertDialog();
    @Override
    public void onBackPressed()
    {
        Log.e("222MAIN_ACT","ДО счетчик=" + getSupportFragmentManager().getBackStackEntryCount());

        // как только будет ноль (последний экран) выполниться else
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.e("222MAIN_ACT","ПОСЛЕ счетчик=" + getSupportFragmentManager().getBackStackEntryCount());

            // часть кода для того чтобы я просто мог только нажать кн назад и удалить view
            if(getSupportFragmentManager().getBackStackEntryCount() == 1)
            {
                Log.e("222MAIN_ACT","фрагмент должен был удалиться");
                ll_frag_choose.removeAllViews();
            }

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
    interface MyInterFaceSuccessfulResponse
    {
        void methodInterfaceSuccessfulResponse(Boolean bool);
    }
    public void setMyInterFaceSuccessfulResponse(MyInterFaceSuccessfulResponse myInterFaceSuccessfulResponse)
    {
        this.myInterFaceSuccessfulResponse = myInterFaceSuccessfulResponse;
    }

    // интерфейс для передачи флага в другой фрагмент
    interface MyInterFacePositionClickCode
    {
        void methodInterfacePosClickCode(int CodePos);
    }
    public void setMyInterFacePositionClickCode(MyInterFacePositionClickCode myInterFacePositionClickCode)
    {
        this.myInterFacePositionClickCode = myInterFacePositionClickCode;
    }

    // интерфейс для передачи флага в другой фрагмент
    interface MyInterFacePositionClickName
    {
        void methodInterfacePosClick(String posName);
    }
    public void setMyInterFacePositionClickName(MyInterFacePositionClickName myInterFacePositionClickName)
    {
        this.myInterFacePositionClickName = myInterFacePositionClickName;
    }


}