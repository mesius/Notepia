package com.topiasoft.notepia;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.Toast;

public class NotesList extends AppCompatActivity {

    private ListView mlist;



    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int DELETE_ID = Menu.FIRST;
    private int mNoteNumber = 1;



    public static final String AUTO_SAVE_NOTE = "AUTO_SAVE_NOTE";
    public boolean autoSave = false;

    SharedPreferences sharedPreferences;

    private NotesDbAdapter mDbHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*TODO Listview en un contenedor relative*/
        setContentView(R.layout.notelist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        /*
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher);
        myToolbar.setTitle("Notepia");
        myToolbar.setNavigationIcon(R.mipmap.ic_launcher);
        */

        //ActionBar myActionBar = getSupportActionBar();
        //myActionBar.setIcon(R.drawable.btn_delete);
        //myActionBar.setLogo(R.mipmap.ic_launcher);
        setTitle(R.string.app_name);




        //mDbHelper = new NotesDbAdapter(getApplicationContext());

        mDbHelper = new NotesDbAdapter (this);
        mDbHelper.open();

        mlist = (ListView) findViewById(R.id.myList);


        sharedPreferences = getSharedPreferences(AUTO_SAVE_NOTE, Context.MODE_PRIVATE);

        /**********************/


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

/****
        Cursor cursor = mySQLiteAdapter.queueAll();
        startManagingCursor(cursor);

        String[] from = new String[]{SQLiteAdapter.KEY_CONTENT};
        int[] to = new int[]{R.id.text};

        SimpleCursorAdapter cursorAdapter =
                new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);

        listContent.setAdapter(cursorAdapter);

***/

        /**********************/

        if (mlist != null){
            mlist.setAdapter(notes);
        }

        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mlist.getItemAtPosition(i);
                int item_id = cursor.getInt(cursor.getColumnIndex(NotesDbAdapter.KEY_ROWID));
                //Toast.makeText(getApplicationContext(),String.valueOf(item_id), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
                intent.putExtra(NotesDbAdapter.KEY_ROWID, l);
                //startActivityForResult(i, ACTIVITY_EDIT);
                //startActivityForResult(i, ACTIVITY_EDIT);
                startActivity(intent);
            }
        });

        //fillData();

        //registerForContextMenu(getListView());

		/*
		Button addnote = (Button)findViewById(R.id.addnotebutton);
		addnote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createNote();
				}*/

        /***Preferences
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        //read
        boolean defaultSaveValue = false;
        autoSave = sharedPreferences.getBoolean(AUTO_SAVE_NOTE,defaultSaveValue);

        //Show
        //Toast.makeText(this,String.valueOf(autoSave), Toast.LENGTH_SHORT).show();


        //write

        autoSave = true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AUTO_SAVE_NOTE,autoSave);
        editor.apply();

        //Show
        //Toast.makeText(this,String.valueOf(autoSave), Toast.LENGTH_SHORT).show();

        End Preferences***/
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
            return true;

        case R.id.action_settings:
            final AlertDialog.Builder dialog_settings = new AlertDialog.Builder(NotesList.this);
            boolean defaultValue = false;
            final boolean autoSave = sharedPreferences.getBoolean(AUTO_SAVE_NOTE,defaultValue);
            dialog_settings.setTitle("Auto save note?");
            //dialog_settings.setMessage("Save auto?");

            dialog_settings.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog_settings, int i) {
                    dialog_settings.cancel();
                }
            });

            int tempSel;
            if (autoSave) {tempSel = 0;} else { tempSel = 1;}
            final CharSequence[] options = {"Yes", "No"};
            dialog_settings.setSingleChoiceItems(options, tempSel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog_settings, int i) {
                    boolean tempB;
                    if (i==0) {tempB = true;} else { tempB = false;}
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(AUTO_SAVE_NOTE,tempB);
                    editor.commit();
                    //Toast.makeText(getApplicationContext(), "Auto Save: "+ String.valueOf(tempB), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Auto Save: "+ options[i], Toast.LENGTH_SHORT).show();
                }
            });
            dialog_settings.show();

            return true;

	    default:
	           return super.onOptionsItemSelected(item);
	    }
	    }

	private void createNote() {
		Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }




/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        //startActivityForResult(i, ACTIVITY_EDIT);
        //startActivityForResult(i, ACTIVITY_EDIT);
        startActivity(i);
    }*/

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

        //setAdapter(notes);

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
