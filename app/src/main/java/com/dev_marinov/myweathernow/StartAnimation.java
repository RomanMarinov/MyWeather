package com.dev_marinov.myweathernow;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Timer;
import java.util.TimerTask;

public class StartAnimation {

    Context context;

    public StartAnimation(MainActivity mainActivity)
    {
        this.context = mainActivity;
    }

    MainActivity mainAct = ((MainActivity)context); // чтобы не писать каждый раз длиннную строку

    // анимация букв при старте приложения
    public void wordsAnimation()
    {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvM.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(), R.anim.bounce_animation);
                        ((MainActivity)context).tvM.setAnimation(((MainActivity)context).animation);
                    }
                    });
            }
        },200);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvY.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(), R.anim.bounce_animation);
                        ((MainActivity)context).tvY.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },400);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvW.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(),R.anim.bounce_animation);
                        ((MainActivity)context).tvW.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },600);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvE1.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(),R.anim.bounce_animation);
                        ((MainActivity)context).tvE1.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },800);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvA.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(),R.anim.bounce_animation);
                        ((MainActivity)context).tvA.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },1000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvT.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(),R.anim.bounce_animation);
                        ((MainActivity)context).tvT.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },1200);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvH.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(),R.anim.bounce_animation);
                        ((MainActivity)context).tvH.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },1400);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvE2.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(),R.anim.bounce_animation);
                        ((MainActivity)context).tvE2.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },1600);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).tvR.setVisibility(View.VISIBLE);
                        // анимация
                        ((MainActivity)context).animation = AnimationUtils.loadAnimation(((MainActivity)context).getBaseContext(),R.anim.bounce_animation);
                        ((MainActivity)context).tvR.setAnimation(((MainActivity)context).animation);
                    }
                });
            }
        },1800);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).llTvAnim.setVisibility(View.GONE);

                        FragmentMenuWeather fragmentMenuWeather = new FragmentMenuWeather();
                        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
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
