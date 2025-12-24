package com.example.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.example.madproject.AppDatabase;
import com.example.madproject.R;
import com.example.madproject.User;
import com.google.android.material.card.MaterialCardView;

public class ProfileFragment extends Fragment {

    private String userEmail;
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
        // 1. Try arguments first
        if (getArguments() != null) {
            userEmail = getArguments().getString("userEmail", null);
        }

        // 2. Fallback to activity intent
        if (userEmail == null && getActivity() != null) {
            userEmail = getActivity().getIntent().getStringExtra("userEmail");
        }

        // Listen for updates from edit fragments
        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            if (userEmail != null && getContext() != null) {
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getDatabase(getContext().getApplicationContext());
                    User user = db.userDao().findByEmail(userEmail);
                    currentUser = user;
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            updateProfileUI(user);
                            checkIncompleteProfile();
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Start with loading layout
        mainView = inflater.inflate(R.layout.fragment_loading, container, false);

        // Load user type and inflate profile layout
        loadUserTypeAndInflateLayout(inflater, container);

        return mainView;
    }

    private void loadUserTypeAndInflateLayout(LayoutInflater inflater, ViewGroup container) {
        if (userEmail == null || getContext() == null) return;

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getContext().getApplicationContext());
            User user = db.userDao().findByEmail(userEmail);

            if (user == null) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "User not found. Please log in again.", Toast.LENGTH_LONG).show()
                    );
                }
                return;
            }

            currentUser = user;
            userType = user.userType;

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    View correctView;
                    if ("employer".equals(userType)) {
                        correctView = inflater.inflate(R.layout.fragment_profile_employer, container, false);
                    } else {
                        correctView = inflater.inflate(R.layout.fragment_profile_employee, container, false);
                    }

                    // Replace loading layout with correct layout
                    if (mainView instanceof ViewGroup) {
                        ViewGroup parent = (ViewGroup) mainView;
                        parent.removeAllViews();
                        parent.addView(correctView);
                    }

                    onRealViewCreated(correctView, user);
                });
            }
        }).start();
    }


    private void onRealViewCreated(View view, User user) {
        initializeViews(view);

        currentUser = user;
        updateProfileUI(user);
        checkIncompleteProfile();

        // Edit button
        Button editButton = view.findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(v -> {
            if (currentUser == null) return;

            Bundle args = getCurrentDataAsBundle();
            args.putString("userEmail", userEmail);

            if ("employer".equals(currentUser.userType)) {
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profile_to_edit_employer, args);
            } else {
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profile_to_edit_employee, args);
            }
        });
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

    private void updateProfileUI(User user) {
        if (user == null) return;

        if ("employer".equals(userType)) {
            // Company info
            textViewCompanyName.setText(!isEmpty(user.companyName) ? user.companyName : "Not provided yet");
            textViewCompanyEmail.setText(!isEmpty(user.email) ? user.email : "Not provided yet");
            textViewLocation.setText(!isEmpty(user.location) ? user.location : "Not provided yet");

            // Company details cards
            textViewAboutCompany.setText(!isEmpty(user.aboutCompany) ? user.aboutCompany : "Not provided yet");
            textViewCompanySize.setText(!isEmpty(user.companySize) ? user.companySize : "Not provided yet");
            textViewIndustry.setText(!isEmpty(user.industry) ? user.industry : "Not provided yet");

            // Always show cards
            cardAbout.setVisibility(View.VISIBLE);
            cardSize.setVisibility(View.VISIBLE);
            cardIndustry.setVisibility(View.VISIBLE);

        } else {
            // Personal info
            textViewName.setText(!isEmpty(user.fullName) ? user.fullName : "Not provided yet");
            textViewEmail.setText(!isEmpty(user.email) ? user.email : "Not provided yet");
            textViewPhone.setText(!isEmpty(user.phone) ? user.phone : "Not provided yet");

            // Employee cards
            textViewWorkingExperience.setText(!isEmpty(user.workingExperience) ? user.workingExperience : "Not provided yet");
            textViewSkills.setText(!isEmpty(user.skills) ? user.skills : "Not provided yet");
            textViewLanguage.setText(!isEmpty(user.language) ? user.language : "Not provided yet");

            // Always show cards
            cardExperience.setVisibility(View.VISIBLE);
            cardSkills.setVisibility(View.VISIBLE);
            cardLanguage.setVisibility(View.VISIBLE);
        }
    }

    private void checkIncompleteProfile() {
        boolean incomplete = false;

        if ("employer".equals(currentUser.userType)) {
            incomplete = isEmpty(currentUser.companyName) ||
                    isEmpty(currentUser.aboutCompany) ||
                    isEmpty(currentUser.companySize) ||
                    isEmpty(currentUser.industry);
        } else {
            incomplete = isEmpty(currentUser.fullName) ||
                    isEmpty(currentUser.workingExperience) ||
                    isEmpty(currentUser.skills) ||
                    isEmpty(currentUser.language);
        }

        if (incomplete) {
            Toast.makeText(getContext(),
                    "Your profile is incomplete. Tap Edit to update your details.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
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





