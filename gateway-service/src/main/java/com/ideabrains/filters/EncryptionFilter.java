package com.ideabrains.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * Created by kamauwamatu
 * Project gateway-service
 * User: kamauwamatu
 * Date: 2019-04-19
 * Time: 21:26
 */
public class EncryptionFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
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

        // Implementation of Conditional Encryption
        RequestContext context = getCurrentContext();
        context.addZuulRequestHeader("x-location", "USA");
        String encryptionType = null;
        // Optional.ofNullable - allows passed parameter to be null
        // Retrieve the Encryption Type
        encryptionType = getEncryptionTypeValue(context, null);

        handleEncryption(context, encryptionType);

        return null;
    }

    /**
     * Handles Encryption Filtering of the Payload
     *
     * @param context        Context
     * @param encryptionType String
     */
    private void handleEncryption(RequestContext context, String encryptionType) {
        if (null != encryptionType) {
            System.out.println(encryptionType);
            // Only Decode if Encryption Mode is 2
        } else if (encryptionType.equals("2")) {
            String body = null;
            try {
                // Retrieve the request body
                InputStream stream = context.getRequest().getInputStream();
                body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
                String contentType = context.getRequest().getContentType();
                System.out.println(contentType);
                // Inject the Unencrypted Request Body
                context.set("requestEntity", new ByteArrayInputStream(decodeRequest(body).getBytes("UTF-8")));
            } catch (IOException e) {
                rethrowRuntimeException(e);
            } catch (Exception ex) {
                // Catching the Decoding, Decryption Exception
                try {
                    // If Decryption Fails Inject the body back into the request
                    assert body != null;
                    context.set("requestEntity", new ByteArrayInputStream((body).getBytes("UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    // I dont know yet how to handle any exception after this.
                    System.out.println(ex.getMessage());
                }
            }
        }
    }


    /**
     * Retrieves the Encryption Value
     *
     * @param context        Context
     * @param encryptionType Encryption Type
     * @return String
     */
    private String getEncryptionTypeValue(RequestContext context, String encryptionType) {

        try {
            encryptionType = Optional.ofNullable(context.getRequest().getHeader("ENCRYPTION-TYPE").trim()).orElse(null);
        } catch (Exception ec) {
            System.out.println(ec.getMessage());
        }
        return encryptionType;
    }

    /**
     * Decodes Request
     *
     * @param encodedString String
     * @return String
     */
    private String decodeRequest(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(String.valueOf(encodedString));
        return new String(decodedBytes);
    }
}
