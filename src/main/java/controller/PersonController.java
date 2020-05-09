package controller;

import model.*;
import view.LoginMenu;
import view.MainMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PersonController {
    private static ArrayList<Person> allPersons = new ArrayList<>();
    private static Person loggedInPerson = null;

    public static void initializePersons() throws FileNotFoundException {
        for (File file : Database.returnListOfFiles(Database.address.get("customers"))) {
            allPersons.add((Customer) Database.read(Customer.class,file.getAbsolutePath()));
        }
        for (File file : Database.returnListOfFiles(Database.address.get("managers"))) {
            allPersons.add((Manager) Database.read(Manager.class,file.getAbsolutePath()));
        }
        for (File file : Database.returnListOfFiles(Database.address.get("salespersons"))) {
            allPersons.add((Salesperson) Database.read(Salesperson.class,file.getAbsolutePath()));
        }
    }

    public static void addPerson (Person person) {
        allPersons.add(person);
    }

    public static void removePersonFromAllPersons (Person person) {
        allPersons.remove(person);
    }

    public static boolean isThereLoggedInPerson(){
        return loggedInPerson!=null;
    }

    public static boolean isTherePersonByUsername(String username) throws UsernameNotFoundException {
        if(findPersonByUsername(username) == null)
            throw new UsernameNotFoundException();
        return true;
    }

    public static Person findPersonByUsername (String username){
        for (Person person : allPersons) {
            if (person.getUsername().equals(username))
                return person;
        }
        return null;
    }

    public void editPersonalInfo(String filedName,String newValue){
        loggedInPerson.setField(filedName,newValue);
    }

    public void LogIn(String username,String password) {
            loggedInPerson = findPersonByUsername(username);
    }

    public void LogOut(){
        loggedInPerson = null;
    }

    public boolean checkPassword(String password,String username) throws WrongPasswordException{
        if (!findPersonByUsername(username).getPassword().equals(password))
            throw new WrongPasswordException();
        return true;
    }

    public static class UsernameNotFoundException extends Exception{
        String message="This username does not exist";
    }

    public static class WrongPasswordException extends Exception{
        String message="Incorrect password";
    }

    public static Person getLoggedInPerson() {
        return loggedInPerson;
    }

    public static void setLoggedInPerson(Person loggedInPerson) {
        PersonController.loggedInPerson = loggedInPerson;
    }

    public static boolean isLoggedInPersonCustomer(){
        return loggedInPerson instanceof Customer;
    }

    public static <T> ArrayList<Person> filterByRoll(Class<T> personType) {
        return allPersons.stream().filter(personType::isInstance).collect(Collectors.toCollection(ArrayList::new));
    }

    public static LoginMenu.PersonType chooseType() throws Exception {
        int chosenType;
        chosenType = MainMenu.scanner.nextInt ( );
        switch (chosenType) {
            case 1:
                return LoginMenu.PersonType.CUSTOMER;
            case 2:
                return LoginMenu.PersonType.SALESPERSON;
            case 3:
                return LoginMenu.PersonType.MANAGER;
            default:
                throw new Exception ("Invalid Input");
        }
    }
}

