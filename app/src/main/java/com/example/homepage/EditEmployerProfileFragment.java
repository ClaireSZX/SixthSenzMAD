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

public class EditEmployerProfileFragment extends Fragment {

    private String userEmail;
    private EditText editTextCompanyName, editTextAbout, editTextSize, editTextIndustry, editTextEmail, editTextLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_employer_profile, container, false);
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
            editTextCompanyName.setText(args.getString("name"));
            editTextAbout.setText(args.getString("about"));
            editTextSize.setText(args.getString("size"));
            editTextIndustry.setText(args.getString("industry"));
            editTextEmail.setText(args.getString("email"));
            editTextLocation.setText(args.getString("location"));
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
                    userToUpdate.companyName = editTextCompanyName.getText().toString();
                    userToUpdate.aboutCompany = editTextAbout.getText().toString();
                    userToUpdate.companySize = editTextSize.getText().toString();
                    userToUpdate.industry = editTextIndustry.getText().toString();
                    userToUpdate.email = editTextEmail.getText().toString();
                    userToUpdate.location = editTextLocation.getText().toString();

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
        editTextCompanyName = view.findViewById(R.id.editTextCompanyName);
        editTextAbout = view.findViewById(R.id.editTextAboutCompany);
        editTextSize = view.findViewById(R.id.editTextCompanySize);
        editTextIndustry = view.findViewById(R.id.editTextIndustry);
        editTextEmail = view.findViewById(R.id.editTextCompanyEmail);
        editTextLocation = view.findViewById(R.id.editTextLocation);
    }

    private void goBack() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}


