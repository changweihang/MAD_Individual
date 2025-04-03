package com.example.individualassignment;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class CompareNumbersActivity extends AppCompatActivity {
    private TextView txtLeftNumber, txtRightNumber;
    private Button btnGreater, btnLesser;
    private int num1, num2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        txtLeftNumber = findViewById(R.id.txtLeftNumber);
        txtRightNumber = findViewById(R.id.txtRightNumber);
        btnGreater = findViewById(R.id.btnGreater);
        btnLesser = findViewById(R.id.btnLesser);

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

        // Display the generated numbers in left and right number textview
        txtLeftNumber.setText(String.valueOf(num1));
        txtRightNumber.setText(String.valueOf(num2));
    }

    //Checks the user's answer and displays a dialog with feedback
    private void checkAnswer(boolean userChoseGreater) {
        boolean correctAnswer = (num1 > num2);
        String message;

        if (userChoseGreater == correctAnswer) {
            message = "Correct! " + num1 + (correctAnswer ? " is greater than " : " is lesser than ") + num2 + "!";
        } else {
            message = "Wrong! " + num1 + (correctAnswer ? " is greater than " : " is lesser than ") + num2 + "!";
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
}
