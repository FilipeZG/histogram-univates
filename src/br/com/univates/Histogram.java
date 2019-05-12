package br.com.univates;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

public class Histogram {
    
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(System.getProperty("java.class.path") + "/game-reviews.csv"));
        lines = lines.subList(1, lines.size());

        Map<String, List<Review>> reviewsYear = lines.stream()
                                                     .map(Histogram::convertToGame)
                                                     .collect(groupingBy(Review::getReleaseYear, TreeMap::new, toList()));

        baseHistogram(reviewsYear);
    }

    private static Review convertToGame(String line) {
        String[] values = line.split(";");
        return new Review(values[0], values[6], values[2], values[4], Double.parseDouble(values[3]));
    }

    private static void baseHistogram(Map<String, List<Review>> reviewsYear) {
        reviewsYear.forEach((year, reviews) -> {
            double sumScore = 0;
            double mediocre = 0;

            for (Review game : reviews) {
                sumScore += game.getScore();
                mediocre += (game.getReview().equals("Mediocre") ? 1 : 0);
            }

            double scoreAverage = sumScore / reviews.size();
            List<String> worstToBestReviews = sortReviewsFromWorstToBest(reviews);

            System.out.println();
            System.out.println(String.format("Game reviews in %s: %s", year, reviews.size()));
            System.out.println(String.format("Mediocre reviews: %.2f%%", (mediocre / reviews.size()) * 100));
            System.out.println(String.format("Average: %.2f", scoreAverage));
            System.out.println(String.format("Standard deviation: %.2f", calcScoreStandardDeviation(reviews, scoreAverage)));
            System.out.println(String.format("Best game: %s", worstToBestReviews.get(worstToBestReviews.size() - 1)));
            System.out.println(String.format("Worst game: %s", worstToBestReviews.get(0)));
            System.out.println("\n---------------------------------");
        });
    }

    private static List<String> sortReviewsFromWorstToBest(List<Review> reviews) {
        return reviews.stream()
                      .collect(groupingBy(Review::getGame, summingDouble(Review::getScore)))
                      .entrySet()
                      .stream()
                      .sorted(Comparator.comparing(Map.Entry::getValue))
                      .map(Map.Entry::getKey)
                      .collect(toList());
    }

    private static double calcScoreStandardDeviation(List<Review> reviews, final double average) {
        double variance = reviews.stream().mapToDouble(review -> Math.pow(review.getScore() - average, 2)).sum() / reviews.size();
        return Math.sqrt(variance);
    }


}
