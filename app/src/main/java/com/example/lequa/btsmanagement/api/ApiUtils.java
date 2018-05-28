package com.example.lequa.btsmanagement.api;

public class ApiUtils {
    public static final String BASE_URL = "http://serverbts220180515045504.azurewebsites.net/";
    public static LoginService getLoginService() {
        return RetrofitClient.getClient(BASE_URL).create(LoginService.class);
    }
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
    public static TramService getTramService() {
        return RetrofitClient.getClient(BASE_URL).create(TramService.class);
    }
    public static HinhAnhTramService getHinhAnhTramService() {
        return RetrofitClient.getClient(BASE_URL).create(HinhAnhTramService.class);
    }
    public static NhaTramService getNhaTramService() {
        return RetrofitClient.getClient(BASE_URL).create(NhaTramService.class);
    }
    public static NhaMangService getNhaMangService() {
        return RetrofitClient.getClient(BASE_URL).create(NhaMangService.class);
    }
    public static RegisterService getRegisterService() {
        return RetrofitClient.getClient(BASE_URL).create(RegisterService.class);
    }
    public static PasswordService getPasswordService() {
        return RetrofitClient.getClient(BASE_URL).create(PasswordService.class);
    }
    public static MatDienService getMatDienService() {
        return RetrofitClient.getClient(BASE_URL).create(MatDienService.class);
    }
    public static NhatKyService getNhatKyService() {
        return RetrofitClient.getClient(BASE_URL).create(NhatKyService.class);
    }
}