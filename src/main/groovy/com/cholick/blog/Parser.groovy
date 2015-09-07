package com.cholick.blog

import com.cholick.blog.models.Comment
import groovy.json.JsonBuilder

class Parser {

    static void main(String[] args) {
        Collection<Comment> comments = []
        comments.addAll(parse(new File('data/raw/blog.jetbrains.com_toolbox.html'), 'comment-body', 'blog1'))
        comments.addAll(parse(new File('data/raw/blog.jetbrains.com_listening.html'), 'comment-body', 'blog2'))
        comments.addAll(parse(new File('data/raw/yc.html'), 'c00', 'yc'))
        comments.addAll(parse(new File('data/raw/reddit.html'), 'usertext-body', 'reddit'))

        new File('data/parsed.json').withWriter { writer ->
            writer.write(new JsonBuilder(comments).toPrettyString())
        }
    }

    static Collection<Comment> parse(File file, String cls, String source) {
        def parsed = new XmlParser().parse(file)

        def comments = parsed."**".findAll {
            it instanceof Node && it.@class.toString().contains(cls)
        }
        return comments.collect { comment ->
            return new Comment(key: comment.hashCode(), value: comment.text(), source: source)
        }
    }
}
