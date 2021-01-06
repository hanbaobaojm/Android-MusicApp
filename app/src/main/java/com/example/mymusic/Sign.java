package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.UUID;

public class Sign extends AppCompatActivity {
    private EditText name, password, password1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.SignName);
        password = (EditText) findViewById(R.id.SignPassword);
        password1 = (EditText) findViewById(R.id.confirmPassword);
        Button sign = findViewById(R.id.button);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirm())
                    save();
                else
                    Toast.makeText(Sign.this,"Password not correct",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmTodo();
    }
    private boolean confirm(){
        String Password1 = password.getText().toString();
        String Password2 = password1.getText().toString();
        if(Password1.equals(Password2))
            return true;
        else
            return false;
    }
    private void save(){
        String Name = name.getText().toString();
        String Password = password.getText().toString();
        getRealm().beginTransaction();
        user User = getRealm().createObject(user.class, UUID.randomUUID().toString());
        User.setName(Name);
        User.setPassword(Password);
        getRealm().commitTransaction();
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        Toast.makeText(this,"Registration successful!",Toast.LENGTH_SHORT).show();
        finish();
    }
}
