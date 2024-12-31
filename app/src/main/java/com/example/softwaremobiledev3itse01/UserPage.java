package com.example.softwaremobiledev3itse01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import org.w3c.dom.Text;

public class UserPage extends AppCompatActivity implements View.OnClickListener {

    private Button logoutbtn;

    private TextView textCreate;

    private TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        logoutbtn = (Button) findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(this);

        textCreate = (TextView) findViewById(R.id.textCreate);
        textCreate.setOnClickListener(this);

        textStatus = (TextView) findViewById(R.id.textStatus);
        textStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logoutbtn) {
            Intent intent = new Intent(UserPage.this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textCreate) {
            Intent intent = new Intent(UserPage.this, CreateFdbkPage.class);
            startActivity(intent);
        } else if(v.getId() == R.id.textStatus){
            Intent intent = new Intent(UserPage.this, StatusTablePage.class);
            startActivity(intent);
        }
    }
}