package com.example.contactsapp;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ContactFormActivity extends AppCompatActivity {
    private Button birthdayButton, saveContactButton;
    private TextInputEditText nameEditText, emailEditText, phoneEditText;
    private Calendar selectedDate; // This Calendar object will keep track of the selected date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        birthdayButton = findViewById(R.id.birthday_button);
        saveContactButton = findViewById(R.id.save_contact_button);

        selectedDate = Calendar.getInstance(); // Initialize the calendar to the current date

        updateDateDisplay(selectedDate.getTime()); // Update display with the current date

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String birthday = dateFormat.format(selectedDate.getTime());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Directly instantiate DatabaseHelper instead of using a singleton instance
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            long id = databaseHelper.addContact(new Contact(name, email, phone, birthday));
            if (id > 0) {
                Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
                finish(); // Close this activity and return to the previous one
            } else {
                Toast.makeText(this, "Failed to save contact", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error saving contact: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            databaseHelper.close();  // Ensure resources are freed correctly
        }
    }



    private void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year1);
                    selectedDate.set(Calendar.MONTH, month1);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateDisplay(selectedDate.getTime());
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateDateDisplay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        birthdayButton.setText(dateFormat.format(date));
    }
}
