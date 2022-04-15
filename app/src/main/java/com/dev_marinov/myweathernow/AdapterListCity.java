package com.dev_marinov.myweathernow;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

class AdapterListCity extends RecyclerView.Adapter<AdapterListCity.Holder> {
    final int DELAY = 200;
    HashMap<Integer, ObjectListCity> hashMap;
    Context context;
    private AlphaAnimation alphaAnim_item; // анимация нажатия элемента списка


    public AdapterListCity(Context context, HashMap<Integer, ObjectListCity> hashMap, RecyclerView recyclerView) {
        this.context = context;
        this.hashMap = hashMap;

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
                ((MainActivity)context).lastVisibleItemPortrait = gridLayoutManager.findFirstVisibleItemPosition();

            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        animationItem(); // анимация нажатий всех кнопок
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_city, parent, false);
        Holder holder_class = new Holder(view);
        return holder_class;
    }

    @Override
    public int getItemViewType(int position) { // ЕСТЬ ЕЩЕ getViewTypeCount
        return super.getItemViewType(position);
        //return R.layout.row_list_city; // было когда проблему lastitem
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterListCity.Holder holder, int position) {
        int myPos = position;
        ObjectListCity objectListCity = hashMap.get(position);
        if(objectListCity != null)
        {
            holder.clRv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up_1));
            holder.tvCity.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up_1));
            holder.tvCode.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up_1));

            holder.tvCity.setText(objectListCity.name); // установка названия страны
            holder.tvCode.setText(objectListCity.code); // установка кода страны

           // Log.e("www","-before position-" + position);
            if(((MainActivity)context).myPositionName.equals(objectListCity.name))
            {
               // Log.e("www","-myPositionName-" + ((MainActivity)context).myPositionName + " = "
                        //+ " objectListCity.name " + objectListCity.name);
               // holder.tvCity.setText(objectListCity.name); // установка названия страны
                holder.clRv.setBackgroundColor(Color.parseColor("#83D8D8D8"));
                //Log.e("www","-after position-" + position);
            }
            else
            {
                holder.clRv.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }

            //Log.e("222","holder.getAdapterPosition()=" + holder.getAdapterPosition());
            //Log.e("222","страна=" + objectListCity.name + " код=" + objectListCity.code);
            // клик на страну для выбора
            holder.clRv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                holder.clRv.setBackgroundColor(Color.parseColor("#FFFFFF")); // для анимации - белый
                holder.clRv.setAlpha(1f); // анимания кнопки затухание
                holder.clRv.startAnimation(alphaAnim_item); // старт анимации

                String name = objectListCity.name;
                String code = (objectListCity.code);
                int codeEdit = Integer.parseInt(code.replaceAll("\\D+","")); // извлечение только цифр из строки

                Log.e("lll","name=" + name);
                MainActivity.myInterFacePositionClickName.methodInterfacePosClick(name);
                ((MainActivity)context).flag = true;
                MainActivity.myInterFacePositionClickCode.methodInterfacePosClickCode(codeEdit);

                // вызов из fragmentChoose метода myDelay, в котором будет через задержку осущетсвлен переход
                // во FragmentMenuWeather с помощью getActivity().onBackPressed();
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();

                    // передача во FragmentMenuWeather названия выбранного элемента
                FragmentMenuWeather fragmentMenuWeather = (FragmentMenuWeather) fragmentManager.findFragmentById(R.id.ll_frag_menu_weather);
                if(fragmentMenuWeather != null)
                {
                    fragmentMenuWeather.getChooseCityInAdapter(name);
                   // Log.e("222ADAP","-click name-" + name);
                    // очистка textview в момент выбора другой страны, т.к. если польз. не выбрал страну и просто
                    // вернулся назад, то textview покажет текущее содержимое
                    fragmentMenuWeather.tvResult.setText("");
                    fragmentMenuWeather.tvShowProgress.setText("");

                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.e("222MAIN_ACT","ДО счетчик ADAPTER прочитано при клике="
                                            + ((MainActivity)context).getSupportFragmentManager().getBackStackEntryCount());

                                    // ((MainActivity)context).ll_frag_choose.removeAllViews();
                                    ((MainActivity)context).onBackPressed();
                                }
                                catch (Exception e)
                                {
                                    Log.e("222","-try catch внутри-" + e);
                                }
                            }
                        }, DELAY);
                    }
                    catch (Exception e)
                    {
                        Log.e("222","-try catch снаружи-" + e);
                    }

                    }
                    }
                });
         }
        else
        {
            Log.e("222ADAP","-objectListCity NULL-");
        }
    }

    @Override
    public int getItemCount() {
       // Log.e("222ADAP","-hashMap.size()-" + hashMap.size());
        return hashMap.size(); // получаю размер hasmap массива
    }

    public class Holder extends RecyclerView.ViewHolder {
        ConstraintLayout clRv;
        TextView tvCity, tvCode;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            clRv = itemView.findViewById(R.id.clRv);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvCode = itemView.findViewById(R.id.tvCode);
        }
    }

    public void updatelist(HashMap<Integer, ObjectListCity> newhashMap) // метод для обработки фильтра ввода выбора стран
    {
        hashMap = new HashMap<>(); // старый массив
        hashMap.putAll(newhashMap); // помещает в старый массив новый массив
        notifyDataSetChanged(); // обнолвяем адаптер
    }

    public void animationItem() // анимация нажатий всех кнопок
    {
        alphaAnim_item = new AlphaAnimation(0.2f, 1.0f);
        alphaAnim_item.setDuration(500);
        //alphaAnim_item.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    public void getPositionClickName()
    {

    }

}
