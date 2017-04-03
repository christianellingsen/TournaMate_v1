package com.dtu.tournamate_v1.myProfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.User;
import com.firebase.client.Config;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by ALIDBS on 25-01-2017.
 */
public class MyProfile extends AppCompatActivity {

private TextView myprofileusername;

    private User user = MyApplication.getUser();
/**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        Firebase.setAndroidContext(this);

        myprofileusername = (TextView) findViewById(R.id.myprofileusername);

        Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
        Firebase userRef = myFirebaseRef.child("users");
        Firebase thisUserRef = userRef.child(MyApplication.getUser().getU_ID());

        //Value event listener for realtime data update
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Getting the data from snapshot
                    userRef user = postSnapshot.getValue(userRef.class);

                    //Adding it to a string
                    String string = "Name: "+user.getName();

                    //Displaying it on textview
                    myprofileusername.setText(string);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
}*/
}
