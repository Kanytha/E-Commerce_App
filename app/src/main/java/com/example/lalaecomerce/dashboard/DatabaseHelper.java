package com.example.lalaecomerce.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "fullName";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_EMAIL + " TEXT PRIMARY KEY, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_FULL_NAME + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Database", "Upgrading from version " + oldVersion + " to " + newVersion);
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_FULL_NAME + " TEXT");
            Log.d("Database", "Added fullName column");
        }
         else {
        }
    }

    // 🔹 INSERT USER (with hashing for better security)
    public void insertUser(String email, String password, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, hashPassword(password));
        contentValues.put(COLUMN_FULL_NAME, fullName); // Add full_name here

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    // 🔹 CHECK IF USER EXISTS
    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_EMAIL}, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // 🔹 VALIDATE LOGIN (with hashed password comparison)
    public boolean validateLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_PASSWORD}, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
        Log.d("Login", "Email: " + email);
        Log.d("Login", "Password (hashed): " + hashPassword(password));
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            if (columnIndex != -1) {  // ✅ Prevent -1 error
                String storedPassword = cursor.getString(columnIndex);
                cursor.close();
                return storedPassword.equals(hashPassword(password)); // ✅ Hash input before comparing
            }
        }
        cursor.close();
        return false;
    }

    // 🔹 PASSWORD HASHING (SHA-256)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = null;

        String query = "SELECT " + COLUMN_FULL_NAME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
            if(columnIndex != -1) {
                fullName = cursor.getString(columnIndex);
            }
        }

        cursor.close();
        db.close();

        return fullName;
    }
    public boolean updateUserEmail(String currentEmail, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, newEmail); // Set the new email value

        // Update the user's email in the database
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_EMAIL + " = ?", new String[]{currentEmail});

        db.close();

        return rowsAffected > 0; // Return true if the update was successful, false otherwise
    }
    public boolean updateUserName(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, newName);

        int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_FULL_NAME + " = ?", new String[]{oldName});
        db.close();
        return rowsUpdated > 0;
    }


    public void printAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            String email = cursor.getString(0);
            String password = cursor.getString(1);
            String fullName = cursor.getString(2);

            Log.d("Database", "User: " + email + ", Password: " + password + ", Full Name: " + fullName);
        }
        cursor.close();
        db.close();
    }





}