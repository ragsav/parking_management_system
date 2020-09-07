package com.example.rent_it.Registration;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegistrationInteractor implements RegistrationContract.Intractor {

    private static final String TAG = RegistrationInteractor.class.getSimpleName();
    private RegistrationContract.onRegistrationListener mOnRegistrationListener;

    public RegistrationInteractor(RegistrationContract.onRegistrationListener onRegistrationListener) {
        this.mOnRegistrationListener = onRegistrationListener;
    }

    @Override
    public void performFirebaseRegistration(final Activity activity,
                                            String name,
                                            String email,
                                            String password) {
        String json_req = "json_req";
        // String url = getContext().getString(R.string.LOGIN_URL);
        String url = "http://rentit6.herokuapp.com/api/register/";
        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("email", email);
            requestBody.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Volley", response.toString());
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                mOnRegistrationListener.onSuccess("Registration success");
                            } else {
                                mOnRegistrationListener.onFailure("Please try again");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mOnRegistrationListener.onFailure("Please try again");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mOnRegistrationListener.onFailure("Please try again");
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        queue.add(req);
    }


}
