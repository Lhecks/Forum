package com.hame.forum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hame.forum.R;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.CommentItems;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private Context mContextComment;
    private SessionManager sessionManager;
    private List<CommentItems> commentItemsList;

    public CommentAdapter(Context mContextComment, SessionManager sessionManager, List<CommentItems> commentItemsList) {
        this.mContextComment = mContextComment;
        this.sessionManager = sessionManager;
        this.commentItemsList = commentItemsList;
    }

    public CommentAdapter(Context mContextComment, List<CommentItems> commentItemsList) {
        this.mContextComment = mContextComment;
        this.commentItemsList = commentItemsList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolderComment;
        LayoutInflater layoutInflater = LayoutInflater.from(mContextComment);
        viewHolderComment = layoutInflater.inflate(R.layout.adapter_comment, parent, false);
        return new CommentHolder(viewHolderComment);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        CommentItems commentItems = commentItemsList.get(position);
        holder.bindingItemsComments(commentItems);
    }

    @Override
    public int getItemCount() {
        return commentItemsList.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        TextView textCommentAdapter, dateCommentAdapter, textUser;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            textUser = itemView.findViewById(R.id.text_get_user_name_comment_adapter);
            textCommentAdapter = itemView.findViewById(R.id.text_comment_adapter);
            dateCommentAdapter = itemView.findViewById(R.id.text_date_comment_adapter);
        }

        public void bindingItemsComments(final CommentItems commentItems) {
            textCommentAdapter.setText(commentItems.getNew_comment());
            textUser.setText(commentItems.getUser_name());
            String date_time = commentItems.getDate_comment() + " at " + commentItems.getTime_comment();
            dateCommentAdapter.setText(date_time);
        }
    }
}
