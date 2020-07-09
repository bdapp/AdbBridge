package me.bello.adbbridge;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";
    private TextView txt;
    private Button btn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        txt = findViewById(R.id.text);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        NetworkType type = NetworkUtils.getNetworkType(this);
        Log.e(TAG, type.toString());
        txt.setText(type.toString());
        if ("WiFi".equals(type.toString())) {
            Map<String, String> map = NetworkUtils.getLocalIpAddress(this);
            String ips = "ip: " + map.get("ip") + "\nssid: " + map.get("ssid");
            txt.setText(ips);
            btn.setVisibility(View.VISIBLE);
        } else {
            txt.setText("请打开WIFI连接");
            btn.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result = execRootCmd(5555);
                        Log.e(TAG, "result " + result);
                        String t = "";
                        if (result == 1) {
                            t  = ("请检查是否有root权限");
                        } else if (result == 0) {
                            t  = "执行成功";
                        } else {
                            t = "执行失败";
                        }

                        Message msg = Message.obtain();
                        msg.obj = t;
                        handler.sendMessage(msg);
                    }
                }).start();

                break;

        }
    }


    private int execRootCmd(int port) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            dos.flush();
            dos.writeBytes("setprop service.adb.tcp.port " + port + "\n");
            dos.flush();
            dos.writeBytes("stop adbd" + "\n");
            dos.flush();
            dos.writeBytes("start adbd" + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    MyHandler handler = new MyHandler();
    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String m = (String) msg.obj;
            Toast.makeText(context, m, Toast.LENGTH_LONG).show();
        }
    }

}