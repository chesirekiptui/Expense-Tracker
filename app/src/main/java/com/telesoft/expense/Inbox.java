package com.telesoft.expense;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Inbox extends AppCompatActivity {
    ListView listView;
    private static  final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ArrayList smsList;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        listView = findViewById(R.id.idList);

        //starting the service
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            showContacts();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showContacts();
            }else {
                Toast.makeText(this, "Get necessary permission first", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showContacts() {
        Uri inboxURI = Uri.parse("content://sns/inbox/MPESA");
        smsList = new ArrayList();

        ContentResolver cr = getContentResolver();


        Cursor c = cr.query(inboxURI, null, null, null, null);
        while(c.moveToNext()){
            String Number = c.getString(c.getColumnIndexOrThrow("address")).toString();
            String Body = c.getString(c.getColumnIndexOrThrow("body")).toString();
            smsList.add("Number: "+Number + "\n"+ "Body: " + Body);
        }
        c.close();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, smsList);
        listView.setAdapter(adapter);
    }

}