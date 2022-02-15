package com.dev_marinov.myweathernow;

import org.junit.Test;

class Test1 {

    @Test
    public void addition_isCorrect() {
        Calc1 calc1 = new Calc1();

        int f = calc1.sum(1,1);
        System.out.println(f);

        int f2 = Calc2.sum(2,2);
        System.out.println(f2);
    }
}
