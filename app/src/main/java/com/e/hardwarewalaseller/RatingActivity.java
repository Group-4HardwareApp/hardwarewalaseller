package com.e.hardwarewalaseller;




import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.hardwarewalaseller.adapter.ShowCommentAdapter;
import com.e.hardwarewalaseller.beans.Comment;
import com.e.hardwarewalaseller.databinding.ShowReviewsActivityBinding;

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

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent in = getIntent();
        commentList = (ArrayList<Comment>) in.getSerializableExtra("commentList");

        for(Comment comment : commentList) {

            adapter = new ShowCommentAdapter(this,commentList);
            binding.rvComment.setLayoutManager(new LinearLayoutManager(RatingActivity.this));
            binding.rvComment.setAdapter(adapter);
        }
    }
}
