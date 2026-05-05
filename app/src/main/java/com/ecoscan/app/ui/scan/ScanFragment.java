package com.ecoscan.app.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ecoscan.app.R;
import com.ecoscan.app.api.ApiService;
import com.ecoscan.app.api.RetrofitClient;
import com.ecoscan.app.ui.product.AddProductActivity;
import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanFragment extends Fragment {
    JSONObject item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnManual = view.findViewById(R.id.btn_manual_entry);
        if (btnManual != null) {
            btnManual.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), AddProductActivity.class);
                intent.putExtra("data", item.toString()); // pass as String
                startActivity(intent);
            });
        }

        int checkPermission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            // Permission already granted — start camera immediately
            PreviewView previewView = view.findViewById(R.id.camera_preview);
            startCamera(previewView);
        }


        String barcode = "5449000221780";
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getProduct(barcode).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String result = response.body().string();
                        item= new JSONObject(result);

//                        Log.d("RESPONSE", json.toString(4));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR",t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int checkPermission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            View view = getView();
            if (view != null) {
                PreviewView previewView = view.findViewById(R.id.camera_preview);
                startCamera(previewView);
            }
        }

    }
    private void startCamera(PreviewView previewView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission just granted — now start camera
                View view = getView();
                if (view != null) {
                    PreviewView previewView = view.findViewById(R.id.camera_preview);
                    startCamera(previewView);
                }
            } else {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_LONG).show();
            }
        }
    }
}