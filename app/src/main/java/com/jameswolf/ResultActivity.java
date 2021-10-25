package com.jameswolf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        GetCarData();
    }

    // General toast method for successes or failures
    private void toastMessage() {
        Toast.makeText(this, "There is no result from this VIN, please try another", Toast.LENGTH_SHORT).show();
    }

    // Method used in onCreate() just to keep the onCreate() cleaner
    private void GetCarData() {
        // Setting Local Variables for UI elements
        Intent in = this.getIntent();
        String searchedVin = in.getStringExtra(MainActivity.VIN);
        TextView mVinResult = this.findViewById(R.id.vin_result);
        TextView mYearResult = this.findViewById(R.id.year_result);
        TextView mMakeResult = this.findViewById(R.id.make_result);
        TextView mModelResult = this.findViewById(R.id.model_result);
        TextView mTrimResult = this.findViewById(R.id.trim_result);
        TextView mTypeResult = this.findViewById(R.id.type_result);
        TextView mBodyClassResult = this.findViewById(R.id.class_result);
        TextView mTransmissionStyleResult = this.findViewById(R.id.transmission_style_result);
        TextView mTransmissionSpeedsResult = this.findViewById(R.id.transmission_speed_result);
        TextView mDriveTypeResult = this.findViewById(R.id.drive_type_result);
        TextView mPlantCountryResult = this.findViewById(R.id.plant_country_result);
        TextView mErrorMessage = this.findViewById(R.id.error_message);

        // Making a call to the API
        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://vpic.nhtsa.dot.gov/api/vehicles/decodevin/%s?format=json", searchedVin);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            // If the response from the API fails
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ResultActivity.this.runOnUiThread(e::printStackTrace);
                toastMessage();
            }

            // If the response from the API is successful
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Creates a json string from the response
                    String jsonString = Objects.requireNonNull(response.body()).string();
                    try {
                        // Turning the json string from the response into a json object and getting the specific parts needed
                        JSONObject obj = new JSONObject(jsonString);
                        String make = obj.getJSONArray("Results").getJSONObject(6).getString("Value");
                        String model = obj.getJSONArray("Results").getJSONObject(8).getString("Value");
                        String year = obj.getJSONArray("Results").getJSONObject(9).getString("Value");
                        String trim = obj.getJSONArray("Results").getJSONObject(12).getString("Value");
                        String type = obj.getJSONArray("Results").getJSONObject(13).getString("Value");
                        String bodyClass = obj.getJSONArray("Results").getJSONObject(21).getString("Value");
                        String transmissionStyle = obj.getJSONArray("Results").getJSONObject(47).getString("Value");
                        String transmissionSpeeds = obj.getJSONArray("Results").getJSONObject(48).getString("Value");
                        String driveType = obj.getJSONArray("Results").getJSONObject(49).getString("Value");
                        String plantCountry = obj.getJSONArray("Results").getJSONObject(14).getString("Value");


                        // Setting the UI elements based on if API returned sufficient data
                        ResultActivity.this.runOnUiThread(() -> {
                            if (year.equals("null")) {
                                mYearResult.setText("");
                            } else {
                                mYearResult.setText(year);
                            }
                            if (make.equals("null")) {
                                mMakeResult.setText("");
                                mVinResult.setText("");
                                mErrorMessage.setText(R.string.error_message);
                            } else {
                                mMakeResult.setText(make);
                            }
                            if (model.equals("null")) {
                                mModelResult.setText("");
                            } else {
                                mModelResult.setText(model);
                            }
                            if (trim.equals("null")) {
                                mTrimResult.setText("");
                            } else {
                                mTrimResult.setText(trim);
                            }
                            if (type.equals("null")) {
                                mTypeResult.setText("");
                            } else {
                                mTypeResult.setText(type);
                            }
                            if (bodyClass.equals("null")) {
                                mBodyClassResult.setText("");
                            } else {
                                mBodyClassResult.setText(bodyClass);
                            }
                            if (transmissionStyle.equals("null")) {
                                mTransmissionStyleResult.setText("");
                            } else {
                                mTransmissionStyleResult.setText(transmissionStyle);
                            }
                            if (transmissionSpeeds.equals("null")) {
                                mTransmissionSpeedsResult.setText("");
                            } else {
                                mTransmissionSpeedsResult.setText(transmissionSpeeds);
                            }
                            if (driveType.equals("null")) {
                                mDriveTypeResult.setText("");
                            } else {
                                mDriveTypeResult.setText(driveType);
                            }
                            if (plantCountry.equals("null")) {
                                mPlantCountryResult.setText("");
                            } else {
                                mPlantCountryResult.setText(plantCountry);
                            }
                            mVinResult.setText(searchedVin);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}