package android.team.unitalk.unitalk.posts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.team.unitalk.unitalk.R;
import android.team.unitalk.unitalk.answers.AnswersOverview;
import android.team.unitalk.unitalk.settings.SettingsActivity;
import android.team.unitalk.unitalk.utils.dataModel.Post;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
 * The PostsOverview, this contains the list of posts related to a specific lecture
 *
 * @author Schnittker
 */

public class PostsOverview extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostOverviewAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private List<Post> post_List;
    private String lectureCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.overview_posts);
        setTitle(getIntent().getStringExtra("lectureName"));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.content_post);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServerData();
                adapter.notifyDataSetChanged();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorRed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lectureCode = getIntent().getStringExtra("lectureCode");

        post_List = new LinkedList<>();

        recyclerView = (RecyclerView) findViewById(R.id.post_recyclerview);
        adapter = new PostOverviewAdapter(post_List, getApplication());
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
     * Queries all posts related to specifc lecture
     */
    public void getServerData() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Getting Serverdata...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/post/get/" + this.lectureCode;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            post_List.clear();
                            for (int i = 0; i < response.length(); i++) {
                                Post post = new Post(response.getJSONObject(i));
                                post_List.add(post);
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
     * Called when the user touches the upVote button
     *
     * @param view the upVote button
     */
    public void upVote(View view) {
        CardView cv = (CardView) view.getParent().getParent();
        Button likeButton = (Button) cv.findViewById(R.id.likeButton);
        likeButton.setBackgroundResource(R.drawable.ic_star_black_24dp);
        TextView postId = (TextView) cv.findViewById(R.id.postId);
        String id = (String) postId.getText();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/rate/like/like";
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user", sharedpreferences.getString(loggedInUser, ""));
        jsonParams.put("postId", id);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if ((boolean) response.get("status")) {
                                getServerData();
                            } else if (!(boolean) response.get("status")) {
                                //
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(myRequest);

    }

    /**
     * Opens a popup dialog to enter the content of a new post
     *
     * @param view a floating action button
     */
    public void goPostCreation(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        final View view_ = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view_)
                .setTitle("Enter new Question");

        builder.setPositiveButton(R.string.entity_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                TextView textView = (TextView) view_.findViewById(R.id.dialog_input);
                String content = textView.getText().toString();

                addPost(content);
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
     * Moves user to AnswersOverview
     *
     * @param view the list item containing the content of the post
     */
    public void goAnswersOverview(View view) {
        TextView post = (TextView) view.findViewById(R.id.postId);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView creator = (TextView) view.findViewById(R.id.creator);
        String postId = post.getText().toString();

        Intent activityIntent = new Intent(this, AnswersOverview.class)
                .putExtra("postId", postId)
                .putExtra("content", content.getText().toString())
                .putExtra("creator", creator.getText().toString());
        startActivity(activityIntent);
    }

    /**
     * Adds a new post related to the specific lecture
     *
     * @param content the content of the new post
     */
    public void addPost(String content) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Logging in ...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + serverAddress + ":8080/post/add/" + lectureCode;
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user", sharedpreferences.getString(loggedInUser, ""));
        jsonParams.put("content", content);
        //jsonParams.put("deviceId", deviceId);

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
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(myRequest);
    }
}
