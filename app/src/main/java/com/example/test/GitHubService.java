package com.example.test;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {

    @GET("users/{userName}/repos")
    Call<List<Repository>> getRepositories(@Path("userName") String name);

}
