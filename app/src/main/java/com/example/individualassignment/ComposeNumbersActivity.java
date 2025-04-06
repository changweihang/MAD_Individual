package com.example.individualassignment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComposeNumbersActivity extends AppCompatActivity {

    private TextView txtTargetNumber, txtStreak;
    private GridLayout gridOptions;
    private Button btnSubmit;
    private int targetNumber;
    private int[] options;
    private ArrayList<Button> optionButtons = new ArrayList<>();
    private ArrayList<Button> selectedButtons = new ArrayList<>();
    private MediaPlayer mediaPlayer;  // ✅ for playing sound
    private int streakCount = 0;  // Variable to track the current streak

    // SharedPreferences specific to ComposeNumbersActivity
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ComposeNumbersStreakPreferences";
    private static final String STREAK_KEY = "streak";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_numbers);

        txtTargetNumber = findViewById(R.id.txtTargetNumber);
        gridOptions = findViewById(R.id.gridOptions);
        btnSubmit = findViewById(R.id.btnCheckAnswer);
        txtStreak = findViewById(R.id.txtStreak);  // Initialize streak TextView

        // Initialize SharedPreferences to get saved streak count
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        streakCount = sharedPreferences.getInt(STREAK_KEY, 0);  // Retrieve the streak count

        // Display the streak count
        txtStreak.setText("Streak: " + streakCount);

        generateNumbers();

        btnSubmit.setOnClickListener(v -> checkAnswer());
    }

    private void generateNumbers() {
        gridOptions.removeAllViews();
        optionButtons.clear();
        selectedButtons.clear();

        Random random = new Random();
        targetNumber = random.nextInt(990) + 10;

        int num1 = random.nextInt(targetNumber - 1) + 1;
        int num2 = targetNumber - num1;

        Set<Integer> uniqueNums = new HashSet<>();
        uniqueNums.add(num1);
        uniqueNums.add(num2);

        while (uniqueNums.size() < 3) {
            uniqueNums.add(random.nextInt(targetNumber));
        }

        options = new int[3];
        int index = 0;
        for (Integer num : uniqueNums) {
            options[index++] = num;
        }

        ArrayList<Integer> optionList = new ArrayList<>();
        for (int opt : options) {
            optionList.add(opt);
        }
        Collections.shuffle(optionList);
        for (int i = 0; i < optionList.size(); i++) {
            options[i] = optionList.get(i);
        }

        txtTargetNumber.setText("Target Number: " + targetNumber);

        for (int i = 0; i < options.length; i++) {
            Button optionButton = new Button(this);
            optionButton.setText(String.valueOf(options[i]));
            optionButton.setTextSize(20);
            optionButton.setPadding(16, 16, 16, 16);
            optionButton.setBackgroundColor(Color.LTGRAY);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            optionButton.setLayoutParams(params);

            optionButton.setOnClickListener(v -> {
                Button btn = (Button) v;
                if (selectedButtons.contains(btn)) {
                    selectedButtons.remove(btn);
                    btn.setBackgroundColor(Color.LTGRAY);
                } else {
                    if (selectedButtons.size() < 2) {
                        selectedButtons.add(btn);
                        btn.setBackgroundColor(Color.MAGENTA);
                    } else {
                        showDialog("Please select exactly two numbers.", false);
                    }
                }
            });

            optionButtons.add(optionButton);
            gridOptions.addView(optionButton);
        }
    }

    private void checkAnswer() {
        if (selectedButtons.size() != 2) {
            showDialog("Please select exactly two numbers.", false);
            return;
        }

        int selected1 = Integer.parseInt(selectedButtons.get(0).getText().toString());
        int selected2 = Integer.parseInt(selectedButtons.get(1).getText().toString());

        if (selected1 + selected2 == targetNumber) {
            playSound(true);
            streakCount++;  // Increment streak for correct answer
            showDialog("Correct! Well done.", true);
        } else {
            playSound(false);
            streakCount = 0;  // Reset streak on wrong answer
            showDialog("Wrong! Please try again.", false);  // Display message for wrong answer
        }

        // Update streak count display
        txtStreak.setText("Streak: " + streakCount);

        // Save the streak value to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(STREAK_KEY, streakCount);
        editor.apply();  // Apply changes
    }

    // ✅ Plays correct or wrong sound
    private void playSound(boolean isCorrect) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, isCorrect ? R.raw.correct : R.raw.wrong);
        mediaPlayer.start();
    }

    private void showDialog(String message, boolean isCorrect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isCorrect) {
                            generateNumbers();
                        } else {
                            for (Button btn : selectedButtons) {
                                btn.setBackgroundColor(Color.LTGRAY);
                            }
                            selectedButtons.clear();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // ✅ Clean up mediaPlayer
    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
