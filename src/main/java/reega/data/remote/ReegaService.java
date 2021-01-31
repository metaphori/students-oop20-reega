package reega.data.remote;

import reega.data.remote.models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

/**
 * Specify the API layout and request types
 */
public interface ReegaService {
    // region auth

    @GET("auth/emailLogin")
    Call<LoginResponse> emailLogin(@Query("email") String email, @Query("password") String password);

    @GET("auth/fcLogin")
    Call<LoginResponse> fiscalCodeLogin(@Query("fc") String email, @Query("password") String password);

    @GET("auth/tokenLogin")
    Call<LoginResponse> tokenCodeLogin(@Query("selector") String email, @Query("validator") String password);

    @POST("auth/storeUserToken")
    Call<Void> storeUserToken(@Body UserAuthToken userAuthToken);

    @POST("auth/addUser")
    Call<Void> addUser(@Body NewUserBody newUser);

    @POST("auth/logout")
    Call<Void> logout();

    // endregion

    // region data

    @POST("data/contract")
    Call<Void> addContract(@Body NewContract contract);

    @GET("data/contract")
    Call<List<ContractModel>> getContracts();

    @GET("data/price_model")
    Call<List<ContractModel.PriceModel>> getPriceModels();

    @POST("data/price_model")
    Call<Void> addPriceModel(@Body ContractModel.PriceModel priceModel);

    @DELETE("data/price_model")
    Call<Void> removePriceModel(@Query("id") int id);

    @POST("data/fillUserData")
    Call<Void> pushData(@Body DataModel data);

    @GET("data/getLatestTimestamp")
    Call<Date> getLatestData(@Query("type") int type, @Query("contract_id") int contractId);

    // endregion
}
