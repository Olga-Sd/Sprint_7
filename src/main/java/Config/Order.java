package Config;

import java.util.List;

public class Order {
    String firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;
    List<String> color;

    public String getNewOrderPath(){
        return "/api/v1/orders";
    }

    public String getFinishOrderPath(int id){
        return "/api/v1/orders/finish/"+id;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public Order(List<String> color){
        this.firstName = "Saske";
        this.lastName = "Uchiha";
        this.address = "Kanoha 34";
        this.metroStation = "Kanoha Station";
        this.phone = "+1234567890";
        this.rentTime = 4;
        this.deliveryDate = "12.03.2022";
        this.comment = "Don't give my scooter to Naruto!";
        this.color = color;
    }
}
