package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.endpoints.Endpoint;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionsService {

    Endpoint endpoint;
    //String apiURL = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";
    @Autowired
    public UserServices userServices;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userServices.findUserById(transaction.senderId());
        User receiver = this.userServices.findUserById(transaction.receiverId());

        userServices.validateTransaction(sender, transaction.value());

        boolean isAutorized = this.autorizeTransaction(sender, transaction.value());

        if(!isAutorized){
            throw new Exception("Transação não autorizada");
        }
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTime_stamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userServices.saveUser(sender);
        this.userServices.saveUser(receiver);

        this.notificationService.sendNotifications(sender, " transação realizada com sucesso!");
        this.notificationService.sendNotifications(receiver, " transação recebida com sucesso!");

        return newTransaction;
    }
    public boolean autorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> autorizationResponse = restTemplate.getForEntity(endpoint.apiURL, Map.class);

        if(autorizationResponse.getStatusCode() == HttpStatus.OK){
            String message = (String) autorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);

        }else return false;
    }
    public List<Transaction> getAllTransactions(){return this.repository.findAll();}
}
