package com.cholick.blog

import com.cholick.blog.models.Analysis
import com.cholick.blog.models.Comment
import com.cholick.blog.models.Probability
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.JsonNode
import com.mashape.unirest.http.Unirest
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class Analyzer {

    static String apiKey

    static void main(String[] args) {
        def secrets = new JsonSlurper().parse(new File('secrets.json'))
        apiKey = secrets['apiKey']

        def parsed = new JsonSlurper().parse(new File('data/parsed.json'))

        Collection<Comment> comments = parsed.collect {
            new Comment(it)
        }

        //playing fast and loose with types...
        Map<String, Analysis> analyzed = new JsonSlurper().parse(new File('data/analyzed.json'))

        int processed = 0
        int skipped = 0
        comments.each { comment ->
            if (analyzed[comment.key]) {
                ++skipped
            } else {
                if (processed < 250) { //run a few at a time to not blow through api quota when error
                    Analysis analysis = analyze(comment.value)
                    if (analysis) {
                        analysis.source = comment.source
                        analyzed[comment.key] = analysis
                    }
                    ++processed
                }
            }
        }
        println("Skipped ${skipped} already processed comment")
        println("Processed ${processed} new comments")

        new File('data/analyzed.json').withWriter { writer ->
            writer.write(new JsonBuilder(analyzed).toPrettyString())
        }
    }

    static Analysis analyze(String text) {
        HttpResponse<JsonNode> response = Unirest
                .post("https://japerk-text-processing.p.mashape.com/sentiment/")
                .header("X-Mashape-Key", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("language", "english")
                .field("text", text)
                .asJson();

        if (response.status != 200) {
            println("Unable to process: [\n$text\n]")
            return null
        }

        def jsonBody = response.body.object
        def analysis = new Analysis(label: jsonBody['label'], probability:
                new Probability(
                        neg: (Double) jsonBody['probability']['neg'],
                        neutral: (Double) jsonBody['probability']['neutral'],
                        pos: (Double) jsonBody['probability']['pos'],
                )
        )

        return analysis
    }
}
