package com.hlit.helplinetelecom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText email, etPassword;
    ProgressBar progressBar;
    ProgressDialog progressDialogLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // getSupportActionBar().hide();

        //String s2 = intent.getStringExtra("key2");


        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


        email = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etUserPassword);


        //calling the method userLogin() for login the user
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userLogin();


            }
        });

        //if user presses on textview not register calling RegisterActivity
        /*findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resgisterIntent = new Intent(login.this, register.class);
                resgisterIntent.putExtra("acts",acts);
                resgisterIntent.putExtra("id",id);
                resgisterIntent.putExtra("quantity",qty);
                startActivity(resgisterIntent);
                finish();
            }
        });*/
    }

    private void userLogin() {
        //first getting the values
        final String emailtext = email.getText().toString();
        final String password = etPassword.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(emailtext)) {
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailtext).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }
        progressDialogLogin = new ProgressDialog(login.this);
        progressDialogLogin.setMessage("Please wait...");
        // progressDialog.setTitle();
        progressDialogLogin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialogLogin.show();
        progressDialogLogin.setCancelable(false);


        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressBar.setVisibility(View.GONE);

                        try {
                            progressDialogLogin.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getString("mobile")

                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //starting the profile activity

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();



                                //startProgress();


                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailtext.trim());
                params.put("password", password.trim());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /*  private void startProgress() {

          if(activity.equals(productDetails.class.getSimpleName().toString())){

              Intent intent = new Intent(getApplicationContext(), productDetails.class);
              startActivity(intent);

          }else {

              startActivity(new Intent(getApplicationContext(), MainActivity.class));

          }


      }
  */
    @Override
    public void onBackPressed() {
        // String preActivityName ;

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {

           /* Intent j = new Intent(this, productDetails.class);
            startActivity(j);
            finish();
*/

        } else {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
            finish();

        }


    }
}
