package com.dev_marinov.myweathernow;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Calc1 calc1 = new Calc1();

        int f = calc1.sum(1,1);
        System.out.println(f);

        int f2 = Calc2.sum(2,2);
        System.out.println(f2);
    }
}