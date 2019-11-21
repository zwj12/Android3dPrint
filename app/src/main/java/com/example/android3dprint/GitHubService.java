package com.example.android3dprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author CNMIZHU7
 * @date 11/21/2019
 * description：
 */

public interface GitHubService {
    @GET("users/{user}/repos")
    Call<List<User>> listRepos(@Path("user") String user);
}
