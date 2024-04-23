package com.example.contactsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ContactsManager.db";
    private static final String TABLE_CONTACTS = "contacts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTHDAY = "birthday";

    // Constructor is now public
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_BIRTHDAY + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple database upgrade strategy: drop and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public long addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_BIRTHDAY, contact.getBirthday());

        long id = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        return id;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_BIRTHDAY },
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int emailIndex = cursor.getColumnIndex(KEY_EMAIL);
            int phoneIndex = cursor.getColumnIndex(KEY_PHONE);
            int birthdayIndex = cursor.getColumnIndex(KEY_BIRTHDAY);

            if (idIndex != -1 && nameIndex != -1 && emailIndex != -1 && phoneIndex != -1 && birthdayIndex != -1) {
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
        }
        cursor.close();
        db.close();
        return contactList;
    }




    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_BIRTHDAY, contact.getBirthday());

        int updateStatus = db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
        return updateStatus;
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }
}
