package hu.mobilalk.shop;

public class Items {
    private String name;
    private String price;
    private String info;
    private float rate;
    private int image;

    public Items(String name, String price, String info, float rate, int image) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.rate = rate;
        this.image = image;
    }

    public Items() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getImage() {
        return image;
    }
}
