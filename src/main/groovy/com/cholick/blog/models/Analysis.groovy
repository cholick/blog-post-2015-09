package com.cholick.blog.models

import groovy.transform.ToString

@ToString(includeNames = true)
class Analysis {
    String source
    String label
    Probability probability
}
