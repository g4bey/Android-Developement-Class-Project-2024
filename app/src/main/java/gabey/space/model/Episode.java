package gabey.space.model;

public class Episode {
    private String title;
    private String description;
    private int id;

    private String img;

    public String getImg() {
        return img;
    }

    public Episode(String title, String description, int id, String img) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.img = img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
