package com.oreilly.android.taskmanager;

import java.util.ArrayList;

import com.oreilly.android.taskmanager.tasks.Task;

import android.app.Application;

public class TaskManagerApplication extends Application {

	private ArrayList<Task> currentTasks;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (null == currentTasks) {
			currentTasks = new ArrayList<Task>();
		}
	}

	public void setCurrentTasks(ArrayList<Task> currentTasks) {
		this.currentTasks = currentTasks;
	}

	public ArrayList<Task> getCurrentTasks() {
		return currentTasks;
	}
	
	public void addTask(Task t) {
		assert(null != t);
		if (null == currentTasks) {
			currentTasks = new ArrayList<Task>();
		}
		currentTasks.add(t);
	}
	
}
