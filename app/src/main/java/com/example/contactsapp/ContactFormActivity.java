package com.example.contactsapp;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class ContactFormActivity extends AppCompatActivity {
    private Button birthdayButton, saveContactButton;
    private TextInputEditText nameEditText, emailEditText, phoneEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        birthdayButton = findViewById(R.id.birthday_button);
        saveContactButton = findViewById(R.id.save_contact_button);

        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
    }

    private void saveContact() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String birthday = birthdayButton.getText().toString();

        // Simple validation example
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming you have a Contact class and a DatabaseHelper class
        Contact newContact = new Contact(name, email, phone, birthday);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        long id = databaseHelper.addContact(newContact);
        if (id > 0) {
            Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity and return to the previous one (if any)
        } else {
            Toast.makeText(this, "Failed to save contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set button text to the selected date
                        birthdayButton.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}