    package com.e.hardwarewalaseller.apis;


    import com.e.hardwarewalaseller.beans.MyResponse;
    import com.e.hardwarewalaseller.beans.NotificationSender;

    import retrofit2.Call;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;
    import retrofit2.http.Body;
    import retrofit2.http.Headers;
    import retrofit2.http.POST;

    public class  NotificationService {
        public static String API_URL="https://fcm.googleapis.com/";


        public static NotificationApi notificationApi;


        public static NotificationApi getNotificationInstance()
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            if(notificationApi==null)
            {
                notificationApi =retrofit.create(NotificationApi.class);
            }
            return  notificationApi;
        }



    public interface NotificationApi
    {

        @Headers(
                {
                        "Content-Type:application/json",
                        "Authorization:key=AAAAc6o9u2w:APA91bHRdoM1udrWMLC9ttnTelx5SN6896Mx2HMvuDjYbchI2G3h4ImnFi2klr2nBbgplOzbKkYqhIDoB5HTfSFnPIeXNkBlYYoGiazHU1REzNDds13o3hnGuIh-3z3oXKFiL1aGOWgb"
                }
        )
        @POST("fcm/send")
        public Call<MyResponse> sendNotification(@Body NotificationSender body);
    }


    }