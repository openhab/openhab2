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


import org.openapitools.client.model.MetadataRefreshMode;
import org.openapitools.client.model.ProblemDetails;
import java.util.UUID;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRefreshApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ItemRefreshApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ItemRefreshApi(ApiClient apiClient) {
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
     * Build call for refreshItem
     * @param itemId Item id. (required)
     * @param metadataRefreshMode (Optional) Specifies the metadata refresh mode. (optional, default to None)
     * @param imageRefreshMode (Optional) Specifies the image refresh mode. (optional, default to None)
     * @param replaceAllMetadata (Optional) Determines if metadata should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @param replaceAllImages (Optional) Determines if images should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> Item metadata refresh queued. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Item to refresh not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call refreshItemCall(UUID itemId, MetadataRefreshMode metadataRefreshMode, MetadataRefreshMode imageRefreshMode, Boolean replaceAllMetadata, Boolean replaceAllImages, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/Items/{itemId}/Refresh"
            .replace("{" + "itemId" + "}", localVarApiClient.escapeString(itemId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (metadataRefreshMode != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("metadataRefreshMode", metadataRefreshMode));
        }

        if (imageRefreshMode != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("imageRefreshMode", imageRefreshMode));
        }

        if (replaceAllMetadata != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("replaceAllMetadata", replaceAllMetadata));
        }

        if (replaceAllImages != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("replaceAllImages", replaceAllImages));
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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call refreshItemValidateBeforeCall(UUID itemId, MetadataRefreshMode metadataRefreshMode, MetadataRefreshMode imageRefreshMode, Boolean replaceAllMetadata, Boolean replaceAllImages, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling refreshItem(Async)");
        }

        return refreshItemCall(itemId, metadataRefreshMode, imageRefreshMode, replaceAllMetadata, replaceAllImages, _callback);

    }

    /**
     * Refreshes metadata for an item.
     * 
     * @param itemId Item id. (required)
     * @param metadataRefreshMode (Optional) Specifies the metadata refresh mode. (optional, default to None)
     * @param imageRefreshMode (Optional) Specifies the image refresh mode. (optional, default to None)
     * @param replaceAllMetadata (Optional) Determines if metadata should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @param replaceAllImages (Optional) Determines if images should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> Item metadata refresh queued. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Item to refresh not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public void refreshItem(UUID itemId, MetadataRefreshMode metadataRefreshMode, MetadataRefreshMode imageRefreshMode, Boolean replaceAllMetadata, Boolean replaceAllImages) throws ApiException {
        refreshItemWithHttpInfo(itemId, metadataRefreshMode, imageRefreshMode, replaceAllMetadata, replaceAllImages);
    }

    /**
     * Refreshes metadata for an item.
     * 
     * @param itemId Item id. (required)
     * @param metadataRefreshMode (Optional) Specifies the metadata refresh mode. (optional, default to None)
     * @param imageRefreshMode (Optional) Specifies the image refresh mode. (optional, default to None)
     * @param replaceAllMetadata (Optional) Determines if metadata should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @param replaceAllImages (Optional) Determines if images should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> Item metadata refresh queued. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Item to refresh not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<Void> refreshItemWithHttpInfo(UUID itemId, MetadataRefreshMode metadataRefreshMode, MetadataRefreshMode imageRefreshMode, Boolean replaceAllMetadata, Boolean replaceAllImages) throws ApiException {
        okhttp3.Call localVarCall = refreshItemValidateBeforeCall(itemId, metadataRefreshMode, imageRefreshMode, replaceAllMetadata, replaceAllImages, null);
        return localVarApiClient.execute(localVarCall);
    }

    /**
     * Refreshes metadata for an item. (asynchronously)
     * 
     * @param itemId Item id. (required)
     * @param metadataRefreshMode (Optional) Specifies the metadata refresh mode. (optional, default to None)
     * @param imageRefreshMode (Optional) Specifies the image refresh mode. (optional, default to None)
     * @param replaceAllMetadata (Optional) Determines if metadata should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @param replaceAllImages (Optional) Determines if images should be replaced. Only applicable if mode is FullRefresh. (optional, default to false)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> Item metadata refresh queued. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Item to refresh not found. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call refreshItemAsync(UUID itemId, MetadataRefreshMode metadataRefreshMode, MetadataRefreshMode imageRefreshMode, Boolean replaceAllMetadata, Boolean replaceAllImages, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = refreshItemValidateBeforeCall(itemId, metadataRefreshMode, imageRefreshMode, replaceAllMetadata, replaceAllImages, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }
}
