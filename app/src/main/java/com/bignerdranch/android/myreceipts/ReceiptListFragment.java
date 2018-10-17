package com.bignerdranch.android.myreceipts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ReceiptListFragment extends Fragment {
    private RecyclerView mReceiptRecyclerView;
    private ReceiptAdapter mAdapter;
    private WebView mWebView;

    public final static String NEW_RECEIPT ="com.bignerdranch.android.myreceipts.new_receipt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt_list, container, false);
        mReceiptRecyclerView = (RecyclerView) view
                .findViewById(R.id.receipt_recycler_view);
        mReceiptRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        ReceiptLab crimeLab = ReceiptLab.get(getActivity());
        List<Receipt> receipts = crimeLab.getReceipts();
        if (mAdapter == null) {
            mAdapter = new ReceiptAdapter(receipts);
            mReceiptRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
            mAdapter.setReceipts(receipts);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_receipt:
                Receipt receipt = new Receipt();
                ReceiptLab.get(getActivity()).addReceipts(receipt);
                Intent intent = ReceiptActivity
                        .newIntent(getActivity(), receipt.getId());
                startActivity(intent);
                return true;
            case R.id.help:
                Intent i = new Intent(getActivity(), HelpWebPage.class);
                startActivity(i);

                //Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Receipt"));
                //startActivity(browserIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ReceiptHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Receipt mReceipt;
        private TextView mTitleTextView;
        private TextView mShopTextView;
        private TextView mDateTextView;

        public ReceiptHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_receipt, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.receipt_title);
            mShopTextView = (TextView) itemView.findViewById(R.id.receipt_shop);
            mDateTextView = (TextView) itemView.findViewById(R.id.receipt_date);
        }
        public void bind(Receipt receipt) {
            mReceipt = receipt;
            mTitleTextView.setText(mReceipt.getTitle());
            mShopTextView.setText(mReceipt.getShop());
            mDateTextView.setText(mReceipt.getDate().toString());
        }
        @Override
        public void onClick(View view) {
            Intent intent = ReceiptActivity.newIntent(getActivity(), mReceipt.getId());
            intent.putExtra(NEW_RECEIPT, "FALSE");
            startActivity(intent);
        }
    }
    private class ReceiptAdapter extends RecyclerView.Adapter<ReceiptHolder> {
        private List<Receipt> mReceipts;
        public ReceiptAdapter(List<Receipt> receipts) {
            mReceipts = receipts;
        }

        @Override
        public ReceiptHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ReceiptHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ReceiptHolder holder, int position) {
            Receipt receipt = mReceipts.get(position);
            holder.bind(receipt);
        }

        @Override
        public int getItemCount() {
            return mReceipts.size();
        }

        public void setReceipts(List<Receipt> receipts) {
            mReceipts = receipts;
        }
    }
}
