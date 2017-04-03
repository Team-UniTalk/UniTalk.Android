package android.team.unitalk.unitalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.team.unitalk.unitalk.lectures.LecturesOverview;
import android.team.unitalk.unitalk.utils.Notifier;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.team.unitalk.unitalk.Constants.serverAddress;


/**
 * The Splashscreen.
 * Activity to load files.
 *
 * @author Mühlenstädt
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * This is one of the two identifier string for the preferences.
     */
    public final static String loggedIn = "loggedIn";
    /**
     * This is one of the two identifier string for the preferences.
     */
    public final static String loggedInUser = "loggedInUser";
    /**
     * The shardepreferences.
     */
    public static SharedPreferences sharedpreferences;
    /**
     * Current context.
     */
    Context context = this;

    /**
     * status for server connection.
     */
    private boolean status = false;

    /**
     * The on create method of the splashscreen.
     *
     * @param savedInstanceState default parameter.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        //checkServerConnection();

        if (sharedpreferences.getBoolean("loggedIn", false)
                && !sharedpreferences.getString(loggedInUser, "").equals("")) {
            String username = sharedpreferences.getString(loggedInUser, "");
            Intent intent = new Intent(context, LecturesOverview.class)
                    .putExtra("userName", username);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * This method is checking the server connection and then send the user into the right activiy,
     * depending on his login state.
     */
    private void checkServerConnection() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/connection/get/";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status = (boolean) response.get("status");

                            if (sharedpreferences.getBoolean("loggedIn", false)
                                    && !sharedpreferences.getString(loggedInUser, "").equals("")) {
                                String username = sharedpreferences.getString(loggedInUser, "");
                                Intent intent = new Intent(context, LecturesOverview.class)
                                        .putExtra("userName", username);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new Notifier(context,
                                    "No server connection possible!").simpleLongToast();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new Notifier(context, "No server connection possible!").simpleLongToast();
                        error.printStackTrace();
                    }
                });
        queue.add(jsObjRequest);
    }
}
