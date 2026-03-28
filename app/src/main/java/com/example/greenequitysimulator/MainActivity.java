package com.example.greenequitysimulator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText stockInput;
    Button getDataBtn;
    TextView resultText;
    ProgressBar loading;

    String API_KEY = "d73mmr9r01qjjol3itrgd73mmr9r01qjjol3its0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge layout
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        stockInput = findViewById(R.id.stockInput);
        getDataBtn = findViewById(R.id.getDataBtn);
        resultText = findViewById(R.id.resultText);
        loading = findViewById(R.id.loading);

        getDataBtn.setOnClickListener(v -> {
            String symbol = stockInput.getText().toString().trim();
            if (symbol.isEmpty()) {
                resultText.setText("Please enter stock symbol");
                return;
            }

            loading.setVisibility(View.VISIBLE);
            getDataBtn.setEnabled(false);

            new Thread(() -> {
                try {
                    String urlString = "https://finnhub.io/api/v1/quote?symbol="
                            + symbol + "&token=" + API_KEY;

                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject json = new JSONObject(response.toString());
                    double current = json.getDouble("c");
                    double high = json.getDouble("h");
                    double low = json.getDouble("l");
                    double prevClose = json.getDouble("pc");

                    runOnUiThread(() -> {
                        resultText.setText(
                                "📊 Stock Data\n\n" +
                                        "Symbol: " + symbol +
                                        "\nCurrent Price: " + current +
                                        "\nHigh: " + high +
                                        "\nLow: " + low +
                                        "\nPrevious Close: " + prevClose
                        );

                        if (current > prevClose) {
                            resultText.setTextColor(Color.GREEN);
                        } else {
                            resultText.setTextColor(Color.RED);
                        }

                        loading.setVisibility(View.GONE);
                        getDataBtn.setEnabled(true);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> resultText.setText("Error fetching data"));
                }
            }).start();
        });
    }
}