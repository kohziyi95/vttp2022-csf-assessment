import { OrderSummary } from './../models';
import { PizzaService } from './../pizza.service';
import { AfterViewInit, Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Order } from '../models';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css'],
})
export class OrdersComponent implements OnInit, AfterViewInit {
  constructor(
    private svc: PizzaService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}
  
  email: string = this.activatedRoute.snapshot.params['email'];
  orderList: OrderSummary[] = [];

  ngOnInit(): void {
    this.showOrders(this.activatedRoute.snapshot.params['email']);
  }

  ngAfterViewInit(): void {
    this.showOrders(this.activatedRoute.snapshot.params['email']);
  }

  showOrders(email: string) {
    this.svc.getOrders(email).subscribe((data) => {
      console.info(data);

      this.orderList = data as any;

      console.info('Showing orders for email: ', email);
      console.info('Order list >>>> ', this.orderList);
      return this.orderList;
    });
  }

  backToOrdering() {
    this.router.navigate(['/']);
  }
}
