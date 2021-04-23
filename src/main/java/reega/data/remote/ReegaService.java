package reega.data.remote;

import reega.data.models.gson.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Specify the API layout and request types.
 */
public interface ReegaService {
    // region auth

    @GET("auth/emailLogin")
    Call<LoginResponse> emailLogin(@Query("email") String email, @Query("password") String password);

    @GET("auth/fcLogin")
    Call<LoginResponse> fiscalCodeLogin(@Query("fc") String fiscalCode, @Query("password") String password);

    @GET("auth/tokenLogin")
    Call<LoginResponse> tokenCodeLogin(@Query("selector") String selector, @Query("validator") String validator);

    @POST("auth/storeUserToken")
    Call<Void> storeUserToken(@Body UserAuthToken userAuthToken);

    @POST("auth/logout")
    Call<Void> logout();

    // endregion

    // region user

    @POST("auth/addUser")
    Call<Void> addUser(@Body NewUserBody newUser);

    @DELETE("auth/removeUser")
    Call<Void> removeUser(@Query("fc") String fiscalCode);

    @GET("data/user_from_contract")
    Call<User> getUserFromContract(@Query("contract_id") int contractID);

    @GET("data/user_search")
    Call<List<User>> searchUser(@Query("keyword") String keyword);

    // endregion

    // region data

    @POST("data/contract")
    Call<ContractModel> addContract(@Body NewContract contract);

    /**
     * @return all the user's contracts
     */
    @GET("data/contract")
    Call<List<ContractModel>> getContracts();

    /**
     * @return all the contracts for a specified user (must be admin)
     */
    @GET("data/user/{fiscal_code}/contracts")
    Call<List<ContractModel>> getContractsForUser(@Path("fiscal_code") String fiscalCode);

    /**
     * @return all the contracts (must be admin)
     */
    @GET("data/contracts")
    Call<List<ContractModel>> getAllContracts();

    @GET("data/contract_search")
    Call<List<ContractModel>> searchContract(@Query("keyword") String keyword);

    @DELETE("data/contract")
    Call<Void> removeContract(@Query("id") int id);

    @POST("data/fillUserData")
    Call<Void> pushData(@Body DataModel data);

    @GET("data/month")
    Call<List<DataModel>> getMonthlyData(@QueryMap Map<String, String> options);

    @GET("data/getLatestTimestamp")
    Call<Date> getLatestData(@Query("type") int type, @Query("contract_id") int contractId);

    @POST("data/history")
    Call<List<MonthlyReportModel>> getBillReport(@Body List<Integer> contractId);

    // endregion

    // region test

    @GET("init")
    Call<String> initTest();

    @POST("terminate")
    Call<Void> terminateTest();

    // endregion
}
