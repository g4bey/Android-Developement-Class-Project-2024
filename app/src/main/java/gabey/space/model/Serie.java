package gabey.space.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Serie implements Comparable<Serie> {
    private final int crop_summary_after = 244;

    private int id;
    private String name;
    private ArrayList<String> genres;
    private String summary;
    private String img;
    private final double score;

    public String removeHtmlTags(String s) {
        return s.replaceAll("<[^>]*>", "");
    }

    public String readMore(String s) {

        if (s.length() > crop_summary_after) {
            return s.substring(0, crop_summary_after) + "...";
        }

        return s;
    }

    public Serie(
            int id, String name,
            ArrayList<String> genres, String summary,
            String img, double score
    ) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.summary = readMore(removeHtmlTags(summary));
        this.img = img;
        this.score = score;
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
        if (this.score > o.score) {
            return 1;
        } else if (this.score == score) {
            return 0;
        } else {
            return -1;
        }
    }
}
