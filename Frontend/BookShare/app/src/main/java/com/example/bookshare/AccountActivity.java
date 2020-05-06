package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    String avatarAddress;
    ImageView avatarImageView;
    FrameLayout avatarLayout;
    private Bitmap bitmap;

    private static final String ROOT_URL = "https://sadbookshare.herokuapp.com/api/account/edit_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        avatarImageView = (ImageView) findViewById(R.id.account_avatar);
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
        avatarLayout = (FrameLayout) findViewById(R.id.account_top_frameLayout);
        avatarLayout.setOnClickListener(this);
        if(MainActivity.MyAccount.Avatar!=null){
            avatarImageView.setImageBitmap(MainActivity.MyAccount.Avatar);
        }
    }

    public void chooseImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooseIntent = Intent.createChooser(getIntent,"Select Image");
        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooseIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        try {
            super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
            if(requestCode==1){
                if(resultCode==RESULT_OK){
                    Uri picUri = imageReturnedIntent.getData();
                    avatarAddress = picUri.toString();//getPath(picUri);
                    if (avatarAddress != null) {
                        try {
                            avatarImageView.setImageURI(imageReturnedIntent.getData());
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                            MainActivity.MyAccount.Avatar = bitmap;
                            uploadBitmap(bitmap);
                        } catch (IOException e) {
                            avatarAddress = "";
                            avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.sign_up_avatar));
                        }
                    }

                }
                else {
                    avatarAddress = "";
                    avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.sign_up_avatar));
                }
            }
        }
        catch (Exception e){
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            Toast.makeText(this, stringWriter.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==avatarLayout){
            chooseImage();
        }
        else {
            DialogFragment dialogFragment = new PasswordChangeDialog();
            dialogFragment.show(getSupportFragmentManager(),"PasswordChangeDialog");
        }
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

    public void sendRequest(String old, String recent, String recentNew) {
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.PUT, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Toast.makeText(AccountActivity.this,"Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        /*try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(),"Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountActivity.this, String.valueOf(error.networkResponse.statusCode), Toast.LENGTH_LONG).show();
                        Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+sharedPreferences.getString("token",""));
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        queue.add(volleyMultipartRequest);
    }
}
