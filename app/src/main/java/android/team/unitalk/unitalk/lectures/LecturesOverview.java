package android.team.unitalk.unitalk.lectures;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.team.unitalk.unitalk.MainActivity;
import android.team.unitalk.unitalk.R;
import android.team.unitalk.unitalk.posts.PostsOverview;
import android.team.unitalk.unitalk.settings.SettingsActivity;
import android.team.unitalk.unitalk.utils.Notifier;
import android.team.unitalk.unitalk.utils.dataModel.Lecture;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.team.unitalk.unitalk.Constants.serverAddress;
import static android.team.unitalk.unitalk.SplashActivity.loggedIn;
import static android.team.unitalk.unitalk.SplashActivity.loggedInUser;
import static android.team.unitalk.unitalk.SplashActivity.sharedpreferences;

/**
 * The LectureOverview, this contains the list of lectures
 *
 * @author Muehlenstaedt
 */

public class LecturesOverview extends AppCompatActivity {

    Context context = this;
    private SwipeRefreshLayout swipeContainer;
    private List<Lecture> lectureList;
    private LecturesOverviewAdapter adapter;
    private String username = sharedpreferences.getString(loggedInUser, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_lectures);//Set Layout

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.content_lectures);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLectureFromUser();
                adapter.notifyDataSetChanged();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorRed);

        lectureList = new LinkedList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lectures_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LecturesOverviewAdapter(lectureList, this);

        recyclerView.setAdapter(adapter);
        getLectureFromUser();
    }

    /**
     * Inflate the Options
     *
     * @param menu the Menu.
     * @return status.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lectures, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent activityIntent = new Intent(this, SettingsActivity.class);
                startActivity(activityIntent);
                return true;
            }
            case R.id.action_logout: {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(loggedIn);
                editor.remove(loggedInUser);
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Moves user to the activity with the overview of the specific posts
     *
     * @param view the textView containing the lecture title
     */
    public void goPostsOverview(View view) {
        TextView tv = (TextView) view.findViewById(R.id.list_item_lectures_textview);
        TextView tw = (TextView) view.findViewById(R.id.list_item_lectures_textview_second);
        String n = tv.getText().toString();
        String c = tw.getText().toString();

        Intent activityIntent = new Intent(this, PostsOverview.class)
                .putExtra("lectureName", n)
                .putExtra("lectureCode", c);

        startActivity(activityIntent);
    }

    /**
     * Opens a popup dialog to enter the name of the new lecture
     *
     * @param view a floating action button
     */
    public void goLectureCreation(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        final View view_ = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view_)
                .setTitle("Create Lecture");

        // Add action buttons
        builder.setPositiveButton(R.string.entity_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                TextView textView = (TextView) view_.findViewById(R.id.dialog_input);
                String lectureName = textView.getText().toString();

                addLectureToServer(lectureName);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * queries all lectures that are related to the user from the server
     */
    public void getLectureFromUser() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Getting Serverdata...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/user/get/" + this.username;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            lectureList.clear();
                            for (int i = 0; i < response.length(); i++) {

                                Lecture lecture = new Lecture(response.getJSONObject(i));
                                lectureList.add(lecture);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        new Notifier(context, "Server Communication failed!").simpleShortToast();
                        progress.dismiss();
                    }
                });
        queue.add(jsObjRequest);
        swipeContainer.setRefreshing(false);

    }

    /**
     * Opens a dialog to enter the access code of a specific lecture.
     *
     * @param view floating action button
     */
    public void goFindLecture(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View view_ = inflater.inflate(R.layout.alert_dialog_numbers, null);
        builder.setView(view_);
        builder.setTitle("Access existing Lecture");

        builder.setPositiveButton(R.string.entity_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                TextView textView = (TextView) view_.findViewById(R.id.dialog_input_number);
                String t = textView.getText().toString();
                getServerLecture(t);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Queries specific lecture from server
     *
     * @param code the code of the lecture to access it
     */
    public void getServerLecture(String code) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Getting Serverdata...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/lecture/get/" + code;

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user", sharedpreferences.getString(loggedInUser, ""));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,
                        url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                lectureList.add(new Lecture(response));
                                adapter.notifyDataSetChanged();
                                progress.dismiss();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(jsObjRequest);
        swipeContainer.setRefreshing(false);
    }


    /**
     * Adds a new lecture to the server
     *
     * @param name the name of the new lecture
     */
    public void addLectureToServer(String name) {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Getting Serverdata...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        name = name.replaceAll(" ", "_"); //remove spaces

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/lecture/add/" + name;

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user", sharedpreferences.getString(loggedInUser, ""));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,
                        url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                                Lecture l = new Lecture(response);
                                lectureList.add(l);

                                adapter.notifyDataSetChanged();
                                progress.dismiss();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(jsObjRequest);
    }

}
