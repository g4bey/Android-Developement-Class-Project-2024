package gabey.space.model;

import java.util.ArrayList;

import gabey.space.utils.StringUtils;

public class CastMember{
    private int id;
    private String name;
    private String img;
    private String character;

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public CastMember(int id, String name, String img, String character) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.character = character;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
