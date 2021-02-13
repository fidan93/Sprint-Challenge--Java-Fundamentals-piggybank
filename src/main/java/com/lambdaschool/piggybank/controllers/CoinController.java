package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CoinController
{
    @Autowired
    CoinRepository coinrep;

    //http://localhost:2019/total
    @GetMapping(value = "/total",
        produces = {"application/json"})
    public ResponseEntity<?> getCoinsTotal()
    {
        List<Coin> myList = new ArrayList<>();
        coinrep.findAll()
            .iterator()
            .forEachRemaining(myList::add);

        double total = 0;

        for (Coin c : myList)
        {
            total = total + c.getValue() * c.getQuantity();
            if (c.getQuantity() == 1)
            {
                System.out.println(c.getQuantity() + " " + c.getName());
            } else
            {
                System.out.println(c.getQuantity() + " " + c.getNameplural());
            }
        }
        System.out.println("The piggy bank holds " + total);
        return new ResponseEntity<>(total,
            HttpStatus.OK);
    }

    @GetMapping(value = "/money/{amount}", produces = {"application/json"})
    public ResponseEntity<?> removeAmmount(@PathVariable double amount)
    {
        List<Coin> myList = new ArrayList<>();
        coinrep.findAll()
            .iterator()
            .forEachRemaining(myList::add);

        double count = amount;
        double total = 0;

        for (Coin c : myList)
        {
            total = total + c.getValue() * c.getQuantity();
        }

        for(Coin c: myList)
        {
            double qxv = Math.round((c.getQuantity()*c.getValue())*100)/100.0;

            if (total - count < 0)
            {
                System.out.println("Money not available");
                break;
            }
            else if (c.getQuantity() * c.getValue() <= count)
            {
                //this goes completely.So nothing to sout(console)
                count = count - qxv;
                total = total - qxv;
            }
            else if (c.getValue() * c.getQuantity() > count && c.getValue() <= count)
            {
                double bigger = Math.round((count / c.getValue())*10)/10;
                double biggerint = Math.round((count - (c.getValue()*bigger))*100)/100.0;
                total = total - c.getValue()*bigger;
                count = biggerint;

                //shows only coins with quantity >0
               if(c.getQuantity()-(int)bigger>0)
               {
                   System.out.println(c.getQuantity() - (int)bigger + " " + c.getName());
               }
            }
            else if(c.getValue() > count )
            {
                //This remains untouched
                System.out.println(c.getQuantity()+" "+c.getName());
                   continue;
            }

        }

        if (total - amount > 0)
        {
            System.out.println("The piggy bank holds $" + (total));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


