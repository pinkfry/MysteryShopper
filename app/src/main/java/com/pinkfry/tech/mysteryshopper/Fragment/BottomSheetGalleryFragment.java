package com.pinkfry.tech.mysteryshopper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pinkfry.tech.mysteryshopper.Activity.UploadImagesActivity;
import com.pinkfry.tech.mysteryshopper.R;

public class BottomSheetGalleryFragment extends BottomSheetDialogFragment {
    Button btnUploadImage, btnGallery;
    String clientName, storeName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadImagesActivity.class);
                intent.putExtra("clientName", clientName);
                intent.putExtra("storeName", storeName);
                startActivity(intent);
                dismiss();
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
