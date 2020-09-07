package com.example.rent_it.Login;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginInteractor implements LoginContract.Intractor {


    private LoginContract.onLoginListener mOnLoginListener;

    public LoginInteractor(LoginContract.onLoginListener onLoginListener) {
        this.mOnLoginListener = onLoginListener;
    }

    @Override
    public void performLogin(Activity activity, String email, String password) {
        String json_req = "json_req";
        // String url = getContext().getString(R.string.LOGIN_URL);
        String url = "http://rentit6.herokuapp.com/api/login/";
        final JSONObject requestBody = new JSONObject();
        try {
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
                            String token = response.getString("token");
                            JSONObject user = response.getJSONObject("user");
                            mOnLoginListener.onSuccess(token, user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mOnLoginListener.onFailure("Please try again");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mOnLoginListener.onFailure("Please try again");
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
