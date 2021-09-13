package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferService {
    private RestTemplate restTemplate = new RestTemplate();
    private String BASE_URL;

    public TransferService(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }


    public List<Transfer> viewAllTransfers(String authentication) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authentication);
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);

        List<Transfer> transfers = null;
        transfers = restTemplate.exchange(BASE_URL + "/transfers/viewAll", HttpMethod.GET, entity, List.class).getBody();

        return transfers;
    }


    public String viewTransferById (long transfer_id, String authentication){
        //long transfer_id = Long.parseLong(input);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authentication);
        HttpEntity<Long> entity = new HttpEntity<>(transfer_id,httpHeaders);

        Transfer transfer = null;
        transfer = restTemplate.exchange(BASE_URL + "/transfers/" + transfer_id, HttpMethod.GET, entity, Transfer.class).getBody();

        String result = "Transfer status:" + transfer.getTransfer_status_id() + "\n Transfer type:" + transfer.getTransfer_type_id() + "\n Account From:" + transfer.getAccount_from() +
                "\n Account To:" + transfer.getAccount_to() + "\n Amount:" + transfer.getAmount();
        return result;
    }
}
