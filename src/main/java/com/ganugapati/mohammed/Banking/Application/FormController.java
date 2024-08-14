package com.ganugapati.mohammed.Banking.Application;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FormController {

    @GetMapping("/")
    public String showForm(Model model) {
        return "form"; // This corresponds to form.html
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam String name, @RequestParam String pin, Model model) {
        // Post to Firestore
        postToFirestore(name, Integer.parseInt(pin));
        model.addAttribute("name", name);
        model.addAttribute("pin", pin);
        return "confirmation"; // This will be a confirmation page you can create
    }



    private void postToFirestore(String name, int pin) {
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("pin", pin);
        db.collection("users").add(user);
    }
}

