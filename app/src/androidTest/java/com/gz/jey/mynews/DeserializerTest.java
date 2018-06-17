package com.gz.jey.mynews;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gz.jey.mynews.Models.NewsSection;
import com.gz.jey.mynews.Utils.NewsDeserializer;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class DeserializerTest {

    String json = "{\n" +
            "  \"status\": \"OK\",\n" +
            "  \"copyright\": \"Copyright (c) 2018 The New York Times Company.  All Rights Reserved.\",\n" +
            "  \"num_results\": 4,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"url\": \"https://www.nytimes.com/2018/05/15/books/anne-frank-diary-new-pages.html\",\n" +
            "      \"section\": \"Books\",\n" +
            "      \"title\": \"Researchers Uncover Two Hidden Pages in Anne Frank’s Diary\",\n" +
            "      \"published_date\": \"2018-05-15\",\n" +
            "      \"multimedia\": [\n" +
            "        {\n" +
            "         \"url\": \"https://static01.nyt.com/images/2017/01/18/world/16annefrank-item1/16annefrank-item1-thumbStandard-v2.jpg\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"url\": \"https://www.nytimes.com/interactive/2018/books/review/summer-reading.html\",\n" +
            "      \"section\": \"Books\",\n" +
            "      \"title\": \"73 Books to Read While the Sun Is Out and the Days Are Long\",\n" +
            "      \"published_date\": \"2018-06-01\",\n" +
            "      \"media\": [\n" +
            "        {\n" +
            "          \"media-metadata\": [\n" +
            "            {\n" +
            "              \"url\": \"https://static01.nyt.com/images/2018/06/03/books/review/03Cover/03Cover-thumbStandard-v2.gif\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"url\": \"https://www.nytimes.com/2018/05/15/books/anne-frank-diary-new-pages.html\",\n" +
            "      \"section\": \"Books\",\n" +
            "      \"title\": \"Researchers Uncover Two Hidden Pages in Anne Frank’s Diary\",\n" +
            "      \"published_date\": \"2018-05-15\",\n" +
            "      \"multimedia\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"url\": \"https://www.nytimes.com/interactive/2018/books/review/summer-reading.html\",\n" +
            "      \"section\": \"Books\",\n" +
            "      \"title\": \"73 Books to Read While the Sun Is Out and the Days Are Long\",\n" +
            "      \"published_date\": \"2018-06-01\",\n" +
            "      \"media\": [\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}"; 

    @Test
    public void DeserializeFromTopStoriesNormal(){
        String x = "https://static01.nyt.com/images/2017/01/18/world/16annefrank-item1/16annefrank-item1-thumbStandard-v2.jpg";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NewsSection.class, new NewsDeserializer())
                .create();

        NewsSection ns = gson.fromJson(json, NewsSection.class);
        assertThat( x , CoreMatchers.containsString(ns.getResults().get(0).getImageUrl()));
        // more assertions based on what the JSON was would be here
    }

    @Test
    public void DeserializeFromMostPopularNormal(){
        String x = "https://static01.nyt.com/images/2018/06/03/books/review/03Cover/03Cover-thumbStandard-v2.gif";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NewsSection.class, new NewsDeserializer())
                .create();

        NewsSection ns = gson.fromJson(json, NewsSection.class);
        assertThat( x , CoreMatchers.containsString(ns.getResults().get(1).getImageUrl()));
        // more assertions based on what the JSON was would be here
    }

    @Test
    public void DeserializeFromTopStoriesWithAPIBadReturns(){
        String x = "";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NewsSection.class, new NewsDeserializer())
                .create();

        NewsSection ns = gson.fromJson(json, NewsSection.class);
        assertThat( x , CoreMatchers.containsString(ns.getResults().get(2).getImageUrl()));
        // more assertions based on what the JSON was would be here
    }

    @Test
    public void DeserializeFromMostPopularWithAPIBadReturns(){
        String x = "";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NewsSection.class, new NewsDeserializer())
                .create();

        NewsSection ns = gson.fromJson(json, NewsSection.class);
        assertThat( x , CoreMatchers.containsString(ns.getResults().get(3).getImageUrl()));
        // more assertions based on what the JSON was would be here
    }
}
