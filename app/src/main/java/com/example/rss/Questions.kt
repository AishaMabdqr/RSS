package com.example.rss

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class Questions (val title : String?, val author:String?){
    override fun toString(): String = title!!
}

class XMLParser{
    private val ns: String? = null

    fun parse(inputStream: InputStream): List<Questions> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }


}
    private fun readSongsRssFeed(parser: XmlPullParser): List<Questions> {

        val question = mutableListOf<Questions>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "entry" ) {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var title: String? = null
                var author: String? = null
                var name : String? = null
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "title" -> title = readTitle(parser)
                        "author" -> {
                             if (parser.name == "author" ){
                                 parser.require(XmlPullParser.START_TAG, ns, "author")
                                  while (parser.next() != XmlPullParser.END_TAG) {
                                if (parser.eventType != XmlPullParser.START_TAG) {
                                    continue
                                }
                                when (parser.name){
                                    "name" -> name = readName(parser)
                                }
                            }
                        }}
                        else -> skip(parser)
                    }
                }
                question.add(Questions(title,name))
            } else {
                skip(parser)
            }
        }
        return question
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "name")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "name")
        return summary
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}