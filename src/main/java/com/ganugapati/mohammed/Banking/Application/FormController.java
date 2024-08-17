package com.ganugapati.mohammed.Banking.Application;

import com.google.api.Http;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class FormController {


    @GetMapping("/")
    public String showForm(Model model)
    {
        return "form"; // This corresponds to form.html
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam String name, @RequestParam String pin, Model model)
    {
        // Post to Firestore
        postToFirestore(name, Integer.parseInt(pin));
        model.addAttribute("name", name);
        model.addAttribute("pin", pin);
        return "confirmation"; // This will be a confirmation page you can create
    }

    @GetMapping("/login")
    public String goToLogin(Model model, HttpSession session)
    {
        session.invalidate();
        return "login";
    }

    @PostMapping("/account")
    public String goToAccount(@RequestParam String name, @RequestParam int pin, Model model, HttpSession session) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            // Query the Firestore database to get the user document based on the name
            DocumentSnapshot document = db.collection("users")
                    .whereEqualTo("name", name)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .findFirst()
                    .orElse(null);

            // Check if a matching document was found
            if (document != null && document.exists()) {
                // Get the stored PIN from the document
                int storedPin = document.getLong("pin").intValue();

                // Compare the provided PIN with the stored PIN
                if (storedPin == pin) {
                    // Successful login

                    //document is user identity in the firebase
                    double checkingBalance = document.getDouble("checkingBalance");
                    double savingBalance = document.getDouble("savingBalance");

                    model.addAttribute("name", name);
                    model.addAttribute("checkingBalance", checkingBalance);
                    model.addAttribute("savingBalance", savingBalance);

                    session.setAttribute("name", name);
                    session.setAttribute("checkingBalance", checkingBalance);
                    session.setAttribute("savingBalance", savingBalance);
                    return "account";
                } else {
                    // PIN mismatch
                    model.addAttribute("error", "Invalid PIN");
                    return "login";
                }
            } else {
                // No document found with the provided name
                model.addAttribute("error", "No account found with that name");
                return "login";
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred during login");
            return "login";
        }
    }

    @GetMapping("/checking")
    public String goToChecking(Model model, HttpSession session){
        double checkingBalance = 0.0;
        Firestore db = FirestoreClient.getFirestore();
        String name = (String) session.getAttribute("name");
        try {
            DocumentSnapshot document = db.collection("users")
                    .whereEqualTo("name", name)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (document != null && document.exists()) {
                checkingBalance = document.getDouble("checkingBalance");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        model.addAttribute("checkingBalance", checkingBalance);
        return "checking";
    }


    // method triggered by post request to / update checking in the html file
    @PostMapping("/updateChecking")
    public String updateCheckingBalance(@RequestParam double amount, HttpSession session, Model model) {
        handleDeposit(session, model, amount, "checkingBalance"); // Deposit amount and update balance
        return "checking";
    }


    @GetMapping("/savings")
    public String goToSavings(Model model, HttpSession session){
        double savingBalance = 0.0;
        Firestore db = FirestoreClient.getFirestore();
        String name = (String) session.getAttribute("name");

        try {
            DocumentSnapshot document = db.collection("users")
                    .whereEqualTo("name", name)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (document != null && document.exists()) {
                savingBalance = document.getDouble("savingBalance");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        model.addAttribute("savingBalance", savingBalance);
        return "savings";
    }


    @PostMapping("/updateSaving")
    public String updateSavingBalance(@RequestParam double amount, HttpSession session, Model model) {
        handleDeposit(session, model, amount, "savingBalance"); // Deposit amount and update balance
        return "savings";
    }

    //helper handleCheckingBalance function to update checking (overload with 'type' input)
    private void handleDeposit(HttpSession session, Model model, double depositAmount, String type) {
        double balance = 0.0;
        Firestore db = FirestoreClient.getFirestore();
        String name = (String) session.getAttribute("name");

        try {
            DocumentSnapshot document = db.collection("users")
                    .whereEqualTo("name", name)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (document != null && document.exists()) {
                if(type.equals("checkingBalance")) {
                    balance = document.getDouble("checkingBalance");
                }
                else if (type.equals("savingBalance")) {
                    balance = document.getDouble("savingBalance");
                }

                // Update balance if deposit amount is greater than 0
                if (depositAmount > 0) {
                    balance += depositAmount;
                    db.collection("users").document(document.getId())
                            .update(type, balance);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        model.addAttribute(type, balance);
    }


    @PostMapping("/withdrawChecking")
    public String withdrawChecking(@RequestParam double amount, HttpSession session, Model model) {
        handleWithdrawal(session, model, amount, "checkingBalance"); // Deposit amount and update balance
        return "checking";
    }

    @PostMapping("/withdrawSavings")
    public String withdrawSavings(@RequestParam double amount, HttpSession session, Model model) {
        handleWithdrawal(session, model, amount, "savingBalance"); // Deposit amount and update balance
        return "savings";
    }




    private void handleWithdrawal(HttpSession session, Model model, double withdrawalAmount, String type) {
        double balance = 0.0;
        Firestore db = FirestoreClient.getFirestore();
        String name = (String) session.getAttribute("name");

        try {
            DocumentSnapshot document = db.collection("users")
                    .whereEqualTo("name", name)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (document != null && document.exists()) {
                if(type.equals("checkingBalance")) {
                    balance = document.getDouble("checkingBalance");
                }
                else if (type.equals("savingBalance")) {
                    balance = document.getDouble("savingBalance");
                }

                // Update balance if deposit amount is greater than 0

                if (withdrawalAmount > 0 && withdrawalAmount < balance) {
                    balance -= withdrawalAmount;
                    db.collection("users").document(document.getId())
                            .update(type, balance);
                }

            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        model.addAttribute(type, balance);
    }



    @GetMapping("/transferMoney")
    public String goToTransferMoney(Model model){
        return "transferMoney";
    }


    private void postToFirestore(String name, int pin)
    {

        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("pin", pin);
        user.put("checkingBalance", 0.0);
        user.put("savingBalance", 0.0);
        db.collection("users").add(user);
    }
}

