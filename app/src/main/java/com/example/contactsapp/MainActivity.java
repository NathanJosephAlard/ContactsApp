package com.example.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactList;
    private List<Contact> filteredList;
    private DatabaseHelper databaseHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        contactList = new ArrayList<>();

        loadContacts();

        contactsAdapter = new ContactsAdapter(this, filteredList);
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactFormActivity.class);
                startActivity(intent);
            }
        });

        searchView = findViewById(R.id.contact_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });
    }

    private void loadContacts() {
        contactList.clear();
        contactList.addAll(databaseHelper.getAllContacts());
        contactsAdapter.notifyDataSetChanged();
    }

    private void filterContacts(String query) {
        if (filteredList == null) {
            filteredList = new ArrayList<>(); // Ensure it's initialized
        }

        filteredList.clear(); // Clear the filtered list before starting the search
        if (query.isEmpty()) {
            filteredList.addAll(contactList); // No search term, add all contacts
        } else {
            query = query.toLowerCase(); // Convert search term to lower case for case-insensitive comparison
            for (Contact contact: contactList) {
                // Check if the contact's name contains the search term
                if (contact.getName().toLowerCase().contains(query)) {
                    filteredList.add(contact); // It's a match, add it to the filtered list
                }
            }
        }

        contactList.addAll(filteredList);
        contactsAdapter.notifyDataSetChanged();
    }

    private void sortContacts(String sortBy) {
        if (sortBy.equals("Name")) {
            Collections.sort(filteredList, new Comparator<Contact>() {
                @Override
                public int compare(Contact c1, Contact c2) {
                    return c1.getName().compareToIgnoreCase(c2.getName());
                }
            });
        } else if (sortBy.equals("Birthday")) {
            Collections.sort(filteredList, new Comparator<Contact>() {
                @Override
                public int compare(Contact c1, Contact c2) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    try {
                        Date date1 = format.parse(c1.getBirthday());
                        Date date2 = format.parse(c2.getBirthday());
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
        }

        // This is the crucial part: updating the adapter with the now-sorted list
       updateList(filteredList);
    }

    public void updateList(List<Contact> newList) {
        contactList.addAll(newList);
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the contact list
        contactList.clear();
        contactList.addAll(databaseHelper.getAllContacts());
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}