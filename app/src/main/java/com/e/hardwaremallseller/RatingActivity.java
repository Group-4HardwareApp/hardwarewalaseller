package com.e.hardwaremallseller;




import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.hardwaremallseller.adapter.ShowCommentAdapter;
import com.e.hardwaremallseller.beans.Comment;
import com.e.hardwaremallseller.databinding.ShowReviewsActivityBinding;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    ShowReviewsActivityBinding binding;
    ShowCommentAdapter adapter;
    ArrayList<Comment> commentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShowReviewsActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        commentList = (ArrayList<Comment>) in.getSerializableExtra("commentList");

        for(Comment comment : commentList) {
            adapter = new ShowCommentAdapter(this,commentList);
            binding.rvComment.setLayoutManager(new LinearLayoutManager(RatingActivity.this));
            binding.rvComment.setAdapter(adapter);
        }
    }
}
