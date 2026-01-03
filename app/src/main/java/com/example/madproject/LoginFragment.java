package com.example.madproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.AppDatabase;
import com.example.homepage.MainActivity;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
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
                Toast.makeText(getContext(),
                        "Please enter email and password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {

                AppDatabase db =
                        AppDatabase.getDatabase(requireContext().getApplicationContext());

                User user = db.userDao().findByEmail(email);

                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {

                    if (user == null || !user.password.equals(password)) {
                        Toast.makeText(getContext(),
                                "Invalid email or password",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // ✅ Save session ONLY after successful login
                    SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", user.getName());
                    editor.apply();

                    prefs.edit()
                            .putString("user_type", user.getUserType())
                            .putString("user_email", user.email)
                            .putInt("user_id", user.id)
                            .apply();

                    Toast.makeText(getContext(),
                            "Login Successful!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("user_email", user.email);
                    intent.putExtra("user_type", user.getUserType());
                    startActivity(intent);
                    getActivity().finish();
                });

            }).start();
        });
    }
}

