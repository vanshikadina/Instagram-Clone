package com.example.campus_market.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campus_market.R;
import com.example.campus_market.adapter.ViewPagerAdapter;
import com.example.campus_market.databinding.ActivityMainBinding;
import com.example.campus_market.databinding.FragmentProfileBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ViewPagerAdapter viewPagerAdapter;

    private String imageUriString;
    private String updatedName;
    private String updatedBio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the adapter
        viewPagerAdapter = new ViewPagerAdapter(this); // Pass the Fragment to the adapter

        // Add fragments to the adapter
        viewPagerAdapter.addFragment(new MyPostFragment(), "Posts");
        viewPagerAdapter.addFragment(new MyLikesFragment(), "Likes");

        // Set up ViewPager2 and TabLayout
        binding.viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(viewPagerAdapter.getPageTitle(position));
        }).attach();

        requireActivity().getSupportFragmentManager().setFragmentResultListener(
                "profile_update", this, (requestKey, result) -> {
                    updatedName = result.getString("updated_name");
                    updatedBio = result.getString("updated_bio");

                    // Update UI with new values
                    binding.name.setText(updatedName);
                    binding.bio.setText(updatedBio);

                    imageUriString = result.getString("profile_image_uri");
                    if (imageUriString != null) {
                        Uri imageUri = Uri.parse(imageUriString);

                        // Update the profile photo in the ImageView
                        binding.profileImage.setImageURI(imageUri);
                    }
                }
        );

        binding.stuBtn.setOnClickListener(v -> {

            // Hide the BottomNavigationView
            View navBar = requireActivity().findViewById(R.id.nav_view);
            if (navBar != null) {
                navBar.setVisibility(View.GONE);
            }

            Bundle args=new Bundle();
            args.putString("profile_image_uri",imageUriString);
            args.putString("name",updatedName);
            args.putString("bio",updatedBio);

            EditProfileFragment editProfileFragment = EditProfileFragment.newInstance(
                    binding.name.getText().toString(),
                    binding.bio.getText().toString()
            );
            editProfileFragment.setArguments(args);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            // Remove child fragments (if necessary)
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment instanceof MyPostFragment || fragment instanceof MyLikesFragment) {
                    fragmentManager.beginTransaction().remove(fragment).commitNow();
                }
            }



            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_home, editProfileFragment)
                    .addToBackStack("EditProfileFragment")
                    .commit();
        });

    }
}