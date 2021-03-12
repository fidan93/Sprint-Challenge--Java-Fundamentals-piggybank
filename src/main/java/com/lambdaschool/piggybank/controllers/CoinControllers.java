package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CoinControllers
{
    @Autowired
    CoinRepository coinRepository;
//http://localhost:2019/total
    @GetMapping(value = "/total", produces = "application/json")
    public ResponseEntity<?> findTotalCoins()
    {
        List<Coin> myList = new ArrayList<>();
        coinRepository.findAll().iterator().forEachRemaining(myList::add);
        double total = 0;

        for(Coin c:myList)
        {
            total = total + c.getQuantity()*c.getValue();
            if(c.getQuantity()>1)
            {
                System.out.println(c.getQuantity() +" "+ c.getNameplural());
            }
            else
            {
                System.out.println(c.getQuantity() +" "+ c.getName());
            }
        }

        System.out.println("The piggy bank holds "+total);

        return new ResponseEntity<>(total,
            HttpStatus.OK);
    }

    //http://localhost:2019/money/{amount}

    @GetMapping(value = "/money/{amount}", produces = "application/json")
    public ResponseEntity<?> countMoney(@PathVariable double amount)
    {
        List<Coin> myList = new ArrayList<>();
        coinRepository.findAll().iterator().forEachRemaining(myList::add);
        double total = 0;
        double amount2 = amount;

        for(Coin c: myList)
        {
            total = total + c.getQuantity() * c.getValue();
        }

        for(Coin c: myList)
        {
            double valtotal = Math.round((c.getQuantity()*c.getValue())*100)/100.0;
            if(amount2 > total)
            {
                System.out.println("Money not available");
                break;
            }
            else if(amount2 >= valtotal)
            {
                amount2 = amount2 - valtotal;
                total = total - valtotal;
                c.setQuantity(0);
            }
            else if(amount2 < valtotal && amount2>=c.getValue() && c.getQuantity()>0)
            {
                amount2 = Math.round((amount2 - c.getValue())*100)/100.0;
                total = Math.round((total - c.getValue())*100)/100.0;
                c.setQuantity(c.getQuantity()-1);
            }

            if(c.getQuantity()>0 && c.getQuantity() <=1)
            {
                System.out.println(c.getQuantity() +" " +c.getName());
            }
            else if(c.getQuantity()>1)
            {
                System.out.println(c.getQuantity() +" " +c.getNameplural());
            }

        }
        System.out.println("The piggy bank holds $"+total);
    return new ResponseEntity<>(HttpStatus.OK);
    }
}
