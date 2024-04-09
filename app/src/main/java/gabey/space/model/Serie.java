package gabey.space.model;

import java.util.ArrayList;

import gabey.space.utils.StringUtils;

public class Serie implements Comparable<Serie> {
    private int id;
    private String name;
    private ArrayList<String> genres;
    private String summary;
    private String img;


    public Serie(
            int id, String name,
            ArrayList<String> genres, String summary,
            String img
    ) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.summary = StringUtils.removeHtmlTags(summary);
        this.img = img;
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

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getGenresAsString() {
        if (genres.isEmpty()) {
            return "";
        } else {
            String res = "Genres: " + genres.get(0);
            for (int i = 1; i < genres.size(); i++) {
                res += ", " + genres.get(i);
            }
            return res;
        }
    }

    public String getSummary() {
        return summary;
    }

    public String getShortSummary() {
        return StringUtils.readMore(this.summary, 240);
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int compareTo(Serie o) {
        return  this.getName().compareTo(o.getName());
    }
}
