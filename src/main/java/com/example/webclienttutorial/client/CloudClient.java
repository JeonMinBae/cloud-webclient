package com.example.webclienttutorial.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.util.function.Consumer;
import java.util.function.Function;

public interface CloudClient {

    CloudClient log(String logName);
    CredentialSpec baseUrl(String baseUrl);


    interface CredentialSpec{
        MethodSpec credential(Credential credential);
    }

    interface MethodSpec{
        UriSpec get();
        UriSpec head();
        RequestBodyUriSpec post();
        RequestBodyUriSpec put();
        RequestBodyUriSpec patch();
        UriSpec options();
        UriSpec delete();
    }

    interface UriSpec{
        QueryStringSpec uri(String uri);
    }

    interface QueryStringSpec{
        RequestHeadersSpec queryString(Object queryStringObject);
    }

    interface RequestHeadersSpec {
//        ResponseHandler accept(MediaType... acceptableMediaTypes);
//        ResponseHandler acceptCharset(Charset... acceptableCharsets);
        RequestHeadersSpec accept(MediaType... acceptableMediaTypes);
        RequestHeadersSpec acceptCharset(Charset... acceptableCharsets);
        RequestHeadersSpec header(String key, String value);
        RequestHeadersSpec headers(HttpHeaders httpHeaders);
        RequestHeadersSpec cookie(String name, String value);
        RequestHeadersSpec cookie(Cookie... cookies);
        RequestHeadersSpec cookies(Consumer<MultiValueMap<String, String>> cookiesConsumer);
        ResponseHandler retrieve();
        <V> Mono<V> exchangeToMono(Function<ClientResponse, ? extends Mono<V>> responseHandler);
        <V> Flux<V> exchangeToFlux(Function<ClientResponse, ? extends Flux<V>> responseHandler);
    }

    interface RequestBodySpec{
        RequestBodySpec contentLength(long contentLength);
        RequestBodySpec contentType(MediaType contentType);
//        <T, P extends Publisher<T>> WebClient.RequestHeadersSpec<?> body(P publisher, Class<T> elementClass);
//        <T, P extends Publisher<T>> WebClient.RequestHeadersSpec<?> body(P publisher, ParameterizedTypeReference<T> elementTypeRef);
//        UriSpec body(Object producer, Class<?> elementClass);
//        UriSpec body(Object producer, ParameterizedTypeReference<?> elementTypeRef);
//        UriSpec body(BodyInserter<?, ? super ClientHttpRequest> inserter);
        UriSpec body(Object body);

    }

    interface ResponseHandler extends RequestSpec{
        ResponseHandler onError(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction);
        ResponseHandler on2xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction);
        ResponseHandler on3xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction);
        ResponseHandler on4xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction);
        ResponseHandler on5xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction);
    }

    interface RequestSpec{
        <T> Mono<ResponseEntity<T>> request(Class<T> cls);
    }

    interface RequestHeaderUriSpec extends UriSpec { }
    interface RequestBodyUriSpec extends UriSpec, RequestBodySpec{ }

}
