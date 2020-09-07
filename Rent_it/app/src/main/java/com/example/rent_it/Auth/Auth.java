package com.example.rent_it.Auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rent_it.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.rent_it.Interfaces.AuthOnCompletePasswordChangeInterface;
import com.example.rent_it.Interfaces.OnSignOutCompleteInterface;
import com.example.rent_it.Interfaces.OnUserTransactionsRetrieved;
import com.example.rent_it.Models.Transaction;
import com.example.rent_it.Models.User;
import com.example.rent_it.Utilities.ApiEndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Auth {

    public User user;
    public Context context;

    public Auth(Context context) {
        this.context = context;
    }

    public void getUser(final AuthOnCompleteRetreiveInterface authOnCompleteRetreiveInterface) {
        String json_req = "json_req";
        // String url = getContext().getString(R.string.LOGIN_URL);
        String url = ApiEndPoints.base_local+"api/user/";
        final JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Volley", response.toString());
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                authOnCompleteRetreiveInterface.onServerUserRetrieveSuccess(response.getJSONObject("user")
                                        ,response.getJSONArray("vehicles"));
//                                mOnRegistrationListener.onSuccess("Registration success");
                            } else {
                                authOnCompleteRetreiveInterface.onServerUserRetrieveFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            authOnCompleteRetreiveInterface.onServerUserRetrieveFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        authOnCompleteRetreiveInterface.onServerUserRetrieveFailure();
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");

                SharedPreferences private_keys = context.getSharedPreferences("PRIVATE_KEYS",
                        Context.MODE_PRIVATE);
                String token = private_keys.getString("login_token", "default_value");
                Log.i("Token", token);
                headers.put("Authorization", token);
                return headers;
            }

        };
        queue.add(req);
    }

    public void changeNamePassword(final AuthOnCompletePasswordChangeInterface authOnCompletePasswordChangeInterface
            , String prev_password, String password, String name) {
        String json_req = "json_req";
        // String url = getContext().getString(R.string.LOGIN_URL);
        String url = "http://rentit6.herokuapp.com/api/user/update";
        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("password", password);
            requestBody.put("prev_password", prev_password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Volley", response.toString());
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONObject user = response.getJSONObject("user");
                                authOnCompletePasswordChangeInterface.onServerUserUpdateSuccess(user);
//                                mOnRegistrationListener.onSuccess("Registration success");
                            } else {

                                authOnCompletePasswordChangeInterface.onServerUserUpdateFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            authOnCompletePasswordChangeInterface.onServerUserUpdateFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        authOnCompletePasswordChangeInterface.onServerUserUpdateFailure();
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");

                SharedPreferences private_keys = context.getSharedPreferences("PRIVATE_KEYS",
                        Context.MODE_PRIVATE);
                String token = private_keys.getString("login_token", "default_value");
                headers.put("token", token);
                return headers;
            }

        };
        queue.add(req);
    }


    public void signOutWithDeviceId(final OnSignOutCompleteInterface onSignOutCompleteInterface) {
        String json_req = "json_req";
        // String url = getContext().getString(R.string.LOGIN_URL);
        String url = "http://rentit6.herokuapp.com/api/user/signOut";
        final JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Volley", response.toString());
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                SharedPreferences private_keys = context.getSharedPreferences("PRIVATE_KEYS",
                                        Context.MODE_PRIVATE);
                                private_keys.edit().remove("login_token").commit();
                                onSignOutCompleteInterface.onSignOutSuccess();
                            } else {

                                onSignOutCompleteInterface.onSignOutFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onSignOutCompleteInterface.onSignOutFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onSignOutCompleteInterface.onSignOutFailure();
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");

                SharedPreferences private_keys = context.getSharedPreferences("PRIVATE_KEYS",
                        Context.MODE_PRIVATE);
                String token = private_keys.getString("login_token", "default_value");
                headers.put("token", token);
                return headers;
            }

        };
        queue.add(req);
    }

    public void simpleSignOut() {
        SharedPreferences private_keys = context.getSharedPreferences("PRIVATE_KEYS",
                Context.MODE_PRIVATE);
        private_keys.edit().remove("login_token").commit();
    }

    public void getUserTransactions(final OnUserTransactionsRetrieved onUserTransactionsRetrieved) {
        final String json_req = "json_req";
        // String url = getContext().getString(R.string.LOGIN_URL);
        String url = ApiEndPoints.base_local+"api/user/transactions";
        final JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Volley", response.toString());
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                final List<Transaction> list_transaction = new ArrayList<>();
                                final JSONArray transaction_array = response.getJSONArray("transactions");
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < transaction_array.length(); i++) {
                                            try {
                                                JSONObject jsonObject = transaction_array.getJSONObject(i);
                                                list_transaction.add(new Transaction(jsonObject.getInt("duration")
                                                        , jsonObject.getInt("time")
                                                        , jsonObject.getString("_id")
                                                        , jsonObject.getBoolean("cancelled")
                                                        , jsonObject.getBoolean("completed")));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                list_transaction.add(new Transaction());
                                            }

                                        }
                                        onUserTransactionsRetrieved.onUserTransactionRetrieveSuccess(list_transaction);
                                    }
                                }.run();


//                                mOnRegistrationListener.onSuccess("Registration success");
                            } else {
                                onUserTransactionsRetrieved.onUserTransactionRetrieveFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onUserTransactionsRetrieved.onUserTransactionRetrieveFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onUserTransactionsRetrieved.onUserTransactionRetrieveFailure();
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");

                SharedPreferences private_keys = context.getSharedPreferences("PRIVATE_KEYS",
                        Context.MODE_PRIVATE);
                String token = private_keys.getString("login_token", "default_value");
                Log.i("Token", token);
                headers.put("Authorization", token);
                return headers;
            }

        };
        queue.add(req);
    }
}
