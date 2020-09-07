package com.example.rent_it.NetworkCalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rent_it.Activities.LoginActivity;
import com.example.rent_it.Interfaces.OnAreaListFetchedInterface;
import com.example.rent_it.Interfaces.OnCityFetchInterface;
import com.example.rent_it.Interfaces.OnLotListFetchedInterface;
import com.example.rent_it.Interfaces.OnSearchResultFetchInterface;
import com.example.rent_it.Models.Area;
import com.example.rent_it.Models.City;
import com.example.rent_it.Models.Lot;
import com.example.rent_it.Utilities.ApiEndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardNetworkUtility {


    Context context;
    OnAreaListFetchedInterface onAreaListFetchedInterface;
    OnLotListFetchedInterface onLotListFetchedInterface;
    OnSearchResultFetchInterface onSearchResultFetchInterface;
    OnCityFetchInterface onCityFetchInterface;

    public DashBoardNetworkUtility(Context context
            , OnAreaListFetchedInterface onAreaListFetchedInterface
            , OnLotListFetchedInterface onLotListFetchedInterface
            , OnSearchResultFetchInterface onSearchResultFetchInterface
            ,OnCityFetchInterface onCityFetchInterface) {
        this.context = context;
        this.onAreaListFetchedInterface = onAreaListFetchedInterface;
        this.onLotListFetchedInterface = onLotListFetchedInterface;
        this.onSearchResultFetchInterface = onSearchResultFetchInterface;
        this.onCityFetchInterface = onCityFetchInterface;
    }

    public void getCityList() {
        String json_req = "json_req";
        String url = ApiEndPoints.base_local+"api/public/city/";
        final JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final JSONArray city_list_json = response.getJSONArray("cities");
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<City> city_list = new ArrayList<>();
                                        for (int i = 0; i < city_list_json.length(); i++) {
                                            JSONObject city_json = city_list_json.getJSONObject(i);
                                            city_list.add(new City(city_json.getString("_id"),city_json.getString("name")));
                                        }
                                        onCityFetchInterface.onCityFetchSuccess(city_list);
                                    } catch (Exception e) {
                                        Log.e("DASH", e.toString());
                                        onCityFetchInterface.onCityFetchFailure();
                                    }

                                }
                            }.run();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            onCityFetchInterface.onCityFetchFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onCityFetchInterface.onCityFetchFailure();
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

    public void getAreaList(String city_id) {
        String json_req = "json_req";
        String url = ApiEndPoints.base_local+"api/public/city/" + city_id;
        final JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final JSONArray area_list_json = response.getJSONArray("areas");
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<Area> area_list = new ArrayList<>();
                                        for (int i = 0; i < area_list_json.length(); i++) {
                                            JSONObject area_json = area_list_json.getJSONObject(i);
//                                            Log.i(area_json.getString("name"),area_json.toString());
                                            area_list.add(new Area(area_json.getString("_id")
                                                    , area_json.getString("name")
                                                    , area_json.getJSONArray("lot_list")==null?new JSONArray():area_json.getJSONArray("lot_list")
                                                    , area_json.getString("city_id")
                                                    , area_json.getString("image_binary")
                                                    , area_json.getString("image_url")
                                                    , area_json.getInt("distance")));
                                        }
                                        onAreaListFetchedInterface.onAreaListFetchSuccess(area_list);
                                    } catch (Exception e) {
                                        Log.e("DASH", e.toString());
                                        onAreaListFetchedInterface.onAreaListFetchFailure();
                                    }

                                }
                            }.run();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            onAreaListFetchedInterface.onAreaListFetchFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onAreaListFetchedInterface.onAreaListFetchFailure();
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

    public void getLotList(final String city_id, final String area_id) {
        Log.i("GET_LOT_LIST",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> getting");
        String json_req = "json_req";
        String url = ApiEndPoints.base_local+"api/public/city/" + city_id + "/" + area_id;
        final JSONObject requestBody = new JSONObject();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE",response.toString());
                        try {
                            final JSONArray lot_list_json = response.getJSONArray("lots");
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<Lot> lot_list = new ArrayList<>();
                                        for (int i = 0; i < lot_list_json.length(); i++) {
                                            JSONObject lot_json = lot_list_json.getJSONObject(i);
                                            lot_list.add(new Lot(lot_json.getString("name")
                                                    , lot_json.getString("city_id")
                                                    , lot_json.getString("area_id")
                                                    , lot_json.getString("_id")
                                                    , lot_json.getInt("open_time")
                                                    , lot_json.getInt("close_time")
                                                    , lot_json.getString("cover_image")
                                                    , lot_json.getString("city_name")
                                                    , lot_json.getString("area_name")
                                            ,lot_json.getBoolean("status")));
                                        }
                                        onLotListFetchedInterface.onLotListFetchSuccess(lot_list);
                                    } catch (Exception e) {
                                        Log.e("DASH", e.toString());
                                        onLotListFetchedInterface.onLotListFetchFailure();
                                    }

                                }
                            }.run();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            onLotListFetchedInterface.onLotListFetchFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLotListFetchedInterface.onLotListFetchFailure();
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

    public void getSearchLotList(final String city_id, final String key) {
        Log.i("MAIN",">>>>>>>>>>>inside lot list ");
        String json_req = "json_req";
        // String url = getContext().getString(R.string.LOGIN_URL);
        String url = ApiEndPoints.base_local+"api/public/search/" + city_id + "/" + key;
        final JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final JSONArray lot_list_json = response.getJSONArray("lots");
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<Lot> lot_list = new ArrayList<>();
                                        for (int i = 0; i < lot_list_json.length(); i++) {
                                            JSONObject lot_json = lot_list_json.getJSONObject(i);
                                            lot_list.add(new Lot(lot_json.getString("name")
                                                    , lot_json.getString("city_id")
                                                    , lot_json.getString("area_id")
                                                    , lot_json.getString("_id")
                                                    , lot_json.getInt("open_time")
                                                    , lot_json.getInt("close_time")
                                                    , lot_json.getString("cover_image")
                                                    , lot_json.getString("city_name")
                                                    , lot_json.getString("area_name")
                                                    ,lot_json.getBoolean("status")));
                                        }
                                        onSearchResultFetchInterface.onSearchResultFetchSuccess(lot_list);
                                    } catch (Exception e) {
                                        Log.e("DASH", e.toString());
                                        onSearchResultFetchInterface.onSearchResultFetchFailure();
                                    }

                                }
                            }.run();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            onSearchResultFetchInterface.onSearchResultFetchFailure();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onSearchResultFetchInterface.onSearchResultFetchFailure();
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
