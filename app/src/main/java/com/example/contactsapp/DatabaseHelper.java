package com.example.contactsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactsManager.db";
    private static final String TABLE_CONTACTS = "contacts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTHDAY = "birthday";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_PHONE + " TEXT," + KEY_BIRTHDAY + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    public long addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("email", contact.getEmail());
        values.put("phone", contact.getPhone());
        values.put("birthday", contact.getBirthday());

        long id = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        return id;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Check for valid column indices
        int idIndex = cursor.getColumnIndex(KEY_ID);
        int nameIndex = cursor.getColumnIndex(KEY_NAME);
        int emailIndex = cursor.getColumnIndex(KEY_EMAIL);
        int phoneIndex = cursor.getColumnIndex(KEY_PHONE);
        int birthdayIndex = cursor.getColumnIndex(KEY_BIRTHDAY);

        if (idIndex == -1 || nameIndex == -1 || emailIndex == -1 || phoneIndex == -1 || birthdayIndex == -1) {
            throw new IllegalArgumentException("Database schema has changed or column names are incorrect");
        }

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(birthdayIndex)
                );
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return contactList;
    }
}
