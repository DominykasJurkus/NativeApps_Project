package com.dominykas.jurkus.WordQuiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    Button btnLogout;
    TextView textView2;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        textView2 = view.findViewById(R.id.textView2);
        btnLogout = view.findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                openGameActivity();

            }
        });

        return view;




    }

    public void openGameActivity(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        Intent intent = new Intent(getActivity(),  RegisterActivity.class);
        startActivity(intent);
    }












}





