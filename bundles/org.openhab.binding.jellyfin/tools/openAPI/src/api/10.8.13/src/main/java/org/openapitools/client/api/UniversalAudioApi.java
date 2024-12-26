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


import java.io.File;
import java.util.UUID;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversalAudioApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public UniversalAudioApi() {
        this(Configuration.getDefaultApiClient());
    }

    public UniversalAudioApi(ApiClient apiClient) {
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
     * Build call for getUniversalAudioStream
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getUniversalAudioStreamCall(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/Audio/{itemId}/universal"
            .replace("{" + "itemId" + "}", localVarApiClient.escapeString(itemId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (container != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "container", container));
        }

        if (mediaSourceId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("mediaSourceId", mediaSourceId));
        }

        if (deviceId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("deviceId", deviceId));
        }

        if (userId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("userId", userId));
        }

        if (audioCodec != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("audioCodec", audioCodec));
        }

        if (maxAudioChannels != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxAudioChannels", maxAudioChannels));
        }

        if (transcodingAudioChannels != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("transcodingAudioChannels", transcodingAudioChannels));
        }

        if (maxStreamingBitrate != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxStreamingBitrate", maxStreamingBitrate));
        }

        if (audioBitRate != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("audioBitRate", audioBitRate));
        }

        if (startTimeTicks != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("startTimeTicks", startTimeTicks));
        }

        if (transcodingContainer != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("transcodingContainer", transcodingContainer));
        }

        if (transcodingProtocol != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("transcodingProtocol", transcodingProtocol));
        }

        if (maxAudioSampleRate != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxAudioSampleRate", maxAudioSampleRate));
        }

        if (maxAudioBitDepth != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxAudioBitDepth", maxAudioBitDepth));
        }

        if (enableRemoteMedia != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("enableRemoteMedia", enableRemoteMedia));
        }

        if (breakOnNonKeyFrames != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("breakOnNonKeyFrames", breakOnNonKeyFrames));
        }

        if (enableRedirection != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("enableRedirection", enableRedirection));
        }

        final String[] localVarAccepts = {
            "audio/*"
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
    private okhttp3.Call getUniversalAudioStreamValidateBeforeCall(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling getUniversalAudioStream(Async)");
        }

        return getUniversalAudioStreamCall(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection, _callback);

    }

    /**
     * Gets an audio stream.
     * 
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @return File
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public File getUniversalAudioStream(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection) throws ApiException {
        ApiResponse<File> localVarResp = getUniversalAudioStreamWithHttpInfo(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection);
        return localVarResp.getData();
    }

    /**
     * Gets an audio stream.
     * 
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @return ApiResponse&lt;File&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<File> getUniversalAudioStreamWithHttpInfo(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection) throws ApiException {
        okhttp3.Call localVarCall = getUniversalAudioStreamValidateBeforeCall(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection, null);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Gets an audio stream. (asynchronously)
     * 
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getUniversalAudioStreamAsync(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection, final ApiCallback<File> _callback) throws ApiException {

        okhttp3.Call localVarCall = getUniversalAudioStreamValidateBeforeCall(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection, _callback);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for headUniversalAudioStream
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call headUniversalAudioStreamCall(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/Audio/{itemId}/universal"
            .replace("{" + "itemId" + "}", localVarApiClient.escapeString(itemId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (container != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "container", container));
        }

        if (mediaSourceId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("mediaSourceId", mediaSourceId));
        }

        if (deviceId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("deviceId", deviceId));
        }

        if (userId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("userId", userId));
        }

        if (audioCodec != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("audioCodec", audioCodec));
        }

        if (maxAudioChannels != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxAudioChannels", maxAudioChannels));
        }

        if (transcodingAudioChannels != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("transcodingAudioChannels", transcodingAudioChannels));
        }

        if (maxStreamingBitrate != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxStreamingBitrate", maxStreamingBitrate));
        }

        if (audioBitRate != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("audioBitRate", audioBitRate));
        }

        if (startTimeTicks != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("startTimeTicks", startTimeTicks));
        }

        if (transcodingContainer != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("transcodingContainer", transcodingContainer));
        }

        if (transcodingProtocol != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("transcodingProtocol", transcodingProtocol));
        }

        if (maxAudioSampleRate != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxAudioSampleRate", maxAudioSampleRate));
        }

        if (maxAudioBitDepth != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxAudioBitDepth", maxAudioBitDepth));
        }

        if (enableRemoteMedia != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("enableRemoteMedia", enableRemoteMedia));
        }

        if (breakOnNonKeyFrames != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("breakOnNonKeyFrames", breakOnNonKeyFrames));
        }

        if (enableRedirection != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("enableRedirection", enableRedirection));
        }

        final String[] localVarAccepts = {
            "audio/*"
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
        return localVarApiClient.buildCall(basePath, localVarPath, "HEAD", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call headUniversalAudioStreamValidateBeforeCall(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'itemId' is set
        if (itemId == null) {
            throw new ApiException("Missing the required parameter 'itemId' when calling headUniversalAudioStream(Async)");
        }

        return headUniversalAudioStreamCall(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection, _callback);

    }

    /**
     * Gets an audio stream.
     * 
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @return File
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public File headUniversalAudioStream(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection) throws ApiException {
        ApiResponse<File> localVarResp = headUniversalAudioStreamWithHttpInfo(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection);
        return localVarResp.getData();
    }

    /**
     * Gets an audio stream.
     * 
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @return ApiResponse&lt;File&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<File> headUniversalAudioStreamWithHttpInfo(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection) throws ApiException {
        okhttp3.Call localVarCall = headUniversalAudioStreamValidateBeforeCall(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection, null);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Gets an audio stream. (asynchronously)
     * 
     * @param itemId The item id. (required)
     * @param container Optional. The audio container. (optional)
     * @param mediaSourceId The media version id, if playing an alternate version. (optional)
     * @param deviceId The device id of the client requesting. Used to stop encoding processes when needed. (optional)
     * @param userId Optional. The user id. (optional)
     * @param audioCodec Optional. The audio codec to transcode to. (optional)
     * @param maxAudioChannels Optional. The maximum number of audio channels. (optional)
     * @param transcodingAudioChannels Optional. The number of how many audio channels to transcode to. (optional)
     * @param maxStreamingBitrate Optional. The maximum streaming bitrate. (optional)
     * @param audioBitRate Optional. Specify an audio bitrate to encode to, e.g. 128000. If omitted this will be left to encoder defaults. (optional)
     * @param startTimeTicks Optional. Specify a starting offset, in ticks. 1 tick &#x3D; 10000 ms. (optional)
     * @param transcodingContainer Optional. The container to transcode to. (optional)
     * @param transcodingProtocol Optional. The transcoding protocol. (optional)
     * @param maxAudioSampleRate Optional. The maximum audio sample rate. (optional)
     * @param maxAudioBitDepth Optional. The maximum audio bit depth. (optional)
     * @param enableRemoteMedia Optional. Whether to enable remote media. (optional)
     * @param breakOnNonKeyFrames Optional. Whether to break on non key frames. (optional, default to false)
     * @param enableRedirection Whether to enable redirection. Defaults to true. (optional, default to true)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Audio stream returned. </td><td>  -  </td></tr>
        <tr><td> 302 </td><td> Redirected to remote audio stream. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Forbidden </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call headUniversalAudioStreamAsync(UUID itemId, List<String> container, String mediaSourceId, String deviceId, UUID userId, String audioCodec, Integer maxAudioChannels, Integer transcodingAudioChannels, Integer maxStreamingBitrate, Integer audioBitRate, Long startTimeTicks, String transcodingContainer, String transcodingProtocol, Integer maxAudioSampleRate, Integer maxAudioBitDepth, Boolean enableRemoteMedia, Boolean breakOnNonKeyFrames, Boolean enableRedirection, final ApiCallback<File> _callback) throws ApiException {

        okhttp3.Call localVarCall = headUniversalAudioStreamValidateBeforeCall(itemId, container, mediaSourceId, deviceId, userId, audioCodec, maxAudioChannels, transcodingAudioChannels, maxStreamingBitrate, audioBitRate, startTimeTicks, transcodingContainer, transcodingProtocol, maxAudioSampleRate, maxAudioBitDepth, enableRemoteMedia, breakOnNonKeyFrames, enableRedirection, _callback);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}
