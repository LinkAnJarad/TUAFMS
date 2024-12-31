package com.example.softwaremobiledev3itse01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerColleges, spinnerAccountType;
    private EditText editTextFirstName, editTextMiddleName;
    private EditText editTextSurname, editTextAccNumber, editTextEmailAddress;
    private EditText editTextPassword2, editTextPassword3;

    public String acct_number;

    private Button submitbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);


        spinnerColleges = (Spinner) findViewById(R.id.spinnerColleges);
        String[] items = {"", "CAHS", "CASE", "CEIS", "CMT", "IBAM", "SLCN", "Employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item, // Default layout for the spinner items
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColleges.setAdapter(adapter);

        spinnerAccountType = (Spinner) findViewById(R.id.spinnerAccountType);
        String[] items_acc = {"", "Employee", "Student"};
        ArrayAdapter<String> adapter_acc = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item, // Default layout for the spinner items
                items_acc
        );
        adapter_acc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccountType.setAdapter(adapter_acc);

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextMiddleName = (EditText) findViewById(R.id.editTextMiddleName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextAccNumber = (EditText) findViewById(R.id.editTextAccNumber);
        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        editTextPassword3 = (EditText) findViewById(R.id.editTextPassword3);
        submitbtn = (Button) findViewById(R.id.submitbtn);

        submitbtn.setOnClickListener(this);


    }

    private void signupUser() {

        handleSSLHandshake();


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ApiConfig.BASEURL+"signup.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ResponseLogs", response);
                        try {
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");

                            if (status.equals("success")) {
                                Intent intent = new Intent(RegisterPage.this, LogInPage.class);
                                intent.putExtra("acct_number", acct_number);
                                startActivity(intent);
                            } else {
                                //Toast.makeText(LogInPage.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(LogInPage.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
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

                String first_name = editTextFirstName.getText().toString();
                String middle_name = editTextMiddleName.getText().toString();
                String last_name = editTextSurname.getText().toString();
                acct_number = editTextAccNumber.getText().toString();
                String college = spinnerColleges.getSelectedItem().toString();
                String acc_type = spinnerAccountType.getSelectedItem().toString();
                String email = editTextEmailAddress.getText().toString();
                String password2 = editTextPassword2.getText().toString();
                String password3 = editTextPassword3.getText().toString();

                params.put("first_name", first_name);
                params.put("middle_name", middle_name);
                params.put("last_name", last_name);
                params.put("acc_number", acct_number);
                params.put("college", college);
                params.put("acc_type", acc_type);
                params.put("email", email);
                params.put("password2", password2);
                params.put("password3", password3);



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
        if (v.getId() == R.id.submitbtn) {
            String password2 = editTextPassword2.getText().toString();
            String password3 = editTextPassword3.getText().toString();
            if (password2.equals(password3)) {
                signupUser();
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT);
            }
        }
    }
}