package com.topiasoft.notepia;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.Toast;

public class NotesList extends ListActivity {

    private ListView mlist;



    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int DELETE_ID = Menu.FIRST;
    private int mNoteNumber = 1;

    private boolean autoSave = false;
    private static final String AUTO_SAVE_NOTE = "AUTO_SAVE_NOTE";

    private NotesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notelist);
        mDbHelper = new NotesDbAdapter (this);
        mDbHelper.open();

/*
        mlist = (ListView) findViewById(R.id.);

        if (mlist != null){
            mlist.setAdapter(mDbHelper);
        }
*/
        fillData();
        //registerForContextMenu(getListView());

		/*
		Button addnote = (Button)findViewById(R.id.addnotebutton);
		addnote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createNote();
				}*/

        /*Preferences*/
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        //read
        boolean defaultSaveValue = false;
        autoSave = sharedPreferences.getBoolean(AUTO_SAVE_NOTE,defaultSaveValue);

        //Show
        Toast.makeText(this,String.valueOf(autoSave), Toast.LENGTH_SHORT).show();


        //write
        autoSave = true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AUTO_SAVE_NOTE,autoSave);
        editor.apply();

        //Show
        Toast.makeText(this,String.valueOf(autoSave), Toast.LENGTH_SHORT).show();

        /*End Preferences*/
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notelist_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu_about:

	           AlertDialog.Builder dialog = new AlertDialog.Builder(NotesList.this);
	           dialog.setTitle("About");
	           dialog.setMessage("Notepia\n\t" + "A simple and light notebook application to take quick notes, reminders, etc.\n\t" + "For more applications, bugs and feature requests you can visit: \n\thttp://www.topiasoft.com"
	        		   );
	           dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

	        	   @Override
	        	   public void onClick(DialogInterface dialog, int which) {
	        		   dialog.cancel();

	        	   }
	           });
	           dialog.show();
	           return true;
	    case R.id.menu_add:
	    	createNote();
	    default:
	           return super.onOptionsItemSelected(item);
	    }
	    }

	private void createNote() {
		Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        //startActivityForResult(i, ACTIVITY_EDIT);
        //startActivityForResult(i, ACTIVITY_EDIT);
        startActivity(i);
    }

	private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);


        String[] from = new String[] { NotesDbAdapter.KEY_TITLE ,NotesDbAdapter.KEY_DATE};
        int[] to = new int[] { R.id.text1 ,R.id.date_row};

        //Nos aseguramos de que existe al menos un registro
        /*
        if (notesCursor.moveToFirst()) {
             //Recorremos el cursor hasta que no haya m�s registros
             do {
                  String codigo= notesCursor.getString(0);
                  String nombre = notesCursor.getString(1);
             } while(notesCursor.moveToNext());
        }
        */
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);

        setListAdapter(notes);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    /*
    @Override
    protected void onPause() {
        listView.setAdapter(null);
        cursor.close();
        DatabaseSetup.deactivate();
        super.onPause();
    }
	*/
    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }



}
