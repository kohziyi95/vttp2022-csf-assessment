// Add your models here if you have any
export interface Order {
  order_id?: string;
  name: string;
  email: string;
  pizza_size: number;
  thick_crust: boolean;
  sauce: string;
  toppings: string;
  comments?: string;
  message?: string;
}

export interface OrderSummary {
  orderId: number;
  name: string;
  email: string;
  amount: number;
}
