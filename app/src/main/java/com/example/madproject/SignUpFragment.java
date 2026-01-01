package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.AppDatabase;
import com.example.homepage.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment {

    private String userType = "user"; // default type
    private EditText editTextName, editTextEmail, editTextPassword;
    private TextInputLayout textInputLayoutName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userType = getArguments().getString("userType", "user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textInputLayoutName = view.findViewById(R.id.textInputLayoutName);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        Button buttonSignUp = view.findViewById(R.id.buttonSignUp);

        // Adjust hint based on user type
        if ("employer".equals(userType)) {
            textInputLayoutName.setHint("Company Name");
        } else {
            textInputLayoutName.setHint("Full Name");
        }

        buttonSignUp.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User();
            newUser.email = email;
            newUser.password = password;
            newUser.userType = userType;
            if ("employer".equals(userType)) {
                newUser.companyName = name;
            } else {
                newUser.fullName = name;
            }

            // Insert user in background
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(getContext());
                db.userDao().insert(newUser);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();

                        // Launch MainActivity after signup
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("user_email", newUser.getEmail());  // pass the new user ID
                        startActivity(intent);

                        // Close SignupActivity so user can't go back
                        getActivity().finish();
                    });
                }
            }).start();
        });

        // ---------- Login text (partial clickable) ----------
        TextView loginSentence = view.findViewById(R.id.textViewLoginSentence);
        String text = "Already have an account? Login";
        SpannableString spannableString = new SpannableString(text);

        int start = text.indexOf("Login");
        int end = start + "Login".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Navigate to LoginFragment
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_signUpFragment_to_loginFragment);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.brand_navy_blue));
                ds.setUnderlineText(true);
            }
        };

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginSentence.setText(spannableString);
        loginSentence.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
