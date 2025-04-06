package com.example.individualassignment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
    private TextView txtGeneratedNumbers, txtStreak;
    private EditText edtAscending1, edtAscending2, edtAscending3;
    private EditText edtDescending1, edtDescending2, edtDescending3;
    private Button btnSubmit;
    private int[] numbers;
    private int streak = 0;  // Track streak
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "OrderNumbersStreakPreferences";
    private static final String STREAK_KEY = "streak";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);

        // Initialize Views
        txtGeneratedNumbers = findViewById(R.id.txtGeneratedNumbers);
        txtStreak = findViewById(R.id.txtStreak);  // Streak TextView
        edtAscending1 = findViewById(R.id.edtAscending1);
        edtAscending2 = findViewById(R.id.edtAscending2);
        edtAscending3 = findViewById(R.id.edtAscending3);
        edtDescending1 = findViewById(R.id.edtDescending1);
        edtDescending2 = findViewById(R.id.edtDescending2);
        edtDescending3 = findViewById(R.id.edtDescending3);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        streak = sharedPreferences.getInt(STREAK_KEY, 0);  // Retrieve the streak value

        // Display the saved streak
        txtStreak.setText("Streak: " + streak);

        generateNumbers();

        // Initialize the MediaPlayer for correct and wrong sounds
        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);

        btnSubmit.setOnClickListener(v -> checkAnswer());
    }

    private void generateNumbers() {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();

        while (uniqueNumbers.size() < 3) {
            uniqueNumbers.add(random.nextInt(1000)); // Numbers between 0 and 999
        }

        numbers = new int[3];
        int index = 0;
        for (Integer num : uniqueNumbers) {
            numbers[index++] = num;
        }

        txtGeneratedNumbers.setText("Question: " + Arrays.toString(numbers));
    }

    private void checkAnswer() {
        if (edtAscending1.getText().toString().trim().isEmpty() || edtAscending2.getText().toString().trim().isEmpty() ||
                edtAscending3.getText().toString().trim().isEmpty() || edtDescending1.getText().toString().trim().isEmpty() ||
                edtDescending2.getText().toString().trim().isEmpty() || edtDescending3.getText().toString().trim().isEmpty()) {
            showDialog("Empty input detected! Please try again.", false);
            return;
        }

        try {
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

            int[] correctAscending = numbers.clone();
            Arrays.sort(correctAscending);
            int[] correctDescending = correctAscending.clone();
            for (int i = 0; i < correctDescending.length / 2; i++) {
                int temp = correctDescending[i];
                correctDescending[i] = correctDescending[correctDescending.length - 1 - i];
                correctDescending[correctDescending.length - 1 - i] = temp;
            }

            if (Arrays.equals(userAscending, correctAscending) && Arrays.equals(userDescending, correctDescending)) {
                correctSound.start();
                streak++;  // Increment streak on correct answer
                showDialog("Correct! Well done.", true);
            } else {
                wrongSound.start();
                streak = 0;  // Reset streak on wrong answer
                showDialog("Wrong! Try again.", false);
            }

            // Update the streak display
            txtStreak.setText("Streak: " + streak);

            // Save the streak value to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(STREAK_KEY, streak);
            editor.apply();

        } catch (Exception e) {
            showDialog("Invalid input! Please enter numbers only.", false);
        }
    }

    private void showDialog(String message, boolean isCorrect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                    if (isCorrect) {
                        generateNumbers();
                        edtAscending1.setText("");
                        edtAscending2.setText("");
                        edtAscending3.setText("");
                        edtDescending1.setText("");
                        edtDescending2.setText("");
                        edtDescending3.setText("");
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (correctSound != null) {
            correctSound.release();
        }
        if (wrongSound != null) {
            wrongSound.release();
        }
    }
}
