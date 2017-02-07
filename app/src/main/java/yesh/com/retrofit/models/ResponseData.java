package yesh.com.retrofit.models;

/**
 * Created by yesh on 2/6/17.
 */

import com.google.gson.JsonElement;

public final class ResponseData extends ResponseStatus {
    private JsonElement data;

    public JsonElement getData() {
        return data;
    }
}