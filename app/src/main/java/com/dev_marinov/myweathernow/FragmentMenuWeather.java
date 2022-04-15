package com.dev_marinov.myweathernow;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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

    TextInputEditText textInputEditTextCity; // данные ввода города/страны
    TextView tvResult; // поле вывода информации о погоде
    Button btnGet, btnChoose; // кн получить погоду и кн перехода во fragmentChoose, чтобы выбрать страну
    String output = ""; // переменная для хранения инфо о погоде
    private AlphaAnimation alphaAnim_btnGet, alphaAnim_btChoose; // анимация нажатия кнопок
    private ProgressBar mHorizontalProgressBar; // прогресс бар для имитации загрузки
    TextView tvShowProgress; // сообщение о процессе загрузки

    ViewGroup viewGroup; // разметка, которая позволяет расположить один или несколько View.
    // layoutInflater класс, используемый для преобразования XML-файла макета в объекты представления динамическим способом
    LayoutInflater layoutInflater;

    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        Log.e("222","-зашел во FragmentMenuWeather-");

        // установить контейнер viewGroup и обработчик инфлятора
        this.viewGroup = container;
        this.layoutInflater = inflater;

        // отображать желаемую разметку и возвращать view в initInterface .
        // onCreateView() возвращает объект View, который является корневым элементом разметки фрагмента.
        return initInterface();
    }

    public View initInterface() {
        View view;

        // если уже есть надутый макет, удалить его.
        if (viewGroup != null)
        {
            viewGroup.removeAllViewsInLayout(); // отличается от removeAllView
        }
        else
        {
            Log.e("222","FragmentMenuWeather viewGroup пустой");
        }
        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;

        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_menu_weather, viewGroup, false);
            Log.e("222","портрет orientation =" + orientation);

            setLayout(view);
        } else {
            view = layoutInflater.inflate(R.layout.fragment_menu_weather_horizontal, viewGroup, false);
            Log.e("222","альбом orientation =" + orientation);

            setLayout(view);
        }

        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    // метод срабатывает когда происходит смена ориентации экрана.
    // мы сохраняем содержимое views в переменные и перезаписываем на новое содержимое
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //Log.e("222","-onConfigurationChanged FragmentMenuWeather-");
        // сохранить текст, который пользователь уже набрал
        String textInputEditTextCityString = textInputEditTextCity.getText().toString();
        // сохранить текст, который отобразился в textview tvResult
        String saveOutPut = tvResult.getText().toString();
        // сохранить текст, который отобразился в textview tvShowProgress
        String saveShowProgress = tvShowProgress.getText().toString();

        // создать новый макет
        View view = initInterface();
        // отображать текст, который пользователь уже набрал в edittext
        textInputEditTextCity.setText(textInputEditTextCityString);
        // отобразить текст который был до этого в textview tvResult
        tvResult.setText(saveOutPut);
        // отобразить текст который был до этого в textview tvShowProgress
        tvShowProgress.setText(saveShowProgress);
        // отображать новую раскладку на экране
        viewGroup.addView(view);

        super.onConfigurationChanged(newConfig);
        //getActivity().setContentView(R.layout.fragment_choose);
        Log.e("222MAIN_ACT","ДО счетчик FragmentMenuWeather  прочитано при повороте="
                + getActivity().getSupportFragmentManager().getBackStackEntryCount());
    }



    public void setLayout(View view)
    {
        myView = view;
        Log.e("222","-зашел в setLayout-");

        animationButtons(); // анимация нажатий всех кнопок

        // найти views в макете
        textInputEditTextCity = view.findViewById(R.id.textInputEditTextCity);

        tvResult = view.findViewById(R.id.tvResult);
        btnGet = view.findViewById(R.id.btnGet);
        btnChoose = view.findViewById(R.id.btnChoose);
        tvShowProgress = view.findViewById(R.id.tvShowProgress);
        mHorizontalProgressBar = view.findViewById(R.id.mHorizontalProgressBar);

        mHorizontalProgressBar.setVisibility(View.INVISIBLE); // скрыть горизонтальный прогресс бар

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
                    tvShowProgress.setText(""); // очищаем сообщение о процессе загрузки
                }
            }
        });
        // кн получения данных о погоде с проверкой интернета
        // Запускам метод getDataWeather в классе RequestWeather и ждем ответ от interface methodInterface булеву
        // если true - данные из сети получены, если false - ошибка в запросе (в onErrorResponse)
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGet.setAlpha(1f); // анимания кнопки затухание
                btnGet.startAnimation(alphaAnim_btnGet); // запуск анимации

                // записываем в переменную что ввел пользователь и удаляем пробелы
                String city = textInputEditTextCity.getText().toString().trim();

                if (city.equals("")) // если ничего не введено, то сообщение "Please, write the name of the city or country"
                {
                    getActivity().runOnUiThread(new Runnable() { // тост в главном полтоке
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Please, write the name of the city or country", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else { // иначе если что-то введено, то
                    // проверка наличия интернера на устройстве. если есть (true), то передаем параметры в getDataWeather
                    // и ждем ответа от methodInterface булеву переменную
                    if (CheckNetwork.isInternetAvailable(getContext())) //возвращает true, если интернет доступен
                    {
                        RequestWeather requestWeather = new RequestWeather(getContext());
                        requestWeather.getDataWeather(getContext(), city);

                        // ожидание ответа от methodInterface
                        ((MainActivity)getActivity()).setMyInterFaceSuccessfulResponse(new MainActivity.MyInterFaceSuccessfulResponse() {
                            @Override
                            public void methodInterfaceSuccessfulResponse(Boolean bool) {
                                if(bool) // сработает только если придет true (т.е. ответ от сервера есть)
                                {
                                    MyAsyncTask myAsyncTask = new MyAsyncTask(); // экземпляр класса задач
                                    myAsyncTask.execute(output); // передаем параметр в виде описания погоды
                                }
                            }
                        });




                    } else { // иначе сообщение пользователю об отвутствия интернета
                        getActivity().runOnUiThread(new Runnable() { // тост в гланом потоке
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "No Internet Connection", 1000).show();
                            }
                        });
                    }
                }
            }
        });
        // кн перехода во fragmentChoose со списоком страна для выбора
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("222","-btnChoose.setOnClickListener-");

                btnChoose.setAlpha(1f); // анимания кнопки затухание
                btnChoose.startAnimation(alphaAnim_btChoose); // запуск анимации кн

                // проверка интернера при отправке нового api запроса на сервер со списком стран
                if(CheckNetwork.isInternetAvailable(getContext())) // возвращает true, если интернет доступен
                {
                    // задержка перехода во фрагмент нужна для того, чтобы до конца сработала анимация нажатия кнопки
                    // а то не красиво :-)
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
                    },200);
                }
                else // иначе сообщение пользователю об отвутствия интернета
                {
                    Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getOutPut(String outputStr) // метод получения данных погоды и отображения
    {
        Log.e("222","-outputStr-" + outputStr);
        output = outputStr;
    }

    // получение названия выбранной страны из клика адаптера и отображение в эдиттекст
    public void getChooseCityInAdapter(String selectCity)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textInputEditTextCity.setText(selectCity);
                Log.e("222","-textInputEditTextCity содержимое-" + textInputEditTextCity.getText().toString());

                //((MainActivity)getActivity()).ll_frag_choose.setVisibility(View.GONE);
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

    // задача постепенно отображать пользовательскую информацию (статус загрузки, успеха и т.д.) при получении данных о погоде
    class MyAsyncTask extends AsyncTask<String, Integer, Void> {
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
    }

}