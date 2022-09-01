package in.ifarms.com;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.ifarms.com.EquipmentQrCodeSearch.BodyOfUpdate;
import in.ifarms.com.FaultReportActivity.BuildingLocationResponse;
import in.ifarms.com.FaultReportActivity.CreateFaultRequest;
import in.ifarms.com.FaultReportActivity.CreateReportResponse;
import in.ifarms.com.FaultReportActivity.FaultReportResponse;
import in.ifarms.com.FaultReportActivity.UploadPictureRequest;
import in.ifarms.com.FaultReportSearch.FaultReportBodyRequest;
import in.ifarms.com.General.TechnicianListResponse;
import in.ifarms.com.General.UserRequest;
import in.ifarms.com.General.UserResponse;
import in.ifarms.com.OcrDashboardItem.GetMeterListResponse;
import in.ifarms.com.OcrDashboardItem.MeterSelectedResponse;
import in.ifarms.com.OcrDashboardItem.PostImageResponse;
import in.ifarms.com.OcrDashboardItem.SaveUtilityRequest;
import in.ifarms.com.OcrDashboardItem.UploadImageOcrRequest;
import in.ifarms.com.QrFaultReportScan.CreateFaultQrReport;
import in.ifarms.com.Search.EditFaultReportRequest;
import in.ifarms.com.Search.EditFaultReportResponse;
import in.ifarms.com.Search.EquipmentSearchResponse;
import in.ifarms.com.Search.SearchResponse;
import in.ifarms.com.Search.UpdateFaultReportResponse;
import in.ifarms.com.TaskSearch.CheckListAddRequest;
import in.ifarms.com.TaskSearch.GetBuildingsResponse;
import in.ifarms.com.TaskSearch.GetPmTaskItemsResponse;
import in.ifarms.com.TaskSearch.GetUpdatePmTaskRequest;
import in.ifarms.com.TaskSearch.GetUpdatePmTaskResponse;
import in.ifarms.com.TaskSearch.TaskSearchResponse;
import in.ifarms.com.TaskSearch.UploadSignatureRequest;
import in.ifarms.com.singpost.SingpostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @POST("loginservice")
    Call<UserResponse> saveUser(@Body UserRequest userRequest);

    @POST("api/logout")
    Call<Void> logoutUser(@Body LogoutClass logoutClass);

    @POST("ws/upload{before}image")
    @Headers("Content-Type: application/json")
    Call<Void> uploadCaptureImage(@Path("before") String before,
                                  @Header("X-Auth-Token") String token,
                                  @Header("workspace") String workspace,
                                  @Body UploadPictureRequest uploadPictureRequest);

    @GET("ws/faultreport/search/?")
    @Headers("Content-Type: application/json")
    Call<List<SearchResponse>> getSearchResult(@Header("X-Auth-Token") String dynamicToken,
                                               @Header("workspace") String dynamicWorkSpace,
                                               @Query("query") String param);

    //this is for editfault report call
    @GET("ws/faultreport/{frid}")
    @Headers("Content-Type: application/json")
    Call<EditFaultReportResponse> getReport(@Path("frid") String id,
                                            @Header("X-Auth-Token") String token,
                                            @Header("workspace") String workspace);


    //update Fault Report request
    //@PUT("engrproject/ws/faultreport")
    @PUT("ws/faultreport/")
    @Headers("Content-Type: application/json")
    Call<UpdateFaultReportResponse> updateReport(@Body EditFaultReportRequest editFaultReportRequest,
                                                 @Header("X-Auth-Token") String token,
                                                 @Header("workspace") String workspace);

    //to get the technician list
    @GET("ws/techlist4")
    @Headers("Content-Type: application/json")
    Call<List<TechnicianListResponse>> technicianCall(@Header("X-Auth-Token") String token,
                                                      @Header("workspace") String workspace);

    //this is for get equipment
    @GET("ws/equipment/search/{query}")
    Call<List<EquipmentSearchResponse>> getEquipment(@Path("query") String query,
                                                     @Header("X-Auth-Token") String token,
                                                     @Header("workspace") String workspace);

    //this is for taskSearch call
    @GET("ws/cs/allTasks/")
    @Headers("Content-Type: application/json")
    Call<ArrayList<TaskSearchResponse>> getTask(@Query("zoneid") String buildingId,
                                                @Query("subzoneid") String locationId,
                                                @Header("X-Auth-Token") String token,
                                                @Header("workspace") String workspace);

    //get building list
    @GET("ws/getbuildings")
    @Headers("Content-Type: application/json")
    Call<List<GetBuildingsResponse>> getBuildingList(@Header("X-Auth-Token") String token,
                                                     @Header("workspace") String workspace);


    //get Globe details
    @GET("ws/getOpenFaultReports/")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getMap(@Query("zoneid") String buildingId,
                            @Query("subzoneid") String locationId,
                            @Header("X-Auth-Token") String token,
                            @Header("workspace") String workspace);


    //Get Pm Task Items
    @GET("ws/cs/task/{path}")
    @Headers("Content-Type: application/json")
    Call<GetPmTaskItemsResponse> getPmItemsTasks(@Path("path") String taskNumber,
                                                 @Header("X-Auth-Token") String token,
                                                 @Header("workspace") String workspace);

    @POST("ws/cs/task")
    @Headers("Content-Type: application/json")
    Call<GetUpdatePmTaskResponse> postPmTaskUpdate(
            @Header("X-Auth-Token") String token,
            @Header("workspace") String workspace,
            @Body GetUpdatePmTaskRequest getUpdatePmTaskRequest);


    @GET("ws/cs/taskchecklist/{path}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getCheckListResponse(@Path("path") String taskNumber,
                                          @Header("X-Auth-Token") String token,
                                          @Header("workspace") String workspace);

    //To save checklist
    @POST("ws/cs/taskchecklistadd")
    @Headers("Content-Type: application/json")
    Call<Void> getCheckListAdd(@Body List<CheckListAddRequest> checkListAddRequest,
                               @Header("X-Auth-Token") String token,
                               @Header("workspace") String workspace);


    //enable notification
    @GET("ws/dr/not/{zero}/{path}")
    @Headers("Content-Type: application/json")
    Call<Void> getNotification(@Path("zero") String zeroOrOne,
                               @Path("path") String devicetoken,
                               @Header("X-Auth-Token") String token,
                               @Header("workspace") String workspace);

    //register device for receiving notification
    @GET("ws/dr/{path}/{workspace}")
    @Headers("Content-Type: application/json")
    Call<Void> registerDevice(@Path("path") String devicetoken,
                              @Path("workspace") String work,
                              @Header("X-Auth-Token") String token,
                              @Header("workspace") String workspace);

    //Upload Signature
    @POST("ws/cs/task/signature")
    @Headers("Content-Type: application/json")
    Call<Void> getSignatureCall(@Body UploadSignatureRequest uploadSignatureRequest,
                                @Header("X-Auth-Token") String token,
                                @Header("workspace") String workspace);

    //Meter list get call in OCR
    @GET("utws/meters")
    @Headers("Content-Type: application/json")
    Call<List<GetMeterListResponse>> getMeterList(@Header("X-Auth-Token") String token,
                                                  @Header("workspace") String workspace);

    //get list of fault search
    @POST("ws/faultreport/searchcri/{path}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getfaultList(@Path("path") int countScroll,
                                  @Body FaultReportBodyRequest faultReportBodyRequest,
                                  @Header("X-Auth-Token") String token,
                                  @Header("workspace") String workspace);


    //MeterSelected Response
    @GET("utws/prevReading/{path}")
    @Headers("Content-Type: application/json")
    Call<MeterSelectedResponse> getMeterSelected(@Path("path") String meterId,
                                                 @Header("X-Auth-Token") String token,
                                                 @Header("workspace") String workspace);

    //UploadImage Ocr
    @POST("ms/utilityreading4/ocr")
    @Headers("Content-Type: application/json")
    Call<PostImageResponse> postImageUploadOcr(@Body UploadImageOcrRequest uploadImageOcrRequest,
                                               @Header("X-Auth-Token") String token,
                                               @Header("workspace") String workspace);

    //Verify the image that was uploaded
    @GET("ms/ocrimage2/313")
    Call<ResponseBody> getVerifyOcr();

    //create new meter reading
    @POST("utws/saveUtility4")
    @Headers("Content-Type: application/json")
    Call<Void> postSaveUtility(@Body SaveUtilityRequest saveUtilityRequest,
                               @Header("X-Auth-Token") String token,
                               @Header("workspace") String workspace);


    //get qr search list
    @GET("ws/cs/taskbyequipcode4/{path}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getQrSeachList(@Path("path") String meterId,
                                    @Header("X-Auth-Token") String token,
                                    @Header("workspace") String workspace);


    //get tasts on qr    http://ifarms.com.sg:9000/cmms/ws/br/tasks/ACMV8312/OPEN
    @GET("ws/br/tasks/{path}/{patch}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getTaskOnQrList(@Path("path") String meterId, @Path("patch") String status,
                                    @Header("X-Auth-Token") String token,
                                    @Header("workspace") String workspace);


    //get faultreport Equip
    @GET("ws/br/{path}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getfaultOnQrList(@Path("path") String meterId,
                                     @Header("X-Auth-Token") String token,
                                     @Header("workspace") String workspace);


    //geting the tno update detail
    @GET("ws/cs/task/{path}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getTaskupdateDetail(@Path("path") String meterId,
                                         @Header("X-Auth-Token") String token,
                                         @Header("workspace") String workspace);

    //update tn0 details
    @POST("ws/cs/task")
    @Headers("Content-Type: application/json")
    Call<Void> updateQrTask(@Body BodyOfUpdate bodyOfUpdate,
                            @Header("X-Auth-Token") String token,
                            @Header("workspace") String workspace);


    //get qr fault created list
    @GET("ms/equipsearch/{path}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getQrFaultReportCreated(@Path("path") String meterId,
                                             @Header("X-Auth-Token") String token,
                                             @Header("workspace") String workspace);


    @GET("ws/faultdependencies/")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getretrieveInfoInFaultReport(@Header("X-Auth-Token") String tokenNumber, @Header("workspace") String workspaceList);

    //create Fault report by qr scan
    @POST("ws/faultreportequipment")
    @Headers("Content-Type: application/json")
    Call<JsonObject> qRcreateFaultRequest(@Body CreateFaultQrReport createFaultQrReport,
                                          @Header("X-Auth-Token") String token,
                                          @Header("workspace") String workspace);

    //create Fault Report
    @POST("ws/faultreport")
    @Headers("Content-Type: application/json")
    Call<CreateReportResponse> createFaultRequest(@Body CreateFaultRequest createFaultRequest,
                                                  @Header("X-Auth-Token") String token,
                                                  @Header("workspace") String workspace);

    //Building location list
    @GET("ws/offlinedata/locations/{buildingId}")
    @Headers("Content-Type: application/json")
    Call<ArrayList<BuildingLocationResponse>> buildingLocationCall(@Path("buildingId") String buildingId,
                                                                   @Header("workspace") String workspace,
                                                                   @Header("X-Auth-Token") String tokenNumber);

    @GET("ws/faultdependencies/")
    @Headers("Content-Type: application/json")
    Call<FaultReportResponse> retrieveInfoInFaultReport(@Header("X-Auth-Token") String tokenNumber,
                                                        @Header("workspace") String workspaceList);


    //get singpost
    @GET("ws/searchequipment/{id}")
    @Headers("Content-Type: application/json")
    Call<SingpostModel> getSingPost(@Path("id") long id,
                                    @Header("X-Auth-Token")String token,
                                    @Header("workspace") String workspace);

    // post singpost
    @POST("ws/updateEquipment")
    @Headers("Content-Type: application/json")
    Call<Void> createSingPost(@Body SingpostModel EquipmentSingPostDTO,
                              @Header("X-Auth-Token") String token,
                              @Header("workspace") String workspace);

//get sing image
    @POST("ws/getlogcard")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getbase64String(@Body SingpostModel singpostModel,
                                    @Header("X-Auth-Token")String token,
                                    @Header("workspace") String workspace);

    //get sing pdf
    @POST("ws/getlogcard")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getPdf(@Body SingpostModel singpostModel,
                                       @Header("X-Auth-Token")String token,
                                       @Header("workspace") String workspace);
}
