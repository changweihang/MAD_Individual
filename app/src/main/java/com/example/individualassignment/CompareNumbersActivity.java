package com.example.individualassignment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class CompareNumbersActivity extends AppCompatActivity {
    private TextView txtLeftNumber, txtRightNumber, txtStreakCount;
    private Button btnGreater, btnLesser;
    private int num1, num2;

    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CompareNumbersStreakPreferences";
    private static final String STREAK_KEY = "streakCount";

    private int streakCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        txtLeftNumber = findViewById(R.id.txtLeftNumber);
        txtRightNumber = findViewById(R.id.txtRightNumber);
        txtStreakCount = findViewById(R.id.txtStreakCount); // Add this line to reference the streak display

        btnGreater = findViewById(R.id.btnGreater);
        btnLesser = findViewById(R.id.btnLesser);

        // Initialize MediaPlayer for correct and wrong sounds
        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);

        // Initialize SharedPreferences to store streak count
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        streakCount = sharedPreferences.getInt(STREAK_KEY, 0); // Get the saved streak count or 0 if not found

        // Display the current streak
        updateStreakDisplay();

        generateNumbers();

        btnGreater.setOnClickListener(v -> checkAnswer(true));
        btnLesser.setOnClickListener(v -> checkAnswer(false));
    }

    // Generates two random unique numbers between 0 and 999 and displays them on the screen.
    private void generateNumbers() {
        Random random = new Random();

        // Ensure the two generated numbers are not the same
        do {
            num1 = random.nextInt(1000);
            num2 = random.nextInt(1000);
        } while (num1 == num2);

        txtLeftNumber.setText(String.valueOf(num1));
        txtRightNumber.setText(String.valueOf(num2));
    }

    // Checks the user's answer and updates streak count accordingly
    private void checkAnswer(boolean userChoseGreater) {
        boolean correctAnswer = (num1 > num2);
        String message;

        if (userChoseGreater == correctAnswer) {
            message = "Correct! " + num1 + (correctAnswer ? " is greater than " : " is lesser than ") + num2 + "!";
            correctSound.start(); // Play correct sound

            // Increment streak count
            streakCount++;
            updateStreakDisplay(); // Update the UI to show the streak
            saveStreakCount(); // Save the streak count in SharedPreferences

        } else {
            message = "Wrong! " + num1 + (correctAnswer ? " is greater than " : " is lesser than ") + num2 + "!";
            wrongSound.start(); // Play wrong sound

            // Reset streak count to 0
            streakCount = 0;
            updateStreakDisplay(); // Update the UI to show the reset streak
            saveStreakCount(); // Save the reset streak count in SharedPreferences
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        generateNumbers();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Method to update the streak display on the screen
    private void updateStreakDisplay() {
        txtStreakCount.setText("Streak: " + streakCount); // Update the text to show the current streak
    }

    // Save the streak count in SharedPreferences
    private void saveStreakCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(STREAK_KEY, streakCount); // Save the streak count
        editor.apply();
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
