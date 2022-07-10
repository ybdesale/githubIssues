package com.yogesh.github_issues;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllCommentsList extends ArrayAdapter<CommentsModel> {
    Context context;
    private ArrayList<CommentsModel> SubsList;

    public AllCommentsList(Activity activity, int userlist, ArrayList<CommentsModel> subList) {
        super(activity, userlist, subList);
        this.context = activity.getApplicationContext();
        this.SubsList = new ArrayList<CommentsModel>();
        this.SubsList.addAll(subList);
    }

    private class ViewHolder {
        TextView CommentsDesc, UpdatedAt;
        ImageView AvatarUrl;
        CommentsModel data;
    }

    ViewHolder holder = null;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            try {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.custom_comments_list, null);

                holder = new ViewHolder();
                holder.CommentsDesc = convertView.findViewById(R.id.desc);
                holder.UpdatedAt = convertView.findViewById(R.id.updated_at);
                holder.AvatarUrl = convertView.findViewById(R.id.user_avatar);

            } catch (Exception e) {
                Toast.makeText(getContext(), "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.data = SubsList.get(position);
            holder.CommentsDesc.setText(holder.data.getBody());

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat output = new SimpleDateFormat("MM-dd-yyyy");
            Date d = input.parse(holder.data.getUpdatedAt());
            String formatted = output.format(d);
            holder.UpdatedAt.setText(formatted);

            Picasso.get()
                    .load(holder.data.getUser().getAvatarUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.AvatarUrl);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }

}