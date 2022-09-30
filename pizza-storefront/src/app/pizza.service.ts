// Implement the methods in PizzaService for Task 3
// Add appropriate parameter and return type 

import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Order, OrderSummary } from "./models";

const URL = "/api/order"

@Injectable()
export class PizzaService {

  constructor(private http:HttpClient) { }

  // POST /api/order
  // Add any required parameters or return type
  createOrder(order: Order) : Promise<String> {

    const headers = new HttpHeaders()
        .set('Content-Type', 'application/json')
        .set('Accept', 'application/json')

      return lastValueFrom(
        this.http.post<String>(URL, order, { headers })
      )
  }

  // GET /api/order/<email>/all
  // Add any required parameters or return type
  getOrders(email:string) { 
    const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Accept', 'application/json')

  return (
    this.http.get<String>(URL + "/" + email + "/all", { headers })
  )
  }

}
