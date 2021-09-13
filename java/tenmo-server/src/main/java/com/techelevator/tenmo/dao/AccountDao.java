package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {


        Account getBalance (String username);
        //void subtractBalance (Transfer transfer);
        //void addBalance (Transfer transfer);
        Account findAccountByAccId (int accountId);
        int findAccId (int userId);
        Account findAccountByUserId (int userId);

    }
