package com.itmg.imachika;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
//         Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
//        show();
        assertEquals("com.itmg.imachica", appContext.getPackageName());
    }
    int[] first = {0,1,2,5,8,10};
    int[] cecond = {0,8,10};

    private void show(){
        final ArrayList<Integer> result = new ArrayList<>();
        final ArrayList<Integer> result1 = new ArrayList<>();
        for(int i = 0 ; i < first.length ; i++ ){
            for(int j = cecond.length - 1 ; j >= 0 ; j --){
                if(first[i] == cecond[j]){
                    result.add(first[i]);
                }
            }
        }
        Log.i("Test show","result === "+result.toString()+"   result1 === "+result1.toString());
    }

}
