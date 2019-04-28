package com.ideabrains.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;


/**
 * Created by kamauwamatu
 * Project spring-cloud-intelligent-routing-filters-poc
 * User: kamauwamatu
 * Date: 2019-04-27
 * Time: 12:21
 */
public class EncryptionFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = getCurrentContext();

        String encryptionType = null;
        encryptionType = getEncryptionTypeValue(context);

        if (null == encryptionType) {
            System.out.println(encryptionType);
            // Only Decode if Encryption Mode is 2
        } else if (encryptionType.equals("2")) {

            try {

                InputStream stream = context.getResponseDataStream();
                String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
                context.setResponseBody(encodeRequest(body));
            } catch (IOException e) {
                rethrowRuntimeException(e);
            }
        }
        return null;
    }

    private String encodeRequest(String body) {
        return (Base64.getEncoder().encodeToString(body.getBytes()));
    }

    /**
     * Retrieves the Encryption Value
     *
     * @param context Context
     * @return String
     */
    private String getEncryptionTypeValue(RequestContext context) {

        String returnVal = null;
        try {
            returnVal = Optional.ofNullable(context.getRequest().getHeader("encryption-type").trim()).orElse(null);
        } catch (Exception ec) {
            System.out.println(ec.getMessage());
        }
        return returnVal;
    }
}
