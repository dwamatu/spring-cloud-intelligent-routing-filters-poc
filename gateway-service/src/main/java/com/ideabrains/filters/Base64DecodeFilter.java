package com.ideabrains.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * Created by kamauwamatu
 * Project gateway-service
 * User: kamauwamatu
 * Date: 2019-04-19
 * Time: 21:26
 */
public class Base64DecodeFilter extends ZuulFilter {
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

        try {

            RequestContext context = getCurrentContext();
            context.addZuulRequestHeader("x-location", "USA");
            InputStream stream = context.getRequest().getInputStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            String contentType = context.getRequest().getContentType();
            System.out.println(contentType);
//            context.setRequest();
            context.set("requestEntity", new ByteArrayInputStream(decodeRequest(body).getBytes("UTF-8")));

        } catch (IOException e) {
            rethrowRuntimeException(e);
        }
        return null;
    }

    private String decodeRequest(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(String.valueOf(encodedString));
        return new String(decodedBytes);
    }
}
