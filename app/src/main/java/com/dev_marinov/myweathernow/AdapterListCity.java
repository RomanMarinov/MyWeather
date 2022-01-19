package com.dev_marinov.myweathernow;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

class AdapterListCity extends RecyclerView.Adapter<AdapterListCity.Holder> {
    HashMap<Integer, ObjectListCity> hashMap;
    Context context;
    private AlphaAnimation alphaAnim_item; // анимация нажатия элемента списка

    public AdapterListCity(Context context, HashMap<Integer, ObjectListCity> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
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
        return R.layout.row_list_city;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterListCity.Holder holder, int position) {
        ObjectListCity objectListCity = hashMap.get(position);
        if(objectListCity != null)
        {
        holder.tvCity.setText(objectListCity.name);
        holder.tvCode.setText(objectListCity.code);

        holder.clRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.clRv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.clRv.setAlpha(1f); // анимания кнопки затухание
                holder.clRv.startAnimation(alphaAnim_item);

                // вызов из fragmentChoose метода myDelay, в котором будет через задержку осущетсвлен переход
                // во FragmentMenuWeather с помощью getActivity().onBackPressed();
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                FragmentChoose fragmentChoose = (FragmentChoose) fragmentManager.findFragmentById(R.id.ll_frag_choose);
                if(fragmentChoose != null)
                {
                    fragmentChoose.myDelay();
                }
                    // передача во FragmentMenuWeather названия выбранного элемента
                FragmentMenuWeather fragmentMenuWeather = (FragmentMenuWeather) fragmentManager.findFragmentById(R.id.ll_frag_menu_weather);
                if(fragmentMenuWeather != null)
                {
                    
                    String name = objectListCity.name;
                    fragmentMenuWeather.getChooseCityInAdapter(name);
                    Log.e("333ADAP","-click name-" + name);
                    }
                }
            });
         }
        else
        {
            Log.e("333ADAP","-objectListCity NULL-");
        }
    }

    @Override
    public int getItemCount() {
        Log.e("333ADAP","-hashMap.size()-" + hashMap.size());
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

}
