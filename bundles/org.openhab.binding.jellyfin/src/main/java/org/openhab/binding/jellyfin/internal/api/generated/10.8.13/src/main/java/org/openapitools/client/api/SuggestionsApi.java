/*
 * Jellyfin API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 10.8.13
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiCallback;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.Pair;
import org.openapitools.client.ProgressRequestBody;
import org.openapitools.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import org.openapitools.client.model.BaseItemDtoQueryResult;
import org.openapitools.client.model.BaseItemKind;
import java.util.UUID;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestionsApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public SuggestionsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public SuggestionsApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public int getHostIndex() {
        return localHostIndex;
    }

    public void setHostIndex(int hostIndex) {
        this.localHostIndex = hostIndex;
    }

    public String getCustomBaseUrl() {
        return localCustomBaseUrl;
    }

    public void setCustomBaseUrl(String customBaseUrl) {
        this.localCustomBaseUrl = customBaseUrl;
    }

    /**
     * Build call for getSuggestions
     * @param userId The user id. (required)
     * @param mediaType The media types. (optional)
     * @param type The type. (optional)
     * @param startIndex Optional. The start index. (optional)
     * @param limit Optional. The limit. (optional)
     * @param enableTotalRecordCount Whether to enable the total record count. (optional, default to false)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Suggestions returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getSuggestionsCall(UUID userId, List<String> mediaType, List<BaseItemKind> type, Integer startIndex, Integer limit, Boolean enableTotalRecordCount, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/Users/{userId}/Suggestions"
            .replace("{" + "userId" + "}", localVarApiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (mediaType != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "mediaType", mediaType));
        }

        if (type != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "type", type));
        }

        if (startIndex != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("startIndex", startIndex));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (enableTotalRecordCount != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("enableTotalRecordCount", enableTotalRecordCount));
        }

        final String[] localVarAccepts = {
            "application/json",
            "application/json; profile=CamelCase",
            "application/json; profile=PascalCase"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "CustomAuthentication" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getSuggestionsValidateBeforeCall(UUID userId, List<String> mediaType, List<BaseItemKind> type, Integer startIndex, Integer limit, Boolean enableTotalRecordCount, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling getSuggestions(Async)");
        }

        return getSuggestionsCall(userId, mediaType, type, startIndex, limit, enableTotalRecordCount, _callback);

    }

    /**
     * Gets suggestions.
     * 
     * @param userId The user id. (required)
     * @param mediaType The media types. (optional)
     * @param type The type. (optional)
     * @param startIndex Optional. The start index. (optional)
     * @param limit Optional. The limit. (optional)
     * @param enableTotalRecordCount Whether to enable the total record count. (optional, default to false)
     * @return BaseItemDtoQueryResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Suggestions returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public BaseItemDtoQueryResult getSuggestions(UUID userId, List<String> mediaType, List<BaseItemKind> type, Integer startIndex, Integer limit, Boolean enableTotalRecordCount) throws ApiException {
        ApiResponse<BaseItemDtoQueryResult> localVarResp = getSuggestionsWithHttpInfo(userId, mediaType, type, startIndex, limit, enableTotalRecordCount);
        return localVarResp.getData();
    }

    /**
     * Gets suggestions.
     * 
     * @param userId The user id. (required)
     * @param mediaType The media types. (optional)
     * @param type The type. (optional)
     * @param startIndex Optional. The start index. (optional)
     * @param limit Optional. The limit. (optional)
     * @param enableTotalRecordCount Whether to enable the total record count. (optional, default to false)
     * @return ApiResponse&lt;BaseItemDtoQueryResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Suggestions returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<BaseItemDtoQueryResult> getSuggestionsWithHttpInfo(UUID userId, List<String> mediaType, List<BaseItemKind> type, Integer startIndex, Integer limit, Boolean enableTotalRecordCount) throws ApiException {
        okhttp3.Call localVarCall = getSuggestionsValidateBeforeCall(userId, mediaType, type, startIndex, limit, enableTotalRecordCount, null);
        Type localVarReturnType = new TypeToken<BaseItemDtoQueryResult>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Gets suggestions. (asynchronously)
     * 
     * @param userId The user id. (required)
     * @param mediaType The media types. (optional)
     * @param type The type. (optional)
     * @param startIndex Optional. The start index. (optional)
     * @param limit Optional. The limit. (optional)
     * @param enableTotalRecordCount Whether to enable the total record count. (optional, default to false)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Suggestions returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getSuggestionsAsync(UUID userId, List<String> mediaType, List<BaseItemKind> type, Integer startIndex, Integer limit, Boolean enableTotalRecordCount, final ApiCallback<BaseItemDtoQueryResult> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSuggestionsValidateBeforeCall(userId, mediaType, type, startIndex, limit, enableTotalRecordCount, _callback);
        Type localVarReturnType = new TypeToken<BaseItemDtoQueryResult>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}
