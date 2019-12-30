package com.example.criminalintent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.criminalintent.mode.PictureUtils;

import java.io.File;

public class ImageViewDialogFragment extends DialogFragment {

    private static final String ARG_FILE = "imageFile";

    private File imageFile;

    public static ImageViewDialogFragment newInstance(File imageFile) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_FILE, imageFile);

        ImageViewDialogFragment fragment = new ImageViewDialogFragment();
        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageFile = (File) getArguments().getSerializable(ARG_FILE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.dialog_image, container, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.dialog_imageView);

        Bitmap bitmap = PictureUtils.getScaledBitmap(imageFile.getPath(), getActivity());
        imageView.setImageBitmap(bitmap);

        return v;

    }
}
