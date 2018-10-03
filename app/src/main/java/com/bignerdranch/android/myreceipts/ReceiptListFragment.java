package com.bignerdranch.android.myreceipts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ReceiptListFragment extends Fragment {
    private RecyclerView mReceiptRecyclerView;
    private ReceiptAdapter mAdapter;

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
        List<Receipt> receipts = crimeLab.getCrimes();
        mAdapter = new ReceiptAdapter(receipts);
        mReceiptRecyclerView.setAdapter(mAdapter);
    }

    private class ReceiptHolder extends RecyclerView.ViewHolder {
        public ReceiptHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_receipt, parent, false));
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

        }

        @Override
        public int getItemCount() {
            return mReceipts.size();
        }
    }
}
