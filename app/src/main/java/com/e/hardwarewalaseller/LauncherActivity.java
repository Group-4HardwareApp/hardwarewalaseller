package com.e.hardwarewalaseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e.hardwarewalaseller.databinding.ActivitySplashBinding;

public class LauncherActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    String currentUser = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(LauncherActivity.this);
        binding = ActivitySplashBinding.inflate(inflater);
        View v = binding.getRoot();
        setContentView(v);
//        anim();

        if (!isConnectedToInternet(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
            builder.setMessage("Please check the Internet connection").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser != null) {
                        sendUserToHomeActivity();
                    } else {
                        sendUserToLogInActivity();
                    }
                }
            }, 4000);

        }

    }

    private void anim() {

        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);

// Start animating the image

        final ImageView splash = (ImageView) findViewById(R.id.ivlogo);
//        ActivitySplashBinding.ivlogo.startAnimation(anim);
        splash.startAnimation(anim);
// Later.. stop the animation
//        ActivitySplashBinding.ivlogo.setAnimation(null);
//        splash.setAnimation(null);
    }

    private void sendUserToHomeActivity() {
        Intent in = new Intent(this, Home.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
    }

    private void sendUserToLogInActivity() {
        Intent in = new Intent(LauncherActivity.this, LogInActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
    }

    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
