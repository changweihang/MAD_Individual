package com.example.individualassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference CardViews instead of Buttons
        CardView cardCompareNumbers = findViewById(R.id.cardCompareNumbers);
        CardView cardOrderNumbers = findViewById(R.id.cardOrderNumbers);
        CardView cardComposeNumbers = findViewById(R.id.cardComposeNumbers);

        // Set click listeners
        cardCompareNumbers.setOnClickListener(v ->
                startActivity(new Intent(this, CompareNumbersActivity.class))
        );

        cardOrderNumbers.setOnClickListener(v ->
                startActivity(new Intent(this, OrderNumbersActivity.class))
        );

        cardComposeNumbers.setOnClickListener(v ->
                startActivity(new Intent(this, ComposeNumbersActivity.class))
        );
    }
}
