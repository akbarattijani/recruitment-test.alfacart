package com.gli.services.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Controller
@RequestMapping("/")
public class DogsController {

    @GetMapping(value = "dogs")
    public @ResponseBody String getAllDogs() {
        try {
            String url = "https://dog.ceo/api/breeds/list/all";
            HttpResponse<String> resp = Unirest
                    .get(url)
                    .header("Content-Type","application/json")
                    .asString();

            System.out.println("getAllDogs (" + url + ") => " + resp.getStatus() + " : " + resp.getBody());

            String responseBody = resp.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject message = jsonObject.getJSONObject("message");
            Iterator<String> keys = message.keys();

            JSONArray result = new JSONArray();
            while(keys.hasNext()) {
                String key = keys.next();
                JSONObject object = new JSONObject();

                object.put("breed", key);
                if (message.get(key) instanceof JSONArray) {
                    JSONArray inArray = message.getJSONArray(key);
                    JSONArray subArray = new JSONArray();
                    for (int i = 0; i < inArray.length(); i++) {
                        JSONObject subObject = new JSONObject();
                        subObject.put("breed", inArray.getString(i));
                        subObject.put("sub_breed", new JSONArray());

                        subArray.put(subObject);
                    }

                    object.put("sub_breed", subArray);
                }

                result.put(object);
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping(value = "dogs/{type}")
    public @ResponseBody String getDogsDetail(@PathVariable(name="type") String type) {
        try {
            String url = "https://dog.ceo/api/breeds/list/all";
            HttpResponse<String> resp = Unirest
                    .get(url)
                    .header("Content-Type","application/json")
                    .asString();

            System.out.println("getDogsDetail (" + url + ") => " + resp.getStatus() + " : " + resp.getBody());

            String responseBody = resp.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject message = jsonObject.getJSONObject("message");

            JSONObject result = new JSONObject();
            result.put("breed", type);

            if (message.get(type) instanceof JSONArray) {
                JSONArray inArray = message.getJSONArray(type);
                JSONArray subArray = new JSONArray();
                for (int i = 0; i < inArray.length(); i++) {
                    JSONObject subObject = new JSONObject();
                    subObject.put("breed", inArray.getString(i));
                    subObject.put("sub_breed", new JSONArray());

                    subArray.put(subObject);
                }

                result.put("sub_breed", subArray);
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}