package vttp2022.assessment.csf.orderbackend.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;

@Service
public class OrderService {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	private PricingService priceSvc;

	public static final String SQL_INSERT_ORDER = "insert into orders(name, email, pizza_size, thick_crust, sauce, toppings, comments) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_GET_ORDER_BY_EMAIL = "select order_id, name, email, pizza_size, thick_crust, sauce, toppings, comments from orders where email = ?";

	// POST /api/order
	// Create a new order by inserting into orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public void createOrder(Order order) {
		String toppings = "";

		for (String topping : order.getToppings()) {
			if (toppings.equals("")) {
				toppings = topping;
			} else {
				toppings = toppings + "," + topping;
			}
		}
		try {
			int updated = template.update(SQL_INSERT_ORDER, order.getName(), order.getEmail(), order.getSize(),
					order.isThickCrust(), order.getSauce(), toppings, order.getComments());
			System.out.printf("updated: %d\n", updated);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// GET /api/order/<email>/all
	// Get a list of orders for email from orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public List<OrderSummary> getOrdersByEmail(String email) {
		// Use priceSvc to calculate the total cost of an order
		List<OrderSummary> orderList = new ArrayList<>();
		try {
			SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDER_BY_EMAIL, email);
			while (rs.next()) {
				OrderSummary summary = new OrderSummary();
				summary.setName(rs.getString("name"));
				summary.setEmail(rs.getString("email"));
				summary.setOrderId(rs.getInt("order_id"));
				Float totalPrice = 0.0f;
				Float pizzaSizePrice = priceSvc.size(rs.getInt("pizza_size"));
				Float saucePrice = priceSvc.sauce(rs.getString("sauce"));
				Float toppingsPrice = 0.0f;
				String[] toppings = rs.getString("toppings").split(",");
				for (int i = 0; i < toppings.length; i++) {
					toppingsPrice += priceSvc.topping(toppings[i]);
				}
				Float crustPrice = rs.getBoolean("thick_crust") ? priceSvc.thickCrust() : priceSvc.thinCrust();
				totalPrice = pizzaSizePrice + saucePrice + toppingsPrice + crustPrice;
				summary.setAmount(totalPrice);
				orderList.add(summary);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return orderList;
	}
}
