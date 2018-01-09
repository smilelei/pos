package com.example.administrator.pospatrol;

import java.io.IOException;

import com.example.administrator.pospatrol.utils.ConfigUtils;
import com.example.administrator.pospatrol.utils.NetUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登陆界面
 *
 * @author xulei 2016-1-23
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    Handler handler;
    SharedPreferences sp;
    boolean nextAct = false;
    String useName;
    String password;
    @Override
    protected void onDestroy() {
        if(!nextAct)
        {
            Log.e("LoginActivity","退出软件");
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        setCustomTitle();
        findViewById(R.id.login_btn).setOnClickListener(this);

        EditText name = (EditText)findViewById(R.id.name);
        EditText pass = (EditText)findViewById(R.id.password);
        useName = sp.getString("usename",null);
        password = sp.getString("password",null);
        if(useName != null)
        {
            useName = name.getText().toString();
        }
        if(password != null)
        {
            password = pass.getText().toString();
        }
        initHandler();
    }

    public void initHandler()
    {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0)
                {
                    Editor ed = sp.edit();
                    ed.putString("usename",useName);
                    ed.putString("password",password);
                    ed.commit();

                    ConfigUtils.usename = useName;
                    startActivity(new Intent(LoginActivity.this, LocationActivity.class));
                    nextAct = true;
                    finish();
                }
                super.handleMessage(msg);
            }

        };
    }
    /**
     * 设置自定义的标题栏
     */
    private void setCustomTitle() {
        Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        TextView title = (TextView) findViewById(R.id.title_label);
        title.setText(findStr(R.string.login));
        leftBtn.setVisibility(View.GONE);
        rightBtn.setText(findStr(R.string.config));
        rightBtn.setOnClickListener(this);
    }

    /**
     * 查找字符串
     */
    private String findStr(int id) {
        return getResources().getString(id);
    }
    public void login()
    {
        EditText name = (EditText)findViewById(R.id.name);
        EditText pass = (EditText)findViewById(R.id.password);
        if(name.getText()==null||pass.getText()==null)
        {
            Toast.makeText(LoginActivity.this,
                    findStr(R.string.toast_login),Toast.LENGTH_LONG).show();
            return;
        }
        useName = name.toString();
        password = pass.toString();
        if("".equals(useName)||"".equals(password))
        {
            Toast.makeText(LoginActivity.this,
                    findStr(R.string.toast_login),Toast.LENGTH_LONG).show();
            return;
        }


        new Thread(new Runnable() {

            @Override
            public void run() {
                String Url = ConfigUtils.getHost(LoginActivity.this)
                        +"/login.do?usename="+useName+"&password="
                        +password;
                Toast.makeText(LoginActivity.this,
                        Url,Toast.LENGTH_LONG).show();
                Log.e("login",Url);

                try {

                    byte[] resp = NetUtils.get(Url);
                    String respStr = new String(resp,"utf-8");
                    if("0".equals(respStr))
                    {
                        handler.sendEmptyMessage(0);
                    }else if("1".equals(respStr))
                    {
                        handler.sendEmptyMessage(1);
                    }else if("2".equals(respStr))
                    {
                        handler.sendEmptyMessage(2);
                    }else if("3".equals(respStr))
                    {
                        handler.sendEmptyMessage(3);
                    }else if("4".equals(respStr))
                    {
                        handler.sendEmptyMessage(4);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            login();
        }else if (v.getId() == R.id.title_right_btn) {



            startActivity(new Intent(this, ConfigActivity.class));
        }
    }
}
