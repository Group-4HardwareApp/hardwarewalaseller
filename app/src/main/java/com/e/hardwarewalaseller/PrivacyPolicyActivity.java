package com.e.hardwarewalaseller;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.hardwarewalaseller.databinding.PrivacypolicyBinding;


public class PrivacyPolicyActivity extends AppCompatActivity {

    PrivacypolicyBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PrivacypolicyBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Privacy Policy");

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.tvTerms.setText("1. Privacy and data protection\n");
        binding.tvCondition.setText("Protecting your privacy is important to us because privacy and data protection is your human right. This Hardwarewale.com Privacy Policy tells you how we collect your personal information and keep it confidential.\n\n1. Only collect and process your data when absolutely necessary.\n2. Cease to process your data at your request.");
        binding.tvabc.setText("2. Your rights");
        binding.tvQue.setText("You may request to review, correct, update, suppress, or otherwise modify any of your Personal Information that you have previously provided to us through the channels mentioned above, or object to the use or processing of such Personal Information by us at any time. If you have privacy concerns regarding access to or the correction of your Personal Information, please contact us using the details in section 3.");
        binding.tvmno.setText("3. Data Protection contact details");
        binding.tvpqr.setText("Contact:- +91963XXX6501\n Email:- mailto@gmail.com");
    }
}
