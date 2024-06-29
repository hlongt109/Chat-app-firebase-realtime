package com.longthph30891.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.chatapp.R;
import com.longthph30891.chatapp.databinding.ActivitySignInBinding;
import com.longthph30891.chatapp.utilities.Constants;
import com.longthph30891.chatapp.utilities.PreferenceManager;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        isLogged();
        setListener();
    }
    public void isLogged(){
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void setListener(){
       binding.tvCreateNewAccount.setOnClickListener(v ->
               startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));
       binding.btnSignIn.setOnClickListener(v ->{
           if (isValidSignInDetails()){
               signIn();
           }
       });
       binding.tvPorgotPassword.setOnClickListener(v ->{

       });
    }
    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS_TABLE)
                .whereEqualTo(Constants.KEY_EMAIL,binding.edEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.edPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                     if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() >0){
                         DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                         preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                         preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                         preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
                         preferenceManager.putString(Constants.KEY_IMAGE,documentSnapshot.getString(Constants.KEY_IMAGE));
                         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                     }else {
                         loading(false);
                         showToat("Unable to sign in");
                     }
                });
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.btnSignIn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private Boolean isValidSignInDetails(){
        if(binding.edEmail.getText().toString().trim().isEmpty()){
            showToat("Enter your email address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.getText().toString()).matches()) {
            showToat("Enter valid email");
            return false;
        } else if (binding.edPassword.getText().toString().trim().isEmpty()) {
            showToat("Enter your password");
            return false;
        }else {
            return true;
        }
    }
    private void showToat(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}