package com.example.madproject;

import android.os.Bundle;
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

// ✅ 导入 TextInputLayout
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment {

    private String userType;
    private EditText editTextName, editTextEmail, editTextPassword;

    // ✅ 声明一个新的变量来引用 TextInputLayout
    private TextInputLayout textInputLayoutName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userType = getArguments().getString("userType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ 在 XML 中，EditText 是被 TextInputLayout 包裹的，所以我们需要获取到这个父布局
        // 我们需要给 TextInputLayout 在 XML 中一个 ID
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName);

        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        Button buttonSignUp = view.findViewById(R.id.buttonSignUp);
        TextView loginLink = view.findViewById(R.id.textViewLoginLink);
        NavController navController = Navigation.findNavController(view);

        // ✅ 关键修正：修改 TextInputLayout 的 hint，而不是 EditText 的
        if ("employer".equals(userType)) {
            textInputLayoutName.setHint("Company Name");
        } else {
            textInputLayoutName.setHint("Full Name");
        }

        loginLink.setOnClickListener(v -> {
            navController.navigate(R.id.action_signUpFragment_to_loginFragment);
        });

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

            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(getContext());
                db.userDao().insert(newUser);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        navController.navigate(R.id.action_signUpFragment_to_loginFragment, bundle);
                    });
                }
            }).start();
        });
    }
}


