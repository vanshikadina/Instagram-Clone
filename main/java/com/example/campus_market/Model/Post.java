package com.example.campus_market.Model;
import android.net.Uri;
public class Post {
    private Uri imageUri;

    public Post(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}

