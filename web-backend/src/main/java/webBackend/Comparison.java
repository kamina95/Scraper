package webBackend;

import javax.persistence.*;

@Entity
@Table(name = "comparison")
public class Comparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_brand", referencedColumnName = "id")
    private int id_brand;

    @Column(name = "url")
    private String url;

    @Column(name = "price")
    private float price;

//    @Column(name = "web-name")
//    private String webName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCards() {
        return id_brand;
    }

    public void setIdCards(int idCards) {
        this.id_brand = idCards;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

//    public String getWebName() {
//        return webName;
//    }
//
//    public void setWebName(String webName) {
//        this.webName = webName;
//    }
}
