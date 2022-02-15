package com.dev_marinov.myweathernow;

import android.app.AlertDialog;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class FragmentMenuWeather extends Fragment {

    View frag;
    TextInputEditText textInputEditTextCity;
    TextView tvResult;
    Button btnGet, btnChoose;
    String output = "";

    private AlphaAnimation alphaAnim_btnGet, alphaAnim_btChoose;

    private ProgressBar mHorizontalProgressBar; // прогресс бар для имитации загрузки
    TextView tvShowProgress; // сообщение о процессе загрузки

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

        tvShowProgress = frag.findViewById(R.id.tvShowProgress);
        mHorizontalProgressBar = frag.findViewById(R.id.mHorizontalProgressBar);
        mHorizontalProgressBar.setVisibility(View.INVISIBLE);

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
                        tvShowProgress.setText("");
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

                if (city.equals("")) // если ничего не введено, то сообщение
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Please, write the name of the city or country", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // проверка интернера при отправке запроса на сервер
                    if (CheckNetwork.isInternetAvailable(getContext())) //возвращает true, если интернет доступен
                    {
                        // надо bool если пользователь сначала ввел правильный адрес, а потом неправльный
                        // и чтобы на неправильный запрос на экране не выводлась погода предыдущего запроса
                        // сохраннего в строке output
                            ((MainActivity)getActivity()).setMyInterFace(new MainActivity.MyInterFace() {
                                @Override
                                public void methodInterface(Boolean bool) {
                                        Log.e("333FRAG_MENU","-bool-" + bool);
                                    if(bool)
                                    {
                                        myAsyncTask myAsyncTask = new myAsyncTask();
                                        myAsyncTask.execute(output); //список адресов файлов для загрузки
                                    }
                                }
                            });

                        ((MainActivity)getActivity()).getWeatherDetail(city);

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "No Internet Connection", 1000).show();
                            }
                        });
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
                    tvShowProgress.setText("");
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

    public void getOutPut(String outputStr) // метод получения данных погоды и отображения
    {
        //Log.e("666","-outputStr-" + outputStr);
        output = outputStr;
    }

    // получение названия выбранной страны из клика адаптера и отображение в эдиттекст
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

    //////////////////////////////////////////////
    class myAsyncTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() { // мы устанавливаем начальный текст перед выполнением задачи.
            super.onPreExecute();
            tvShowProgress.setText("data search...");
            mHorizontalProgressBar.setVisibility(View.VISIBLE);
            mHorizontalProgressBar.setProgress(0); // показать пользователю начало процесса
        }

        // Методы onProgressUpdate() выполняются в потоке UI, потому мы можем смело обращаться к нашим компонентам UI.
        // Метод onProgressUpdate() используется для вывода промежуточных результатов и имеет доступ к UI.
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tvShowProgress.setText("process...");
            // tvShowProgress.setText("Этаж: " + values[0]); // было
            mHorizontalProgressBar.setProgress(values[0]);
        }


        @Override
        protected Void doInBackground(String... urls) { // было (Void... voids)
            try {
                // тяжелый код который должен быть тут
                int counter = 0;
                for (String url : urls) {
                    Log.e("666","-url-" + url);
                    Log.e("666","-urls-" + urls);
                    // загружаем файл или лезем на другой этаж
                    getFloor(counter);
                    Log.e("666","-counter до publishProgress-");
                    // выводим промежуточные результаты
                    // Таким образом, метод publishProgress() является своеобразным мостиком для передачи данных
                    // из doInBackground() в onProgressUpdate(). Мы передаём значение счётчика, которое выводится в текстовой метке.
                    // Когда мы в методе doInBackground() вызываем метод publishProgress() и передаём туда данные,
                    // то срабатывает метод onProgressUpdate(), который получает эти данные. Тип принимаемых данных
                    // равен второму типу из угловых скобок, у нас это Integer.
                    publishProgress(++counter);
                    Log.e("666","-counter после publishProgress-");
                }
                TimeUnit.SECONDS.sleep(1); // задержка выполнения участка try
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Методы onPostExecute() выполняются в потоке UI, потому мы можем смело обращаться к нашим компонентам UI.
        // onPostExecute() вызываются системой в начале и конце выполнения задачи
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvShowProgress.setText("successful data received ✔");
            tvResult.setText(output);
            //Log.e("666","-output-" + output);
            //mStartButton.setVisibility(View.VISIBLE);
            mHorizontalProgressBar.setProgress(100);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mHorizontalProgressBar.setVisibility(View.INVISIBLE);
                }
            },1000);
        }

// Здесь мы реализуем свою логику тяжёлой работы. Пока у нас здесь просто пауза на одну секунду.
        private void getFloor(int floor) throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }
        //////////////////////////////////////////////
    }


}