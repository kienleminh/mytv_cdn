package vn.vnpt.api;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

/**
 *
 * @author Nguyen Nhu Son
 */
public class Json {
    
    public static final int SUCCESS = 1;
    public static final int ERROR = 0;

    String error_code = "-1";
    String message = "Tham số không hợp lệ";
    
    Object data;   

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return gson.toJson(this);
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("error_code", error_code);
        obj.put("message", message);
        return obj;
    }

    public static Json invalidParam(String params) {
        Json json = new Json();
        json.error_code = "-1";
        json.message = "Invalid parameters [" + params + "]";
        return json;
    }

    public void invalidAuth() {
        this.error_code = "401";
        this.message = "Xác thực không hợp lệ";
    }

    public Json exception() {
        this.error_code = "-99";
        this.message = "Lỗi hệ thống";
        return this;
    }
}
