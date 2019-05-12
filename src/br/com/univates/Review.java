package br.com.univates;

class Review {

    private final String game;
    private final String releaseYear;
    private final String review;
    private final String genre;
    private final double score;

    public Review(String game, String releaseYear, String review, String genre, double score) {
        this.releaseYear = releaseYear;
        this.review = review;
        this.score = score;
        this.game = game;
        this.genre = genre;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public double getScore() {
        return score;
    }

    public String getReview() {
        return review;
    }

    public String getGame() {
        return game;
    }

    public String getGenre() {
        return genre;
    }
}
