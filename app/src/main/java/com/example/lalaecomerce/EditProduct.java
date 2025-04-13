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
import android.widget.Switch;
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

public class EditProduct extends AppCompatActivity {

    private EditText productName, productPrice, productDescription;
    private ImageView productImage; // Ensure this matches the layout ID
    private Uri selectImageUri;
    private Button changeImageButton, saveChangesButton;
    private Product editedProduct;
    private Switch markAsSoldOutSwitch;

    private Uri selectedImageUri; // To store the URI of the selected image
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize views
        productName = findViewById(R.id.editProductName);
        productPrice = findViewById(R.id.editProductPrice);
        productDescription = findViewById(R.id.editProductDescription);
        productImage = findViewById(R.id.editProductImage);
        changeImageButton = findViewById(R.id.changeImageButton);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        // Retrieve the product from the intent
        product = getIntent().getParcelableExtra("product");

        if (product != null) {
            // Populate the form with existing product data
            productName.setText(product.getName());
            productPrice.setText(String.valueOf(product.getPrice()));
            productDescription.setText(product.getDescription());

            // Load the existing image using Glide
            Glide.with(this)
                    .load(product.getImageUri())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(productImage);
        }

        // Set up click listeners
        changeImageButton.setOnClickListener(v -> showImageOptionsDialog());
        saveChangesButton.setOnClickListener(v -> saveChanges());
    }

    private void showImageOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source")
                .setItems(new CharSequence[]{"Gallery", "Camera"}, (dialog, which) -> {
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

    private void saveChanges() {
        String name = productName.getText().toString().trim();
        String priceString = productPrice.getText().toString().trim();
        String description = productDescription.getText().toString().trim();

        if (name.isEmpty() || priceString.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);

        // Update the product object
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);

        // Only update the image URL if a new image was selected
        if (selectedImageUri != null) {
            product.setImageUri(selectedImageUri);
        }

        // Return the updated product to the calling activity
        Intent intent = new Intent();
        intent.putExtra("updatedProduct", product);
        setResult(RESULT_OK, intent);
        finish(); // Close the activity
    }

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
}

