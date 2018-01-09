package com.example.administrator.pospatrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 配置界面
 * @author xulei
 * 2016-1-23
 */
public class ConfigActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setCustomTitle();

    }


    /**
     * 设置自定义的标题栏
     */
    private void setCustomTitle() {
        Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        TextView title = (TextView) findViewById(R.id.title_label);
        title.setText(findStr(R.string.config));
        rightBtn.setVisibility(View.GONE);
        leftBtn.setText(findStr(R.string.login));
        leftBtn.setOnClickListener(this);
    }

    /**
     * 查找字符串
     */
    private String findStr(int id){
        return getResources().getString(id);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.title_left_btn){
            finish();
        }else if(v.getId()==R.id.save_btn){

        }
    }
}
