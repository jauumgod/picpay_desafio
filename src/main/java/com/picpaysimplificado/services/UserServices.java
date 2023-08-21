package com.picpaysimplificado.services;


import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServices {

    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuario do tipo Logista, não está autorizado a realizar transações");
        }
        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Saldo Insuficiente");
        }
    }
    public User findUserById(Long id) throws Exception {
        return (User) this.repository.findUserById(id).orElseThrow(()-> new Exception("usuario não encontrado"));
    }

    public void saveUser(User user){
        this.repository.save(user);
    }
}
