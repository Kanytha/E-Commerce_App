package com.example.lalaecomerce;

import android.os.Parcel;
import android.os.Parcelable;

public class Admin implements Parcelable {

    private long id;
    private String email;
    private String password;
    private String role;
    private String name; // Add the 'name' field

    public Admin( String email, String password, String role,String name) {
        this.id +=1;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    // Parcelable implementation
    protected Admin(Parcel in) {
        id = in.readLong();
        email = in.readString();
        password = in.readString();
        role = in.readString();
        name = in.readString(); // Read the 'name' field
    }

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

//    public Admin(String email, String password, String role, String fullName) {
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(role);
        dest.writeString(name); // Write the 'name' field
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}