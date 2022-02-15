package com.dev_marinov.myweathernow;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class RequestWeather {

    String output = "";
    Context context;

    public RequestWeather(MainActivity mainActivity)
    {
        this.context = mainActivity;
    }

    MainActivity mainAct = ((MainActivity)context); // чтобы не писать каждый раз длиннную строку



    public String method(String city)
    {
        Log.e("333MAIN_ACT", "-сработал getWeatherDetail-");
        String tempUrl =  "";
        if(!city.equals(""))
        {
            tempUrl = MainActivity.url + "?q=" + city + "&appid=" + MainActivity.appId;

            // сайт просмотра json http://jsonviewer.stack.hu/
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("333MAIN_ACT", "-response-" + response);

                    try {
                        // строку response в JSONObject
                        JSONObject jsonObjectResponce = new JSONObject(response);
                        // получаем из jsonObjectResponce данные по ключу weather
                        JSONArray jsonArrayWeather = jsonObjectResponce.getJSONArray("weather");
                        // получаем из jsonArrayWeather получаем данные индекс 0
                        JSONObject jsonObject_O = jsonArrayWeather.getJSONObject(0);
                        // получаем из jsonObject_O строку description
                        String description = jsonObject_O.getString("description"); // -----------------> данные для отображения=

                        // получаем из jsonObjectResponce данные по ключу main
                        JSONObject jsonObjectMain = jsonObjectResponce.getJSONObject("main");
                        // получаем из jsonObjectMain double значение по ключу temp
                        double temp = jsonObjectMain.getDouble("temp") - 273.15; // -----------------> данные для отображения=
                        // получаем из jsonObjectMain double значение по ключу feels_like
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15; // -----------------> данные для отображения=
                        // получаем из jsonObjectMain double значение по ключу temp_min
                        double temp_min = jsonObjectMain.getDouble("temp_min") - 273.15; // -----------------> данные для отображения=
                        // получаем из jsonObjectMain double значение по ключу temp_max
                        double temp_max = jsonObjectMain.getDouble("temp_max") - 273.15; // -----------------> данные для отображения=
                        // получаем из jsonObjectMain int значение по ключу pressure
                        int pressure = jsonObjectMain.getInt("pressure"); // -----------------> данные для отображения=
                        // получаем из jsonObjectMain int значение по ключу humidity
                        int humidity = jsonObjectMain.getInt("humidity"); // -----------------> данные для отображения=

                        // получаем из jsonObjectResponce данные int по ключу clouds
                        JSONObject jsonObjectClouds = jsonObjectResponce.getJSONObject("clouds"); // -----------------> данные для отображения=
                        int clouds = jsonObjectClouds.getInt("all");

                        // получаем из jsonObjectResponce данные по ключу wind
                        JSONObject jsonObjectWind = jsonObjectResponce.getJSONObject("wind");
                        // получаем из jsonObjectWind строку speed
                        String speed = jsonObjectWind.getString("speed"); // -----------------> данные для отображения=
                        // получаем из jsonObjectWind строку deg
                        String deg = jsonObjectWind.getString("deg"); // -----------------> данные для отображения=

                        // получаем из jsonObjectResponce строку name
                        String name = jsonObjectResponce.getString("name"); // -----------------> данные для отображения=

                        output = "City: " + name
                                + "\ndescription: " + description
                                + "\ntemp: " + MainActivity.decimalFormat.format(temp)  + " ℃"
                                + "\ntemp min: " + MainActivity.decimalFormat.format(temp_min) + " ℃"
                                + "\ntemp max: " + MainActivity.decimalFormat.format(temp_max)  + " ℃"
                                + "\nfeels like: " + MainActivity.decimalFormat.format(feelsLike) + " ℃"
                                + "\npreassure: " + pressure + " hPa"
                                + "\nhumidity: " + humidity + " %"
                                + "\ncloudnes: " + clouds + " %"
                                + "\nwind speed: " + speed + " m/s"
                                + "\ndeg: " + deg;

                        //finalOutput = output; // переменная для хранения данных о погоде
                        //flagCheckCorrectRespone = true;





                        MainActivity.myInterFace.methodInterface(true);

//                        // передача во врагмент данных
//                        FragmentMenuWeather fragmentMenuWeather = (FragmentMenuWeather) getSupportFragmentManager().findFragmentById(R.id.ll_frag_menu_weather);
//                        if(fragmentMenuWeather != null)
//                        {
//                            fragmentMenuWeather.getOutPut(output);
//                            Log.e("333MAIN_ACT", "-output-" + output);
//                        }


                    } catch (JSONException e) {
                        Log.e("333MAIN_ACT", "-try catch-" + e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    MainActivity.myInterFace.methodInterface(false);

                    //flagCheckCorrectRespone = false;

                       Toast.makeText(context.getApplicationContext(), "error searching" + error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            requestQueue.add(stringRequest);

            return output;
        }
        else
        {
            return "333";
        }
    }
}
