package com.example.campus_market.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campus_market.R;

public class EditProfileFragment extends Fragment {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageView profileImageView;
    private ImageView backButton; // Reference to the back button (arrow icon)

    String imageUpdate;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(String name, String bio) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("bio", bio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the bottom navigation bar when editing the profile
        if (getActivity() != null) {
            // Assuming your activity is hosting the bottom navigation bar in a View with the id bottom_navigation
            View bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);
            }
        }

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        imageUpdate=selectedImageUri.toString();

                        // Update the ImageView in the current fragment
                        profileImageView.setImageURI(selectedImageUri);

                        // Pass the selected image URI back to ProfileFragment
                        Bundle bundle = new Bundle();
                        bundle.putString("profile_image_uri", selectedImageUri.toString());
                        requireActivity().getSupportFragmentManager().setFragmentResult("profile_image_update", bundle);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        EditText nameEditText = view.findViewById(R.id.edit_name);
        EditText bioEditText=view.findViewById(R.id.edit_bio);
        profileImageView = view.findViewById(R.id.edit_profile_image);

        // Pre-fill fields if data is passed
        if (getArguments() != null) {
            String bio = getArguments().getString("bio", "");
            String name = getArguments().getString("name", "");
            String imageUriString = getArguments().getString("profile_image_uri");

            bioEditText.setText(bio);
            nameEditText.setText(name);

            if (imageUriString != null && profileImageView != null) {
                profileImageView.setImageURI(Uri.parse(imageUriString));
            }
        }

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            // Handle save button click
            String updatedName = nameEditText.getText().toString().trim();
            String updatedBio = bioEditText.getText().toString().trim();

            // TODO: Save updated data (e.g., database, shared preferences, or API call)
            // Pass updated data back to ProfileFragment
            // Check if the name is empty
            if (updatedName.isEmpty()) {
                // Show error message (e.g., Toast or set error on EditText)
                nameEditText.setError("Name cannot be empty");  // This shows an error on the name field
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return; // Prevent save if name is empty
            }

            //Pass updated data back to ProfileFragment
            Bundle result = new Bundle();
            result.putString("updated_name", updatedName);
            result.putString("updated_bio", updatedBio);
            result.putString("profile_image_uri", imageUpdate);  // Make sure this key is the same as in listener
            requireActivity().getSupportFragmentManager().setFragmentResult("profile_update", result);

            // Optionally, you can show a Toast to confirm that changes have been saved
            Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
            // Navigate back to the previous fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        // Initialize back button (arrow icon)
        backButton = view.findViewById(R.id.arrow_icon);
        backButton.setOnClickListener(v -> {
            // Show the bottom navigation bar when returning to ProfileFragment
            if (getActivity() != null) {
                View bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                if (bottomNavigationView != null) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }

            // Navigate back to the ProfileFragment when the back button is clicked
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        Button changeProfileButton = view.findViewById(R.id.change_photo_button);
        changeProfileButton.setOnClickListener(v -> showProfilePictureOptions());
        // Rest of the view initialization...

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Show the bottom navigation bar when exiting the EditProfileFragment (if not already done)
        if (getActivity() != null) {
            View bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
    }

    // Show options for changing or removing profile picture
    private void showProfilePictureOptions() {
        // Create a dialog with two options: Remove Profile or Open Photo Library
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Profile Picture")
                .setItems(new CharSequence[]{"Remove Profile Picture", "Change to New Picture"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            removeProfilePicture(); // Remove the current profile picture
                        } else if (which == 1) {
                            openGallery(); // Open the gallery to pick a new picture
                        }
                    }
                })
                .create()
                .show();
    }

    private void removeProfilePicture() {
        profileImageView.setImageResource(R.drawable.profile); // Set to default picture
    }

    // Open the photo gallery to pick a new profile picture
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);

    }
}

