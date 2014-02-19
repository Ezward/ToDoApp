package com.example.todoapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
		
	private int position;
	private String value;
	private EditText etEditItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		value = getIntent().getStringExtra("value");
		position = getIntent().getIntExtra("position", 0);
		
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(value);
		
	}
		
	public void onEditItemOk(View v) {
		// send back Ok result
		Intent i = new Intent();
		i.putExtra("position", position);
		i.putExtra("value", etEditItem.getText().toString());
		setResult(RESULT_OK, i); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

}
