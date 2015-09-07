package com.cholick.blog

import com.cholick.blog.models.Analysis
import groovy.json.JsonSlurper

class Summarizer {

    static void main(String[] args) {
        //playing fast and loose with types...
        Map<String, Analysis> analyzed = new JsonSlurper().parse(new File('data/analyzed.json'))

        Map<String, Map<String, Integer>> countsByLabel = [:].withDefault {
            return ["pos": 0, "neg": 0, "neutral": 0]
        }

        analyzed.values().each { analyzedComment ->
            ++countsByLabel[analyzedComment.source][analyzedComment.label]
            ++countsByLabel["all"][analyzedComment.label]
        }

        println(countsByLabel)
    }
}
