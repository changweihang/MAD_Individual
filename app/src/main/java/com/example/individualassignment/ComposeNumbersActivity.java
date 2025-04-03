package com.example.individualassignment;

import android.content.DialogInterface;
import android.graphics.Color;
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

    private TextView txtTargetNumber;
    private GridLayout gridOptions;
    private Button btnSubmit;
    private int targetNumber;
    private int[] options;  // 3 options
    private ArrayList<Button> optionButtons = new ArrayList<>();
    private ArrayList<Button> selectedButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_numbers);

        txtTargetNumber = findViewById(R.id.txtTargetNumber);
        gridOptions = findViewById(R.id.gridOptions);
        btnSubmit = findViewById(R.id.btnCheckAnswer);

        generateNumbers();

        btnSubmit.setOnClickListener(v -> checkAnswer());
    }

    private void generateNumbers() {
        // Clear previous dynamic buttons if any
        gridOptions.removeAllViews();
        optionButtons.clear();
        selectedButtons.clear();

        Random random = new Random();
        // Generate a target number between 10 and 999
        targetNumber = random.nextInt(990) + 10; // Range: 10 to 999

        // Generate two numbers that sum to targetNumber.
        int num1 = random.nextInt(targetNumber - 1) + 1;
        int num2 = targetNumber - num1;

        // Use a set to generate additional unique numbers (all must be smaller than targetNumber)
        Set<Integer> uniqueNums = new HashSet<>();
        uniqueNums.add(num1);
        uniqueNums.add(num2);

        // Generate one additional unique random number between 0 (inclusive) and targetNumber (exclusive)
        while (uniqueNums.size() < 3) {
            uniqueNums.add(random.nextInt(targetNumber));
        }

        // Convert the set to an array (3 unique options)
        options = new int[3];
        int index = 0;
        for (Integer num : uniqueNums) {
            options[index++] = num;
        }

        // Shuffle the options so the sum pair isn't always in the same position
        ArrayList<Integer> optionList = new ArrayList<>();
        for (int opt : options) {
            optionList.add(opt);
        }
        Collections.shuffle(optionList);
        for (int i = 0; i < optionList.size(); i++) {
            options[i] = optionList.get(i);
        }

        // Update the target number display
        txtTargetNumber.setText("Target Number: " + targetNumber);

        // Dynamically add option buttons to the GridLayout.
        // (Ensure gridOptions has enough columns; e.g., columnCount="3")
        for (int i = 0; i < options.length; i++) {
            Button optionButton = new Button(this);
            optionButton.setText(String.valueOf(options[i]));
            optionButton.setTextSize(20);
            optionButton.setPadding(16, 16, 16, 16);
            // Set a default background color
            optionButton.setBackgroundColor(Color.LTGRAY);
            // Set layout parameters – let GridLayout determine the size
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            optionButton.setLayoutParams(params);

            // When a button is clicked, toggle its selection
            optionButton.setOnClickListener(v -> {
                Button btn = (Button) v;
                if (selectedButtons.contains(btn)) {
                    // Deselect it
                    selectedButtons.remove(btn);
                    btn.setBackgroundColor(Color.LTGRAY);
                } else {
                    if (selectedButtons.size() < 2) {
                        // Select it
                        selectedButtons.add(btn);
                        btn.setBackgroundColor(Color.MAGENTA);
                    } else {
                        // Optionally, you can show a message if more than 2 selections are attempted\n"
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
        // Parse the selected numbers from the button texts
        int selected1 = Integer.parseInt(selectedButtons.get(0).getText().toString());
        int selected2 = Integer.parseInt(selectedButtons.get(1).getText().toString());

        if (selected1 + selected2 == targetNumber) {
            showDialog("Correct! Well done.", true);
        } else {
            showDialog("Wrong! Please try again.", false);
        }
    }

    private void showDialog(String message, boolean isCorrect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isCorrect) {
                            generateNumbers();
                        } else {
                            // If not correct, clear selections and reset button colors
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
}