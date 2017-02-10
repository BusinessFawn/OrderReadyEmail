package a4tay.com.orderreadyemail;

/**
 * Created by johnkonderla on 2/10/17.
 */

public class Transaction {

    String name;
    String phone;
    String carrier;
    String order;

    public Transaction(String name, String phone, String carrier, String order) {
        this.name = name;
        this.phone = phone;
        this.carrier = carrier;
        this.order = order;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getName() {
        return name;
    }

    public String getOrder() {
        return order;
    }

    public String getPhone() {
        return phone;
    }
}
