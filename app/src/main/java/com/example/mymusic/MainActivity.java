package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmResults;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText name, password;
    private Button login, signup;
    private Realm realm;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        name = (EditText) findViewById(R.id.editName);
        password = (EditText) findViewById(R.id.editPassword);
        login = findViewById(R.id.LogIn);
        signup = findViewById(R.id.SignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Sign.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                Intent intent = new Intent(MainActivity.this, MainPage.class);
                if(flag==1)
                startActivity(intent);
                else
                    wrong();
            }
        });
    }
    private void getData(){
        String Name = name.getText().toString();
        String Password = password.getText().toString();
        realm = ((MainApplication)getApplication()).getRealmTodo();
        RealmResults<user> userList = realm.where(user.class).findAll();
        int m = userList.size();
        //RealmResults<user> realmResults = realm.where(user.class).equalTo("name", Name).findAllAsync();
        //int i = realmResults.size();
        for(int j=0;j<m;j++) {
            if(userList.get(j).getName().equals(Name)&&userList.get(j).getPassword().equals(Password)){
            //if(Password.equals(realmResults.get(j).getPassword())) {
                flag=1;
                break;
            }
            else
                flag=0;
        }
    }
    private void wrong() {
        Toast.makeText(this,"The name or password is wrong!",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }
}
