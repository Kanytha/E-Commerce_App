package com.example.lalaecomerce;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LalaEcommerce.db";
    private static final int DATABASE_VERSION = 2;

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_FULL_NAME = "full_name";

    // Products Table
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_PRICE = "price";
    private static final String COLUMN_PRODUCT_IMAGE_URL = "image_url";
    private static final String COLUMN_PRODUCT_CATEGORY = "category";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_EMAIL + " TEXT PRIMARY KEY, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_ROLE + " TEXT, "
                + COLUMN_FULL_NAME + " TEXT)");

        insertAdminIfNotExists(db);

        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_PRICE + " REAL, "
                + COLUMN_PRODUCT_IMAGE_URL + " TEXT, "
                + COLUMN_PRODUCT_CATEGORY + " TEXT, "
                + COLUMN_PRODUCT_DESCRIPTION + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Recreate tables
        onCreate(db);
    }


    private void insertAdminIfNotExists(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + " = ?",
                new String[]{"admin@example.com"}, null, null, null);
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, "admin@example.com");
            values.put(COLUMN_PASSWORD, hashPassword("adminpassword"));
            values.put(COLUMN_ROLE, "admin");
            values.put(COLUMN_FULL_NAME, "Admin User");
            db.insert(TABLE_USERS, null, values);
            Log.d("DatabaseHelper", "Admin inserted");
        }
        cursor.close();
    }

    public boolean insertUser(String email, String password, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email.toLowerCase());
        contentValues.put(COLUMN_PASSWORD, hashPassword(password));
        contentValues.put(COLUMN_ROLE, "user");
        contentValues.put(COLUMN_FULL_NAME, fullName);
        return db.insert(TABLE_USERS, null, contentValues) != -1;
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_EMAIL}, COLUMN_EMAIL + " = ?", new String[]{email.toLowerCase()}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean validateLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PASSWORD}, COLUMN_EMAIL + " = ?", new String[]{email.toLowerCase()}, null, null, null);
        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(0);
            cursor.close();
            return storedPassword.equals(hashPassword(password));
        }
        cursor.close();
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ROLE}, COLUMN_EMAIL + " = ?", new String[]{email.toLowerCase()}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        return "user";
    }

    public Admin getAdminByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_ROLE, COLUMN_FULL_NAME}, COLUMN_EMAIL + " = ?", new String[]{email.toLowerCase()}, null, null, null);
        if (cursor.moveToFirst()) {
            String storedEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            cursor.close();
            return new Admin(storedEmail, password, role, fullName);
        }
        cursor.close();
        return null;
    }

    public boolean updateAdmin(String fullName, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_PASSWORD, hashPassword(password));
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email.toLowerCase()});
        return rowsAffected > 0;
    }

    public boolean updateUserEmail(String currentEmail, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, newEmail);
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{currentEmail.toLowerCase()});
        return rowsAffected > 0;
    }

    public boolean updateUserName(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, newName);
        int rowsUpdated = db.update(TABLE_USERS, values, COLUMN_FULL_NAME + " = ?", new String[]{oldName});
        return rowsUpdated > 0;
    }

    public String getUserFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_FULL_NAME}, COLUMN_EMAIL + " = ?", new String[]{email.toLowerCase()}, null, null, null);
        if (cursor.moveToFirst()) {
            String fullName = cursor.getString(0);
            cursor.close();
            return fullName;
        }
        cursor.close();
        return null;
    }

    public void printAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        while (cursor.moveToNext()) {
            String email = cursor.getString(0);
            String password = cursor.getString(1);
            String fullName = cursor.getString(2);
            Log.d("Database", "User: " + email + ", Password: " + password + ", Full Name: " + fullName);
        }
        cursor.close();
    }

    public boolean addProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_IMAGE_URL, product.getImageUri().toString());
        values.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
        long result = db.insert(TABLE_PRODUCTS, null, values);
        if (result != -1) {
            product.setId(result);
            return true;
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_IMAGE_URL, product.getImageUri().toString());
        values.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
        int rowsAffected = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getId())});
        return rowsAffected > 0;
    }

    public ArrayList<Product> getProductsByCategory(String category) {
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_PRODUCTS, null, COLUMN_PRODUCT_CATEGORY + " = ?", new String[]{category}, null, null, null)) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE_URL));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION));
                Product product = new Product(name, price, description, Uri.parse(imageUrl), category);
                products.add(product);
            }
        }
        return products;
    }
}
