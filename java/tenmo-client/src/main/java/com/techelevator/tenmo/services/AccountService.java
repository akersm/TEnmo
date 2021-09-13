package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;


public class AccountService {
    private RestTemplate restTemplate = new RestTemplate();
    private String BASE_URL;

    public AccountService(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public BigDecimal getBalance(String authentication) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authentication);
        HttpEntity entity = new HttpEntity(httpHeaders);

        BigDecimal balance = restTemplate.exchange("http://localhost:8080/balance",
                HttpMethod.GET, entity, BigDecimal.class).getBody();

        return balance;
    }

    public List<String> findUsers(String authentication) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authentication);
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);

        List<String> users = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, entity, List.class).getBody();

        //System.out.println(u + "\n");
        return users;
    }

    public Transfer sendBucks(String newTransfer, String authentication) {
        Transfer transfer = makeTransfer(newTransfer);
        if (transfer == null) {
            System.out.println("Invalid transfer information");;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        try {
            restTemplate.postForObject(BASE_URL + "/transfer/send", entity, String.class);
            System.out.println("Approved");
            //??message to let them know it's sent
        } catch (RestClientResponseException ex) {

            System.out.println("Invalid entry. Please try again");;
        }

        return transfer;
    }

    private Transfer makeTransfer (String csv){
        String[] parsed = csv.split(",");
        if (parsed.length < 3 || parsed.length > 4) {
            return null;
        }
        return new Transfer(
                2,
                2,
                ((Integer.parseInt(parsed[0].trim())) + 1000),
                ((Integer.parseInt(parsed[1].trim()) + 1000)),
                new BigDecimal(parsed[2].trim())
        );
    }

}
