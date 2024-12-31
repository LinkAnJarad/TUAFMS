package com.example.softwaremobiledev3itse01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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

public class StatusTablePage extends AppCompatActivity implements View.OnClickListener {

    private Button logoutbtn;

    private TextView textHome;

    private TextView textCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_table_page);

        logoutbtn = (Button) findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(this);

        textHome = (TextView) findViewById(R.id.textHome);
        textHome.setOnClickListener(this);

        textCreate = (TextView) findViewById(R.id.textCreate);
        textCreate.setOnClickListener(this);

        // Get the TableLayout from the XML
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        getStatus();

        // Sample data
//        String[][] data = {
//                {"10187719", "Urgent Facility Complain", "In Progress"},
//                {"10877923", "Professor Complain", "In Progress"},
//                {"1097619", "Staff Complain", "3"}
//        };
//
//        // Loop through the data and create rows dynamically
//        for (String[] row : data) {
//            TableRow tableRow = new TableRow(this);
//
//            for (String cell : row) {
//                TextView textView = new TextView(this);
//                textView.setText(cell);
//                textView.setPadding(16, 16, 16, 16);
//                textView.setTextColor(Color.BLACK); // Optional: Change text color
//                textView.setBackgroundColor(Color.WHITE); // Optional: Set background
//                tableRow.addView(textView);
//            }
//
//            tableLayout.addView(tableRow);
//        }
    }

    private void getStatus() {

        handleSSLHandshake();


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ApiConfig.BASEURL+"status.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ResponseLogs", response);
                        try {
                            // Parse the JSON response as an array
                            JSONArray jsonArray = new JSONArray(response);

                            // Get reference to your TableLayout
                            TableLayout tableLayout = findViewById(R.id.tableLayout);

                            // Loop through the JSON array and create table rows
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Extract data from JSON object
                                String refNo = jsonObject.getString("fdbk_refno");
                                String type = jsonObject.getString("fdbk_type");
                                String status = jsonObject.getString("fdbk_status");
                                String priority = jsonObject.getString("fdbk_priority");
                                if (priority.equals("null")) {
                                    priority = "Not set";
                                }

                                // Create a new TableRow
                                TableRow tableRow = new TableRow(StatusTablePage.this);

                                // Create TextViews for each column
                                String[] rowData = {refNo, type, status, priority};
                                for (String cell : rowData) {
                                    TextView textView = new TextView(StatusTablePage.this);
                                    textView.setText(cell);
                                    textView.setPadding(16, 16, 16, 16);
                                    textView.setTextColor(Color.BLACK); // Set text color
                                    textView.setBackgroundColor(Color.WHITE); // Set background
                                    TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
                                    textView.setLayoutParams(params);
                                    tableRow.addView(textView);
                                }

                                // Add the TableRow to the TableLayout
                                tableLayout.addView(tableRow);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StatusTablePage.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StatusTablePage.this, error + "", Toast.LENGTH_SHORT).show();
                Log.d("VolleyError", error+"");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters
                params.put("user_id", session.acct_number);
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
            Intent intent = new Intent(StatusTablePage.this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textHome) {
            Intent intent = new Intent(StatusTablePage.this, UserPage.class);
            startActivity(intent);
        } else if(v.getId() == R.id.textCreate){
            Intent intent = new Intent(StatusTablePage.this, CreateFdbkPage.class);
            startActivity(intent);
        }
    }
}