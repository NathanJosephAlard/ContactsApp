package com.example.contactsapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditContactActivity extends AppCompatActivity {
    private TextInputEditText nameEditText, emailEditText, phoneEditText;
    private Button saveButton, cancelButton, birthdayButton;
    private Calendar birthdayCalendar;
    private DatabaseHelper databaseHelper;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        databaseHelper = new DatabaseHelper(this);
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        birthdayButton = findViewById(R.id.birthday_button);
        saveButton = findViewById(R.id.save_contact_button);

        birthdayCalendar = Calendar.getInstance();

        // Check if this is editing an existing contact
        if (getIntent().hasExtra("contact")) {
            contact = (Contact) getIntent().getSerializableExtra("contact");
            fillExistingContactData();
        } else {
            contact = new Contact();  // This is a new contact
        }

        birthdayButton.setOnClickListener(v -> showDatePickerDialog());
        saveButton.setOnClickListener(v -> saveContact());
    }

    private void fillExistingContactData() {
        nameEditText.setText(contact.getName());
        emailEditText.setText(contact.getEmail());
        phoneEditText.setText(contact.getPhone());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        birthdayButton.setText(dateFormat.format(contact.getBirthday()));
    }

    private void showDatePickerDialog() {
        int year = birthdayCalendar.get(Calendar.YEAR);
        int month = birthdayCalendar.get(Calendar.MONTH);
        int day = birthdayCalendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            birthdayCalendar.set(Calendar.YEAR, year1);
            birthdayCalendar.set(Calendar.MONTH, monthOfYear);
            birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateDisplay();
        }, year, month, day).show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        birthdayButton.setText(dateFormat.format(birthdayCalendar.getTime()));
    }

    private void saveContact() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String birthday = dateFormat.format(birthdayCalendar.getTime());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the contact data
        contact.setName(name);
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setBirthday(birthday);

        boolean success;
        if (getIntent().hasExtra("contact")) {
            success = databaseHelper.updateContact(contact) > 0;  // Update the existing contact
        } else {
            success = databaseHelper.addContact(contact) > 0;     // Save the new contact
        }

        if (success) {
            Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to save contact", Toast.LENGTH_SHORT).show();
        }
    }
}
