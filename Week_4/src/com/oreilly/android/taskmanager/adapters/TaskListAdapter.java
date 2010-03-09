package com.oreilly.android.taskmanager.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.oreilly.android.taskmanager.R;
import com.oreilly.android.taskmanager.tasks.Task;
import com.oreilly.android.taskmanager.views.TaskListItem;

public class TaskListAdapter extends BaseAdapter {
	
	private ArrayList<Task> tasks;
	private Context context;

	public TaskListAdapter(Context context, ArrayList<Task> tasks) {
		this.tasks = tasks;
		this.context = context;
	}

	public int getCount() {
		return tasks.size();
	}

	public Task getItem(int position) {
		return (null == tasks) ? null : tasks.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TaskListItem tli;
		if (null == convertView) {
			tli = (TaskListItem)View.inflate(context, R.layout.task_list_item, null);
		} else {
			tli = (TaskListItem)convertView;
		}
		tli.setTask(tasks.get(position));
		return tli;
	}

	public void forceReload() {
		notifyDataSetChanged();
	}

	public void toggleTaskCompleteAtPosition(int position) {
		Task task = getItem(position);
		task.toggleComplete();
		notifyDataSetChanged();
	}

	public Long[] removeCompletedTasks() {
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		ArrayList<Long> completedIds = new ArrayList<Long>();
		for (Task task : tasks) {
			if (task.isComplete()) {
				completedIds.add(task.getId());
				completedTasks.add(task);
			}
		}
		tasks.removeAll(completedTasks);
		notifyDataSetChanged();
		return completedIds.toArray(new Long[]{});
	}

}
