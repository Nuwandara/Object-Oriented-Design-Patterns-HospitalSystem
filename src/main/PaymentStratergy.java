package main;

public interface PaymentStratergy {

    public void pay(double amount);
}

class CardPayment implements PaymentStratergy {

    @Override
    public void pay(double amount) {

        System.out.println("Card payment done Successfully.");
        System.out.println("Paid amount : " + amount + " Rs/=");
    }

}

class CashPayment implements PaymentStratergy {

    @Override
    public void pay(double amount) {
        System.out.println("Cash payment done Successfully.");
        System.out.println("Paid amount : " + amount + " Rs/=");
    }
}

class InsurancePayment implements PaymentStratergy {

    @Override
    public void pay(double amount) {
        System.out.println("Insurance payment done Successfully.");
        System.out.println("Paid amount : " + amount + " Rs/=");
    }
}
