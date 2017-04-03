package android.team.unitalk.unitalk.answers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.team.unitalk.unitalk.R;
import android.team.unitalk.unitalk.settings.SettingsActivity;
import android.team.unitalk.unitalk.utils.dataModel.Answer;
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
import static android.team.unitalk.unitalk.SplashActivity.loggedInUser;
import static android.team.unitalk.unitalk.SplashActivity.sharedpreferences;


/**
 * The AnswersOverview, this contains the list of answers related to a specific post
 *
 * @author Schnittker
 */

public class AnswersOverview extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnswersOverviewAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private List<Answer> answerList;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_answers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.content_answer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServerData();
                adapter.notifyDataSetChanged();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorRed);

        Intent intent = getIntent();

        String postIdstring = intent.getStringExtra("postId");
        postId = Integer.parseInt(postIdstring);
        //setTitle(intent.getStringExtra("content"));
        setTitle("");

        String title = intent.getStringExtra("content");
        String titleCreator = intent.getStringExtra("creator");

        TextView titleView = (TextView) findViewById(R.id.answerOverviewTitle);
        TextView creatorView = (TextView) findViewById(R.id.answerOverviewCreator);

        titleView.setText(title);
        creatorView.setText(titleCreator);

        answerList = new LinkedList<>();

        recyclerView = (RecyclerView) findViewById(R.id.answer_recyclerview);
        adapter = new AnswersOverviewAdapter(answerList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getServerData();
    }

    /**
     * Inflate the Options
     *
     * @param menu the Menu.
     * @return status.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts, menu);
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
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * Get all answers from server related to a specific post
     */
    public void getServerData() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Getting Serverdata...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/answer/get/" + postId;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            answerList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                Answer answer = new Answer(response.getJSONObject(i));
                                answerList.add(answer);
                                adapter.notifyDataSetChanged();
                                progress.dismiss();
                            }
                            if (response.length() == 0) progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                    }
                });
        queue.add(jsObjRequest);
        swipeContainer.setRefreshing(false);
    }

    /**
     * Opens an alert dialog to enter the content of a new answer
     *
     * @param view a floating action button
     */
    public void goAnswerCreation(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        final View view_ = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view_)
                .setTitle("Enter your Answer");

        // Add action buttons
        builder.setPositiveButton(R.string.entity_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                TextView textView = (TextView) view_.findViewById(R.id.dialog_input);
                String content = textView.getText().toString();
                addAnswer(content);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Adds a new answer related to a specific post
     *
     * @param content the content of the new post
     */
    public void addAnswer(String content) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Logging in ...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/answer/add/" + postId;
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user", sharedpreferences.getString(loggedInUser, ""));
        jsonParams.put("content", content);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if ((boolean) response.get("status")) {
                                progress.dismiss();
                                getServerData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
}
