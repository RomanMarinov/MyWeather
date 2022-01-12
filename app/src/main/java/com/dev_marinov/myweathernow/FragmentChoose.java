package com.dev_marinov.myweathernow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class FragmentChoose extends Fragment{

    View frag;
    TextInputEditText editTextInputLayoutCity;
    RecyclerView rvListCity; // прокручиваемый список
    AdapterListCity adapterListCity;
    int count = -1;
    HashMap<Integer, ObjectListCity> hashMap = new HashMap<>();
    private static int DELAY = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        frag = inflater.inflate(R.layout.fragment_choose, container, false);

        editTextInputLayoutCity = frag.findViewById(R.id.editTextInputLayoutCity);
        rvListCity = frag.findViewById(R.id.rvListCity);

        getCity(); // запрос на получение списка стран

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvListCity.setLayoutManager(linearLayoutManager);

        adapterListCity = new AdapterListCity(getContext(), hashMap);
        rvListCity.setAdapter(adapterListCity);

        // поле ввода данных выбора страны для функции фильтр
        editTextInputLayoutCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) { // закончил ввод последнего символа
                //Log.e("333FRAG_CHOO", "editable = " + editable);
                searchTextChange(editable.toString());
            }
        });

        return frag;
    }

    public void getCity()  // запрос на получение списка стран
    {
        hashMap.clear(); // очистка массива

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post("https://dev-marinov.ru/server/serverMyWeatherNow/serverMyWeatherNow.php", null,
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("333MAIN_ACT", "-onFailure error searching -" + responseString);
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        //Log.e("333MAIN_ACT", "-responseString-" + responseString);

                        try {
                            JSONArray jsonArray = new JSONArray(responseString);

                            String arrayString = jsonArray.getString(0);

                            JSONArray jsonArray1 = new JSONArray(arrayString);

                            for (int i = 0; i < jsonArray1.length(); i++) {

                                count ++;
                                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String code = jsonObject.getString("code");

                                hashMap.put(count, new ObjectListCity(name, code));
                            }
                            adapterListCity.notifyDataSetChanged();

                            // Log.e("333MAIN_ACT", "-hashMap.size()-" + hashMap.size());
                        } catch (Exception e) {
                            Log.e("333MAIN_ACT", "-try catch-" + e);
                        }
                    }
                });
    }


    // задержка. после выбора валюты пройдет немного времени мы вернемся во FragmentCalculation
    public void myDelay()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().onBackPressed();
            }
        }, DELAY);
    }

    // метод для реализации фильтра поиска стран из списка
    public void searchTextChange(String newText)
    {
        String userInput = newText.toLowerCase(); // преобразовываем текст в нижний регистр
        HashMap<Integer, ObjectListCity> newhashMap = new HashMap<>(); // создали новый пустой массив для записи по фильтру
        int count = -1; // счетчик для формирования нового массива
        for (int i = 0; i < hashMap.size(); i++) { // перебор старого массива с данными
            String name = hashMap.get(i).name;
            // преобразовываем текст в нижний регистр и проверяем содержит ли name - userInput
            if(name.toLowerCase().contains(userInput))
            {
                count++;
                newhashMap.put(count, new ObjectListCity(name,null)); // заполняем новый массив
            }
        }
        adapterListCity.updatelist(newhashMap); // помещаем его в адаптер
    }

}