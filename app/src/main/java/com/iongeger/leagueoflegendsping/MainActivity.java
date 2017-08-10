package com.iongeger.leagueoflegendsping;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity {

    private TextView pingText;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pingText = (TextView) findViewById(R.id.ping_text);


        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                String ping = Ping("las.leagueoflegends.com");
                                float num = 0;
                                try {
                                    if (!"".equals(ping)) {
                                        ping = ping.substring((ping.indexOf("time=")) + 5);
                                        ping = ping.substring(0, ping.indexOf("\n"));
                                        num = Float.parseFloat(ping.substring(0, ping.indexOf(" ")));
                                        num /= 2;
                                        if (num > 300) {
                                            pingText.setTextColor(Color.parseColor("#D50000"));
                                        } else {
                                            pingText.setTextColor(Color.parseColor("#64DD17"));
                                        }
                                        pingText.setText(String.valueOf(num) + " ms");
                                        Log.d("", "---------------------------run: " + pingText.getText());
                                    }
                                } catch (Exception e){
                                    Log.d(e.toString(), "run: NO PUEDO MOSTRAR NADA");
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {Log.d(e.toString(), "run: NO PUEDO MOSTRAR NADA");}
            }
        };
        t.start();

    }

    public String Ping(String url) {
        String str = "";
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            int i;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((i = reader.read(buffer)) > 0){
                output.append(buffer, 0, i);
            }
            reader.close();

            // body.append(output.toString()+"\n");
            str = output.toString();
            // Log.d(TAG, str);
        } catch (IOException e) {
            // body.append("Error\n");
            e.printStackTrace();
        }
        return str;
    }




}
