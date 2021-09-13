package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements  AccountDao{

        private JdbcTemplate jdbcTemplate;

        public JdbcAccountDao(DataSource dx){
            this.jdbcTemplate = new JdbcTemplate(dx);
        }

        @Override
        public Account getBalance(String username) {
            String sql = "SELECT balance FROM accounts a " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "WHERE username = ? ";
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,username);
            Account balance = new Account();
            if(result.next()) {
                BigDecimal accountBalance = result.getBigDecimal("balance");
                balance.setBalance(accountBalance);
            }
            return balance;
        }

//    @Override
//    public void subtractBalance(Transfer transfer) {
//
//    }

//    @Override
//    public void addBalance(Transfer transfer) {
//
//    }

        @Override
        public Account findAccountByAccId(int accountId) {
            String sql = "SELECT * from accounts WHERE account_id = ? ";
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,accountId);

            Account account = new Account();
            if(result.next()){
                account = mapRowToAccount(result);
            }
            return account;
        }

        @Override
        public int findAccId(int userId) {
            String sql = "SELECT account_id FROM users WHERE user_id = ?;";
            int id = jdbcTemplate.queryForObject(sql, Integer.class,userId);
            //Integer or int
            return id;
        }

        @Override
        public Account findAccountByUserId(int userId) {
            Account account = new Account();
            String sql = "SELECT user_id, account_id, balance FROM accounts WHERE user_id = ? ";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId);
            while(results.next()){
                account = mapRowToAccount(results);
            }
            return account;
        }

        private Account mapRowToAccount (SqlRowSet results){
            Account account = new Account();
            account.setAccountId(results.getInt("account_id"));
            account.setUserId(results.getInt("user_id"));
            account.setBalance(results.getBigDecimal("balance"));
            return account;
        }
    }

