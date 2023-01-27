package com.example.a10_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "address_book.db";
    private static final int DATABASE_VERSION = 1;

    private ListView person_listView;
    private ArrayList<Person> person_list;
    private ArrayAdapter<Person> array_adapter;

    PersonDatabase person_database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//person_list = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        person_database = new PersonDatabase(this, DATABASE_NAME, DATABASE_VERSION);

        person_listView = (ListView) findViewById(R.id.person_listView);

        File file = this.getDatabasePath(DATABASE_NAME);
        System.out.println(file.getAbsolutePath());

        person_list = new ArrayList<Person>();
        person_database.load(person_list);

        array_adapter = new ArrayAdapter<Person>(this, R.layout.list_item, person_list);

        person_listView.setAdapter(array_adapter);
        array_adapter.notifyDataSetChanged();
    }//onCreate

    public class PersonDatabase extends SQLiteOpenHelper {
        public PersonDatabase(Context context, String name, int version) {
            super(context, name, null, version);
        }//PersonDatabase()

        private static final String TABLE_PERSON = "person";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_PHONE = "phone";
        private static final String COLUMN_EMAIL = "email";

        public void load (ArrayList<Person> person_list) {
            person_list.clear();

            String query = "SELECT * FROM " + TABLE_PERSON;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            cursor.moveToFirst();

            for(int i = 0; i < cursor.getCount(); i++) {
                //get all fields
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);

                //create a new person object
                Person person = new Person(name, phone, email);
                person_list.add(person);

                //move to the next record
                cursor.moveToNext();
            }//for
        }

        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String CreateTableStr = "CREATE TABLE " + TABLE_PERSON + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PHONE + " TEXT, "
                    + COLUMN_EMAIL + " TEXT" + ")";
            sqLiteDatabase.execSQL(CreateTableStr);
        }//onCreate()

        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}//onUpgrade()
    }//PersonDatabase()

    public class Person {

        private String name = "";
        private String phone = "";
        private String email = "";
        private static final long serialVersionUID = 1L;

        public Person(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }//Person

        //set / get operations
        public String getName() { return name; }//getName
        public void setName(String name) { this.name = name; }//setName

        public String getPhone() { return phone; }//getPhone
        public void setPhone(String phone) { this.phone = phone; }//setPhone

        public String getEmail() { return email; }//getEmail
        public void setEmail(String email) { this.email = email; }//setEmail

        //toString method
        public String toString() {
            return name + ":" + phone + ":" + email;
        }//toString

        public String set_name( String name ) {
            return this.name = name;
        }//set_name

        public String set_phone( String phone ) {
            return this.phone = phone;
        }//set_phone

        public String set_email( String email ) {
            return this.email = email;
        }//set_email
    }//Person class
}//MainActivity()
