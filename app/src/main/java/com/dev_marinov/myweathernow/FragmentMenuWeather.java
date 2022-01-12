package com.dev_marinov.myweathernow;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentMenuWeather extends Fragment {

    View frag;
    TextInputEditText textInputEditTextCity;
    TextView tvResult;
    Button btnGet, btnChoose;
    String output = "";
    private AlphaAnimation alphaAnim_btnGet, alphaAnim_btChoose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        frag = inflater.inflate(R.layout.fragment_menu_weather, container, false);

        animationButtons(); // анимация нажатий всех кнопок

        Log.e("333FRAG_MENU","-запустился FragmentMenuWeather-");

        textInputEditTextCity = frag.findViewById(R.id.textInputEditTextCity);
        tvResult = frag.findViewById(R.id.tvResult);
        btnGet = frag.findViewById(R.id.btnGet);
        btnChoose = frag.findViewById(R.id.btnChoose);

        // если поле ввода для получения погоды будет пусто и поле вывода инфо о погоде тоже станет пустым
        textInputEditTextCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                    if(editable.length() == 0)
                    {
                        tvResult.setText(""); // очищаем поле вывода инфо о погоде
                    }
            }
        });
            // кн получения данных о погоде
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGet.setAlpha(1f); // анимания кнопки затухание
                btnGet.startAnimation(alphaAnim_btnGet);
                String city = textInputEditTextCity.getText().toString().trim();

                if(city.equals("")) // если ничего не введено, то сообщение
                {
                    Toast.makeText(getContext(), "Please, write the name of the city or country", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // проверка интернера при отправке запроса на сервер
                    if(CheckNetwork.isInternetAvailable(getContext())) //returns true if internet available
                    {
                        ((MainActivity)getActivity()).getWeatherDetail(city);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"No Internet Connection",1000).show();
                    }
                }
            }
        });
            // кн выбора страны чтобы посмотреть там погоду и переход во фрагмент со списоком стран
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("333FRAG_MENU","-нажал на кн btnChoose-");
                btnChoose.setAlpha(1f); // анимания кнопки затухание
                btnChoose.startAnimation(alphaAnim_btChoose);

                // проверка интернера при отправке запроса на сервер
                if(CheckNetwork.isInternetAvailable(getContext())) //returns true if internet available
                {
                    textInputEditTextCity.setText("");
                    tvResult.setText("");
                    // задержка перехода во фрагмент нужна для того, чтобы до конца сработала анимация нажатия кнопки
                    // а то не красиво
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            FragmentChoose fragmentChoose = new FragmentChoose();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.ll_frag_choose, fragmentChoose, "ll_frag_choose");
                            fragmentTransaction.addToBackStack("ll_frag_choose");
                            fragmentTransaction.commit();
                        }
                    },400);
                }
                else
                {
                    Toast.makeText(getContext(),"No Internet Connection",1000).show();
                }
            }
        });

        return frag;
    }

    public void getOutPut(String output) // метод получения данных погоды и отображения
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResult.setText(output);
            }
        });
    }
    // получение названия выбранной стары из клика адаптера и отображение в эдиттекст
    public void getChooseCityInAdapter(String selectCity)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textInputEditTextCity.setText(selectCity);
            }
        });
    }

    public void animationButtons() // анимация нажатий всех кнопок
    {
        alphaAnim_btnGet = new AlphaAnimation(0.2f, 1.0f);
        alphaAnim_btnGet.setDuration(500);
        alphaAnim_btChoose = new AlphaAnimation(0.2f, 1.0f);
        alphaAnim_btChoose.setDuration(500);
    }

}