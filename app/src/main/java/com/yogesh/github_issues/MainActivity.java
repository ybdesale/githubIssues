package com.yogesh.github_issues;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private TextView msg;
    private ProgressDialog dialogProgress = null;
    private AllIssuesList issueAdap;
    ArrayList<IssueModel> issueEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeclareVariables();
        getData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, IssueDetails.class);
                intent.putExtra("IssueNumber",issueEntities.get(i).getIssueNumber());
                intent.putExtra("IssueUrl",issueEntities.get(i).getIssueUrl());
                intent.putExtra("IssueBody",issueEntities.get(i).getBody());
                intent.putExtra("IssueStatus",issueEntities.get(i).getStatus());
                intent.putExtra("CommentUrl",issueEntities.get(i).getCommentsUrl());
                intent.putExtra("IssueTitle",issueEntities.get(i).getTitle());
                intent.putExtra("UpdatedAt",issueEntities.get(i).getUpdatedAt());
                intent.putExtra("UserName",issueEntities.get(i).getUser().getName());
                intent.putExtra("UserAvatar",issueEntities.get(i).getUser().getAvatarUrl());
                startActivity(intent);
            }
        });

    }

    public void DeclareVariables() {
        list = findViewById(R.id.mylist);
        msg = findViewById(R.id.msg);

        dialogProgress = new ProgressDialog(getApplicationContext());
        dialogProgress.setMessage("Please Wait...");
        dialogProgress.setCancelable(false);
    }

    private void getData() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, "https://api.github.com/repos/square/okhttp/issues", null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonResponse) {
                        dialogProgress.dismiss();
                        try {
                            issueEntities = new ArrayList<IssueModel>();
                            for (int i = 0; i < jsonResponse.length(); i++) {
                                JSONObject jsonObject = jsonResponse.getJSONObject(i);
                                IssueModel issueEntity = new IssueModel();

                                issueEntity.setIssueNumber(jsonObject.getInt("number"));
                                issueEntity.setIssueUrl(jsonObject.getString("url"));
                                issueEntity.setCommentsUrl(jsonObject.getString("comments_url"));
                                issueEntity.setTitle(jsonObject.getString("title"));
                                issueEntity.setUpdatedAt(jsonObject.getString("updated_at"));
                                issueEntity.setBody(jsonObject.getString("body"));
                                issueEntity.setStatus(jsonObject.getString("state"));

                                JSONObject userObject = jsonObject.getJSONObject("user");
                                UserModel userModel = new UserModel();
                                userModel.setId(userObject.getInt("id"));
                                userModel.setName(userObject.getString("login"));
                                userModel.setAvatarUrl(userObject.getString("avatar_url"));
                                issueEntity.setUser(userModel);

                                issueEntities.add(issueEntity);
                            }

                            setData(issueEntities);
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

    private void setData(ArrayList<IssueModel> issues) {
        try {
            if (issues.size() > 0) {
                list.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                issueAdap = new AllIssuesList(this, R.layout.custom_issues_list, issues);
                list.setAdapter(issueAdap);
            } else {
                list.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
                String text = "Currently no Issues to Display";
                msg.setText(text);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
        }
    }
}