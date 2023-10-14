package webBackend;

public class GraphicCard {
    int id;
    String model;
    String brand;
    String memorySize;
    String displays;
    String resoulution;
    String color;
    String memoryInterface;



    public GraphicCard(int id, String model, String brand, String memorySize, String displays, String resoulution, String color, String memoryInterface) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.memorySize = memorySize;
        this.displays = displays;
        this.resoulution = resoulution;
        this.color = color;
        this.memoryInterface = memoryInterface;
    }

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

    public String getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(String memorySize) {
        this.memorySize = memorySize;
    }

    public String getDisplays() {
        return displays;
    }

    public void setDisplays(String displays) {
        this.displays = displays;
    }

    public String getResoulution() {
        return resoulution;
    }

    public void setResoulution(String resoulution) {
        this.resoulution = resoulution;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMemoryInterface() {
        return memoryInterface;
    }

    public void setMemoryInterface(String memoryInterface) {
        this.memoryInterface = memoryInterface;
    }
}
