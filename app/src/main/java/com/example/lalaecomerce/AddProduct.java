package com.example.lalaecomerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProduct extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 1;
    public static final int PICK_IMAGE_REQUEST = 2;
    private EditText productName, productPrice, productDescription;
    private ImageView productImage;
    private Button selectImageButton, saveButton;

    private Uri selectedImageUri; // To store the URI of the selected image
    private String category; // To store the category passed from the previous activity

    // Constants for request codes
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        productName = findViewById(R.id.addProductName);
        productPrice = findViewById(R.id.addProductPrice);
        productDescription = findViewById(R.id.addProductDescription);
        productImage = findViewById(R.id.addProductImage);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve the category from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category")) {
            category = intent.getStringExtra("category");
        }

        // Set up click listeners
        selectImageButton.setOnClickListener(v -> showImageOptionsDialog());
        saveButton.setOnClickListener(v -> saveProduct());
    }

    private void showImageOptionsDialog() {
        // Show dialog to choose between gallery and camera
        String[] options = {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openGallery();
                    } else {
                        openCamera();
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                // Get the image URI from the gallery
                selectedImageUri = data.getData();
                productImage.setImageURI(selectedImageUri);
            } else if (requestCode == CAMERA_REQUEST) {
                // Get the image from the camera
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                productImage.setImageBitmap(imageBitmap);

                try {
                    File imageFile = saveBitmapToFile(imageBitmap);
                    selectedImageUri = Uri.fromFile(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) throws IOException {
        // Create a unique file name for the image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);

        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        return imageFile;
    }

    private void saveProduct() {
        // Retrieve input values
        String name = productName.getText().toString().trim();
        String priceString = productPrice.getText().toString().trim();
        String description = productDescription.getText().toString().trim();

        // Validate input fields
        if (name.isEmpty() || priceString.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);

        // Create a new Product object
        Product newProduct = new Product(name, price, description, selectedImageUri, category);

        // Return the new product to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("PRODUCT", newProduct); // Pass the product as Parcelable
        setResult(RESULT_OK, resultIntent);
        finish(); // Close the activity
    }
}