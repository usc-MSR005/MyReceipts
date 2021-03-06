package com.bignerdranch.android.myreceipts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class ReceiptFragment extends Fragment {

    private static final String ARG_RECEIPT_ID = "receipt_id";
    private static final String DIALOG_DATE = "DialogDate";

    public final static String LATITUDE ="com.bignerdranch.android.myreceipts.latitude";
    public final static String LONGITUDE ="com.bignerdranch.android.myreceipts.longitude";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO= 2;

    private Receipt mReceipt;
    private File mPhotoFile;
    private EditText mTitleField;
    private EditText mShopField;
    private Button mDateButton;
    private EditText mCommentField;
    private Button mDeleteButton;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mShowMapButton;
    private TextView mLatitude;
    private TextView mLongitude;
    private GoogleApiClient mClient;

    public static ReceiptFragment newInstance(UUID receiptId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECEIPT_ID, receiptId);
        ReceiptFragment fragment = new ReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID receiptId = (UUID) getArguments().getSerializable(ARG_RECEIPT_ID);
        mReceipt = ReceiptLab.get(getActivity()).getReceipt(receiptId);
        mPhotoFile = ReceiptLab.get(getActivity()).getPhotoFile(mReceipt);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    public void onPause() {
        super.onPause();
        ReceiptLab.get(getActivity())
                .updateReceipt(mReceipt);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_receipt, container, false);

        mTitleField = (EditText) v.findViewById(R.id.receipt_title);
        mTitleField.setText(mReceipt.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
                    // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence c, int start, int before, int count) {
                mReceipt.setTitle(c.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mShopField = (EditText) v.findViewById(R.id.receipt_shop);
        mShopField.setText(mReceipt.getShop());
        mShopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mReceipt.setShop(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mShowMapButton = (Button) v.findViewById(R.id.show_map);
        mShowMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra(LATITUDE, String.valueOf(mReceipt.getLat()));
                intent.putExtra(LONGITUDE, String.valueOf(mReceipt.getLong()));
                startActivity(intent);
            }
        });

        mDateButton = (Button) v.findViewById(R.id.receipt_date);
        mDateButton.setText(mReceipt.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mReceipt.getDate());
                dialog.setTargetFragment(ReceiptFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mCommentField = (EditText) v.findViewById(R.id.receipt_comment);
        mCommentField.setText(mReceipt.getComment());
        mCommentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mReceipt.setComment(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mDeleteButton = (Button) v.findViewById(R.id.receipt_delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiptLab.get(getActivity()).deleteReceipt(mReceipt);
                getActivity().finish();
            }
        });
        mReportButton = (Button) v.findViewById(R.id.receipt_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getReceiptReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.receipt_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.receipt_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null;
                //captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.myreceipts.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.receipt_photo);
        updatePhotoView();

        mLatitude = (TextView) v.findViewById(R.id.receipt_latitude);
        mLongitude = (TextView) v.findViewById(R.id.location_longitude);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LocationRequest request = LocationRequest.create();
                        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        request.setNumUpdates(1);
                        request.setInterval(0);

                        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationServices.FusedLocationApi
                                .requestLocationUpdates(mClient, request, new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        Log.i(TAG, "Got a fix: " + location);

                                        if(mReceipt.getLat() == 0.0 && mReceipt.getLong() == 0.0) {
                                            mReceipt.setLat(location.getLatitude());
                                            mReceipt.setLong(location.getLongitude());

                                            mLatitude.setText("Latitude: " + location.getLatitude());
                                            mLongitude.setText("Longitude: " + location.getLongitude());
                                        }

                                        Log.i(TAG, String.valueOf(location.getLatitude()));
                                        Log.i(TAG, String.valueOf(location.getLongitude()));
                                        Log.i(TAG, String.valueOf(mReceipt.getLat()));
                                        Log.i(TAG, String.valueOf(mReceipt.getLong()));
                                    }

                                });

                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();

        if(mReceipt.getLat() == 0.0 && mReceipt.getLong() == 0.0){

            mDeleteButton.setVisibility(View.INVISIBLE);

        } else {

            mLatitude.setText("Latitude: " + String.valueOf(mReceipt.getLat()));
            mLongitude.setText("Longitude: " + String.valueOf(mReceipt.getLong()));

        }
        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mReceipt.setDate(date);
            mDateButton.setText(mReceipt.getDate().toString());
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.myreceipt.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    private String getReceiptReport() {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mReceipt.getDate()).toString();

        String report = getString(R.string.crime_report,
                mReceipt.getTitle(), mReceipt.getShop(), dateString, mReceipt.getComment());
        return report;
    }
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
