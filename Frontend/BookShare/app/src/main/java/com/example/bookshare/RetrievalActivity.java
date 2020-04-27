package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RetrievalActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText emailEditText;
    MaterialButton submitButton;
    TextView textView;
    final String address = "https://sadbookshare.herokuapp.com/api/account/reset_password";
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieval);
        emailEditText = (TextInputEditText) findViewById(R.id.retrieval_email);
        submitButton = (MaterialButton) findViewById(R.id.retrieval_email_button);
        textView = (TextView) findViewById(R.id.retrieval_response_textView);
        submitButton.setOnClickListener(this);
        queue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        if(!emailEditText.getText().toString().matches("")){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email",emailEditText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, address, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(new JSONObject(new String(error.networkResponse.data)).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            queue.add(request);
        }
        else {
            emailEditText.setError(getString(R.string.retrieve_email_error));
            textView.setVisibility(View.GONE);
        }
    }
}
