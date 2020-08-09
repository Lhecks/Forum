package com.hame.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.hame.forum.Comments;
import com.hame.forum.R;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.ForumItems;

import org.w3c.dom.Comment;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumHolder> {
    private Context myContext;
    private SessionManager sessionManager;
    private List<ForumItems> forumItemsList;

    public ForumAdapter(Context myContext, List<ForumItems> forumItemsList) {
        this.myContext = myContext;
        this.forumItemsList = forumItemsList;
    }

    public ForumAdapter(Context myContext, SessionManager sessionManager, List<ForumItems> forumItemsList) {
        this.myContext = myContext;
        this.sessionManager = sessionManager;
        this.forumItemsList = forumItemsList;
    }

    @NonNull
    @Override
    public ForumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewForum;
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        viewForum = layoutInflater.inflate(R.layout.adapter_forum, parent, false);
        return new ForumHolder(viewForum);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumHolder holder, final int position) {
        final ForumItems forumItems = forumItemsList.get(position);

        holder.bindingItemsForum(forumItems);
        holder.cardViewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForum = new Intent(v.getContext(), Comments.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentForum.putExtra("forum_subject", forumItemsList.get(position));
                v.getContext().startActivity(intentForum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return forumItemsList.size();
    }

    private void passData(int id) {
        Intent intent = new Intent(myContext, Comments.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("forum_subject", "" + id);
        myContext.startActivity(intent);
    }

    public static class ForumHolder extends RecyclerView.ViewHolder {
        private CardView cardViewForum;
        private TextView textSubjectForum, textUserName, textDateForum, textCommentNumberForum, textSeenForum;

        public ForumHolder(@NonNull View itemView) {
            super(itemView);
            textSubjectForum = itemView.findViewById(R.id.forum_adapter_get_subject);
            textDateForum = itemView.findViewById(R.id.forum_adapter_get_date_time);
            textUserName = itemView.findViewById(R.id.forum_adapter_get_user_name);
//            textCommentNumberForum = itemView.findViewById(R.id.text_comment_number_forum_adapter);
            textSeenForum = itemView.findViewById(R.id.text_seen_number_forum_adapter);
            cardViewForum = itemView.findViewById(R.id.card_adapter_main);
        }

        void bindingItemsForum(final ForumItems forumItems) {
            textSubjectForum.setText(forumItems.getForum_subject());
            textUserName.setText(forumItems.getUser_name());
            textDateForum.setText(forumItems.getDate_forum());
            String date_time = forumItems.getDate_forum() + " at " + forumItems.getForum_time();
            textDateForum.setText(date_time);
        }
    }
}