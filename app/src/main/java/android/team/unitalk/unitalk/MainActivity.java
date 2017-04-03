package android.team.unitalk.unitalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.team.unitalk.unitalk.lectures.LecturesOverview;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import static android.team.unitalk.unitalk.Constants.serverAddress;
import static android.team.unitalk.unitalk.SplashActivity.loggedIn;
import static android.team.unitalk.unitalk.SplashActivity.loggedInUser;
import static android.team.unitalk.unitalk.SplashActivity.sharedpreferences;


/**
 * The class MainActivity.
 *
 * @author Muehlenstaedt
 */
public class MainActivity extends AppCompatActivity {

    /**
     * the login status, needed for server authentication.
     */
    public boolean login = false;
    /**
     * the current context.
     */
    Context context = this;

    /**
     * the oncreate method.
     *
     * @param savedInstanceState default parameter.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        super.onCreate(savedInstanceState);
    }

    /**
     * This method started with the click of the Login button.
     *
     * @param view Current view.
     */
    public void clickLogin(View view) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Logging in ...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        final String username = ((EditText) findViewById(R.id.username)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/user/add/" + username;
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("username", username);
        jsonParams.put("password", password);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            login = (boolean) response.get("status");
                            progress.dismiss();
                            if (login) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(loggedIn, true);
                                editor.putString(loggedInUser, username);
                                editor.apply();

                                Intent intent = new Intent(context, LecturesOverview.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(context,
                                        "Wrong Credentials",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (username.equals("") || password.equals("")) {
                            Toast.makeText(context, "Input empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context,
                                    "Server Communication failed", Toast.LENGTH_SHORT).show();
                        }
                        progress.dismiss();
                        System.err.println(error);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(myRequest);
    }

    /**
     * Inflate the Options
     *
     * @param menu the Menu.
     * @return status.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * this methode makes the settings clickable.
     *
     * @param item the menu.
     * @return if button got clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.debug_user: {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(loggedIn, true);
                editor.putString(loggedInUser, "y0000000");
                editor.apply();

                Intent intent = new Intent(this, LecturesOverview.class);
                startActivity(intent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
