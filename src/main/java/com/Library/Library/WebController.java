package com.Library.Library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class WebController {
    public static int times = 50;
    private MySql mySql;
    @Autowired
    public WebController(DataSource dataSource){
        this.mySql = new MySql(dataSource);
    }

    @GetMapping("/listBook")
    public List<List<Object>> listBook(){
        String query = "SELECT name,free FROM books";
        List<List<Object>> listOfBooks = new ArrayList<>();
        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
        for(Map<String,Object> map : response){
            List<Object> listOfProperties = new ArrayList<>();
            String owner = "{free}";
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if(entry.getKey().equals("free")){
                    int temp = (int) entry.getValue();
                    if(temp!=0){
                        String queryForOwner = "SELECT surname FROM clients WHERE id = ?";
                        String[] value = new String[]{String.valueOf(temp)};
                        owner = mySql.select(queryForOwner,value);
                    }
                    listOfProperties.add(owner);
                }
                else{
                    listOfProperties.add(entry.getValue());
                }
            }
            listOfBooks.add(listOfProperties);
        }
        return listOfBooks;
    }
//    @PostMapping("/authorSearch")
//    public List<Object> authorSearch(@RequestParam String author){
//        List<Object> list = new ArrayList<>();
//        String query = "SELECT name,author,free,year,publisher FROM books WHERE author = '"+author+"'";
//        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
//        for(Map<String,Object> map : response){
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                list.add(entry.getValue());
//            }
//        }
//        return list;
//    }
//    @PostMapping("/nameSearch")
//    public List<Object> nameSearch(@RequestParam String name){
//        List<Object> list = new ArrayList<>();
//        String query = "SELECT name,author,free,year,publisher FROM books WHERE name = '"+name+"'";
//        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
//        for(Map<String,Object> map : response){
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                list.add(entry.getValue());
//            }
//        }
//        return list;
//    }
    @PostMapping("/search")
    public List<List<Object>> search(@RequestParam String searchBy ,@RequestParam String value){
        List<List<Object>> list = new ArrayList<>();
        String query = "SELECT name,author,free,year,publisher FROM books WHERE "+searchBy+" = '"+value+"'";
        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
        for(Map<String,Object> map : response){
            List<Object> listOfProperties = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                listOfProperties.add(entry.getValue());
            }
            list.add(listOfProperties);
        }
        return list;
    }
    @PostMapping("/clientSearch")
    public List<Object> clientSearch(@RequestParam String name,@RequestParam String surname){
        List<Object> list = new ArrayList<>();
        String queryForUserId = "SELECT id FROM clients WHERE name = ? AND surname = ?";
        String[] values = new String[]{name,surname};
        String userId = mySql.select(queryForUserId,values);
        System.out.println(userId);
        String query = "SELECT name,author,free,year,publisher FROM books WHERE free = '"+userId+"'";
        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
        for(Map<String,Object> map : response){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }
//    @PostMapping("/availabilitySearch")
//    public List<List<Object>> availabilitySearch(@RequestParam String availability){
//        List<List<Object>> listOfUsers = new ArrayList<>();
//        String query = "SELECT name,author,free,year,publisher FROM books WHERE free = '"+availability+"'";
//        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
//        for(Map<String,Object> map : response){
//            List<Object> listOfProperties = new ArrayList<>();
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                listOfProperties.add(entry.getValue());
//            }
//            listOfUsers.add(listOfProperties);
//        }
//        return listOfUsers;
//    }
    @PostMapping("/bookProperties")
    public List<List<Object>> bookProperties(@RequestParam String bookName){
        String query = "SELECT name,author,free,year,publisher FROM books WHERE name = '"+bookName+"'";
        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
        List<List<Object>> listOfBooks = new ArrayList<>();
        for(Map<String,Object> map : response){
            List<Object> listOfProperties = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if(entry.getKey().equals("free")){
                    String ownerName = "";
                    int owner = (int) entry.getValue();
                    if(owner!=0){
                        String queryForOwner = "SELECT surname FROM clients WHERE id = ?";
                        String[] value = new String[]{String.valueOf(owner)};
                        ownerName = mySql.select(queryForOwner,value);
                    }
                    else{
                        ownerName = "{free}";
                    }
                    listOfProperties.add(ownerName);
                }
                else{
                    listOfProperties.add(entry.getValue());
                }
            }
            listOfBooks.add(listOfProperties);
        }
        return listOfBooks;
    }
    @GetMapping("/listClient")
    public List<List<Object>> listClient(){
        String query = "SELECT id,name,surname FROM clients";
        List<List<Object>> listOfClients = new ArrayList<>();
        List<Map<String,Object>> response = new ArrayList<>(mySql.select(query));
        for(Map<String,Object> map : response){
            List<Object> listOfProperties= new ArrayList<>();
            for(Map.Entry<String,Object> entry: map.entrySet()){
                listOfProperties.add(entry.getValue());
            }
            listOfClients.add(listOfProperties);
        }
        return listOfClients;
    }
    @PostMapping("/userProperties")
    public List<Object> userProperties(@RequestParam String userId){
        String query = "SELECT id,name,surname FROM clients WHERE id = '"+userId+"'";
        String queryForRents = "SELECT name FROM Books WHERE free = '"+userId+"'";
        List<Object> usersData = new ArrayList<>();
        List<Map<String,Object>> userProperties = new ArrayList<>(mySql.select(query));
        List<Map<String,Object>> userRents = new ArrayList<>(mySql.select(queryForRents));
        for(Map<String,Object> map : userProperties){
            for(Map.Entry<String,Object> entry:map.entrySet()){
                usersData.add(entry.getValue());
            }
        }
        for(Map<String,Object> map : userRents){
            for(Map.Entry<String,Object> entry:map.entrySet()){
                usersData.add(entry.getValue());
            }
        }
        return usersData;
    }

    @PutMapping("/userUpdate")
    public void userUpdate(@RequestParam String name,@RequestParam String surname,@RequestParam boolean delete){
        String query = "";
        if(delete){
            query = "DELETE FROM clients WHERE name = ? AND surname = ?";
        }
        else{
            query = "INSERT INTO clients (name,surname) VALUES (?,?)";
        }
        String[] values = new String[]{name,surname};
        mySql.update(query,values);
    }
    @PutMapping("/rentBook")
    public void rentBook(@RequestParam String userId,@RequestParam String bookId){
        String query = "UPDATE books SET free = ? WHERE id = ?";
        String[] values = new String[]{userId,bookId};
        mySql.update(query,values);
    }
    @PutMapping("/returnBook")
    public void returnBook(@RequestParam String bookId){
        String query = "UPDATE books SET free = ? WHERE id = ?";
        String userId = "0";
        String[] values = new String[]{userId,bookId};
        mySql.update(query,values);
    }
}
