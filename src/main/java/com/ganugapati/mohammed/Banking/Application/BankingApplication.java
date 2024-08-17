package com.ganugapati.mohammed.Banking.Application;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);

		try {
			FileInputStream serviceAccount = new FileInputStream("/Users/pigeon/Dropbox/My Mac (Kousthub’s MacBook Pro)/Downloads/BigBhaiSab/src/main/java/com/ganugapati/mohammed/Banking/Application/serviceAccountKey.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://banking-application-189ab-default-rtdb.firebaseio.com/")
					.build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner console = new Scanner(System.in);
		System.out.println("Welcome to the Apex Banking Reserve");
		System.out.println("Please enter your name:");
		String name = console.nextLine();
		System.out.println("Please enter your pin #:");
		int pin = console.nextInt();
		System.out.println("Done");
		postToFirebase(name, pin);
		postToFirestore(name, pin);




	}
	private static void postToFirebase(String name, int pin) {
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");


		// Create a unique key for each user
		String userId = ref.push().getKey();

		// Create a user object to store in Firebase
		User user = new User(name, pin);

		// Save the user object under the generated key
		ref.child(userId).setValueAsync(user);
	}
	private static void postToFirestore(String name, int pin)
	{
		Firestore db = FirestoreClient.getFirestore();
		Map<String, Object> user = new HashMap<>();
		user.put("name", name);
		user.put("pin", pin);
		ApiFuture<DocumentReference> future = db.collection("users").add(user);
	}
}
