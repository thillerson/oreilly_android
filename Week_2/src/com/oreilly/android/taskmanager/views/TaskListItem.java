package com.oreilly.android.taskmanager.views;

import com.oreilly.android.taskmanager.tasks.Task;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

public class TaskListItem extends LinearLayout {
	
	private Task task;
	private CheckedTextView checkbox;

	public TaskListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		checkbox = (CheckedTextView)findViewById(android.R.id.text1);
	}

	public void setTask(Task task) {
		this.task = task;
		checkbox.setText(task.getName());
		checkbox.setChecked(task.isComplete());
	}

	public Task getTask() {
		return task;
	}

}
