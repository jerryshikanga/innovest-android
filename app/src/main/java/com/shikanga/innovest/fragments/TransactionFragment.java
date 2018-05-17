package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shikanga.innovest.R;
import com.shikanga.innovest.models.Account;
import com.shikanga.innovest.services.AccountService;
import com.shikanga.innovest.services.UserService;

public class TransactionFragment extends Fragment {
    private View view;
    private EditText depositView, withdrawView;
    private TextView accountName, accountBalance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transactions, container, false);
        depositView = view.findViewById(R.id.deposit_amount);
        withdrawView = view.findViewById(R.id.withdraw_amount);
        accountName = view.findViewById(R.id.account_name);
        accountBalance = view.findViewById(R.id.account_balance);

        accountName.setText(UserService.getLoggedInUser(getActivity()).getFullName());

        depositView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL){
                    attemptDeposit();
                    return true;
                }
                return false;
            }
        });

        withdrawView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL){
                    attemptWithdrawal();
                    return true;
                }
                return false;
            }
        });

        AccountTask accountTask = new AccountTask(UserService.getLoggedInUser(getActivity()).getId());
        accountTask.execute();

        return view;
    }

    private Boolean attemptWithdrawal()
    {
        withdrawView.setError(null);
        int amount = Integer.parseInt(withdrawView.getText().toString());
        if (!TextUtils.isDigitsOnly(withdrawView.getText())){
            withdrawView.setError(getString(R.string.error_amount_invalid));
            withdrawView.requestFocus();
            return false;
        }
        if (!amountIsValid(amount)){
            withdrawView.setError(getString(R.string.error_amount_invalid));
            withdrawView.requestFocus();
            return false;
        }
        AccountWithdrawTask withdrawTask = new AccountWithdrawTask(amount);
        withdrawTask.execute();
        return true;
    }

    private Boolean attemptDeposit(){
        depositView.setError(null);
        int amount = Integer.parseInt(depositView.getText().toString());
        if (!TextUtils.isDigitsOnly(depositView.getText())){
            depositView.setError(getString(R.string.error_amount_invalid));
            depositView.requestFocus();
            return false;
        }
        if (!amountIsValid(amount)){
            depositView.setError(getString(R.string.error_amount_invalid));
            depositView.requestFocus();
            return false;
        }
        AccountDepositTask depositTask = new AccountDepositTask(amount);
        depositTask.execute();
        return true;
    }

    private Boolean amountIsValid(int amount){
        return amount>0;
    }


    private  class AccountTask extends AsyncTask<Void, Void, Account>{
        private int accountId;

        public AccountTask(int accountId) {
            this.accountId = accountId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Account doInBackground(Void... voids) {
            return AccountService.getAccount(this.accountId, getActivity());
        }

        @Override
        protected void onPostExecute(Account account) {
            accountBalance.append(String.valueOf(account.getBalance())); ;
            super.onPostExecute(account);
        }
    }

    private class AccountWithdrawTask extends AsyncTask<Void, Void, Boolean>{
        private  int amount;

        public AccountWithdrawTask(int amount) {
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return AccountService.requestWithdraw(this.amount, getActivity());
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success){
                Toast.makeText(getActivity(), R.string.message_withdrawal_request_successful, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), R.string.message_withdrawal_request_unsuccessful, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AccountDepositTask extends AsyncTask<Void, Void, Boolean>{
        private int amount;

        public AccountDepositTask(int amount) {
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return AccountService.requestDeposit(this.amount, getActivity());
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success){
                Toast.makeText(getActivity(), R.string.message_deposit_request_successfull, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), R.string.message_deposit_request_unsuccessful, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
