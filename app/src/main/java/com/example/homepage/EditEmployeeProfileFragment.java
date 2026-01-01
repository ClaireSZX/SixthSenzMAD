package com.example.homepage;

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
import com.example.madproject.R;
import com.example.madproject.User;

public class EditEmployeeProfileFragment extends Fragment {

    private String userEmail;
    private EditText editTextName, editTextExperience, editTextSkills, editTextLanguage, editTextEmail, editTextPhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_employee_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化视图
        initializeViews(view);

        // 从 arguments 获取 userId，并预填充数据
        if (getArguments() != null) {
            userEmail = getArguments().getString("user_email", null);
            Bundle args = getArguments();
            editTextName.setText(args.getString("name"));
            editTextExperience.setText(args.getString("experience"));
            editTextSkills.setText(args.getString("skills"));
            editTextLanguage.setText(args.getString("language"));
            editTextEmail.setText(args.getString("email"));
            editTextPhone.setText(args.getString("phone"));
        }

        Button buttonSave = view.findViewById(R.id.buttonSave);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(v -> {
            if (userEmail == null) return;

            // 在后台线程中更新数据库
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(getContext().getApplicationContext());
                User userToUpdate = db.userDao().findByEmail(userEmail);

                if (userToUpdate != null) {
                    // 从EditText获取新数据并更新User对象
                    userToUpdate.fullName = editTextName.getText().toString();
                    userToUpdate.workingExperience = editTextExperience.getText().toString();
                    userToUpdate.skills = editTextSkills.getText().toString();
                    userToUpdate.language = editTextLanguage.getText().toString();
                    userToUpdate.email = editTextEmail.getText().toString();
                    userToUpdate.phone = editTextPhone.getText().toString();

                    // 将更新后的User对象写回数据库
                    db.userDao().update(userToUpdate);

                    // 操作完成后，回到UI线程
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            // 发送一个结果，通知ProfileFragment刷新
                            getParentFragmentManager().setFragmentResult("requestKey", new Bundle());
                            Toast.makeText(getContext(), "Details Saved!", Toast.LENGTH_SHORT).show();
                            goBack();
                        });
                    }
                }
            }).start();
        });

        buttonCancel.setOnClickListener(v -> goBack());
    }

    private void initializeViews(View view) {
        editTextName = view.findViewById(R.id.editTextEmployeeName);
        editTextExperience = view.findViewById(R.id.editTextWorkingExperience);
        editTextSkills = view.findViewById(R.id.editTextSkills);
        editTextLanguage = view.findViewById(R.id.editTextLanguage);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
    }

    private void goBack() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}


