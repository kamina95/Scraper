package webBackend;

import javax.persistence.*;

@Entity
@Table(name="graphic_cards")
public class GraphicCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "model" )
    private String model;

    @Column(name = "description")
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GraphicCardsAnnotation{" +
                "id=" + id + '\'' +
                "model='" + model + '\'' +
                "description='" + description + '\'' +
                '}';
    }
}
