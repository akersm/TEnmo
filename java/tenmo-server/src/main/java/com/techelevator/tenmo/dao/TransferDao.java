package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    String sendTransfer (Transfer transfer);
    List<Transfer> viewAllTransfers (String username);
    Transfer findTransferById (long transfer_id);
//    void deductTransfer(Transfer transfer);
//    void sendTransferFunds(Transfer transfer);
}
