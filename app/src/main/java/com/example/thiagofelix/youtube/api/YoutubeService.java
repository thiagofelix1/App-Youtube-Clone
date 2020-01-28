package com.example.thiagofelix.youtube.api;

import com.example.thiagofelix.youtube.model.Resultado;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {

    /* parametros utilizados
    https://www.googleapis.com/youtube/v3/
     search
    ?part = snipptet
    &order = date
     &maxResults = 20
     &key = AIzaSyAJnvmr3SXjLFwW1zSxVA0oFAOkxHykw8o
     &channelId = UCU5JicSrEM5A63jkJ2QvGYw
     &q=desenvolvimento+android
     */

    /* url
    https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResults=20&key=AIzaSyAJnvmr3SXjLFwW1zSxVA0oFAOkxHykw8o&channelId=UCU5JicSrEM5A63jkJ2QvGYw
     */

    @GET("search")
    Call<Resultado>recuperarVideos(@Query("part") String part,
                                   @Query("order") String order,
                                   @Query("maxResults") String maxResults,
                                   @Query("key") String key,
                                   @Query("channelId") String channelId,
                                   @Query("q") String q);
}
