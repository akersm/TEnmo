package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    @Override
    public String sendTransfer(Transfer transfer) {
        //check to see if there is money first
        String balanceCheck = "SELECT balance FROM accounts WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(balanceCheck,transfer.getAccount_from());
        BigDecimal currentBalance = BigDecimal.ZERO;
        if (result.next()){
            currentBalance = result.getBigDecimal("balance");
        }


        if (currentBalance.subtract(transfer.getAmount()).compareTo(BigDecimal.ZERO) >= 0) {
            //create a transfer
            String sql = "INSERT into transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?,?,?,?,?)";
            jdbcTemplate.update(sql, transfer.getTransfer_type_id(), transfer.getTransfer_status_id(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());

            //deduct from sender
            String sqlDeduct = "UPDATE accounts " +
                    "SET balance = balance - ? " + //maybe do subquery to get balance
                    "WHERE account_id = ? ";
            jdbcTemplate.update(sqlDeduct, transfer.getAmount(), transfer.getAccount_from());

            //add to receiver
            String sqlAdd = "UPDATE accounts " +
                    "SET balance = balance + ? " + //maybe do subquery to get balance
                    "WHERE account_id = ? ";
            jdbcTemplate.update(sqlAdd, transfer.getAmount(), transfer.getAccount_to());

            return "Transaction complete";
        } else {
            return "Insufficient funds";
        }
    }

//    @Override
//    public void deductTransfer(Transfer transfer){
//    //deduct from sender
//    String sqlDeduct = "UPDATE accounts " +
//            "SET balance = balance - ? " + //maybe do subquery to get balance
//            "WHERE account_id = ? ";
//        jdbcTemplate.update(sqlDeduct,transfer.getAmount(),transfer.getAccount_from());
//}
//
//    @Override
//    public void sendTransferFunds(Transfer transfer){
//                //add to receiver
//        String sqlAdd = "UPDATE accounts " +
//                "SET balance = balance + ? " + //maybe do subquery to get balance
//                "WHERE account_id = ? ";
//        jdbcTemplate.update(sqlAdd, transfer.getAmount() , transfer.getAccount_to());
//    }


    @Override
    public List<Transfer> viewAllTransfers(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM transfers t " +
                "JOIN accounts a ON t.account_from = a.account_id " +
                "JOIN users u ON a.user_id = u.user_id " +
                "WHERE username = ? ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);

        while (results.next()) {
            Transfer transfer = mapRowToTransfers(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer findTransferById(long transfer_id) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfers " +
                "WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transfer_id);
        while (result.next()) {
            transfer = mapRowToTransfers(result);
        }
        return transfer;
    }

    private Transfer mapRowToTransfers(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_type_id(results.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(results.getInt("transfer_status_id"));
        transfer.setAccount_from(results.getInt("account_from"));
        transfer.setAccount_to(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}


