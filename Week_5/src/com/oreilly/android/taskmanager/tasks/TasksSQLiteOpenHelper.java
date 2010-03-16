package com.oreilly.android.taskmanager.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksSQLiteOpenHelper extends SQLiteOpenHelper {
	
	public static final int VERSION = 2;
	public static final String DB_NAME  = "tasks_db.sqlite";
	public static final String TASKS_TABLE  = "tasks";
	public static final String TASK_ID = "id";
	public static final String TASK_NAME = "name";
	public static final String TASK_COMPLETE  = "complete";
	public static final String TASK_ADDRESS = "address";
	public static final String TASK_LATITUDE = "latitude";
	public static final String TASK_LONGITUDE = "longitude";
	
	public TasksSQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		dropAndCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("alter table " + TASKS_TABLE + " add column " + TASK_ADDRESS + " text");
		db.execSQL("alter table " + TASKS_TABLE + " add column " + TASK_LATITUDE + " real");
		db.execSQL("alter table " + TASKS_TABLE + " add column " + TASK_LONGITUDE + " real");
	}

	protected void dropAndCreate(SQLiteDatabase db) {
		db.execSQL("drop table if exists " + TASKS_TABLE + ";");
		createTables(db);
	}
	
	protected void createTables(SQLiteDatabase db) {
		db.execSQL(
				"create table " + TASKS_TABLE +" (" +
				TASK_ID + " integer primary key autoincrement not null," +
				TASK_NAME + " text, " +
				TASK_COMPLETE + " text, " +
				TASK_ADDRESS + " text, " +
				TASK_LATITUDE + " real, " +
				TASK_LONGITUDE + " real " +
				");"
			);
	}
}
