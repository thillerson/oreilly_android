package com.oreilly.android.taskmanager;

import android.app.Activity;

public class TaskManagerActivity extends Activity {

	public TaskManagerActivity() {
		super();
	}

	protected TaskManagerApplication getStuffApplication() {
		return (TaskManagerApplication)getApplication();
	}

}