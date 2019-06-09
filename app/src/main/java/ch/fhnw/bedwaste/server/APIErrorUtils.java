package ch.fhnw.bedwaste.server;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class APIErrorUtils {
    public static APIError parseError(Response<?> response) {
        //the response converter is required to parse the JSON error,the response converter is available via our Retrofit object
        //passing our APIError class as the parameter to the responseBodyConverter method,
        //The responseConverter method will return the appropriate converter to parse the response body type-here a JSON converter
        Converter<ResponseBody, APIError> converter =
                APIClient.getClient().responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            //parse the received response body data into an APIError object
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
