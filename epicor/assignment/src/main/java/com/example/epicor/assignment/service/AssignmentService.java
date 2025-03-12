package com.example.epicor.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author - Praveen Paidisetty
 *
 * This is the service layer.
 */
@Service
public class AssignmentService {


    /**
     *  bean of the RestTemplate.
     */
    @Autowired
    private RestTemplate template;

    /**
     *  given the data source, retrieved from the properties file.
     */
    @Value("${source}")
    String inputUrl;

    /**
     * this variable stores the content after getting filtered by exclusions and constraints.
     * this is used across the other methods.
     */
    private String globalContent = null;

    /**
     * A predefined set of common English stop_words used for filtering text.
     * the set includes prepositions,conjunctions,articles and pronouns.
     */
    private static final Set<String> STOPWORDS = Set.of(
            "about", "above", "across", "after",
            "against", "along", "among", "around", "there", "when", "now", "then",
            "at", "before", "behind", "below", "as", "their", "not", "one",
            "beneath", "beside", "between", "beyond", "by",
            "despite", "down", "during", "except", "for",
            "from", "in", "inside", "into", "like", "near",
            "of", "off", "on", "onto", "out", "outside",
            "over", "past", "since", "through", "throughout",
            "till", "to", "toward", "under", "underneath", "until",
            "up", "upon", "with", "within", "without",
            "I", "he", "she", "it",
            "we", "they", "me", "him", "her", "us",
            "them", "mine", "yours", "his", "hers", "ours", "theirs",
            "myself", "yourself", "himself", "herself", "itself", "ourselves",
            "themselves", "this", "that", "these", "those", "who",
            "whom", "whose", "which", "what", "anyone", "everyone", "someone",
            "nobody", "each", "few", "many", "several", "all", "some",
            "and", "but", "or", "nor",
            "yet", "so", "although", "because", "if", "unless", "while",
            "the", "a", "an",
            "is", "was", "were", "am", "are", "be", "been", "being",
            "you", "i", "my", "your",
            "has", "have", "had", "do", "does", "did", "can", "could",
            "shall", "should", "will", "would", "may", "might", "must"
    );


    /**
     *
     * @return the total count of words from the given sources excluding the stop_words,
     * special characters, numbers, punctuation and postfix 's.
     * the code does the following:
     * 1. using rest template to get the result from the data source.
     * 2. splitting the data on the basis of white spaces.
     * 3. unifying the content, converting to lower case  - HAPPY and happy are the same now.
     * 4. checking the exclusions.
     * 5. forming the string removing all constraints and exclusion and gives you the count.
     */
    public Integer getTotalWordsCount() {
        String content = template.getForEntity(inputUrl, String.class).getBody();
        globalContent = Optional.ofNullable(content).isEmpty() ? "" : Pattern.compile("\\s+").
                splitAsStream(content.toLowerCase())
                .map(word -> word.replaceAll("'s$", ""))
                .map(word -> word.replaceAll("\\W+", ""))
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .filter(word -> !STOPWORDS.contains(word))
                .collect(Collectors.joining(" "));

        globalContent = Pattern.compile("[^a-zA-Z]+")
                .splitAsStream(globalContent)
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.joining(" "));


        int wordCount = globalContent.split(" ").length;

        return wordCount;
    }

    /**
     * @return the top five frequent words from the data source.
     *
     * the following code does the count of each word and provides the top five.
     *
     *  the following code does the following:
     *  1. storing the count of each word.
     *  2. arranging the count in descending order.
     *  3. gives you 5 records in the form of a map.
     */
    public Map<String, Long> topFiveFrequentWords() {

        String content = globalContent;
        Map<String, Long> freq = Stream.of(content.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

        return freq;
    }


    /**
     *
     * @return the list of to fifty unique words sorted in alphabetic order.
     *the code does the following:
     * 1. distinct -  this method filters the unique words which occurs only once.
     * 2. sorted -  natural order of sorting.
     * 3. Providing the 50 elements in the list.
     */
    public List<String> getTopFiftyUniqueWords() {
        String content = globalContent;
        var topFifty = Stream.of(content.split("\\s+"))
                .distinct()        //
                .map(String::trim)   // removing the trailing spaces
                .filter(word -> !word.isEmpty())  // checking isn't the word is blank or empty
                .sorted()  // natural order of sorting
                .limit(50)  // as per the specification 50
                .toList();
        return topFifty;
    }
}
