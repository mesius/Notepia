package com.topiasoft.notepia;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by fm on 14/11/2016.
 */

public class NoteEdit extends AppCompatActivity{

    public static int numTitle = 1;
    public static String curDate = "";
    public static String curText = "";
    public static String newText = "";
    public static boolean changed = false;
    private EditText mBodyText;
    private TextView mDateText;
    private Long mRowId;
    private boolean autoSave = false;
    private static final String AUTO_SAVE_NOTE = "AUTO_SAVE_NOTE";

    /*Preferences*/
    SharedPreferences sharedPreferences;

    private Cursor note;

    private NotesDbAdapter mDbHelper;
    //Context context = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.app_name);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);



        /*mBodyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        /*
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setLogo(R.mipmap.ic_launcher);
        */

        //ActionBar myActionBar = getSupportActionBar();
        //myActionBar.setIcon(R.mipmap.ic_launcher);
        //myActionBar.setLogo(R.drawable.btn_delete);


        sharedPreferences = getSharedPreferences(AUTO_SAVE_NOTE, Context.MODE_PRIVATE);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();



        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mDateText = (TextView) findViewById(R.id.notelist_date);


        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);

        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
        curDate = formatter.format(curDateTime);

        mDateText.setText(""+curDate);


        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                    : null;
        }

        populateFields();

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        //outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.noteedit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //AlertDialog.Builder dialog = new AlertDialog.Builder(NoteEdit.this);


        newText = mBodyText.getText().toString();
        if (!newText.equals(curText)) changed = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                if (changed) {
                    //boolean defaultValue = false;
                    //final boolean autoSave = sharedPreferences.getBoolean(AUTO_SAVE_NOTE,defaultValue);
                    boolean defaultValuex = false;
                    final boolean autoSavex = sharedPreferences.getBoolean(AUTO_SAVE_NOTE,defaultValuex);

                    if (autoSavex){
                        saveState();
                        showSaveMsg();
                        finish();
                    } else {
                        AlertDialog.Builder dialog3 = new AlertDialog.Builder(NoteEdit.this);
                        dialog3.setTitle("Back");
                        dialog3.setMessage("Save Changes?"
                        );
                        dialog3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog3, int which) {
                                dialog3.cancel();
                                saveState();
                                showSaveMsg();
                                finish();
                            }
                        });
                        dialog3.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog3, int which) {
                                dialog3.cancel();
                                finish();
                            }
                        });
                        dialog3.show();
                        //finish();

                    }

                } else {
                    finish();
                }
                changed = false;
                //break;
                return true;
            case R.id.menu_about:

		    	/* Here is the introduce about myself */
                AlertDialog.Builder dialog = new AlertDialog.Builder(NoteEdit.this);
                dialog.setTitle("About");
                dialog.setMessage("Notepia\n\t" + "A simple and light notes application to take quick notes, reminders, etc.\n\t" + "For more applications, bugs and feature requests you can visit: \n\thttp://www.topiasoft.com"
                );
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                dialog.show();
                //break;
                return true;
            case R.id.menu_delete:
                if(note != null){
					/* Here is the introduce about myself */
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(NoteEdit.this);
                    dialog2.setTitle("Delete Note");
                    dialog2.setMessage("Do you want to delete the current note?"
                    );
                    dialog2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog2, int which) {
                            note.close();
                            note = null;
                            if(mRowId != null){
                                mDbHelper.deleteNote(mRowId);

                                Context context = getApplicationContext();
                                CharSequence text = "Note deleted";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            finish();
                        }
                    });
                    dialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog2, int which) {
                            dialog2.cancel();
                            //finish();
                        }
                    });
                    dialog2.show();
                }
                //break;
                return true;
            case R.id.menu_save:
                saveState();
                showSaveMsg();
                //changed = false;
                //finish();
                //break;
                return true;
            case R.id.action_share:
                //note.close();
                //note = null;
                shareIt();
                //break;
                return true;
            case R.id.action_settings:
                final AlertDialog.Builder dialog_settings = new AlertDialog.Builder(NoteEdit.this);
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
                final CharSequence[] options = {"Si", "No"};
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
	    	/*
		    case R.id.menu_back:
		    	//note.close();
		    	//note = null;
	    		finish();
	    		return true;
	    		*/
            default:
                return super.onOptionsItemSelected(item);
                //return true;
        }
    }



    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //String shareBody = "Here is the share content body";
        String shareBody = mBodyText.getText().toString();
        String subject = shareBody;
        if (subject.length() > 25) {
            subject = subject.substring(0, 25);
        }
        subject=subject.replace("\r"," ");
        subject=subject.replace("\n"," ");
        Context context = getApplicationContext();
        CharSequence shareVia = context.getString(R.string.share_via);

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, shareVia));
        //startActivity(Intent.createChooser(sharingIntent, R.string.share_via));
    }

    private void saveState() {
        //String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        String title = body;
        if (title.length() > 20) {
            title = title.substring(0, 20);
        }
        title=title.replace("\r"," ");
        title=title.replace("\n"," ");


        if(mRowId == null){
            long id = mDbHelper.createNote(title, body, curDate);
            if(id > 0){
                mRowId = id;
            }else{
                Log.e("saveState","failed to create note");
            }
        }else{
            if(!mDbHelper.updateNote(mRowId, title, body, curDate)){
                Log.e("saveState","failed to update note");
            }
        }
        curText = mBodyText.getText().toString();
        changed = false;

    }

    private void showSaveMsg()
    {
        Context context = getApplicationContext();
        CharSequence text = getText(R.string.toast_note_saved);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        //Toast.makeText(context, text, duration).show();
    }


    private void populateFields() {
        if (mRowId != null) {
            note = mDbHelper.fetchNote(mRowId);
            //startManagingCursor(note);
	            /*
	            mTitleText.setText(note.getString(
	    	            note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
	    	    */
            String tmptext;
            tmptext = note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));
            tmptext = tmptext.substring(0, tmptext.length());
            mBodyText.setText(tmptext);
            curText = mBodyText.getText().toString();
            mBodyText.setSelection(mBodyText.getText().length());
            changed = false;
	            /*curText = note.getString(
	                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));*/
        }
    }


    @Override
    public void onBackPressed() {
        newText = mBodyText.getText().toString();
        if (!newText.equals(curText)) changed = true;
        if (changed) {
            //boolean defaultValue = false;
            //final boolean autoSave = sharedPreferences.getBoolean(AUTO_SAVE_NOTE,defaultValue);
            boolean defaultValuex = false;
            final boolean autoSavex = sharedPreferences.getBoolean(AUTO_SAVE_NOTE,defaultValuex);

            if (autoSavex){
                saveState();
                showSaveMsg();
                finish();
            } else {
                AlertDialog.Builder dialog3 = new AlertDialog.Builder(NoteEdit.this);
                dialog3.setTitle("Back");
                dialog3.setMessage("Save Changes?"
                );
                dialog3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog3, int which) {
                        dialog3.cancel();
                        saveState();
                        showSaveMsg();
                        finish();
                    }
                });
                dialog3.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog3, int which) {
                        dialog3.cancel();
                        finish();
                    }
                });
                dialog3.show();
                //finish();

            }

        } else {
            //Toast toast = Toast.makeText(getApplicationContext(),"Back!", Toast.LENGTH_SHORT);
            //toast.show();
            finish();
        }


        //super.onBackPressed();
    }


}
