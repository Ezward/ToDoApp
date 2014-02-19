package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity {
	public static final int EDIT_ITEM_REQUEST_CODE = 1;
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        readItems();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(todoAdapter);
    	etNewItem = (EditText) findViewById(R.id.etNewItem);
    	
    	setupListViewListener();
    }


    private void setupListViewListener() {
		// set long click listener to delete item
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int position, long id) {
				// remove the item through the adapter
				todoAdapter.remove(todoItems.get(position));
				writeItems();
				return true;
			}
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener () {

			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int position, long id) {
				// start item edit EditItemActivity
				Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
				i.putExtra("value", todoItems.get(position)); 
				i.putExtra("position", position);

				startActivityForResult(i, EDIT_ITEM_REQUEST_CODE);
			}
			
		});
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // EDIT_ITEM_REQUEST_CODE is defined above
      if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST_CODE) {
         // Extract value from result extras
         String value = data.getExtras().getString("value");
         int position = data.getExtras().getInt("position");
         
         // update array then tell adapter of the change
         todoItems.set(position, value);
         todoAdapter.notifyDataSetChanged();
         
         // persist the change
         writeItems();
      }
    } 


    public void onAddedItem(View v) {
    	// get text from edit field;
    	final String itemText = etNewItem.getText().toString();
    	
    	// add it to the list adaptor
    	todoAdapter.add(itemText);
    	
    	// clear the text from the text field so we don't add twice
    	etNewItem.setText("");
    	
    	// persist
    	writeItems();
    }
    

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }
	
	private void readItems() {
		// read persisted list items from file
		final File filesDir = getFilesDir();
		final File todoFile = new File(filesDir, "todo.txt");
		
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			todoItems = new ArrayList<String>();
			e.printStackTrace();
		}
	}
	
	private void writeItems() {
		// write list items to file
		final File filesDir = getFilesDir();
		final File todoFile = new File(filesDir, "todo.txt");
		
		try {
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
