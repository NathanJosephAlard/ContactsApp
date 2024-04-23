package com.example.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_CONTACT = 1; // Request code for adding/editing a contact

    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactList;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAddContact;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(this, contactList);
        recyclerView.setAdapter(contactsAdapter);

        databaseHelper = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();

        fabAddContact = findViewById(R.id.fab_add_contact);
        fabAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactFormActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);
        });

        loadContacts();
    }

    private void loadContacts() {
        executorService.execute(() -> {
            List<Contact> contacts = databaseHelper.getAllContacts();
            new Handler(Looper.getMainLooper()).post(() -> {
                contactList.clear();
                contactList.addAll(contacts);
                contactsAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_CONTACT && resultCode == RESULT_OK) {
            loadContacts();  // Reload contacts if a new contact was added or an existing one was updated
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();  // Ensure the latest contacts are displayed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();  // Shutdown the executor service to avoid memory leaks
    }
}
