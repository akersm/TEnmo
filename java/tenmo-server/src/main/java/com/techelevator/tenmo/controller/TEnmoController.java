package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TEnmoController {

        @Autowired
        private UserDao userDao;

        @Autowired
        private AccountDao accountDao;

        @Autowired
        private TransferDao transferDao;

        @RequestMapping(path = "/balance", method = RequestMethod.GET)
        public BigDecimal getBalance(Principal principal){
            return accountDao.getBalance(principal.getName()).getBalance();
        }

        @RequestMapping(path = "/users", method = RequestMethod.GET)
        public List<String> listAllUsers(){
                List<User> input = userDao.findAll();
                List<String> users = new ArrayList<>();
                String str = "";
                for (User u : input) {
                        str = u.getId() + "      " + u.getUsername();

                        users.add(str);
                        // }

                }
                return users;
        }

        @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
        public String transferMoney(@RequestBody Transfer transfer){
                return transferDao.sendTransfer(transfer);
//                transferDao.deductTransfer(transfer);
//                transferDao.sendTransferFunds(transfer);
        }

        @RequestMapping(path = "/transfers/viewAll", method = RequestMethod.GET)
        public List<Transfer> viewAllTransfers (Principal principal){
            return transferDao.viewAllTransfers(principal.getName());//get by username?
        }

        @RequestMapping(path = "/transfers/{id}",method = RequestMethod.GET)
        public Transfer viewTransferById (@PathVariable("id") long transfer_id){
            return transferDao.findTransferById(transfer_id);
        }
    }

