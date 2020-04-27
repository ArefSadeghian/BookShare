package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout usernameLayout;
    TextInputEditText username;
    MaterialButton password;
    TextInputLayout firstNameLayout;
    TextInputEditText firstName;
    TextInputLayout lastNameLayout;
    TextInputEditText lastName;
    TextInputEditText email;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        username = (TextInputEditText) findViewById(R.id.account_username);
        password = (MaterialButton) findViewById(R.id.account_password);
        firstName = (TextInputEditText) findViewById(R.id.account_first_name);
        lastName = (TextInputEditText) findViewById(R.id.account_last_name);
        email = (TextInputEditText) findViewById(R.id.account_email);
        usernameLayout = (TextInputLayout)  findViewById(R.id.account_username_layout);
        firstNameLayout = (TextInputLayout)  findViewById(R.id.account_first_name_layout);
        lastNameLayout = (TextInputLayout)  findViewById(R.id.account_last_name_layout);
        username.setText(getIntent().getExtras().getString("username"));
        username.setFocusable(false);
        firstName.setText(getIntent().getExtras().getString("firstName"));
        firstName.setFocusable(false);
        lastName.setText(getIntent().getExtras().getString("lastName"));
        lastName.setFocusable(false);
        email.setText(getIntent().getExtras().getString("email"));
        email.setFocusable(false);
        sharedPreferences = getSharedPreferences("com.example.bookshare", MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);
        usernameLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new NameChangeDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("code",1);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(),"NameChangeDialog");
            }
        });
        firstNameLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new NameChangeDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("code",2);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(),"NameChangeDialog");
            }
        });
        lastNameLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new NameChangeDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("code",3);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(),"NameChangeDialog");
            }
        });
        password.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        DialogFragment dialogFragment = new PasswordChangeDialog();
        dialogFragment.show(getSupportFragmentManager(),"PasswordChangeDialog");
    }

    public void sendRequest(final String key, final String value) {
        String address = "https://sadbookshare.herokuapp.com/api/account/edit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, address, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(AccountActivity.this, "onResponse", Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putString(key,value).commit();
                    MainActivity.MyAccount.Username = sharedPreferences.getString("username","username");
                    MainActivity.MyAccount.FirstName = sharedPreferences.getString("firstName","firstName");
                    MainActivity.MyAccount.LastName = sharedPreferences.getString("lastName","lastName");
                    username.setText(MainActivity.MyAccount.Username);
                    firstName.setText(MainActivity.MyAccount.FirstName);
                    lastName.setText(MainActivity.MyAccount.LastName);
                    Toast.makeText(AccountActivity.this, "Data altered successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AccountActivity.this, "60", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountActivity.this, "onErrorResponse", Toast.LENGTH_SHORT).show();
                //Toast.makeText(AccountActivity.this, new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+sharedPreferences.getString("token",""));
                return headers;
            }
        };
        queue.add(request);
    }

    public void sendResponse(String old, String recent, String recentNew) {
        String address = "https://sadbookshare.herokuapp.com/api/account/change_password";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("old_password",old);
            jsonObject.put("new_password",recent);
            jsonObject.put("new_password_confirmation",recentNew);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, address, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(AccountActivity.this, "onResponse", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AccountActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AccountActivity.this, "60", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountActivity.this, "onErrorResponse", Toast.LENGTH_SHORT).show();
                //Toast.makeText(AccountActivity.this, new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+sharedPreferences.getString("token",""));
                return headers;
            }
        };
        queue.add(request);
    }
}
