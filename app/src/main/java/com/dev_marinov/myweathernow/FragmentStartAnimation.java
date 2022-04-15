package com.dev_marinov.myweathernow;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentStartAnimation extends Fragment {

    TextView tvM, tvY, tvW, tvE1, tvA, tvT, tvH, tvE2, tvR;
    Animation animation;
    LinearLayout llTvAnim;

    ViewGroup viewGroup; // разметка, которая позволяет расположить один или несколько View.
    // layoutInflater класс, используемый для преобразования XML-файла макета в объекты представления динамическим способом
    LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("222","FragmentStartAnimation заходит");
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
            view = layoutInflater.inflate(R.layout.fragment_start_animation, viewGroup, false);
            Log.e("222","портрет orientation =" + orientation);
        } else {
            view = layoutInflater.inflate(R.layout.fragment_start_animation_horiz, viewGroup, false);
            Log.e("222","альбом orientation =" + orientation);
        }

        setAnimation(view);

        Log.e("поток","поток=" + Thread.currentThread().getName());

        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        // ПОСЛЕ УСТАНОВКИ ЭТИХ ДВУХ СТРОК СТАЛА МЕНЯТСЯ КАРТИНКА ФОНА
        // ВО ВРЕМЯ АНИМАЦИИ ПРИ ПЕРЕВОРОТА ЭКРАНА
        // создать новый макет
        View view = initInterface();
        // отображать новую раскладку на экране
        viewGroup.addView(view);

        super.onConfigurationChanged(newConfig);
    }

    public void setAnimation(View view)
    {
        llTvAnim = view.findViewById(R.id.llTvAnim);
        tvM = view.findViewById(R.id.tvM);
        tvY = view.findViewById(R.id.tvY);
        tvW = view.findViewById(R.id.tvW);
        tvE1 = view.findViewById(R.id.tvE1);
        tvA = view.findViewById(R.id.tvA);
        tvT = view.findViewById(R.id.tvT);
        tvH = view.findViewById(R.id.tvH);
        tvE2 = view.findViewById(R.id.tvE2);
        tvR = view.findViewById(R.id.tvR);

        wordsAnimation();
    }
    // анимация букв при старте приложения
    public void wordsAnimation()
    {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvM.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(), R.anim.bounce_animation);
                        tvM.setAnimation(animation);
                    }
                });
            }
        },200);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvY.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(), R.anim.bounce_animation);
                        tvY.setAnimation( animation);
                    }
                });
            }
        },400);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvW.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(),R.anim.bounce_animation);
                        tvW.setAnimation( animation);
                    }
                });
            }
        },600);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvE1.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(),R.anim.bounce_animation);
                        tvE1.setAnimation( animation);
                    }
                });
            }
        },800);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvA.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(),R.anim.bounce_animation);
                        tvA.setAnimation( animation);
                    }
                });
            }
        },1000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvT.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(),R.anim.bounce_animation);
                        tvT.setAnimation( animation);
                    }
                });
            }
        },1200);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvH.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(),R.anim.bounce_animation);
                        tvH.setAnimation( animation);
                    }
                });
            }
        },1400);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvE2.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(),R.anim.bounce_animation);
                        tvE2.setAnimation( animation);
                    }
                });
            }
        },1600);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvR.setVisibility(View.VISIBLE);
                        // анимация
                        animation = AnimationUtils.loadAnimation(((MainActivity)getActivity()).getBaseContext(),R.anim.bounce_animation);
                        tvR.setAnimation( animation);
                    }
                });
            }
        },1800);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llTvAnim.setVisibility(View.GONE);

                        FragmentMenuWeather fragmentMenuWeather = new FragmentMenuWeather();
                        FragmentManager fragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.ll_frag_menu_weather, fragmentMenuWeather, "ll_frag_menu_weather");
                        //fragmentTransaction.addToBackStack("ll_frag_menu_weather");
                        fragmentTransaction.commit();
                    }
                });
            }
        },4000);
    }
}