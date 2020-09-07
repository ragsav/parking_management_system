package com.example.rent_it.Interfaces;

import com.example.rent_it.Models.Transaction;

import java.util.List;

public interface OnUserTransactionsRetrieved {
    void onUserTransactionRetrieveSuccess(List<Transaction> list_transaction);
    void onUserTransactionRetrieveFailure();
}
