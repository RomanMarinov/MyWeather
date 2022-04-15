package com.dev_marinov.myweathernow;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class FragmentChoose extends Fragment {

    TextInputEditText editTextInputLayoutCity;
    RecyclerView rvListCity; // прокручиваемый список
    AdapterListCity adapterListCity;
    int count = -1;
    HashMap<Integer, ObjectListCity> hashMap = new HashMap<>();
    private static int DELAY = 200;

    ViewGroup viewGroup; // разметка, которая позволяет расположить один или несколько View.
    // layoutInflater класс, используемый для преобразования XML-файла макета в объекты представления динамическим способом
    LayoutInflater layoutInflater;
    GridLayoutManager gridLayoutManager;

    boolean flagPortrait, flagLandscape;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //frag = inflater.inflate(R.layout.fragment_choose, container, false);
        Log.e("777","-зашел во FragmentChoose-");
        // установить контейнер viewGroup и обработчик инфлятора
        this.viewGroup = container;
        this.layoutInflater = inflater;

        // отображать желаемую разметку и возвращать view в initInterface .
        // onCreateView() возвращает объект View, который является корневым элементом разметки фрагмента.
        return initInterface();
    }

    public View initInterface() {
        Log.e("777","зашел в initInterface");
        View view;
        if(hashMap.size() == 0)
        {
            getCity(); // запрос на получение списка стран
        }

        // если уже есть надутый макет, удалить его.
        if (viewGroup != null)
        {
            viewGroup.removeAllViewsInLayout(); // отличается от removeAllView
        }

        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;

        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_choose, viewGroup, false);
            setLayoutWithData(view, 1, ((MainActivity)getActivity()).lastVisibleItemPortrait);
            Log.e("777","- lastVisibleItemPortrait-"+ ((MainActivity)getActivity()).lastVisibleItemPortrait);
        } else {
            view = layoutInflater.inflate(R.layout.fragment_choose_horizontal, viewGroup, false);
            setLayoutWithData(view, 3, ((MainActivity)getActivity()).lastVisibleItemPortrait);
            Log.e("777","- lastVisableItemLandscape-"+ ((MainActivity)getActivity()).lastVisibleItemPortrait);
        }

        // поле ввода данных выбора страны для функции фильтр
        editTextInputLayoutCity = view.findViewById(R.id.editTextInputLayoutCity);
        editTextInputLayoutCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) { // закончил ввод последнего символа
                //Log.e("333", "editable = " + editable);
                searchTextChange(editable.toString());
            }
        });

        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    // метод срабатывает когда происходит смена ориентации экрана.
    // мы сохраняем содержимое views в переменные и перезаписываем на новое содержимое
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("777","-onConfigurationChanged-");
        // сохранить текст, который пользователь уже набрал
        String editTextInputLayoutCityString = editTextInputLayoutCity.getText().toString();

        // создать новый макет
        View view = initInterface();
        // отображать текст, который пользователь уже набрал в edittext
        editTextInputLayoutCity.setText(editTextInputLayoutCityString);

        // отображать новую раскладку на экране
        viewGroup.addView(view);

        super.onConfigurationChanged(newConfig);

        Log.e("777","-onConfigurationChanged после -");
    }

    public void getCity()  // запрос на получение списка стран
    {
        Log.e("777", "-сработал getCity()");
        hashMap.clear(); // очистка массива

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post("https://dev-marinov.ru/server/serverMyWeatherNow/serverMyWeatherNow.php", null,
            new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("222", "-onFailure error searching -" + responseString);
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    //Log.e("333", "-responseString-" + responseString);
                    try {
                        JSONArray jsonArray = new JSONArray(responseString);
                        String arrayString = jsonArray.getString(0);
                        JSONArray jsonArray1 = new JSONArray(arrayString);

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            count ++;
                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String code = jsonObject.getString("code");

                            hashMap.put(count, new ObjectListCity(name, code + " " + i));
                        }

                        adapterListCity.notifyDataSetChanged();
                        Log.e("777", "-adapterListCity.notifyDataSetChanged()-");
                        // Log.e("333", "-hashMap.size()-" + hashMap.size());
                        } catch (Exception e) {
                            Log.e("777", "-try catch-" + e);
                    }
                }
            });
    }
// вроде решение но для пересоздания макета
    // https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state
    public void setLayoutWithData(View view, int column, int lastVisableItem)
    {
        Log.e("777", "зашел setLayoutWithData");
        rvListCity = view.findViewById(R.id.rvListCity);
        rvListCity.setHasFixedSize(false);

        gridLayoutManager = new GridLayoutManager(getContext(), column);
        rvListCity.setLayoutManager(gridLayoutManager);
        adapterListCity = new AdapterListCity(getContext(), hashMap, rvListCity);
        rvListCity.setAdapter(adapterListCity);
//        adapterListCity.notifyDataSetChanged();

        if(((MainActivity)getActivity()).flag)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("ccc", "new Handler().flag" + ((MainActivity)getActivity()).flag
                                    + " myPositionCode=" + ((MainActivity)getActivity()).myPositionCode);
                            gridLayoutManager.scrollToPositionWithOffset(((MainActivity)getActivity()).myPositionCode, 0);
                            ((MainActivity)getActivity()).flag = false;
                        }
                    });
                }
            },500);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("ccc", "new Handler().flag" + ((MainActivity)getActivity()).flag);
                            gridLayoutManager.scrollToPositionWithOffset(lastVisableItem, 0);
                        }
                    });
                }
            },500);
        }

    }

    // метод для реализации фильтра поиска стран из списка
    public void searchTextChange(String newText)
    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

        HashMap<Integer, ObjectListCity> newhashMap = new HashMap<>(); // создали новый пустой массив для записи по фильтру
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.e("333","-поток-"+ Thread.currentThread().getName());
                // Log.e("222", "-hashMap.size()-" + hashMap.size());
                String userInput = newText.toLowerCase(); // преобразовываем текст в нижний регистр
                int count = -1; // счетчик для формирования нового массива
                for (int i = 0; i < hashMap.size(); i++) { // перебор старого массива с данными
                    String name = hashMap.get(i).name;
                    String code = hashMap.get(i).code;
                    // преобразовываем текст в нижний регистр и проверяем содержит ли name - userInput
                    if(name.toLowerCase().contains(userInput))
                    {
                        count++;
                        Log.e("333","-name-"+ name + " code " + code);
                        newhashMap.put(count, new ObjectListCity(name, code)); // заполняем новый массив
                    }
                }
                adapterListCity.updatelist(newhashMap); // помещаем его в адаптер
            }
        });
    }

}
