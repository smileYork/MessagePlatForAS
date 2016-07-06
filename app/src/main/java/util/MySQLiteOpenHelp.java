package util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import globalData.GlobalData;

public class MySQLiteOpenHelp extends SQLiteOpenHelper {

    private static final int version = 1;

    public static final String DATABASE_NAME = "MessagePlat.db";

    public MySQLiteOpenHelp(Context context) {
	super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

	String TABLEPUSH = "create table " + GlobalData.TABLE_PUSH
		+ " (id int  AUTO_INCREMENT primary key," + // rowID
		"comefrom varchar(16)," + // 短信来源
		"messageBody varchar(64)," + // 短信内容
		"recvdate date," + // 收到短信的时间
		"msgType int," + // 收到短信的类型, 1代表常规短信，0代表非常规
		"uuid varchar(32)" + // 收到短信的类型, 1代表常规短信，0代表非常规
		");";
	db.execSQL(TABLEPUSH);

	String TABLESEND = "create table " + GlobalData.TABLE_SEND
		+ " (id int  AUTO_INCREMENT primary key," + // rowID
		"comefrom varchar(16)," + // 短信来源
		"returnBody varchar(64)," + // 短信内容
		"recvdate date," + // 收到短信的时间
		"lastsendtime bigint," + // 收到短信的时间
		"sendsum int" + // 收到短信的时间
		");";
	db.execSQL(TABLESEND);

	String TABLEAUTOINFO = "create table " + GlobalData.TABLE_AUTOINFO
		+ " (id int  AUTO_INCREMENT primary key," + // rowID
		"key varchar(16)," + // 短信来源
		"autoInfo varchar(64)," + // 短信内容
		"type int" + // 回复短信的类型, 1代表通用短信，0代表定制短信
		");";
	db.execSQL(TABLEAUTOINFO);

	String TABLEBLACKLIST = "create table " + GlobalData.TABLE_BLACKLIST
		+ " (id int  AUTO_INCREMENT primary key," + // rowID
		"phone varchar(20)" + // 黑名单号码
		");";
	db.execSQL(TABLEBLACKLIST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

	String drop_tb2 = "drop table " + GlobalData.TABLE_PUSH;
	db.execSQL(drop_tb2);

	String TABLEPUSH = "create table " + GlobalData.TABLE_PUSH
		+ " (id int  AUTO_INCREMENT primary key," + // rowID
		"comefrom varchar(16)," + // 短信来源
		"messageBody varchar(64)," + // 短信内容
		"recvdate date," + // 收到短信的时间
		"msgType int," + // 收到短信的类型, 1代表常规短信，0代表非常规
		"uuid varchar(32)" + // 收到短信的类型, 1代表常规短信，0代表非常规
		");";
	db.execSQL(TABLEPUSH);

	String TABLESEND = "create table " + GlobalData.TABLE_SEND
		+ " (id int  AUTO_INCREMENT primary key," + // rowID
		"comefrom varchar(16)," + // 短信来源
		"returnBody varchar(64)," + // 短信内容
		"recvdate date" + // 收到短信的时间
		");";
	db.execSQL(TABLESEND);

    }

}
