package com.example.rent_it.Interfaces;

import org.json.JSONObject;

public interface AuthOnCompletePasswordChangeInterface {
    void onServerUserUpdateSuccess(JSONObject user);

    void onServerUserUpdateFailure();

}
