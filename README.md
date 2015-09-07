A small chunk of quick and dirty Groovy to do sentiment analysis on four posts about the recent announcement
around [Jetbrains pricing change](https://www.jetbrains.com/toolbox/).

The posts
* http://blog.jetbrains.com/blog/2015/09/03/introducing-jetbrains-toolbox/
* http://blog.jetbrains.com/blog/2015/09/04/we-are-listening/
* https://news.ycombinator.com/item?id=10170089
* https://www.reddit.com/r/programming/comments/3ji148/jetbrains_toolbox_monthly_yearly_subscription_for/

The code pulls from the comment bodies (imperfectly) and posts to text-processing.com for sentiment analysis
(using https://market.mashape.com/japerk/text-processing).

[parsed.json](data/parsed.json) contains the data after scraping it from the saved pages and
[analyzed.json](data/analyzed.json) has the sentiment analysis scores

Groovy's xml parser is much picker than a browser, so the raw html files have been stripped of some interfering
tags (like unclosed img tags) and entities.

The API key exists in a root folder in a file called secrets:
```
{
    "apiKey": "some value"
}
```
