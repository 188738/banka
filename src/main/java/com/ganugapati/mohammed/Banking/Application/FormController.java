package com.ganugapati.mohammed.Banking.Application;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
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
    public String goToAccount(@RequestParam String name, @RequestParam int pin, Model model, HttpSession session)
    {
        model.addAttribute("name", name);
        session.setAttribute("name", name);
        return "account";

    }

    @GetMapping("/checking")
    public String goToChecking(Model model){
        return "checking";
    }

    @GetMapping("/savings")
    public String goToSavings(Model model){
        return "savings";
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
        db.collection("users").add(user);
    }
}

