package com.e.hardwarewalalseller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.hardwarewalalseller.apis.NotificationService;
import com.e.hardwarewalalseller.beans.Data;
import com.e.hardwarewalalseller.beans.MyResponse;
import com.e.hardwarewalalseller.beans.NotificationSender;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class breakabl extends AppCompatActivity
{

Button btnsendNotif;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifier);

        btnsendNotif=findViewById(R.id.sendnotifcation);


        btnsendNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//api part
                Data data=new Data();

                data.setTitle("this is title _sid");//pass title here
                data.setMessage("this is message");//pass message here
//                data.setImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
                String token = FirebaseInstanceId.getInstance().getToken();

                Toast.makeText(breakabl.this, "TOKEN==>"+token, Toast.LENGTH_SHORT).show();
                Log.e("tag","token==============+>"+token);
                //dynamically fetch token here
//                String to="dC8SXDK4TVO22yqXEDym_J:APA91bHAbTYcoyypOo_AC8XlxD_uNXZW3eRdqn3jy4qCS02N2fLbz4UIv76-SqpjBb-UmtXSOebuSFTMoexVopCJBxLs3Ar_9ebAykhNmCALNg74-4eojzJ7se8iZMbxsmB2VT8GwfYX";

                String to ="eR50yds-QRKgLu4_7_tZoh:APA91bEsiApagBkxU1bbgLFpuxlUlrkVgUQ7PKlt2go3mm-_3caEv2tJs4oc20LpjVvjij_LLy3Ca5Rm6mASeLYnhfxnun0DDU-YbyD6ss0mKr4cO1bulWoqMnv5IziEzqJxo1P9XZVJ";

                //get recievers token in to
                Toast.makeText(breakabl.this, ""+to, Toast.LENGTH_SHORT).show();
                NotificationSender sender = new NotificationSender(to,data);
                NotificationService.NotificationApi obj = NotificationService.getNotificationInstance();
                Call<MyResponse> call = obj.sendNotification(sender);
                call.enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                        Log.e("**","1 in onresponse");
                        if(response.code()==200)
                        {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
                            Toast.makeText(breakabl.this, ""+response.code(), Toast.LENGTH_SHORT).show();
                            Log.e("**","1 in onresponse");
                            if(response.body().success!=1)
                            {
                                Toast.makeText(breakabl.this, "API failure !!", Toast.LENGTH_SHORT).show();
                                //put toast here to check failure
                            }
                            else
                            {
                                Toast.makeText(breakabl.this, "running...", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else
                        {
                            Log.e("response body",""+response.body());
                            Log.e("response code",""+response.code());
                        }

                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {

                        Toast.makeText(breakabl.this, "FAILED", Toast.LENGTH_SHORT).show();

                    }
                });

                //api call end

            }
        });



    }

}
