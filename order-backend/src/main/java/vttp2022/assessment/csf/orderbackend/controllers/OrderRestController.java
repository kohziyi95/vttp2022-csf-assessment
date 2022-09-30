package vttp2022.assessment.csf.orderbackend.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping(path="/api/order")
public class OrderRestController {

    @Autowired
    private OrderService orderSvc;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postOrder(@RequestBody String payload){
        JsonReader jsonReader = Json.createReader(new StringReader(payload));
        JsonObject jsonObject = jsonReader.readObject();

        Order order = new Order();
        order.setName(jsonObject.getString("name"));
        order.setEmail(jsonObject.getString("email"));
        order.setSize(jsonObject.getInt("pizza_size"));
        order.setSauce(jsonObject.getString("sauce"));
        order.setThickCrust(jsonObject.getBoolean("thick_crust"));
        List<String> toppings = new LinkedList<>();
        String[] toppingArray = jsonObject.getString("toppings").split(",");
        toppings = Arrays.asList(toppingArray);
        order.setToppings(toppings);
        order.setComments(jsonObject.getString("comments"));

        orderSvc.createOrder(order);


        return ResponseEntity.ok("Order added for " + order.getEmail());
    }

    @GetMapping(path="/{email}/all")
    public ResponseEntity<String> getAllOrdersByEmail(@PathVariable String email){
        List<OrderSummary> orderList = orderSvc.getOrdersByEmail(email);
        if (orderList.isEmpty()){
            JsonObject resp = Json.createObjectBuilder().add("message", "No orders found for email: " + email).build();
            return ResponseEntity.ok(resp.toString());
        } 
        List<JsonObject> orderListJson = new ArrayList<>();
        orderList.forEach(summary -> orderListJson.add(summary.toJson()));
        return ResponseEntity.ok(orderListJson.toString());
    }
    
}
