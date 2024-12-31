package com.example.softwaremobiledev3itse01;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CreateFdbkPage extends AppCompatActivity implements View.OnClickListener {

    private Button logoutbtn;

    private TextView textHome;

    private Button submitbtn;

    private Spinner spinnerCollege, spinnerCategory;
    private EditText editTextDesc;

    private TextView textStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fdbk_page);

        logoutbtn = (Button) findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(this);

        textHome = (TextView) findViewById(R.id.textHome);
        textHome.setOnClickListener(this);

        textStatus = (TextView) findViewById(R.id.textStatus);
        textStatus.setOnClickListener(this);

        editTextDesc = (EditText) findViewById(R.id.editTextDesc);

        spinnerCollege = (Spinner) findViewById(R.id.spinnerCollege);
        String[] items = {"", "CAHS", "CASE", "CEIS", "CMT", "IBAM", "SLCN", "Employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item, // Default layout for the spinner items
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCollege.setAdapter(adapter);

        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        String[] items_cat = {"", "Professor", "Student Org", "Facility", "Others"};
        ArrayAdapter<String> adapter_cat = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item, // Default layout for the spinner items
                items_cat
        );
        adapter_cat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter_cat);

        submitbtn = (Button) findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(this);
    }

    private void showMessageBox(String title, String message) {
        // Create the AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title); // Set the title
        builder.setMessage(message); // Set the message

        // Add an OK button
        builder.setPositiveButton("Okay, got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to execute when OK is clicked
                dialog.dismiss(); // Close the dialog
            }
        });

        // Display the dialog
        builder.show();
    }

    private void createfdbk() {

        handleSSLHandshake();


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ApiConfig.BASEURL+"create.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ResponseCreate", response);
                        try {
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");

                            if (status.equals("success")) {
                                //Toast.makeText(CreateFdbkPage.this, "Feedback submitted", Toast.LENGTH_LONG);
                                showMessageBox("Success", "Feedback Submitted");

                            } else {
                                Toast.makeText(CreateFdbkPage.this, "Create failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateFdbkPage.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LogInPage.this, error + "", Toast.LENGTH_SHORT).show();
                Log.d("VolleyError", error+"");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters
//                params.put("email", stremail);
//                params.put("password", strpassword);

                String desc = editTextDesc.getText().toString();
                String college = spinnerCollege.getSelectedItem().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String acct_number = session.acct_number;
                //extras.getString("")
                params.put("user_id", acct_number);
                params.put("desc", desc);
                params.put("college", college);
                params.put("category", category);

                return params;
            }
        };
        queue.add(stringRequest);

    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logoutbtn) {
            Intent intent = new Intent(CreateFdbkPage.this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textHome) {
            Intent intent = new Intent(CreateFdbkPage.this, UserPage.class);
            startActivity(intent);
        } else if(v.getId() == R.id.textStatus){
            Intent intent = new Intent(CreateFdbkPage.this, StatusTablePage.class);
            startActivity(intent);
        } else if (v.getId() == R.id.submitbtn) {
            createfdbk();
        }
    }
}