package com.yogesh.github_issues;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class IssueDetails extends AppCompatActivity {
    private String IssueUrl, IssueBody, IssueStatus, CommentUrl, IssueTitle, UpdatedAt, UserName, UserAvatarUrl;
    private int IssueNumber;
    private TextView title, status, userName, desc, msg;
    private ImageView userAvatar;
    private ListView commentsList;
    private ProgressDialog dialogProgress = null;
    private AllCommentsList commentsAdap;
    ArrayList<CommentsModel> commentsEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);

        DeclareVariables();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IssueNumber = extras.getInt("IssueNumber");
            IssueUrl = extras.getString("IssueUrl");
            IssueBody = extras.getString("IssueBody");
            IssueStatus = extras.getString("IssueStatus");
            CommentUrl = extras.getString("CommentUrl");
            IssueTitle = extras.getString("IssueTitle");
            UpdatedAt = extras.getString("UpdatedAt");
            UserName = extras.getString("UserName");
            UserAvatarUrl = extras.getString("UserAvatar");
        }

        title.setText(IssueTitle +" #"+ String.valueOf(IssueNumber));
        status.setText(IssueStatus);
        userName.setText(UserName);
        desc.setText(IssueBody);
        Picasso.get()
                .load(UserAvatarUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(userAvatar);

        getData();
    }

    public void DeclareVariables() {
        title = findViewById(R.id.title);
        status = findViewById(R.id.status);
        userName = findViewById(R.id.user);
        desc = findViewById(R.id.desc);
        userAvatar = findViewById(R.id.user_avatar);
        msg = findViewById(R.id.msg);
        commentsList = findViewById(R.id.comments_list);

        dialogProgress = new ProgressDialog(getApplicationContext());
        dialogProgress.setMessage("Please Wait...");
        dialogProgress.setCancelable(false);
    }

    private void getData() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, CommentUrl, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonResponse) {
                        dialogProgress.dismiss();
                        try {
                            commentsEntities = new ArrayList<CommentsModel>();
                            for (int i = 0; i < jsonResponse.length(); i++) {
                                JSONObject jsonObject = jsonResponse.getJSONObject(i);
                                CommentsModel commentEntity = new CommentsModel();

                                commentEntity.setUpdatedAt(jsonObject.getString("updated_at"));
                                commentEntity.setBody(jsonObject.getString("body"));

                                JSONObject userObject = jsonObject.getJSONObject("user");
                                UserModel userModel = new UserModel();
                                userModel.setId(userObject.getInt("id"));
                                userModel.setName(userObject.getString("login"));
                                userModel.setAvatarUrl(userObject.getString("avatar_url"));
                                commentEntity.setUser(userModel);

                                commentsEntities.add(commentEntity);
                            }

                            setData(commentsEntities);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialogProgress.dismiss();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    private void setData(ArrayList<CommentsModel> comments) {
        try {
            if (comments.size() > 0) {
                commentsList.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                commentsAdap = new AllCommentsList(this, R.layout.custom_comments_list, comments);
                commentsList.setAdapter(commentsAdap);
            } else {
                commentsList.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
                String text = "Currently no Comments";
                msg.setText(text);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
        }
    }

}