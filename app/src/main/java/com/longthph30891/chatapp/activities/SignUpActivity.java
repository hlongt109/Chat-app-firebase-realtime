package com.longthph30891.chatapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.chatapp.models.User;
import com.longthph30891.chatapp.databinding.ActivitySignUpBinding;
import com.longthph30891.chatapp.utilities.Constants;
import com.longthph30891.chatapp.utilities.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();
    }
    private void setListener(){
        binding.tvSignIn.setOnClickListener(v -> onBackPressed());
        binding.btnSignUp.setOnClickListener(v -> {
            if(isValidSignUpDetail()){
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }
    private void showToat(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void signUp(){
         loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String id = UUID.randomUUID().toString();
        String name = binding.edNameSignUp.getText().toString();
        String email = binding.edEmailSignUp.getText().toString();
        String password = binding.edPasswordSignUp.getText().toString();
        User user = new User(id,name,email,password,encodedImage);
        HashMap<String, Object> map = user.convertHashMap();
        database.collection("User").document(id).set(map)
                .addOnSuccessListener(unused -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,id);
                    preferenceManager.putString(Constants.KEY_NAME,binding.edNameSignUp.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception ->{
                    loading(false);
                    showToat(exception.getMessage());
                });
    }
    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            o -> {
                if(o.getResultCode() == RESULT_OK){
                    if (o.getData() != null){
                        Uri imageUri = o.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.tvAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private Boolean isValidSignUpDetail(){
        if(encodedImage == null){
            showToat("Select profile image");
            return false;
        }else if (binding.edNameSignUp.getText().toString().trim().isEmpty()){
            showToat("Enter name");
            return false;
        } else if (binding.edEmailSignUp.getText().toString().trim().isEmpty()) {
            showToat("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmailSignUp.getText().toString()).matches()) {
            showToat("Enter valid email address");
            return false;
        } else if (binding.edPasswordSignUp.getText().toString().trim().isEmpty()) {
            showToat("Enter password");
            return false;
        }else if (binding.edConfirmPasswordSignUp.getText().toString().trim().isEmpty()){
            showToat("Confirm your password");
            return false;
        } else if (!binding.edPasswordSignUp.getText().toString().equals(binding.edConfirmPasswordSignUp.getText().toString())) {
            showToat("Password & corfirm password must be same");
            return false;
        }else {
            return true;
        }
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.btnSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}