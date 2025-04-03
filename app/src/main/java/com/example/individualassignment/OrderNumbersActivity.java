package com.example.individualassignment;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OrderNumbersActivity extends AppCompatActivity {
    private TextView txtGeneratedNumbers;
    private EditText edtAscending1, edtAscending2, edtAscending3;
    private EditText edtDescending1, edtDescending2, edtDescending3;
    private Button btnSubmit;
    private int[] numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);

        txtGeneratedNumbers = findViewById(R.id.txtGeneratedNumbers);
        edtAscending1 = findViewById(R.id.edtAscending1);
        edtAscending2 = findViewById(R.id.edtAscending2);
        edtAscending3 = findViewById(R.id.edtAscending3);
        edtDescending1 = findViewById(R.id.edtDescending1);
        edtDescending2 = findViewById(R.id.edtDescending2);
        edtDescending3 = findViewById(R.id.edtDescending3);
        btnSubmit = findViewById(R.id.btnSubmit);

        generateNumbers();

        btnSubmit.setOnClickListener(v -> checkAnswer());
    }

    private void generateNumbers() {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>(); // Using a Set to ensure uniqueness

        // Keep generating random numbers until we have 3 unique numbers
        while (uniqueNumbers.size() < 3) {
            uniqueNumbers.add(random.nextInt(1000)); // Numbers between 0 and 999
        }

        // Convert the Set back to an array
        numbers = new int[3];
        int index = 0;
        for (Integer num : uniqueNumbers) {
            numbers[index++] = num;
        }

        txtGeneratedNumbers.setText("Question: " + Arrays.toString(numbers));
    }

    private void checkAnswer() {
        // Check if any EditText is empty
        if (edtAscending1.getText().toString().trim().isEmpty() || edtAscending2.getText().toString().trim().isEmpty() ||
                edtAscending3.getText().toString().trim().isEmpty() || edtDescending1.getText().toString().trim().isEmpty() ||
                edtDescending2.getText().toString().trim().isEmpty() || edtDescending3.getText().toString().trim().isEmpty()) {
            showDialog("Empty input detected! Please try again.", false);
            return;
        }

        try {
            // Get user's ascending and descending order input after trimming spaces
            int[] userAscending = {
                    Integer.parseInt(edtAscending1.getText().toString().trim()),
                    Integer.parseInt(edtAscending2.getText().toString().trim()),
                    Integer.parseInt(edtAscending3.getText().toString().trim())
            };

            int[] userDescending = {
                    Integer.parseInt(edtDescending1.getText().toString().trim()),
                    Integer.parseInt(edtDescending2.getText().toString().trim()),
                    Integer.parseInt(edtDescending3.getText().toString().trim())
            };

            // Sort numbers in ascending and descending order
            int[] correctAscending = numbers.clone();
            Arrays.sort(correctAscending);
            int[] correctDescending = correctAscending.clone();
            for (int i = 0; i < correctDescending.length / 2; i++) {
                int temp = correctDescending[i];
                correctDescending[i] = correctDescending[correctDescending.length - 1 - i];
                correctDescending[correctDescending.length - 1 - i] = temp;
            }

            // Check if user's input matches the correct answers
            if (Arrays.equals(userAscending, correctAscending) && Arrays.equals(userDescending, correctDescending)) {
                showDialog("Correct! Well done.", true);
            } else {
                showDialog("Wrong! Try again.", false);
            }
        } catch (Exception e) {
            showDialog("Invalid input! Please enter numbers only.", false);
        }
    }

    private void showDialog(String message, boolean isCorrect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isCorrect) {
                            generateNumbers();
                            edtAscending1.setText("");
                            edtAscending2.setText("");
                            edtAscending3.setText("");
                            edtDescending1.setText("");
                            edtDescending2.setText("");
                            edtDescending3.setText("");
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
