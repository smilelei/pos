package com.example.administrator.pospatrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.pospatrol.beans.FormBean;
import com.example.administrator.pospatrol.beans.FormBean.FormProject;
import com.example.administrator.pospatrol.utils.XmlUtils;

/**
 * 表单列表界面
 *
 * @author xulei 2016-1-23
 */
public class FormListActivity extends Activity implements View.OnClickListener {
    String posNo;
    String posName;
    ExpandableListView exList;
    ExpandableListAdapter adapter;
    List<FormBean> forms;
    private List<Map<String, String>> groupData;
    private List<List<Map<String, Object>>> childData;
    Handler handler;
    boolean nextAct = false;
    @Override
    protected void onDestroy() {
        if(!nextAct)
        {
            Log.e("FormListActivity","退出程序");
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list);

        // 获取从终端列表界面传入的参数
        Intent intent = getIntent();
        if (intent != null) {
            posNo = intent.getStringExtra("posNo");// 终端编号
            posName = intent.getStringExtra("posName");// 终端名称
        }
        setCustomTitle();// 设置标题

        exList = (ExpandableListView) findViewById(R.id.form_ex_list);

        exList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // 获取点击行的数据，生成弹出窗内容
                Map<String, Object> projectMap = (Map<String, Object>) childData
                        .get(groupPosition).get(childPosition);
                FormProject project = (FormProject) projectMap.get("project");
                showProjectDialog(project);
                return true;
            }
        });

        handler = new Handler() {// 用户处理数据加载后的界面更新
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 1) {// 数据加载成功
                    adapter = new SimpleExpandableListAdapter(
                            FormListActivity.this, groupData,
                            R.layout.form_group_item,
                            new String[] { "group_title" },
                            new int[] { R.id.group_name }, childData,
                            R.layout.form_child_item,
                            new String[] { "child_title" },
                            new int[] { R.id.child_name });
                    exList.setAdapter(adapter);
                    exList.expandGroup(0);// 默认打开第一组
                } else {
                    Toast.makeText(FormListActivity.this,
                            findStr(R.string.loaddata_err), Toast.LENGTH_LONG)
                            .show();
                }
            }
        };

        getFormData();
    }

    /**
     * 生成弹出窗
     *
     * @param project
     */
    protected void showProjectDialog(FormProject project) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(project.getName());
        String inputType = project.getType();

        final List<FormBean.FormProject.ProjectResult> results = project
                .getResults();
        String[] items = new String[results.size()];// 弹出窗列表显示数据

        if (inputType.equals(FormBean.FormProject.TYPE_RADIO)) {// 检查项目类型为单选
            int checkIndex = -1;
            for (int i = 0; i < items.length; i++) {
                items[i] = results.get(i).getName();
                if ("1".equals(results.get(i).getValue())) {
                    checkIndex = i;
                }
            }
            builder.setSingleChoiceItems(items, checkIndex,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            results.get(which).setValue("1");
                        }
                    });
            builder.setPositiveButton(findStr(R.string.confirm), null);
        } else if (inputType.equals(FormBean.FormProject.TYPE_CHECKBOX)) {// 多选
            boolean[] checkedItems = new boolean[results.size()];
            for (int i = 0; i < items.length; i++) {
                items[i] = results.get(i).getName();
                if ("1".equals(results.get(i).getValue())) {
                    checkedItems[i] = true;
                } else {
                    checkedItems[i] = false;
                }
            }
            builder.setMultiChoiceItems(items, checkedItems,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            if (isChecked) {
                                results.get(which).setValue("1");
                            } else {
                                results.get(which).setValue("0");
                            }
                        }
                    });
            builder.setPositiveButton(findStr(R.string.confirm), null);
        } else if (inputType.equals(FormBean.FormProject.TYPE_INPUT)) {// 输入框
            final TableLayout layout = new TableLayout(this);// 输入框需要自行生成视图组件
            layout.setBackgroundColor(Color.WHITE);
            int padding = (int) getResources().getDimension(
                    R.dimen.window_padding);
            layout.setPadding(padding, padding, padding, padding);
            layout.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            View itemView = null;
            for (int i = 0; i < items.length; i++) {
                itemView = getLayoutInflater().inflate(
                        R.layout.dialog_input_item, null);
                TextView label = (TextView) itemView
                        .findViewById(R.id.input_label);
                label.setText(results.get(i).getName() + ":");
                EditText valueEt = (EditText) itemView
                        .findViewById(R.id.input_value);
                valueEt.setText(results.get(i).getValue());// 回填数据
                layout.addView(itemView);
            }
            builder.setView(layout);
            builder.setPositiveButton(findStr(R.string.confirm),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < layout.getChildCount(); i++) {
                                View itemView = layout.getChildAt(i);
                                EditText valueEt = (EditText) itemView
                                        .findViewById(R.id.input_value);
                                Log.e("onClick", valueEt.getText() + "<<");
                                String value = valueEt.getText() + "";
                                results.get(i).setValue(value);// 设置数据
                            }
                        }
                    });
        }

        builder.create().show();
    }

    private void getFormData() {
        new Thread(new Runnable() {// 启动线程从服务器端获得数据并解析
            @Override
            public void run() {
                try {
                    forms = XmlUtils.getForms(getAssets().open(
                            "forms.xml"));
                    // 获取对象数据后生成适配器使用的数据
                    groupData = new ArrayList<Map<String, String>>();
                    Map<String, String> groupMap = null;
                    childData = new ArrayList<List<Map<String, Object>>>();
                    List<Map<String, Object>> childs = null;
                    Map<String, Object> childMap = null;
                    for (FormBean form : forms) {
                        groupMap = new HashMap<String, String>();
                        groupMap.put("group_title", form.getName());
                        List<FormBean.FormProject> projects = form
                                .getProjects();
                        childs = new ArrayList<Map<String, Object>>();
                        for (FormBean.FormProject project : projects) {
                            childMap = new HashMap<String, Object>();
                            childMap.put("child_title",
                                    project.getName());
                            childMap.put("project", project);
                            childs.add(childMap);
                        }
                        childData.add(childs);
                        groupData.add(groupMap);
                    }
                    handler.sendEmptyMessage(1);// 数据加载成功，发送消息
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);// 数据加载失败
                }
            }
        }).start();
    }

    /**
     * 设置自定义的标题栏
     */
    private void setCustomTitle() {
        Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        TextView title = (TextView) findViewById(R.id.title_label);
        title.setText(posName == null ? "" : posName);
        leftBtn.setText(findStr(R.string.back));
        leftBtn.setOnClickListener(this);
        rightBtn.setText(findStr(R.string.next));
        rightBtn.setOnClickListener(this);
    }

    /**
     * 查找字符串
     */
    private String findStr(int id) {
        return getResources().getString(id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_left_btn) {// 返回
            startActivity(new Intent(this,PosListActivity.class));
            nextAct =false;
            finish();
        } else if (v.getId() == R.id.title_right_btn) {// 下一步
            Log.e("form", XmlUtils.fromToXml(forms));
            startActivity(new Intent(this, PhotosActivity.class));
            nextAct = true;
            finish();
        }
    }
}
