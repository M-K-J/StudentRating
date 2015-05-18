package com.example.admin2015.student_rating;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by admin2015 on 23.04.2015.
 */
public class RecognitionAPI {
    private static final String TAG = RecognitionAPI.class.getName();

    private static final String RECOGNITION_API_URL = "http://192.168.178.21:5050/predict";

    public static String requestRecognition(Bitmap bitmap) throws Exception {
        String result = new String();
        try {
            JSONObject jsonRequest = constructPredictionRequest(bitmap);
            HttpResponse response = makeSynchronousRequest(jsonRequest);
            result = evaluateResponse(response);
        } catch (Exception e) {
            Log.e(TAG, "Recognition failed!", e);
            throw e;
        }
        return result;
    }

    private static HttpResponse makeSynchronousRequest(JSONObject jsonRequest) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(RECOGNITION_API_URL);
        httpPost.setEntity(new StringEntity(jsonRequest.toString()));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        return httpClient.execute(httpPost);
    }

    private static String evaluateResponse(HttpResponse response) throws IOException, JSONException {
        String result = new String();
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String entityContent = readEntityContentToString(entity);
            JSONObject jsonResponse = new JSONObject(entityContent);
            result = jsonResponse.getString("name");
        }
        return result;
    }

    private static String readEntityContentToString(HttpEntity entity) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream content = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private static JSONObject constructPredictionRequest(Bitmap bitmap) throws JSONException {
        String base64encBitmap = ImageHelper.getBase64Jpeg(bitmap, 100);
        // Now create the object:
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image", base64encBitmap);
        } catch (JSONException e) {
            Log.e(TAG, "Could not create Json object", e);
            throw e;
        }
        return jsonObject;
    }
}
