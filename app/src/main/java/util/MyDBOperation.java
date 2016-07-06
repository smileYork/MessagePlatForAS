package util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import globalData.GlobalData;
import model.AutoInfo;
import model.BlackPhone;
import model.Push;
import model.Send;
import model.ShowItem;

public class MyDBOperation {

    private SQLiteDatabase db;

    private MySQLiteOpenHelp mysql;

    private int getSendTime = 0;

    public MyDBOperation(Context context) {
        mysql = new MySQLiteOpenHelp(context);
        db = mysql.getReadableDatabase();
    }

    public long insertBlackPone(String phone) {

        ContentValues content = new ContentValues();
        content.put("phone", phone);

        return db.insert(GlobalData.TABLE_BLACKLIST, null, content);
    }

    public boolean findSamePhone(String phone) {

        String sql = "select count(*) as num from "
                + GlobalData.TABLE_BLACKLIST + " where phone = " + phone;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            int number = cursor.getInt(cursor

                    .getColumnIndex("num"));

            if (number == 1) {
                return true;
            }
        }

        return false;

    }

    public List<BlackPhone> getLocalBlackList() {

        List<BlackPhone> list_back = new ArrayList<BlackPhone>();

        BlackPhone blackPhone;

        String sql = "select phone from " + GlobalData.TABLE_BLACKLIST;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            blackPhone = new BlackPhone();

            blackPhone.setPhone(cursor.getString(cursor

                    .getColumnIndex("phone")));

            list_back.add(blackPhone);
        }
        return list_back;

    }

    public void deletePhoneFromLocalBlackList(String phone) {

        String sql = "delete from " + GlobalData.TABLE_BLACKLIST
                + " where phone = '" + phone + "'";
        db.execSQL(sql);
    }

    public long setCommonMessage(String message) {

        String sql = "delete from " + GlobalData.TABLE_AUTOINFO
                + " where type = 1";

        db.execSQL(sql);

        ContentValues content = new ContentValues();

        content.put("autoInfo", message);

        content.put("type", 1);

        return db.insert(GlobalData.TABLE_AUTOINFO, null, content);
    }

    public boolean findSpecialMessageByKey(String key) {

        String sql = "select count(*) as num from " + GlobalData.TABLE_AUTOINFO
                + " where key = '" + key + "'";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            int number = cursor.getInt(cursor

                    .getColumnIndex("num"));

            if (number == 1) {
                return true;
            }
        }

        return false;
    }

    public long addSpecialMessage(String key, String message) {

        ContentValues content = new ContentValues();

        content.put("key", key);

        content.put("autoInfo", message);

        content.put("type", 0);

        return db.insert(GlobalData.TABLE_AUTOINFO, null, content);
    }

    public String getCommonMessage() {

        String content = "";

        String sql = "select autoInfo from " + GlobalData.TABLE_AUTOINFO
                + " where type = 1";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            content = cursor.getString(cursor

                    .getColumnIndex("autoInfo"));

        }

        return content;
    }

    public List<AutoInfo> getLocalSpecailMessageList() {

        List<AutoInfo> list_special = new ArrayList<AutoInfo>();

        AutoInfo autoInfo;

        String sql = "select key,autoInfo from " + GlobalData.TABLE_AUTOINFO
                + " where type =0";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            autoInfo = new AutoInfo();

            autoInfo.setKey(cursor.getString(cursor

                    .getColumnIndex("key")));

            autoInfo.setAutoInfo(cursor.getString(cursor

                    .getColumnIndex("autoInfo")));

            list_special.add(autoInfo);
        }
        return list_special;
    }

    public void deleteSpecailFromLocalSpecailList(String key) {
        String sql = "delete from " + GlobalData.TABLE_AUTOINFO
                + " where key = '" + key + "'";
        db.execSQL(sql);
    }

    public boolean insertBuffPush(ShowItem item) {

        try {

            if (!findBuffMessageById(item.getUuid())) {

                Log.i(GlobalData.LOG, "将id = " + item.getId()
                        + "的短信信息成功插入到上传队列Push");

                ContentValues content = new ContentValues();

                content.put("comeFrom", item.getPhone());

                content.put("messageBody", item.getContent());

                content.put("recvDate", item.getTime());

                content.put("msgType", judgePhoneType(item.getPhone()));

                content.put("uuid", item.getUuid());

                db.insert(GlobalData.TABLE_PUSH, null, content);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean findBuffMessageById(String Uuid) {

        String sql = "select count(*) as num from " + GlobalData.TABLE_PUSH
                + " where uuid = '" + Uuid + "'";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            int number = cursor.getInt(cursor

                    .getColumnIndex("num"));

            if (number == 1) {
                return true;
            }
        }

        return false;
    }

    private int judgePhoneType(String phone) {
        if (UsuallyMethod.isPhoneNumber(phone)) {
            return 1;
        }
        return 0;
    }

    public boolean findPhoneFromBlack(String phone) {

        if ((phone.length() < 11) || (phone.length() > 14)) {

            return false;

        }

        String mobile = phone.substring(phone.length() - 11, phone.length());

        String sql = "select count(*) as num from "
                + GlobalData.TABLE_BLACKLIST + " where phone = '" + mobile
                + "'";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            int number = cursor.getInt(cursor

                    .getColumnIndex("num"));

            if (number == 1) {
                return true;
            }
        }

        return false;

    }

    public boolean insertBuffSend(ShowItem item) {

        try {
            if (!findBuffSendById(item.getId())) {

                Log.i(GlobalData.LOG, "将id = " + item.getId()
                        + "的短信信息成功插入到回复队列Send");

                ContentValues content = new ContentValues();

                content.put("id", item.getId());

                content.put("comeFrom", item.getPhone());

                content.put("returnBody", getReturnMessage(item.getContent()));

                content.put("recvDate", item.getTime());

                content.put("lastsendtime", 0);

                content.put("sendsum", 0);

                db.insert(GlobalData.TABLE_SEND, null, content);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean findBuffSendById(int id) {

        String sql = "select count(*) as num from " + GlobalData.TABLE_SEND
                + " where id = " + id;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            int number = cursor.getInt(cursor

                    .getColumnIndex("num"));

            if (number == 1) {
                return true;
            }
        }

        return false;
    }

    // 获取返回短信的信息内容,返回短信内容算法
    private String getReturnMessage(String content) {

        // 统一回复信息
        if (GlobalData.RETURN_TYPE == 0) {

            return GlobalData.MESSAGE_COMMON;

        } else {

            String key = content.substring(0, 2);

            String sql = "select autoInfo,count(*)  as num from "
                    + GlobalData.TABLE_AUTOINFO + " where key like '" + key
                    + "%'";

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToNext()) {

                int number = cursor.getInt(cursor

                        .getColumnIndex("num"));

                if (number == 1) {// 匹配到定制短信
                    return cursor.getString(cursor

                            .getColumnIndex("autoInfo"));
                }

            }

        }
        // 最后没有匹配到定制短信
        return GlobalData.MESSAGE_COMMON;
    }

    public List<Push> getBuffForPush() {

        List<Push> temp_list = new ArrayList<Push>();

        Push push;

        String sql = "select id,comefrom,messageBody,recvdate,msgType,uuid from "
                + GlobalData.TABLE_PUSH;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            push = new Push();

            push.setId(cursor.getInt(cursor.getColumnIndex("id")));

            push.setComefrom(cursor.getString(cursor.getColumnIndex("comefrom")));

            push.setMessageBody(cursor.getString(cursor
                    .getColumnIndex("messageBody")));

            push.setRecvDate(cursor.getString(cursor.getColumnIndex("recvdate")));

            push.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));

            push.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));

            temp_list.add(push);
        }
        return temp_list;

    }

    public List<Send> getBuffForSend() {

        List<Send> temp_list = new ArrayList<Send>();

        Send send;

        String sql = "select id,comefrom,returnBody,recvdate from "
                + GlobalData.TABLE_SEND;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            send = new Send();

            send.setId(cursor.getInt(cursor.getColumnIndex("id")));

            send.setComFrom(cursor.getString(cursor.getColumnIndex("comefrom")));

            send.setReturnBody(cursor.getString(cursor
                    .getColumnIndex("returnBody")));

            send.setRecvDate(cursor.getString(cursor.getColumnIndex("recvdate")));

            temp_list.add(send);
        }
        return temp_list;

    }

    public void deleteLocalBuffSend() {

        String sql = "delete from " + GlobalData.TABLE_SEND;
        db.execSQL(sql);
    }

    public void deletSucessSendFromBuff(int id) {
        String sql = "delete from " + GlobalData.TABLE_SEND + " where id = "
                + id;
        db.execSQL(sql);
    }

    public void deleteAllLocalPush() {
        String sql = "delete from " + GlobalData.TABLE_PUSH;
        db.execSQL(sql);
    }

    public int getBuffForSendNumber() {

        int number = 0;

        String sql = "select count(*) as num from " + GlobalData.TABLE_SEND;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {
            number = cursor.getInt(cursor.getColumnIndex("num"));
        }

        return number;
    }

    public int getBuffForPushNumber() {

        int number = 0;

        String sql = "select count(*) as num from " + GlobalData.TABLE_PUSH;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {
            number = cursor.getInt(cursor.getColumnIndex("num"));
        }

        return number;
    }

    public List<ShowItem> getBuffForShowPush() {

        List<ShowItem> temp_list = new ArrayList<ShowItem>();

        ShowItem showItem;

        String sql = "select comefrom,messageBody,recvdate from "
                + GlobalData.TABLE_PUSH;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            showItem = new ShowItem();

            showItem.setPhone(cursor.getString(cursor
                    .getColumnIndex("comefrom")));

            showItem.setContent(cursor.getString(cursor
                    .getColumnIndex("messageBody")));

            showItem.setTime(cursor.getString(cursor.getColumnIndex("recvdate")));

            temp_list.add(showItem);
        }
        return temp_list;
    }

    public List<ShowItem> getBuffForShowSend() {

        List<ShowItem> temp_list = new ArrayList<ShowItem>();

        ShowItem showItem;

        String sql = "select comefrom,returnBody,recvdate from "
                + GlobalData.TABLE_SEND;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            showItem = new ShowItem();

            showItem.setPhone(cursor.getString(cursor
                    .getColumnIndex("comefrom")));

            showItem.setContent(cursor.getString(cursor
                    .getColumnIndex("returnBody")));

            showItem.setTime(cursor.getString(cursor.getColumnIndex("recvdate")));

            temp_list.add(showItem);
        }
        return temp_list;
    }

    public List<Push> addBuffToList(List<Push> list_push) {

        List<Push> temp_list = new ArrayList<Push>();

        Push push;

        String sql = "select id,comefrom,messageBody,recvdate,msgType,uuid from "
                + GlobalData.TABLE_PUSH;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));

            if (!isFindPushById(temp_list, id)) {

                push = new Push();

                push.setId(cursor.getInt(cursor.getColumnIndex("id")));

                push.setComefrom(cursor.getString(cursor
                        .getColumnIndex("comefrom")));

                push.setMessageBody(cursor.getString(cursor
                        .getColumnIndex("messageBody")));

                push.setRecvDate(cursor.getString(cursor
                        .getColumnIndex("recvdate")));

                push.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));

                push.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));

                temp_list.add(push);
            }
        }
        return temp_list;

    }

    private boolean isFindPushById(List<Push> temp_list, int id) {

        for (int i = 0; i < temp_list.size(); i++) {
            if (temp_list.get(i).getId() == id) {
                return true;
            }
        }

        return false;
    }

    public Send getOneSendFromDB() {

        if (getSendTime >= (GlobalData.usefulSendMessageTime / GlobalData
                .sleepSomeTime())) {

            deleteOldMessage();

            updateSend();

            getSendTime = 0;

        }

        Send send = null;

        // 优先发送未处理的
        String sql = "select id,comefrom,returnBody,recvdate,lastsendtime,sendsum from "
                + GlobalData.TABLE_SEND
                + " where lastsendtime=0  order by sendsum desc limit 1";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            send = new Send();

            send.setId(cursor.getInt(cursor.getColumnIndex("id")));

            send.setComFrom(cursor.getString(cursor.getColumnIndex("comefrom")));

            send.setReturnBody(cursor.getString(cursor
                    .getColumnIndex("returnBody")));

            send.setRecvDate(cursor.getString(cursor.getColumnIndex("recvdate")));

            send.setLastSendTime(cursor.getLong(cursor
                    .getColumnIndex("lastsendtime")));

            send.setSendCount(cursor.getInt(cursor.getColumnIndex("sendsum")));

            updateSend(send);

        }

        cursor.close();

        if (send != null) {
            Log.i(GlobalData.LOG, "从本地Send表获取一条待发送短信---------" + send.getId()
                    + "-------" + TimeAndDateType.nowDateWithTime());
        }

        getSendTime++;

        return send;
    }

    private void updateSend(Send send) {

        String sql = "update " + GlobalData.TABLE_SEND + " set lastsendtime = "
                + System.currentTimeMillis() + " where  id= " + send.getId();

        Log.i(GlobalData.LOG, "发送完短信更新本地短信库	" + sql);

        db.execSQL(sql);

    }

    private void updateSend() {

        String sql = "update " + GlobalData.TABLE_SEND
                + " set lastsendtime=0 , sendsum=sendsum+1 where  ("
                + System.currentTimeMillis() + "-lastsendtime)>"
                + GlobalData.usefulSendMessageTime;

        Log.i(GlobalData.LOG, " 更新" + sql);

        db.execSQL(sql);
    }

    @SuppressLint("SimpleDateFormat")
    private void deleteOldMessage() {

        Log.i(GlobalData.LOG, "删除过期短信 ");

        String sql = "delete from " + GlobalData.TABLE_SEND
                + " where sendsum>=720";

        db.execSQL(sql);
    }

    public void deleteSucessPushByUuid(String uuid) {

        String sql = "delete from " + GlobalData.TABLE_PUSH + " where uuid = '"
                + uuid + "'";

        db.execSQL(sql);

        Log.i(GlobalData.LOG, "删除PUSH表记录" + uuid);
    }

    public Push getOnePushFromDB() {

        Push push = null;

        String sql = "select id,comefrom,messageBody,recvdate,msgType,uuid from "
                + GlobalData.TABLE_PUSH + " order by id";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {

            push = new Push();

            push.setId(cursor.getInt(cursor.getColumnIndex("id")));

            push.setComefrom(cursor.getString(cursor.getColumnIndex("comefrom")));

            push.setMessageBody(cursor.getString(cursor
                    .getColumnIndex("messageBody")));

            push.setRecvDate(cursor.getString(cursor.getColumnIndex("recvdate")));

            push.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));

            push.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));

            Log.i(GlobalData.LOG, "从Push队列提取一条数据" + push.getId());

        }
        return push;
    }

}
