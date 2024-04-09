package gabey.space.db;

import java.util.ArrayList;

public class DBContract {
    public static final String DATABASE_NAME = "OriginalDB";
    public static class FavoriteSeries {
        public static final String TABLE_NAME = "FavoriteSeries";
        public static final String _ID = "id";
        public static final String COLUMN_API_ID = "api_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_SCORE = "score";
    }

    public static class SearchHistory {
        public static final String TABLE_NAME = "SearchHistory";
        public static final String _ID = "id";
        public static final String COLUMN_query = "queryContent";

    }
}
