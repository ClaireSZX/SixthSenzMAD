package com.example.madproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.card.MaterialCardView;

public class ProfileFragment extends Fragment {

    private int userId;
    private String userType;
    private User currentUser;
    private View mainView;

    // Employee Views
    private TextView textViewName, textViewEmail, textViewPhone;
    private TextView textViewWorkingExperience, textViewSkills, textViewLanguage;
    private MaterialCardView cardExperience, cardSkills, cardLanguage;

    // Employer Views
    private TextView textViewCompanyName, textViewCompanyEmail, textViewLocation;
    private TextView textViewAboutCompany, textViewCompanySize, textViewIndustry;
    private MaterialCardView cardAbout, cardSize, cardIndustry;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            loadUserData();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_loading, container, false);
        loadUserTypeAndInflateLayout(inflater, container);
        return mainView;
    }

    private void loadUserTypeAndInflateLayout(LayoutInflater inflater, ViewGroup container) {
        if (userId == -1 || getContext() == null) return;
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getContext().getApplicationContext());
            String type = db.userDao().findById(userId).userType;
            this.userType = type;
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    View correctView;
                    if ("employer".equals(type)) {
                        correctView = inflater.inflate(R.layout.fragment_profile_employer, container, false);
                    } else {
                        correctView = inflater.inflate(R.layout.fragment_profile_employee, container, false);
                    }
                    ViewGroup parent = (ViewGroup) mainView;
                    parent.removeAllViews();
                    parent.addView(correctView);
                    onRealViewCreated(correctView);
                });
            }
        }).start();
    }

    private void onRealViewCreated(View view) {
        initializeViews(view);
        loadUserData();

        Button editButton = view.findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(getContext(), "User data not loaded yet.", Toast.LENGTH_SHORT).show();
                return;
            }
            Fragment editFragment;
            Bundle args = getCurrentDataAsBundle();
            args.putInt("userId", userId);

            if ("employer".equals(currentUser.userType)) {
                editFragment = new EditEmployerProfileFragment();
            } else {
                editFragment = new EditEmployeeProfileFragment();
            }
            editFragment.setArguments(args);

            if (getActivity() != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // 使用在临时 activity_app.xml 中定义的容器 ID
                fragmentTransaction.replace(R.id.temporary_container, editFragment);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void loadUserData() {
        if (userId == -1 || getContext() == null) return;
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getContext().getApplicationContext());
            currentUser = db.userDao().findById(userId);

            if (currentUser != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if ("employer".equals(currentUser.userType)) {
                        updateEmployerProfileUI(currentUser);
                    } else {
                        updateEmployeeProfileUI(currentUser);
                    }
                });
            }
        }).start();
    }

    private void initializeViews(View view) {
        if ("employer".equals(userType)) {
            textViewCompanyName = view.findViewById(R.id.textViewCompanyName);
            textViewCompanyEmail = view.findViewById(R.id.textViewCompanyEmail);
            textViewLocation = view.findViewById(R.id.textViewLocation);
            textViewAboutCompany = view.findViewById(R.id.textViewAboutCompany);
            textViewCompanySize = view.findViewById(R.id.textViewCompanySize);
            textViewIndustry = view.findViewById(R.id.textViewIndustry);
            cardAbout = view.findViewById(R.id.card_about_company);
            cardSize = view.findViewById(R.id.card_company_size);
            cardIndustry = view.findViewById(R.id.card_industry);
        } else {
            textViewName = view.findViewById(R.id.textViewName);
            textViewEmail = view.findViewById(R.id.textViewEmail);
            textViewPhone = view.findViewById(R.id.textViewPhone);
            textViewWorkingExperience = view.findViewById(R.id.textViewWorkingExperience);
            textViewSkills = view.findViewById(R.id.textViewSkills);
            textViewLanguage = view.findViewById(R.id.textViewLanguage);
            cardExperience = view.findViewById(R.id.card_working_experience);
            cardSkills = view.findViewById(R.id.card_skills);
            cardLanguage = view.findViewById(R.id.card_language);
        }
    }

    private void updateEmployeeProfileUI(User user) {
        if (user.fullName != null) textViewName.setText(user.fullName);
        if (user.email != null) textViewEmail.setText(user.email);
        if (user.phone != null) textViewPhone.setText(user.phone);

        updateCardVisibility(cardExperience, textViewWorkingExperience, user.workingExperience);
        updateCardVisibility(cardSkills, textViewSkills, user.skills);
        updateCardVisibility(cardLanguage, textViewLanguage, user.language);
    }

    private void updateEmployerProfileUI(User user) {
        if (user.companyName != null) textViewCompanyName.setText(user.companyName);
        if (user.email != null) textViewCompanyEmail.setText(user.email);
        if (user.location != null) textViewLocation.setText(user.location);

        updateCardVisibility(cardAbout, textViewAboutCompany, user.aboutCompany);
        updateCardVisibility(cardSize, textViewCompanySize, user.companySize);
        updateCardVisibility(cardIndustry, textViewIndustry, user.industry);
    }

    private void updateCardVisibility(MaterialCardView card, TextView textView, String text) {
        if (card == null || textView == null) return;
        card.setVisibility(View.VISIBLE);
        if (text != null && !text.trim().isEmpty()) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private Bundle getCurrentDataAsBundle() {
        Bundle bundle = new Bundle();
        if (currentUser == null) return bundle;

        if ("employer".equals(currentUser.userType)) {
            bundle.putString("name", currentUser.companyName);
            bundle.putString("about", currentUser.aboutCompany);
            bundle.putString("size", currentUser.companySize);
            bundle.putString("industry", currentUser.industry);
            bundle.putString("email", currentUser.email);
            bundle.putString("location", currentUser.location);
        } else {
            bundle.putString("name", currentUser.fullName);
            bundle.putString("experience", currentUser.workingExperience);
            bundle.putString("skills", currentUser.skills);
            bundle.putString("language", currentUser.language);
            bundle.putString("email", currentUser.email);
            bundle.putString("phone", currentUser.phone);
        }
        return bundle;
    }
}









