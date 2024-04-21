package com.example.mlproject;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText CHK_ACCT, Duration, History, Purposeofcredit, CreditAmount, BalanceinSavingsAC, Employment, Install_rate, Maritalstatus, Coapplicant, PresentResident, RealEstate, Age, Otherinstallment, Residence, Num_Credits, Job, Nodependents, Phone, Foreign;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditText fields
        CHK_ACCT = findViewById(R.id.CHK_ACCT);
        Duration = findViewById(R.id.Duration);
        History = findViewById(R.id.History);
        Purposeofcredit = findViewById(R.id.Purposeofcredit);
        CreditAmount = findViewById(R.id.CreditAmount);
        BalanceinSavingsAC = findViewById(R.id.BalanceinSavingsAC);
        Employment = findViewById(R.id.Employment);
        Install_rate = findViewById(R.id.Install_rate);
        Maritalstatus = findViewById(R.id.Maritalstatus);
        Coapplicant = findViewById(R.id.Coapplicant);
        PresentResident = findViewById(R.id.PresentResident);
        RealEstate = findViewById(R.id.RealEstate);
        Age = findViewById(R.id.Age);
        Otherinstallment = findViewById(R.id.Otherinstallment);
        Residence = findViewById(R.id.Residence);
        Num_Credits = findViewById(R.id.Num_Credits);
        Job = findViewById(R.id.Job);
        Nodependents = findViewById(R.id.Nodependents);
        Phone = findViewById(R.id.Phone);
        Foreign = findViewById(R.id.Foreign);
        // Initialize submit button
        submit = findViewById(R.id.submit);

        // Set click listener for submit button
        submit.setOnClickListener(v -> {
            new ApiRequestTask().execute();
        });
    }

    private class ApiRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // Get values from EditText fields
            String chkAcct = CHK_ACCT.getText().toString();
            String duration = Duration.getText().toString();
            String history = History.getText().toString();
            String purposeOfCredit = Purposeofcredit.getText().toString();
            String creditAmount = CreditAmount.getText().toString();
            String balanceInSavingsAC = BalanceinSavingsAC.getText().toString();
            String employment = Employment.getText().toString();
            String installRate = Install_rate.getText().toString();
            String maritalStatus = Maritalstatus.getText().toString();
            String coapplicant = Coapplicant.getText().toString();
            String presentResident = PresentResident.getText().toString();
            String realEstate = RealEstate.getText().toString();
            String age = Age.getText().toString();
            String otherInstallment = Otherinstallment.getText().toString();
            String residence = Residence.getText().toString();
            String numCredits = Num_Credits.getText().toString();
            String job = Job.getText().toString();
            String noDependents = Nodependents.getText().toString();
            String phone = Phone.getText().toString();
            String foreign = Foreign.getText().toString();

            // Create JSON string
            String jsonData = "{\"S_No\":\"" + 1+"\",\"CHK_ACCT\": \"" + chkAcct + "\", \"Duration\": \"" + duration + "\", \"History\": \"" + history + "\", \"Purposeofcredit\": \"" + purposeOfCredit + "\", \"CreditAmount\": \"" + creditAmount + "\", \"BalanceinSavingsAC\": \"" + balanceInSavingsAC + "\", \"Employment\": \"" + employment + "\", \"Install_rate\": \"" + installRate + "\", \"Maritalstatus\": \"" + maritalStatus +"\"S_No_\":\"" + 1+ "\", \"Coapplicant\": \"" + coapplicant + "\", \"PresentResident\": \"" + presentResident + "\", \"RealEstate\": \"" + realEstate + "\", \"Age\": \"" + age + "\", \"Otherinstallment\": \"" + otherInstallment + "\", \"Residence\": \"" + residence + "\", \"Num_Credits\": \"" + numCredits + "\", \"Job\": \"" + job + "\", \"Nodependents\": \"" + noDependents + "\", \"Phone\": \"" + phone + "\", \"Foreign\": \"" + foreign + "\", \"Output\": \"" + "" + "\"}";

            // Create OkHttpClient instance
            OkHttpClient client = new OkHttpClient();

            // Create RequestBody
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonData);

            // Create Request
            Request request = new Request.Builder()
                    .url("https://tusharharyana.pythonanywhere.com/predict") // Your API URL
                    .post(body)
                    .build();

            // Execute request and handle response
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Error: " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(result);
                int output = jsonResponse.getInt("Output");

                // Determine the message based on the value of the "output" field
                String message;
                if (output == 1) {
                    message = "Customer Credit rating: GOOD";
                } else {
                    message = "Customer Credit rating: BAD";
                }

                // Create an AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // Set the title and message of the dialog
                builder.setTitle("API Response")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Dismiss the dialog when the OK button is clicked
                                dialogInterface.dismiss();
                            }
                        });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } catch (JSONException e) {
                e.printStackTrace();
                // Show an error message if there's an issue parsing the JSON response
                Toast.makeText(MainActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
