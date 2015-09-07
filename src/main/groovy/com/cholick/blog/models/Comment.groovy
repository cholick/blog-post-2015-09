package com.cholick.blog.models

import groovy.transform.ToString

@ToString(includeNames = true)
class Comment {
    String key
    String value
    String source
}
