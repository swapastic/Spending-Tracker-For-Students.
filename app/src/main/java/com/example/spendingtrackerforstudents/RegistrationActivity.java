package com.example.spendingtrackerforstudents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mpass;
    private Button btnreg;
    private TextView msignin;
    private ProgressDialog mdialog;

    //firebase...
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();

        mdialog=new ProgressDialog(this);
        registration();
    }

    private void registration() {
        mEmail = findViewById(R.id.email_reg);
        mpass = findViewById(R.id.Password_reg);
        msignin = findViewById(R.id.signinhere_reg);
        btnreg = findViewById(R.id.btn_REG);

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = mEmail.getText().toString().trim();
                String pass = mpass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email Required...");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    mpass.setError("Password Required..");
                    return;
                }

                mdialog.setMessage("Progressing...");
                mdialog.show();



                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()){

                            mdialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration complete",Toast.LENGTH_SHORT).show();


                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }else {
                            mdialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
        msignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

}
