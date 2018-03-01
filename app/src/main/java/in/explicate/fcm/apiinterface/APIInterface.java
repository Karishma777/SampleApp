package in.explicate.fcm.apiinterface;

import com.google.gson.JsonObject;

import in.explicate.fcm.apiinterface.models.ActivityAPIModel;
import in.explicate.fcm.apiinterface.models.ActivityDetailAPIModel;
import in.explicate.fcm.apiinterface.models.ActivityProjectAPIModel;
import in.explicate.fcm.apiinterface.models.ActivityProjectDetailAPIModel;
import in.explicate.fcm.apiinterface.models.BadmintonAPIModel;
import in.explicate.fcm.apiinterface.models.CategoryApiModel;
import in.explicate.fcm.apiinterface.models.ChangePasswordModel;
import in.explicate.fcm.apiinterface.models.ClassesAPIModel;
import in.explicate.fcm.apiinterface.models.ClassesDetailAPIModel;
import in.explicate.fcm.apiinterface.models.ClassifiedApiModel;
import in.explicate.fcm.apiinterface.models.ClassifiedCategoryAPIModel;
import in.explicate.fcm.apiinterface.models.ClassifiedCountApiModel;
import in.explicate.fcm.apiinterface.models.ClassifiedDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.ClubHouseAPIModel;
import in.explicate.fcm.apiinterface.models.ClubhouseEventApiModel;
import in.explicate.fcm.apiinterface.models.CommitteApiModel;
import in.explicate.fcm.apiinterface.models.CommitteeMembarApi;
import in.explicate.fcm.apiinterface.models.CounsilApiModel;
import in.explicate.fcm.apiinterface.models.CounsilMemberApiModel;
import in.explicate.fcm.apiinterface.models.DepartmentApiDetail;
import in.explicate.fcm.apiinterface.models.DeviceAPIModel;
import in.explicate.fcm.apiinterface.models.EmployeeAPIModel;
import in.explicate.fcm.apiinterface.models.FeedbackAPIModel;
import in.explicate.fcm.apiinterface.models.FeedbackCommitteeAPIModel;
import in.explicate.fcm.apiinterface.models.FeedbackCommitteeDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.FeedbackDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.GetInfraSearchModel;
import in.explicate.fcm.apiinterface.models.MyInfraInfoAPI;
import in.explicate.fcm.apiinterface.models.NewsLetterAPIModel;
import in.explicate.fcm.apiinterface.models.OTPModel;
import in.explicate.fcm.apiinterface.models.PendingComplaintAPIModel;
import in.explicate.fcm.apiinterface.models.RegistrationModel;
import in.explicate.fcm.apiinterface.models.ResidentialProjectApiModel;
import in.explicate.fcm.apiinterface.models.ResponseModel;
import in.explicate.fcm.apiinterface.models.StdProcDetailModel;
import in.explicate.fcm.apiinterface.models.SuggesstionAPIModel;
import in.explicate.fcm.apiinterface.models.SuggestionCommitteeAPIModel;
import in.explicate.fcm.apiinterface.models.SuggestionCommitteeDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.SuggestionDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.VerifyOTPModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {


  /*

    @POST("social_register")
    Call<User> createUser(@Body User user);

    @GET("get_social_content_link?")
    Call<DigitalContent> doGetDigitalContent(@Query("user_id") String id, @Query("target_id") String target_id);

    @GET("get_reward?")
    Call<Reward> getReward(@Query("user_id") String id, @Query("target_id") String target_id);

    @POST("user_analytics")
    Call<Analytics> addAnalytics(@Body Analytics analytics);

    */


    /* Emergency Contact API */

    @POST("EmergencyContactsRService/retrieveAllEmergencyContacts")
    Call<EmployeeAPIModel> retrieveAllEmergencyContacts();

    @POST("EmergencyContactsRService/retrieveTotalEmergencyContactsCount")
    Call<JsonObject> retrieveTotalEmergencyContactsCount();

    @POST("EmergencyContactsRService/retrieveModifiedEmergencyContacts")
    Call<EmployeeAPIModel> retrieveModifiedEmergencyContacts();

    @POST("EmergencyContactsRService/retrieveNewlyCreatedEmergencyContacts")
    Call<EmployeeAPIModel> retrieveNewlyCreatedEmergencyContacts();


     /* NewsLetters API */

    @POST("NewsLettersRService/getAllNewsLetters")
    Call<NewsLetterAPIModel> getAllNewsLetters();

    @POST("NewsLettersRService/downloadSelectedNewsLetter")
    Call<ResponseModel> createUser();


    @POST("NewsLettersRService/searchAllNewsLetters")
    @FormUrlEncoded
    Call<NewsLetterAPIModel> getNewsletterSearchResult(@Field("searchNL") String text);

    //parameters in this model


    /*****Committees API *****/

    @POST("CommitteesRService/getCommittees")
    Call<CommitteApiModel> getCommittees();

    @FormUrlEncoded
    @POST("CommitteesRService/getCommitteeMembers")
    Call<CommitteeMembarApi> getCommitteeMembers(@Field("selectedCommitteeID") String selectedCommitteeID);


    /*****Council API *****/

    @POST("CouncilsRService/getCouncils")
    Call<CounsilApiModel> getCouncils();

    @FormUrlEncoded
    @POST("CouncilsRService/getCouncilMembers")
    Call<CounsilMemberApiModel> getCouncilMembers(@Field("selectedCouncilID") String selectedCouncilID);


    /*****Standard Procedure *****/

    @POST("StandardProceduresRService/getStandardProceduresByDepartment")
    @FormUrlEncoded
    Call<StdProcDetailModel> getStandardProceduresByDepartment(@Field("selectedDepartmentID") String id);

    @POST("DepartmentsRService/getDepartments")
    Call<DepartmentApiDetail> getDepartments();

    @POST("StandardProceduresRService/getStandardProcedureDetailsById")
    @FormUrlEncoded
    Call<StdProcDetailModel> getStandardProcedureDetailsById(@Field("selectedSopID") String id);

    @POST("StandardProceduresRService/getStandardProceduresBySearch")
    @FormUrlEncoded
    Call<StdProcDetailModel> getStandardProceduresBySearch(@Field("searchSop") String string);


    /******Activity API *****/


    @POST("ActivityRService/getCommonActivities")
    Call<ActivityAPIModel> getCommonActivities();

    @POST("ActivityRService/getActivityDetails")
    @FormUrlEncoded
    Call<ActivityDetailAPIModel> getActivityDetails(@Field("selectedActivityID") String selectedActivityID);

    @POST("ActivityRService/searchCommonActivities")
    @FormUrlEncoded
    Call<ActivityAPIModel> searchCommonActivities(@Field("searchCommonActivity") String text);


    @POST("ActivityRService/getMyProjectActivities")
    @FormUrlEncoded
    Call<ActivityProjectAPIModel> getMyProjectActivities(@Field("loggedinUserID") String myProjectID);

    @POST("ActivityRService/getProjectActivityDetails")
    @FormUrlEncoded
    Call<ActivityProjectDetailAPIModel> getProjectActivityDetails(@Field("selectedActivityID") String selectedActivityID);

    @POST("ActivityRService/searchMyProjectActivity")
    @FormUrlEncoded
    Call<ActivityProjectAPIModel> searchMyProjectActivity(@Field("searchMyProjectActivity") String text, @Field("loggedinUserID") String myProjectID);


    /**
     * Feedback
     ****/

    @POST("FeedbackSuggestionRService/retrieveAllFeedbacks")
    @FormUrlEncoded
    Call<FeedbackAPIModel> retrieveAllFeedbacks(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/retrieveFeedbackDetails")
    @FormUrlEncoded
    Call<FeedbackDetailsAPIModel> retrieveFeedbackDetails(@Field("selectedFeedbackID") String selectedFeedbackID, @Field("loggedinUserID") String loggedUserID);

    @FormUrlEncoded
    @POST("FeedbackSuggestionRService/addFeedback")
    Call<JsonObject> addFeedback(@Field("subject") String subject, @Field("description") String description, @Field("selectedDeptID") String selectedDeptID, @Field("loggedinUserID") String loggedinUserID);


    @POST("FeedbackSuggestionRService/isFeedbackAllowed")
    @FormUrlEncoded
    Call<JsonObject> isFeedbackAllowed(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/getFeedbackStatus")
    @FormUrlEncoded
    Call<JsonObject> getFeedbackStatus(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/searchMyAllFeedbacksByDate")
    @FormUrlEncoded
    Call<FeedbackAPIModel> searchMyAllFeedbacksByDate(@Field("loggedinUserID") String loggedinUserID, @Field("fromDate") String fromDate, @Field("toDate") String toDate);


    @POST("FeedbackSuggestionRService/searchMyAllFeedbacks")
    @FormUrlEncoded
    Call<FeedbackAPIModel> searchMyAllFeedbacks(@Field("loggedinUserID") String loggedinUserID, @Field("searchFeedback") String searchFeedback);


    /**
     * Feedback TO Committes
     ****/

    @POST("FeedbackSuggestionRService/retrieveAllCommitteeFeedbacks")
    @FormUrlEncoded
    Call<FeedbackCommitteeAPIModel> retrieveAllCommitteeFeedbacks(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/retrieveCommitteeFeedbackDetails")
    @FormUrlEncoded
    Call<FeedbackCommitteeDetailsAPIModel> retrieveCommitteeFeedbackDetails(@Field("selectedFeedbackID") String selectedFeedbackID, @Field("loggedinUserID") String loggedUserID);

    @POST("FeedbackSuggestionRService/addCommitteeFeedback")
    @FormUrlEncoded
    Call<JsonObject> addCommitteeFeedback(@Field("loggedinUserID") String loggedinUserID, @Field("subject") String subject, @Field("description") String description, @Field("selectedCommitteeID") String selectedDeptID);


    @POST("FeedbackSuggestionRService/isCommitteeFeedbackAllowed")
    @FormUrlEncoded
    Call<JsonObject> isCommitteeFeedbackAllowed(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/getCommitteeFeedbackStatus")
    @FormUrlEncoded
    Call<JsonObject> getCommitteeFeedbackStatus(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/searchMyAllCommitteeFeedbacksByDate")
    @FormUrlEncoded
    Call<FeedbackCommitteeAPIModel> searchMyAllCommitteeFeedbacksByDate(@Field("loggedinUserID") String loggedinUserID, @Field("fromDate") String fromDate, @Field("toDate") String toDate);


    @POST("FeedbackSuggestionRService/searchMyAllCommitteeFeedbacks")
    @FormUrlEncoded
    Call<FeedbackCommitteeAPIModel> searchMyAllCommitteeFeedbacks(@Field("loggedinUserID") String loggedinUserID, @Field("searchCommitteeFeedback") String searchFeedback);


    /**
     * Suggestion To Magarpatta
     ****/

    @POST("FeedbackSuggestionRService/retrieveAllSuggestions")
    @FormUrlEncoded
    Call<SuggesstionAPIModel> retrieveAllSuggestions(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/retrieveSuggestionDetails")
    @FormUrlEncoded
    Call<SuggestionDetailsAPIModel> retrieveSuggestionDetails(@Field("selectedSuggestionID") String selectedFeedbackID, @Field("loggedinUserID") String loggedUserID);

    @POST("FeedbackSuggestionRService/addSuggestion")
    @FormUrlEncoded
    Call<JsonObject> addSuggestion(@Field("loggedinUserID") String loggedinUserID, @Field("subject") String subject, @Field("description") String description, @Field("selectedDeptID") String selectedDeptID);


    @POST("FeedbackSuggestionRService/isSuggestionAllowed")
    @FormUrlEncoded
    Call<JsonObject> isSuggestionAllowed(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/getSuggestionStatus")
    @FormUrlEncoded
    Call<JsonObject> getSuggestionStatus(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/searchMyAllSuggestionsByDate")
    @FormUrlEncoded
    Call<SuggesstionAPIModel> searchMyAllSuggestionsByDate(@Field("loggedinUserID") String loggedinUserID, @Field("fromDate") String fromDate, @Field("toDate") String toDate);


    @POST("FeedbackSuggestionRService/searchMyAllSuggestions")
    @FormUrlEncoded
    Call<SuggesstionAPIModel> searchMyAllSuggestions(@Field("loggedinUserID") String loggedinUserID, @Field("searchSuggestion") String searchFeedback);


    /**
     * Suggestion TO Committes
     ****/

    @POST("FeedbackSuggestionRService/retrieveAllCommitteeSuggestions")
    @FormUrlEncoded
    Call<SuggestionCommitteeAPIModel> retrieveAllCommitteeSuggestions(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/retrieveCommitteeSuggestionDetails")
    @FormUrlEncoded
    Call<SuggestionCommitteeDetailsAPIModel> retrieveCommitteeSuggestionDetails(@Field("selectedSuggestionID") String selectedFeedbackID, @Field("loggedinUserID") String loggedUserID);

    @POST("FeedbackSuggestionRService/addCommitteeSuggestion")
    @FormUrlEncoded
    Call<JsonObject> addCommitteeSuggestion(@Field("loggedinUserID") String loggedinUserID, @Field("subject") String subject, @Field("description") String description, @Field("selectedCommitteeID") String selectedDeptID);


    @POST("FeedbackSuggestionRService/isCommitteeSuggestionAllowed")
    @FormUrlEncoded
    Call<JsonObject> isCommitteeSuggestionAllowed(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/getCommitteeSuggestionStatus")
    @FormUrlEncoded
    Call<JsonObject> getCommitteeSuggestionStatus(@Field("loggedinUserID") String loggedinUserID);

    @POST("FeedbackSuggestionRService/searchMyAllCommitteeSuggestionsByDate")
    @FormUrlEncoded
    Call<SuggestionCommitteeAPIModel> searchMyAllCommitteeSuggestionsByDate(@Field("loggedinUserID") String loggedinUserID, @Field("fromDate") String fromDate, @Field("toDate") String toDate);


    @POST("FeedbackSuggestionRService/searchMyAllCommitteeSuggestions")
    @FormUrlEncoded
    Call<SuggestionCommitteeAPIModel> searchMyAllCommitteeSuggestions(@Field("loggedinUserID") String loggedinUserID, @Field("searchCommitteeSuggestion") String searchFeedback);


    /**
     * Badminton API
     ***/


    @POST("BadmintonRService/getSelectedDateAvailableTimeSlots")
    @FormUrlEncoded
    Call<BadmintonAPIModel> getSelectedDateAvailableTimeSlots(@Field("selectedDate") String selectedDate);

    /****MyInfrInfo API *****/

    @POST("InfraRService/getMyInfra")
    @FormUrlEncoded
    Call<MyInfraInfoAPI> getMyInfra(@Field("loggedinUserID") String loggedinUserID);

    @POST("InfraRService/getInfraInfo")
    @FormUrlEncoded
    Call<MyInfraInfoAPI> getInfraInfo(@Field("loggedInUserInfraID") String loggedInUserInfraID, @Field("loggedinUserID") String loggedinUserID);

    /***My Device ****/
    @POST("UserDevicesRService/getMyDevices")
    @FormUrlEncoded
    Call<DeviceAPIModel> getMyDevices(@Field("loggedinUserID") String loggedinUserID);

    @POST("ProjectsRService/getResidentialProjects")
    Call<ResidentialProjectApiModel> getResidentialProjects();


    @POST("InfraRService/getNotMyInfra")
    @FormUrlEncoded
    Call<MyInfraInfoAPI> getNotMyInfra(@Field("infraSearchstr") String infraSearchstr, @Field("selectedProjectID") String selectedProjectID, @Field("loggedinUserID") String loggedinUserID);


    @POST("UserInfraRService/requestUserInfra")
    @FormUrlEncoded
    Call<JsonObject> requestUserInfra(@Field("loggedinUserID") String loggedinUserID, @Field("selectedInfraID") String selectedInfraID, @Field("citizenID") String citizenID);


    /***Clubhouse*****/

    @POST("ClubhouseRService/getAllClubhouses")
    Call<ClubHouseAPIModel> getAllClubhouses();

    @POST("ClubhouseRService/retrieveAvailableClubHouse")
    @FormUrlEncoded
    Call<JsonObject> retrieveAvailableClubHouse(@Field("selectedDate") String selectedDate, @Field("selectedClubhouseID") String selectedClubhouseID);

    @POST("ClubhouseRService/getCurrentRecreationalClasses")
    @FormUrlEncoded
    Call<ClassesAPIModel> getCurrentRecreationalClasses(@Field("selectedClubhouseID") String selectedClubhouseID);

    @POST("ClubhouseRService/getRecreationalClassesDetails")
    @FormUrlEncoded
    Call<ClassesDetailAPIModel> getRecreationalClassesDetails(@Field("selectedClassBookingID") String selectedClassBookingID);

    @POST("ClubhouseRService/getAllClubhouseEvents")
    Call<ClubhouseEventApiModel> getAllClubhouseEvents();

    @POST("ClubhouseRService/clubhouseReservation")
    @FormUrlEncoded
    Call<JsonObject> clubhouseReservation(@Field("selectedClubhouseID") String selectedClubhouseID, @Field("selectedEventID") String selectedEventID, @Field("loggedinUserID") String loggedinUserID, @Field("selectedDate") String selectedDate);


    /***Classfieds*****/


    @POST("ClassifiedsRService/getAllClassifiedsCategories")
    Call<ClassifiedCategoryAPIModel> getAllClassifiedsCategories();

    @POST("ClassifiedsRService/addClassifieds")
    @FormUrlEncoded
    Call<JsonObject> addClassifieds(@Field("subject") String subject, @Field("description") String description, @Field("selectedClassifiedsCategoryID") String selectedClassifiedsCategoryID, @Field("loggedinUserID") String loggedinUserID);

    @POST("ClassifiedsRService/getMyClassifiedsByCategoryCount")
    @FormUrlEncoded
    Call<ClassifiedCountApiModel> getMyClassifiedsByCategoryCount(@Field("loggedinUserID") String loggedinUserID);

    @POST("ClassifiedsRService/getMyClassifiedsByCategory")
    @FormUrlEncoded
    Call<ClassifiedApiModel> getMyClassifiedsByCategory(@Field("loggedinUserID") String loggedinUserID, @Field("selectedCategoryID") String selectedCategoryID);

    @POST("ClassifiedsRService/addResponseReplyMyClassified")
    @FormUrlEncoded
    Call<JsonObject> addResponseReplyMyClassified(@Field("selectedResponseID") String selectedResponseID, @Field("loggedinUserID") String loggedinUserID, @Field("reply") String reply);

    //Pending API to get details and replies

    @POST("ClassifiedsRService/getMyClassifiedDetails")
    @FormUrlEncoded
    Call<ClassifiedDetailsAPIModel> getMyClassifiedDetails(@Field("loggedinUserID") String loggedinUserID, @Field("selectedClassifiedID") String selectedClassifiedID);


    /****Other classifieds****/


    @POST("ClassifiedsRService/getOthersClassifiedsByCategoryCount")
    @FormUrlEncoded
    Call<ClassifiedCountApiModel> getOthersClassifiedsByCategoryCount(@Field("loggedinUserID") String loggedinUserID);

    @POST("ClassifiedsRService/getOthersClassifiedsByCategory")
    @FormUrlEncoded
    Call<ClassifiedApiModel> getOthersClassifiedsByCategory(@Field("loggedinUserID") String loggedinUserID, @Field("selectedCategoryID") String selectedCategoryID);


    @POST("ClassifiedsRService/getOthersClassifiedDetails")
    @FormUrlEncoded
    Call<ClassifiedDetailsAPIModel> getOthersClassifiedDetails(@Field("loggedinUserID") String loggedinUserID, @Field("selectedClassifiedID") String selectedClassifiedID);


    @POST("ClassifiedsRService/searchOthersAllClassifieds")
    @FormUrlEncoded
    Call<ClassifiedApiModel> searchOthersAllClassifieds(@Field("searchClassifieds") String searchClassifieds, @Field("selectedCategoryID") String selectedCategoryID, @Field("loggedinUserID") String loggedinUserID);


    /*****CSM *******/

    @POST("ComplaintsRService/getMyPendingComplaints")
    @FormUrlEncoded
    Call<PendingComplaintAPIModel> getMyPendingComplaints(@Field("selectedInfraID") String selectedInfraID);

    @POST("ComplaintsRService/getMyOldComplaints")
    @FormUrlEncoded
    Call<PendingComplaintAPIModel> getMyOldComplaints(@Field("selectedInfraID") String selectedInfraID, @Field("fromDate") String fromDate, @Field("toDate") String toDate);

    @POST("ComplaintsRService/retrieveComplaintCategories")
    Call<CategoryApiModel> retrieveComplaintCategories();

    @POST("ComplaintsRService/retrieveComplaintTypes")
    @FormUrlEncoded
    Call<CategoryApiModel> retrieveComplaintTypes(@Field("selectedComplaintCategoryID") String selectedComplaintCategoryID);

    @FormUrlEncoded
    @POST("ComplaintsRService/registerMyComplaint")
    Call<JsonObject> registerNewComplaint(@Field("complaintTypeID") String complaintTypeID, @Field("complaintDetails") String complaintDetails, @Field("selectedInfraID") String selectedInfraID, @Field("loggedinUserID") String loggedinUserID);


    @POST("ComplaintsRService/getOtherPendingComplaints")
    @FormUrlEncoded
    Call<PendingComplaintAPIModel> getOtherPendingComplaints(@Field("loggedinUserID") String loggedinUserID);

    @POST("ProjectsRService/getMAndResidentialProjects")
    Call<ResidentialProjectApiModel> getMAndResidentialProjects();


    @POST("InfraRService/getNotMyInfra")
    @FormUrlEncoded
    Call<MyInfraInfoAPI> getNonResidentialCommonInfra(@Field("infraSearchstr") String infraSearchstr, @Field("selectedProjectID") String selectedProjectID, @Field("loggedinUserID") String loggedinUserID);


    @FormUrlEncoded
    @POST("ComplaintsRService/registerOthersComplaint")
    Call<JsonObject> registerOthersComplaint(@Field("complaintTypeID") String complaintTypeID, @Field("complaintDetails") String complaintDetails, @Field("selectedInfraID") String selectedInfraID, @Field("loggedinUserID") String loggedinUserID);


    @POST("ComplaintsRService/getOtherOldComplaints")
    @FormUrlEncoded
    Call<PendingComplaintAPIModel> getOtherOldComplaints(@Field("loggedinUserID") String loggedinUserID, @Field("fromDate") String fromDate, @Field("toDate") String toDate);

    //Send OTP
    @POST("OTPRService/sendSignupOTP")
    @FormUrlEncoded
    Call<OTPModel> sendOTP(@Field("mobiles") String mobiles);

    //Agreement
    @POST("AppLicenseAgreementRService/getAppLicenseAgreementByLicenseType")
    @FormUrlEncoded
    Call<OTPModel> GetLicence(@Field("selectedLicenseTypeID") String selectedLicenseTypeID);

    //ResidentialProject
    @POST("ProjectsRService/getResidentialProjects")
    @FormUrlEncoded
    Call<ResidentialProjectApiModel> getResidentialProject();

    //getByinfraSearch
    @POST("InfraRService/getInfraBySearch")
    @FormUrlEncoded
    Call<GetInfraSearchModel> getInfrasearch(@Field("infraSearchstr") String infraSearchstr, @Field("selectedProjectID") String selectedProjectID);

    //registration
    @POST("AppAccountRService/signUpRequest")
    @FormUrlEncoded
    Call<RegistrationModel> Register(@Field("name") String name, @Field("userName") String userName, @Field("password") String password, @Field("selectedInfraID") String selectedInfraID, @Field("citizenID") String citizenID, @Field("email") String email, @Field("mobile") String mobile);

    //verify OTP
    @POST("OTPRService/verifySignupOTP")
    @FormUrlEncoded
    Call<VerifyOTPModel> verifyOTP(@Field("otp") String otp, @Field("mobile") String mobile);


    //resend OTP
    @POST("OTPRService/resendSignupOTP")
    @FormUrlEncoded
    Call<OTPModel> resendOTP(@Field("mobile") String mobile);

    //changePassword

    @POST("AppAuthRService/changePassword")
    @FormUrlEncoded
    Call<ChangePasswordModel> changePassword(@Field("loggedinUserID") String loggedinUserID,@Field("oldPassword")String oldPassword,@Field("newPassword")String newPassword);

}
