package com.example.git_chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseUser current_user;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference mDatabase = database.getReference();

    private DatabaseReference userRef = mDatabase.child("Users");

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    private Toolbar mToolbar;


    private ProgressDialog mRegProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegProgress = new ProgressDialog(this);


        mDisplayName = findViewById(R.id.reg_display_name);
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.reg_password);
        mCreateBtn = findViewById(R.id.reg_create_btn);

        mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCreateBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) ){

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){





                                try{
                                    current_user = mAuth.getCurrentUser();
                                    String uid = current_user.getUid();

                                    HashMap<String,String> userMap = new HashMap<>();
                                    userMap.put("name",display_name);
                                    userMap.put("status","Hi there I'm using GIT-Chat");
                                    userMap.put("image","default");
                                    userMap.put("thumb_image","default");

                                    userRef.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            mRegProgress.dismiss();

                                            Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();


                                        }
                                    });
                                }
                                catch (NullPointerException e){
                                    e.printStackTrace();
                                }




//





                            }
                            else {

                                mRegProgress.hide();
                                Toast.makeText(RegisterActivity.this,"Cannot Sign in. Please check the form and try again",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }


            }
        });
    }


}
