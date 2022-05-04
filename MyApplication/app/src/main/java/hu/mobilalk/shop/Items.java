package hu.mobilalk.shop;

public class Items {
    private String name;
    private String price;
    private String info;
    private float rate;
    private int image;
    private int count;
    private String id;

    public Items(String name, String price, String info, float rate, int image, int count) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.rate = rate;
        this.image = image;
        this.count = count;
    }

    public Items() {}

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getInfo() {
        return info;
    }

    public float getRate() {
        return rate;
    }

    public int getImage() {
        return image;
    }

    public int getCount() {
        return count;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
