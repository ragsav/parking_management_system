package com.example.rent_it.Interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface AuthOnCompleteRetreiveInterface {
    void onServerUserRetrieveSuccess(JSONObject user, JSONArray vehicles);

    void onServerUserRetrieveFailure();


}
