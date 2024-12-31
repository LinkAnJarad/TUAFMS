package com.example.softwaremobiledev3itse01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.softwaremobiledev3itse01.ui.home.HomeFragment;

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

public class LogInPage extends AppCompatActivity implements View.OnClickListener {

    private TextView createtextbtn;
    private EditText blnkemail, blnkpassword;
    private String stremail, strpassword;
    private Button loginbtn1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        createtextbtn = (TextView) findViewById(R.id.createtextbtn);
        createtextbtn.setOnClickListener(this);
        blnkemail = (EditText) findViewById(R.id.blnkemail);
        blnkpassword = (EditText) findViewById(R.id.blnkpassword);
        loginbtn1 = (Button) findViewById(R.id.loginbtn1);
        loginbtn1.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createtextbtn) {
            Intent intent = new Intent(LogInPage.this, RegisterPage.class);
            startActivity(intent);
        } else if (v.getId() == R.id.loginbtn1) {
            loginUser();
        }

    }

    private void loginUser() {

        handleSSLHandshake();

        stremail = blnkemail.getText().toString().trim();
        strpassword = blnkpassword.getText().toString().trim();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ApiConfig.BASEURL+"login.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ResponseLogs", response);
                        try {
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");



                            if (status.equals("success")) {
                                Intent intent = new Intent(LogInPage.this, UserPage.class);
                                String acct_number = json_response.getString("user_id");
                                session.acct_number = acct_number;
                                startActivity(intent);
                            } else {
                                Toast.makeText(LogInPage.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LogInPage.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogInPage.this, error + "", Toast.LENGTH_SHORT).show();
                Log.d("VolleyError", error+"");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters
                params.put("email", stremail);
                params.put("password", strpassword);
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


}