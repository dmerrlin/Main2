package com.example.main2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    public String[] masstr;
    public int n = 0;
    public  TextView textView1;
    public  TableLayout tableLayout;
    public  SQLiteDatabase sqLiteDatabase;
    public DBhelper dbHelper;
    public int ROWS;
    public int COLUMNS;
    public LinearLayout ll;
    public String item;
    final Context context = this;
    public  ArrayAdapter<String> adapter;
    int day = 0;
    int month = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll = (LinearLayout) findViewById(R.id.linearLayout);
        dbHelper = new DBhelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();


        Cursor cursor;
        String sqlQuery = "select s.name from Subject s WHERE s.id = s.id";
        cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{});
        textView1 = (TextView) findViewById(R.id.textView1);
        String[] masSub = dbHelper.logCursor(cursor, true);
        item = masSub[0];

        cursor.close();
        if (item != null) {
            Spinner spinner = (Spinner) findViewById(R.id.cities);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, masSub);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            item = masSub[0];

            spinner.setAdapter(adapter);
            AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    // Получаем выбранный объект
                    item = (String) parent.getItemAtPosition(position);
                    UpdateDate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
            spinner.setOnItemSelectedListener(itemSelectedListener);
            sqlQuery = "select d.date from date d, DateStudent ds, subject s WHERE ds.date = d.id and ds.subject = s.id and s.name = ? GROUP BY d.date";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{item});
            textView1 = (TextView) findViewById(R.id.textView1);
            masstr = dbHelper.logCursor(cursor, true);

            textView1.setText(masstr[n]);
            cursor.close();
            Log.d(LOG_TAG, "--- ---");
            sqlQuery = "select st.name,ds.presence from date d, DateStudent ds, subject sub, student st WHERE ds.date = d.id and ds.subject = sub.id and ds.student = st.id and d.date =" + masstr[0] + " and sub.name = ? ";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{item});
            String[] masStudent = dbHelper.logCursor(cursor, false);
            cursor.close();


            ROWS = masStudent.length / 2;
           COLUMNS = 2;

            tableLayout = (TableLayout) findViewById(R.id.tableLayout);
            int k = 0;
            for (int i = 0; i < ROWS; i++) {

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                for (int j = 0; j < COLUMNS; j++) {

                    TextView textView = new TextView(this);
                    textView.setText(masStudent[k]);
                    k++;
                    textView.setTextSize(15);
                    textView.setShadowLayer(5, 2, 2, 143);


                    tableRow.addView(textView, j);
                }

                tableLayout.addView(tableRow, i);
            }
        }
    }

        public void UpdateDate(){

            String sqlQuery = "select d.date from date d, DateStudent ds, subject s WHERE ds.date = d.id and ds.subject = s.id and s.name = ? GROUP BY d.date";
            Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {item});
            textView1 = (TextView) findViewById(R.id.textView1);
            masstr = dbHelper.logCursor(cursor, true);

            textView1.setText(masstr[n]);
            cursor.close();
            Log.d(LOG_TAG, "--- ---");
            sqlQuery = "select st.name,ds.presence from date d, DateStudent ds, subject sub, student st WHERE ds.date = d.id and ds.subject = sub.id and ds.student = st.id and d.date ="+masstr[0]+" and sub.name = ? ";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {item});
            String[] masStudent = dbHelper.logCursor(cursor, false);
            cursor.close();


            ROWS = masStudent.length/2;
            COLUMNS = 2;

            tableLayout = (TableLayout) findViewById(R.id.tableLayout);
            tableLayout.removeAllViews();

            int k = 0;
            for (int i = 0; i < ROWS; i++) {

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                for (int j = 0; j <COLUMNS; j++) {

                    TextView textView= new TextView( this);
                    textView.setText(masStudent[k]);
                    k++;
                    textView.setTextSize(15);
                    textView.setShadowLayer(5, 2, 2 , 143);


                    tableRow.addView(textView, j);
                }

                tableLayout.addView(tableRow, i);
            }

        }


    public void RightClick(View view){
        if (masstr != null) {
            if (n != 0) n--;
            else n = masstr.length - 1;
            textView1.setText(masstr[n]);
            UpdateDate();
        }

    }

    public void LeftClick(View view) {
        if (masstr != null) {
            if (n != masstr.length - 1) n++;
            else n = 0;
            textView1.setText(masstr[n]);
            UpdateDate();
        }
    }

    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";
    boolean writeFile(String str) {
        try {
            // отрываем поток для записи
            if (str == "") {
                Toast.makeText(this, "Некоректный список", Toast.LENGTH_LONG).show();
                return false;
            } else {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        openFileOutput(FILENAME, MODE_PRIVATE)));
                // пишем данные
                bw.write(str);
                // закрываем поток
                bw.close();
                Log.d(LOG_TAG, "Файл записан");
                return true;
            }
            } catch(FileNotFoundException e){
                e.printStackTrace();
                return false;
            } catch(IOException e){
            Toast.makeText(this, "Некоректный список", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
            }

    }

    void readFile(String sub, String date) {
        try {

            Cursor cursor;
            String  sqlQuery =  "select d.id from date d WHERE   d.date = ? ";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {date});
            String id_d = logCursor(cursor);
            cursor.close();
            ContentValues contentValues = new ContentValues();
            if(id_d == ""){
                contentValues.put("date", date);
                sqLiteDatabase.insert("date", null, contentValues);
            }

            contentValues.clear();

            contentValues.put("name", sub);
            sqLiteDatabase.insert("subject", null, contentValues);

            contentValues.clear();

            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            int s = 0;
            while ((str = br.readLine()) != null) {


                sqlQuery = "SELECT s.id FROM student s WHERE s.name = ?";
                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {str});
                String st = (logCursor(cursor));
                cursor.close();
                int id_student;

                if (st=="")
                {
                    contentValues.clear();
                    contentValues.put("name", str);
                    sqLiteDatabase.insert("student", null, contentValues);
                    sqlQuery = "SELECT id FROM student WHERE rowid=last_insert_rowid();";
                    cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {});
                    id_student = Integer.parseInt(logCursor(cursor));

                } else id_student = Integer.parseInt(st);

                sqlQuery = "SELECT id FROM subject s WHERE s.name = ?";
                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {sub});
                int id_subject;
                id_subject = Integer.parseInt(logCursor(cursor));
                cursor.close();
                sqlQuery =  "select d.id from date d WHERE   d.date = ? ";
                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {date});
                int id_date;
                id_date = Integer.parseInt(logCursor(cursor));
                cursor.close();

                sqlQuery = "SELECT ds.id FROM DateStudent ds WHERE ds.subject = " +  id_subject +" and ds.student = " +  id_student + " and ds.date = " + id_date;
                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {});
                 st = (logCursor(cursor));
                cursor.close();

                if (st=="") {
                    contentValues.clear();
                    contentValues.put("subject", id_subject);
                    contentValues.put("student", id_student);
                    contentValues.put("date", id_date);
                    contentValues.put("presence", false);
                    sqLiteDatabase.insert("DateStudent", null, contentValues);
                }

            }

            sqlQuery = "select s.name from Subject s WHERE s.id = s.id ";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {});
            String[] masSub = dbHelper.logCursor(cursor, true);
            Spinner spinner = (Spinner) findViewById(R.id.cities);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, masSub);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            item = masSub[0];
            cursor.close();
            spinner.setAdapter(adapter);
            AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    // Получаем выбранный объект
                    item = (String)parent.getItemAtPosition(position);
                    UpdateDate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String logCursor(Cursor cursor) {
        String str = "";
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                do {
                    str = "";
                    for (String cn : cursor.getColumnNames()) {
                        str = str.concat(cursor.getString(cursor.getColumnIndex(cn)));
                    }
                    Log.d(LOG_TAG, str);

                } while (cursor.moveToNext());

            }
        } else Log.d(LOG_TAG, "Cursor is null");
        return str;

    }
        void UpdateTeable(String name){
            ContentValues cv = new ContentValues();
            cv.put("presence", true);
            Cursor cursor;
            String  sqlQuery = "select s.id from subject s  WHERE s.name = ? ";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {item});
            String id_sub =logCursor(cursor);

            cursor.close();
            sqlQuery = "select ds.id from date d, DateStudent ds, subject sub, student st WHERE ds.date = d.id and ds.subject =  sub.id and ds.student = st.id and d.date ="+masstr[n]+" and sub.id = "+id_sub+" and st.name = ? ";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {name});
             String id =logCursor(cursor);

            cursor.close();
                if (id!="") {
                    int updCount = sqLiteDatabase.update(" DateStudent", cv, "id = ?",
                            new String[] { id });

                   UpdateDate();
                } else   Toast.makeText(this, "Такого ученика не существует", Toast.LENGTH_SHORT).show();

        }

        public void clickQRCode(View view){
            try {

                // Запускаем переход на com.google.zxing.client.android.SCAN с помощью intent:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            } catch (ActivityNotFoundException anfe) {

                // Предлагаем загрузить с Play Market:
                showDialog(MainActivity.this, "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
            }

        }



    // alert dialog для перехода к загрузке приложения сканера:
    private static AlertDialog showDialog(final Activity act, CharSequence title,
                                          CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

                // Ссылка поискового запроса для загрузки приложения:
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    // Обрабатываем результат, полученный от приложения сканера:
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                // Получаем данные после работы сканера и выводим их в Toast сообщении:
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                UpdateTeable(contents);
                Toast toast = Toast.makeText(this, "Студент: " + contents + " добавлен ", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void dateClick(View view){
        //get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_adddate, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        //set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        //set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //get user input and set it to result
                                //edit text
                                if (masstr != null) {
                                    checkDate(userInput.getText().toString());
                                }

                            }

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        //create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //show it
        alertDialog.show();

    }

    public void studentClick(View view){
        //get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_addstudent, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        //set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        //set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //get user input and set it to result
                                //edit text
                                if (masstr != null) {
                                    addStudent(userInput.getText() + "");
                                    UpdateDate();
                                }
                            }

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        //create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //show it
        alertDialog.show();

    }


    public void subjectClick(View view){
        //get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_addsubject, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        //set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText NameInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput1);
        final EditText DateInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput2);
        final EditText StudentInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput3);

        //set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Cursor cursor;
                                String sqlQuery = "select s.id from Subject s WHERE s.name = ?";
                                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[] {NameInput.getText()+""});
                                String id_name =logCursor(cursor);
                                cursor.close();
                                if(id_name=="" && checkDateSub( DateInput.getText()+"")==true && writeFile(StudentInput.getText()+"")==true){
                                    readFile(NameInput.getText()+"", DateInput.getText()+"");
                                    UpdateDate();
                                }
                            }

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        //create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //show it
        alertDialog.show();

    }

    public boolean checkDateSub(String str) {
        char[] chars = str.toCharArray();
        String ch = ".";
        str = "";
        day = 0;
        month = 0;
        boolean check = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ch.charAt(0)) {
                try {
                    day = Integer.parseInt(str);
                    str = "";
                    check = true;
                    if (day > 31 || day < 1 )
                            {
                    Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
                                return false;}
                } catch (Exception e) {
                    Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return false;
                }
            } else {
                str += chars[i];
            }

        }
        if (str.length() != 0 && check == true) {
            try {
                month = Integer.parseInt(str);
                if (month > 12 || month < 1 )  {
                    Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
                    return false;} else
                return true;
            } catch (Exception e) {
                Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return false;
            }
        } else  {
            Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
            return false;}
    }

    public void checkDate(String str){
        char[] chars = str.toCharArray();
        String ch = ".";
        str = "";
        day = 0;
        month = 0;
        boolean check = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ch.charAt(0)) {
                try {
                    day = Integer.parseInt(str);
                    str = "";
                    if (day > 31 || day < 1 )
                    {
                        Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
                        break; } else  check = true;

                } catch (Exception e) {
                    Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {
                str += chars[i];
            }

        }
        if (str.length() != 0 && check == true) {
            try {
                month = Integer.parseInt(str);
                if (month > 12 || month < 1) {
                    Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();

                } else {
                    ContentValues contentValues = new ContentValues();
                    String sqlQuery = "select st.name from student st WHERE st.id = st.id ";
                    Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{});
                    String[] masStudent = dbHelper.logCursor(cursor, true);
                    String date_str = day + "." + month;

                    sqlQuery = "select d.id from date d WHERE d.date = ?";
                    cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{date_str});
                    String id_dat = logCursor(cursor);
                    cursor.close();
                    if (id_dat == "") {
                        contentValues.put("date", date_str);
                        sqLiteDatabase.insert("date", null, contentValues);
                        cursor.close();

                        for (int i = 0; i < masStudent.length; i++) {
                            str = masStudent[i];
                            sqlQuery = "SELECT s.id FROM student s WHERE s.name = ?";
                            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{str});
                            String st = (logCursor(cursor));
                            cursor.close();
                            int id_student;

                            id_student = Integer.parseInt(st);

                            sqlQuery = "SELECT id FROM subject s WHERE s.name = ?";
                            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{item});
                            int id_subject;
                            id_subject = Integer.parseInt(logCursor(cursor));
                            cursor.close();
                            sqlQuery = "select d.id from date d WHERE d.date = ?";
                            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{date_str});
                            int id_date;
                            id_date = Integer.parseInt(logCursor(cursor));
                            cursor.close();

                            sqlQuery = "SELECT ds.id FROM DateStudent ds WHERE ds.subject = " + id_subject + " and ds.student = " + id_student + " and ds.date = " + id_date;
                            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{});
                            st = (logCursor(cursor));
                            cursor.close();

                            if (st == "") {
                                contentValues.clear();
                                contentValues.put("subject", id_subject);
                                contentValues.put("student", id_student);
                                contentValues.put("date", id_date);
                                contentValues.put("presence", false);
                                sqLiteDatabase.insert("DateStudent", null, contentValues);
                            }

                        }
                        Toast.makeText(this, "Дата: " + day + "." + month + "добавлена", Toast.LENGTH_LONG).show();
                        UpdateDate();
                    } else  Toast.makeText(this, "Дату уже принадлежит предмету: "+ item , Toast.LENGTH_LONG).show();
                }
                } catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
                }

        } else   Toast.makeText(this, "Некоректная дата", Toast.LENGTH_LONG).show();
    }

    public void addStudent(String str) {
        if (str != "") {
            Cursor cursor;
            String sqlQuery = "select d.date from date d, DateStudent ds, subject sub WHERE ds.date = d.id and ds.subject = sub.id and sub.name = ? ";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{item});
            String[] masdate = dbHelper.logCursor(cursor, true);
            cursor.close();
            ContentValues contentValues = new ContentValues();

            sqlQuery = "SELECT s.id FROM student s WHERE s.name = ?";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{str});
            String st = (logCursor(cursor));
            cursor.close();
            int id_student;

            if (st == "") {
                contentValues.clear();
                contentValues.put("name", str);
                sqLiteDatabase.insert("student", null, contentValues);
                sqlQuery = "SELECT id FROM student WHERE rowid=last_insert_rowid();";
                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{});
                id_student = Integer.parseInt(logCursor(cursor));

            } else id_student = Integer.parseInt(st);

            sqlQuery = "SELECT id FROM subject s WHERE s.name = ?";
            cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{item});
            int id_subject;
            id_subject = Integer.parseInt(logCursor(cursor));
            cursor.close();

            if (st=="") {
            for (int i = 0; i < masdate.length; i++) {

                sqlQuery = "select d.id from date d WHERE   d.date = ? ";
                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{masdate[i]});
                int id_date;
                id_date = Integer.parseInt(logCursor(cursor));
                cursor.close();

                sqlQuery = "SELECT ds.id FROM DateStudent ds WHERE ds.subject = " + id_subject + " and ds.student = " + id_student + " and ds.date = " + id_date;
                cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{});
                st = (logCursor(cursor));
                cursor.close();

                if (st == "") {
                    contentValues.clear();
                    contentValues.put("subject", id_subject);
                    contentValues.put("student", id_student);
                    contentValues.put("date", id_date);
                    contentValues.put("presence", false);
                    sqLiteDatabase.insert("DateStudent", null, contentValues);
                }
            }
                Toast.makeText(this, "Студент:" + str + " добавлен", Toast.LENGTH_LONG).show();
            } else Toast.makeText(this, "Студент уже добавлен", Toast.LENGTH_LONG).show();

        } else  Toast.makeText(this, "Ошибка добавления студента", Toast.LENGTH_LONG).show();
    }
}
