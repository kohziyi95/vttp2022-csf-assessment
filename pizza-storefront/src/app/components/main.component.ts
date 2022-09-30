import { Router } from '@angular/router';
import { PizzaService } from './../pizza.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Order } from '../models';

const SIZES: string[] = [
  'Personal - 6 inches',
  'Regular - 9 inches',
  'Large - 12 inches',
  'Extra Large - 15 inches',
];

const PizzaToppings: string[] = [
  'Chicken',
  'Seafood',
  'Beef',
  'Vegetables',
  'Cheese',
  'Arugula',
  'Pineapple',
];

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {
  pizzaSize = SIZES[0];

  constructor(private fb: FormBuilder, private svc: PizzaService, private router: Router) {}
  orderForm!: FormGroup;

  ngOnInit(): void {
    this.orderForm = this.createForm();
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)];
  }

  createForm(): FormGroup {
    let form = this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      email: this.fb.control<string>('', [
        Validators.required,
        Validators.email,
      ]),
      size: this.fb.control<number>(0, [Validators.required]),
      base: this.fb.control<string>('', [Validators.required]),
      sauce: this.fb.control<string>('', [Validators.required]),
      toppingChicken: this.fb.control<boolean>(false),
      toppingSeafood: this.fb.control<boolean>(false),
      toppingBeef: this.fb.control<boolean>(false),
      toppingVegetables: this.fb.control<boolean>(false),
      toppingCheese: this.fb.control<boolean>(false),
      toppingArugula: this.fb.control<boolean>(false),
      toppingPineapple: this.fb.control<boolean>(false),
      comments: this.fb.control<string>(''),
    });

    return form;
  }

  toppingError(): boolean {
    let hasError: boolean = true;
    PizzaToppings.forEach((topping) => {
      if (this.orderForm.get('topping' + topping)?.value == true){
        hasError = false;
      }
    });

    return hasError;
  }

  sendOrder() {
    console.log(this.orderForm.value);
    let thick_crust = (this.orderForm.get("base")?.value == 'thick')? true: false;
    let toppings!:string;
    PizzaToppings.forEach((topping) => {
      if (this.orderForm.get('topping' + topping)?.value == true){
        if (toppings == undefined){
          toppings = topping.toLowerCase();
        } else {
          toppings = toppings + "," + topping.toLowerCase();
        }
      }
    });
    // console.log(toppings);
    let order:Order = {
      "name" : this.orderForm.get("name")?.value, 
      "email" : this.orderForm.get("email")?.value, 
      "pizza_size" : this.orderForm.get("size")?.value, 
      "thick_crust" : thick_crust, 
      "sauce" : this.orderForm.get("sauce")?.value, 
      "toppings" : toppings, 
      "comments" : this.orderForm.get("comments")?.value, 
    }
    console.info("Sending order:" , order);
    this.svc.createOrder(order);
    this.router.navigate(['/orders', order.email])
  }

  listOrders(){
    this.router.navigate(['/orders', this.orderForm.get("email")?.value]);
  }
}
