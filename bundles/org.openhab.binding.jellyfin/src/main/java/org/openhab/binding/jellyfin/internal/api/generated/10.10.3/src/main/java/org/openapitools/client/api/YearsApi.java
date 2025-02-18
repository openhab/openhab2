/*
 * Jellyfin API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 10.10.3
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


import org.openapitools.client.model.BaseItemDto;
import org.openapitools.client.model.BaseItemDtoQueryResult;
import org.openapitools.client.model.BaseItemKind;
import org.openapitools.client.model.ImageType;
import org.openapitools.client.model.ItemFields;
import org.openapitools.client.model.ItemSortBy;
import org.openapitools.client.model.MediaType;
import org.openapitools.client.model.ProblemDetails;
import org.openapitools.client.model.SortOrder;
import java.util.UUID;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearsApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public YearsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public YearsApi(ApiClient apiClient) {
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
     * Build call for getYear
     * @param year The year. (required)
     * @param userId Optional. Filter by user id, and attach user data. (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year returned. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Year not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getYearCall(Integer year, UUID userId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/Years/{year}"
            .replace("{" + "year" + "}", localVarApiClient.escapeString(year.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (userId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("userId", userId));
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
    private okhttp3.Call getYearValidateBeforeCall(Integer year, UUID userId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'year' is set
        if (year == null) {
            throw new ApiException("Missing the required parameter 'year' when calling getYear(Async)");
        }

        return getYearCall(year, userId, _callback);

    }

    /**
     * Gets a year.
     * 
     * @param year The year. (required)
     * @param userId Optional. Filter by user id, and attach user data. (optional)
     * @return BaseItemDto
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year returned. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Year not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public BaseItemDto getYear(Integer year, UUID userId) throws ApiException {
        ApiResponse<BaseItemDto> localVarResp = getYearWithHttpInfo(year, userId);
        return localVarResp.getData();
    }

    /**
     * Gets a year.
     * 
     * @param year The year. (required)
     * @param userId Optional. Filter by user id, and attach user data. (optional)
     * @return ApiResponse&lt;BaseItemDto&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year returned. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Year not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<BaseItemDto> getYearWithHttpInfo(Integer year, UUID userId) throws ApiException {
        okhttp3.Call localVarCall = getYearValidateBeforeCall(year, userId, null);
        Type localVarReturnType = new TypeToken<BaseItemDto>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Gets a year. (asynchronously)
     * 
     * @param year The year. (required)
     * @param userId Optional. Filter by user id, and attach user data. (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year returned. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Year not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getYearAsync(Integer year, UUID userId, final ApiCallback<BaseItemDto> _callback) throws ApiException {

        okhttp3.Call localVarCall = getYearValidateBeforeCall(year, userId, _callback);
        Type localVarReturnType = new TypeToken<BaseItemDto>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for getYears
     * @param startIndex Skips over a given number of items within the results. Use for paging. (optional)
     * @param limit Optional. The maximum number of records to return. (optional)
     * @param sortOrder Sort Order - Ascending,Descending. (optional)
     * @param parentId Specify this to localize the search to a specific item or folder. Omit to use the root. (optional)
     * @param fields Optional. Specify additional fields of information to return in the output. (optional)
     * @param excludeItemTypes Optional. If specified, results will be excluded based on item type. This allows multiple, comma delimited. (optional)
     * @param includeItemTypes Optional. If specified, results will be included based on item type. This allows multiple, comma delimited. (optional)
     * @param mediaTypes Optional. Filter by MediaType. Allows multiple, comma delimited. (optional)
     * @param sortBy Optional. Specify one or more sort orders, comma delimited. Options: Album, AlbumArtist, Artist, Budget, CommunityRating, CriticRating, DateCreated, DatePlayed, PlayCount, PremiereDate, ProductionYear, SortName, Random, Revenue, Runtime. (optional)
     * @param enableUserData Optional. Include user data. (optional)
     * @param imageTypeLimit Optional. The max number of images to return, per image type. (optional)
     * @param enableImageTypes Optional. The image types to include in the output. (optional)
     * @param userId User Id. (optional)
     * @param recursive Search recursively. (optional, default to true)
     * @param enableImages Optional. Include image information in output. (optional, default to true)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year query returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getYearsCall(Integer startIndex, Integer limit, List<SortOrder> sortOrder, UUID parentId, List<ItemFields> fields, List<BaseItemKind> excludeItemTypes, List<BaseItemKind> includeItemTypes, List<MediaType> mediaTypes, List<ItemSortBy> sortBy, Boolean enableUserData, Integer imageTypeLimit, List<ImageType> enableImageTypes, UUID userId, Boolean recursive, Boolean enableImages, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/Years";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (startIndex != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("startIndex", startIndex));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (sortOrder != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "sortOrder", sortOrder));
        }

        if (parentId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("parentId", parentId));
        }

        if (fields != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "fields", fields));
        }

        if (excludeItemTypes != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "excludeItemTypes", excludeItemTypes));
        }

        if (includeItemTypes != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "includeItemTypes", includeItemTypes));
        }

        if (mediaTypes != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "mediaTypes", mediaTypes));
        }

        if (sortBy != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "sortBy", sortBy));
        }

        if (enableUserData != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("enableUserData", enableUserData));
        }

        if (imageTypeLimit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("imageTypeLimit", imageTypeLimit));
        }

        if (enableImageTypes != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "enableImageTypes", enableImageTypes));
        }

        if (userId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("userId", userId));
        }

        if (recursive != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("recursive", recursive));
        }

        if (enableImages != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("enableImages", enableImages));
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
    private okhttp3.Call getYearsValidateBeforeCall(Integer startIndex, Integer limit, List<SortOrder> sortOrder, UUID parentId, List<ItemFields> fields, List<BaseItemKind> excludeItemTypes, List<BaseItemKind> includeItemTypes, List<MediaType> mediaTypes, List<ItemSortBy> sortBy, Boolean enableUserData, Integer imageTypeLimit, List<ImageType> enableImageTypes, UUID userId, Boolean recursive, Boolean enableImages, final ApiCallback _callback) throws ApiException {
        return getYearsCall(startIndex, limit, sortOrder, parentId, fields, excludeItemTypes, includeItemTypes, mediaTypes, sortBy, enableUserData, imageTypeLimit, enableImageTypes, userId, recursive, enableImages, _callback);

    }

    /**
     * Get years.
     * 
     * @param startIndex Skips over a given number of items within the results. Use for paging. (optional)
     * @param limit Optional. The maximum number of records to return. (optional)
     * @param sortOrder Sort Order - Ascending,Descending. (optional)
     * @param parentId Specify this to localize the search to a specific item or folder. Omit to use the root. (optional)
     * @param fields Optional. Specify additional fields of information to return in the output. (optional)
     * @param excludeItemTypes Optional. If specified, results will be excluded based on item type. This allows multiple, comma delimited. (optional)
     * @param includeItemTypes Optional. If specified, results will be included based on item type. This allows multiple, comma delimited. (optional)
     * @param mediaTypes Optional. Filter by MediaType. Allows multiple, comma delimited. (optional)
     * @param sortBy Optional. Specify one or more sort orders, comma delimited. Options: Album, AlbumArtist, Artist, Budget, CommunityRating, CriticRating, DateCreated, DatePlayed, PlayCount, PremiereDate, ProductionYear, SortName, Random, Revenue, Runtime. (optional)
     * @param enableUserData Optional. Include user data. (optional)
     * @param imageTypeLimit Optional. The max number of images to return, per image type. (optional)
     * @param enableImageTypes Optional. The image types to include in the output. (optional)
     * @param userId User Id. (optional)
     * @param recursive Search recursively. (optional, default to true)
     * @param enableImages Optional. Include image information in output. (optional, default to true)
     * @return BaseItemDtoQueryResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year query returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public BaseItemDtoQueryResult getYears(Integer startIndex, Integer limit, List<SortOrder> sortOrder, UUID parentId, List<ItemFields> fields, List<BaseItemKind> excludeItemTypes, List<BaseItemKind> includeItemTypes, List<MediaType> mediaTypes, List<ItemSortBy> sortBy, Boolean enableUserData, Integer imageTypeLimit, List<ImageType> enableImageTypes, UUID userId, Boolean recursive, Boolean enableImages) throws ApiException {
        ApiResponse<BaseItemDtoQueryResult> localVarResp = getYearsWithHttpInfo(startIndex, limit, sortOrder, parentId, fields, excludeItemTypes, includeItemTypes, mediaTypes, sortBy, enableUserData, imageTypeLimit, enableImageTypes, userId, recursive, enableImages);
        return localVarResp.getData();
    }

    /**
     * Get years.
     * 
     * @param startIndex Skips over a given number of items within the results. Use for paging. (optional)
     * @param limit Optional. The maximum number of records to return. (optional)
     * @param sortOrder Sort Order - Ascending,Descending. (optional)
     * @param parentId Specify this to localize the search to a specific item or folder. Omit to use the root. (optional)
     * @param fields Optional. Specify additional fields of information to return in the output. (optional)
     * @param excludeItemTypes Optional. If specified, results will be excluded based on item type. This allows multiple, comma delimited. (optional)
     * @param includeItemTypes Optional. If specified, results will be included based on item type. This allows multiple, comma delimited. (optional)
     * @param mediaTypes Optional. Filter by MediaType. Allows multiple, comma delimited. (optional)
     * @param sortBy Optional. Specify one or more sort orders, comma delimited. Options: Album, AlbumArtist, Artist, Budget, CommunityRating, CriticRating, DateCreated, DatePlayed, PlayCount, PremiereDate, ProductionYear, SortName, Random, Revenue, Runtime. (optional)
     * @param enableUserData Optional. Include user data. (optional)
     * @param imageTypeLimit Optional. The max number of images to return, per image type. (optional)
     * @param enableImageTypes Optional. The image types to include in the output. (optional)
     * @param userId User Id. (optional)
     * @param recursive Search recursively. (optional, default to true)
     * @param enableImages Optional. Include image information in output. (optional, default to true)
     * @return ApiResponse&lt;BaseItemDtoQueryResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year query returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<BaseItemDtoQueryResult> getYearsWithHttpInfo(Integer startIndex, Integer limit, List<SortOrder> sortOrder, UUID parentId, List<ItemFields> fields, List<BaseItemKind> excludeItemTypes, List<BaseItemKind> includeItemTypes, List<MediaType> mediaTypes, List<ItemSortBy> sortBy, Boolean enableUserData, Integer imageTypeLimit, List<ImageType> enableImageTypes, UUID userId, Boolean recursive, Boolean enableImages) throws ApiException {
        okhttp3.Call localVarCall = getYearsValidateBeforeCall(startIndex, limit, sortOrder, parentId, fields, excludeItemTypes, includeItemTypes, mediaTypes, sortBy, enableUserData, imageTypeLimit, enableImageTypes, userId, recursive, enableImages, null);
        Type localVarReturnType = new TypeToken<BaseItemDtoQueryResult>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Get years. (asynchronously)
     * 
     * @param startIndex Skips over a given number of items within the results. Use for paging. (optional)
     * @param limit Optional. The maximum number of records to return. (optional)
     * @param sortOrder Sort Order - Ascending,Descending. (optional)
     * @param parentId Specify this to localize the search to a specific item or folder. Omit to use the root. (optional)
     * @param fields Optional. Specify additional fields of information to return in the output. (optional)
     * @param excludeItemTypes Optional. If specified, results will be excluded based on item type. This allows multiple, comma delimited. (optional)
     * @param includeItemTypes Optional. If specified, results will be included based on item type. This allows multiple, comma delimited. (optional)
     * @param mediaTypes Optional. Filter by MediaType. Allows multiple, comma delimited. (optional)
     * @param sortBy Optional. Specify one or more sort orders, comma delimited. Options: Album, AlbumArtist, Artist, Budget, CommunityRating, CriticRating, DateCreated, DatePlayed, PlayCount, PremiereDate, ProductionYear, SortName, Random, Revenue, Runtime. (optional)
     * @param enableUserData Optional. Include user data. (optional)
     * @param imageTypeLimit Optional. The max number of images to return, per image type. (optional)
     * @param enableImageTypes Optional. The image types to include in the output. (optional)
     * @param userId User Id. (optional)
     * @param recursive Search recursively. (optional, default to true)
     * @param enableImages Optional. Include image information in output. (optional, default to true)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Year query returned. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getYearsAsync(Integer startIndex, Integer limit, List<SortOrder> sortOrder, UUID parentId, List<ItemFields> fields, List<BaseItemKind> excludeItemTypes, List<BaseItemKind> includeItemTypes, List<MediaType> mediaTypes, List<ItemSortBy> sortBy, Boolean enableUserData, Integer imageTypeLimit, List<ImageType> enableImageTypes, UUID userId, Boolean recursive, Boolean enableImages, final ApiCallback<BaseItemDtoQueryResult> _callback) throws ApiException {

        okhttp3.Call localVarCall = getYearsValidateBeforeCall(startIndex, limit, sortOrder, parentId, fields, excludeItemTypes, includeItemTypes, mediaTypes, sortBy, enableUserData, imageTypeLimit, enableImageTypes, userId, recursive, enableImages, _callback);
        Type localVarReturnType = new TypeToken<BaseItemDtoQueryResult>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}
