package com.oreilly.android.taskmanager;

import com.oreilly.android.taskmanager.tasks.Task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends TaskManagerActivity {
	
	private EditText taskNameEditText;
	private Button addButton;
	private Button cancelButton;
	private boolean changesPending;
	private AlertDialog unsavedChangesDialog;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        
        setUpViews();
    }

	protected void addTask() {
		String taskName = taskNameEditText.getText().toString();
		Task t = new Task(taskName);
		getStuffApplication().addTask(t);
		finish();
	}

	protected void cancel() {
		if (changesPending) {
			unsavedChangesDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.unsaved_changes_title)
				.setMessage(R.string.unsaved_changes_message)
				.setPositiveButton(R.string.add_task, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						addTask();
					}
				})
				.setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						unsavedChangesDialog.cancel();
					}
				})
				.create();
			unsavedChangesDialog.show();
		} else {
			finish();
		}
	}

	private void setUpViews() {
		taskNameEditText = (EditText)findViewById(R.id.task_name);
		addButton = (Button)findViewById(R.id.add_button);
		cancelButton = (Button)findViewById(R.id.cancel_button);
		
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addTask();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});
		taskNameEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changesPending = true;
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void afterTextChanged(Editable s) { }
		});
	}

}
