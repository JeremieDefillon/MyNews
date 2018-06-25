package com.gz.jey.mynews.Utils;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.gz.jey.mynews.Models.NewsSection;
import com.gz.jey.mynews.Models.Result;

import java.lang.reflect.Type;

public class NewsDeserializer implements JsonDeserializer<NewsSection>
{
    @Override
    public NewsSection deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        // Get the "content" element from the parsed JSON
        JsonObject jo = je.getAsJsonObject();

        // Deserializing the "content" and use a new instance of Gson to avoid infinite recursion
        NewsSection news = new NewsSection();

        // Access json elements as jo.has("some_element")
        if(jo.has("status")){news.setStatus(jo.get("status").getAsString());}
        if(jo.has("num_results")){ news.setNumResults(jo.get("num_results").getAsInt());
            Log.d("RESULTS => ", news.getNumResults().toString());}
        if(jo.has("results")){

            JsonArray jsonResultsArray = jo.get("results").getAsJsonArray();
            for(int i=0; i < jsonResultsArray.size(); i++){

                Result result = new Result();
                JsonObject j = jsonResultsArray.get(i).getAsJsonObject();


                if(j.has("section")){result.setSection(j.get("section").getAsString());}
                else{result.setSection("");}
                if(j.has("subsection")){result.setSubsection(j.get("subsection").getAsString());}
                else{result.setSubsection("");}
                if(j.has("title")){result.setTitle(j.get("title").getAsString());   }
                if(j.has("url")){result.setUrl(j.get("url").getAsString());}
                if(j.has("published_date")){result.setPublishedDate(Formatter.getDateFormated(j.get("published_date").getAsString()));}
                if(j.has("multimedia")){
                    if (!j.get("multimedia").isJsonPrimitive()){
                        JsonArray jm = j.get("multimedia").getAsJsonArray();
                        String image ="";
                        if(jm.size()>0){
                            image = jm.get(0).getAsJsonObject().get("url").getAsString();
                        }
                        result.setImageUrl(image);
                    }else{
                        result.setImageUrl("");
                    }
                }

                if(j.has("media")){
                    if(!j.get("media").isJsonPrimitive()){
                        JsonArray jm = j.get("media").getAsJsonArray();
                        String image ="";
                        if(jm.size()>0){
                            JsonArray jt = jm.get(0).getAsJsonObject().get("media-metadata").getAsJsonArray();
                            image = jt.get(0).getAsJsonObject().get("url").getAsString();
                        }
                        result.setImageUrl(image);
                    }else{
                        result.setImageUrl("");
                    }
                }

                news.getResults().add(result);
            }
        }

        return news;
    }
}