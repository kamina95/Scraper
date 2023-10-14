package webBackend;

import javax.persistence.*;

@Entity
@Table(name="graphic_cards")
public class GraphicCardsAnnotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "model" )
    private String model;

    @Column(name = "brand")
    private String brand;

    @Column(name = "url")
    private String url;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "price")
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GraphicCardsAnnotation{" +
                "model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price + '\'' +
                '}';
    }

    //    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public String getMemoryInterface() {
//        return memoryInterface;
//    }
//
//    public void setMemoryInterface(String memoryInterface) {
//        this.memoryInterface = memoryInterface;
//    }
}
