package com.jameswolf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String VIN = "Vin Number";
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchView = findViewById(R.id.vin_search_bar);
    }

    public void vin_search(View view) {
        if (mSearchView.getQuery().toString().length() != 17) {
            Toast.makeText(view.getContext(), "A VIN must be 17 characters long, please recheck your input", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, ResultActivity.class);

            String message = mSearchView.getQuery().toString();
            intent.putExtra(VIN, message);
            view.getContext().startActivity(intent);
        }
    }
}