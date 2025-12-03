package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homepage.MainActivity;
import com.example.homepage.ProfileFragment;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextEmail = view.findViewById(R.id.editTextEmailLogin);
        EditText editTextPassword = view.findViewById(R.id.editTextPasswordLogin);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // 在后台线程中执行数据库查询
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(getContext());
                User user = db.userDao().findByEmail(email);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (user != null && user.password.equals(password)) {
                            Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("userEmail", user.email);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        });
    }
}





