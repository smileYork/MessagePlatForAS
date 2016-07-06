package com.jht.messageplat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.jht.messageplat.R;
import globalData.GlobalData;
import service.GetMessageService;
import service.PushService;
import service.SendService;
import util.AlterDialogShow;
import util.ToastShow;

public class MainActivity extends FragmentActivity {

    private AutoServiceActivity autoServiceActivity = new AutoServiceActivity();

    private BlackListActivity blackListActivity = new BlackListActivity();

    private SettingActivity settingActivity = new SettingActivity();

    private RadioGroup radioGroup;

    private ToggleButton turnService;

    private FragmentTransaction transaction;

    private FragmentManager fragmentManager;

    private SharedPreferences sp;

    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findView();

        getSetting();

        setListen();
    }

    private void getSetting() {

        GlobalData.RETURN_TYPE = sp.getInt("returnType", 0);

        GlobalData.RETURN_SPEED = sp.getInt("returnSpeed", 0);

        if (sp.getBoolean("url_change", false)) {
            GlobalData.ABSTRACT_URL = sp.getString("url_address", "");
        }

    }

    private void setListen() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int checkId) {

                switch (checkId) {

                    case R.id.rbService:

                        autoServiceActivity = new AutoServiceActivity();

                        transaction = fragmentManager.beginTransaction();

                        transaction.replace(R.id.content, (Fragment) autoServiceActivity);

                        transaction.commit();
                        break;

                    case R.id.rbBlackList:

                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content, (Fragment) blackListActivity);
                        transaction.commit();
                        break;

                    case R.id.rbSetting:

                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content, (Fragment) settingActivity);
                        transaction.commit();
                        break;

                }
            }

        });

        turnService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {

                    StartLocalService();

                    AlterDialogShow.WarninDialog(MainActivity.this, "收发服务已开启!");

                    GlobalData.SERVICE_OPEN = true;

                } else {
                    AlterDialogShow.WarninDialog(MainActivity.this, "收发服务已关闭!");
                    GlobalData.SERVICE_OPEN = false;
                    StopLocalService();
                }
            }

        });

    }

    protected void StartLocalService() {

        // 启动处理短信到来service
        Intent intent = new Intent(MainActivity.this, GetMessageService.class);
        startService(intent);

        // 启动处理本地上传服务器信息表的service
        Intent intent_send = new Intent(getApplicationContext(), SendService.class);
        startService(intent_send);

        // 启动处理本地回复短信信息表的service
        Intent intent_push = new Intent(getApplicationContext(), PushService.class);
        startService(intent_push);

    }

    protected void StopLocalService() {

        // 停止处理短信的service
        Intent intent = new Intent(MainActivity.this, GetMessageService.class);
        stopService(intent);

        // 停止处理本地上传服务器信息表的service
        Intent intent_send = new Intent(getApplicationContext(), SendService.class);
        stopService(intent_send);

        // 停止处理本地回复短信信息表的service
        Intent intent_push = new Intent(getApplicationContext(), PushService.class);
        stopService(intent_push);

    }

    private void findView() {

        sp = getSharedPreferences("setting", Activity.MODE_PRIVATE);

        radioGroup = (RadioGroup) findViewById(R.id.tab_menu);

        turnService = (ToggleButton) findViewById(R.id.turnService);

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content, (Fragment) autoServiceActivity);

        transaction.commit();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                long secondTime = System.currentTimeMillis();

                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    ToastShow.ToastShowShort(this, "连按两次返回退出程序");

                    firstTime = secondTime;// 更新firstTime

                    return false;
                }

                if (GlobalData.SERVICE_OPEN) {

                    StopLocalService();
                }

                System.exit(0);
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (GlobalData.SERVICE_OPEN) {

            StopLocalService();
        }
    }

}
