package com.example.webclienttutorial.client;

import com.example.webclienttutorial.util.OpenApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


@RequiredArgsConstructor
public abstract class BaseNCloudClient implements CloudClient {

    private final WebClient webClient;
    private final MakeSignature makeSignature;

    private String _logName;
    private CloudCredential _credential;
    private String _baseUrl;
    private HttpHeaders _headers;
    private HttpMethod _httpMethod;
    private Object _body;
    private MultiValueMap<String, String> _cookies;
    private String _uri;
    private String _uriWithoutQueryString;
    private String _queryString;
    Map<Predicate<HttpStatus>, Function<ClientResponse, Mono<? extends Throwable>>> _onStatus;

    private void init() {
        _logName = "";
        _credential = null;
        _baseUrl = null;
        _headers = new HttpHeaders();
        _httpMethod = null;
        _body = new Object();
        _cookies = new LinkedMultiValueMap<>(3);
        _uri = null;
        _uriWithoutQueryString = null;
        _queryString = null;
        _onStatus = new HashMap();
    }

    @Override
    public CloudClient log(String logName) {
        this._logName = logName;
        return this;
    }

    @Override
    public CredentialSpec baseUrl(String baseUrl) {
        init();
        this._baseUrl = baseUrl;
        return new TestCredential();
    }

    private HttpHeaders getHeaders() {
        if (this._headers == null) {
            this._headers = new HttpHeaders();
        }
        return this._headers;
    }


    private class TestCredential implements CredentialSpec {
        @Override
        public MethodSpec credential(Credential credential) {
            _credential = (CloudCredential) credential;
            makeSignature.setAccessKey(_credential.getAccessKey());
            makeSignature.setSecretKey(_credential.getSecretKey());
            return new TestMethod();
        }
    }


    private class TestMethod implements MethodSpec {
        @Override
        public UriSpec get() {
            return methodInternal(HttpMethod.GET);
        }

        @Override
        public UriSpec head() {
            return methodInternal(HttpMethod.HEAD);
        }

        @Override
        public RequestBodyUriSpec post() {
            return methodInternal(HttpMethod.POST);
        }

        @Override
        public RequestBodyUriSpec put() {
            return methodInternal(HttpMethod.PUT);
        }

        @Override
        public RequestBodyUriSpec patch() {
            return methodInternal(HttpMethod.PATCH);
        }

        @Override
        public UriSpec options() {
            return methodInternal(HttpMethod.OPTIONS);
        }

        @Override
        public UriSpec delete() {
            return methodInternal(HttpMethod.DELETE);
        }

        private RequestBodyUriSpec methodInternal(HttpMethod httpMethod) {
            return new TestRequestBodyUriSpec(httpMethod);
        }

    }

    private class TestRequestBodyUriSpec implements RequestBodyUriSpec {
        public TestRequestBodyUriSpec(HttpMethod httpMethod) {
            _httpMethod = httpMethod;
        }

        @Override
        public QueryStringSpec uri(String uri) {
            _uriWithoutQueryString = uri;
            return new TestQueryString();
        }

        @Override
        public RequestBodySpec contentLength(long contentLength) {
            getHeaders().setContentLength(contentLength);
            return this;
        }

        @Override
        public RequestBodySpec contentType(MediaType contentType) {
            getHeaders().setContentType(contentType);
            return this;
        }

        @Override
        public UriSpec body(Object body) {
            _body = body;
            return this;
        }
    }

    private class TestQueryString implements QueryStringSpec {
        @Override
        public RequestHeadersSpec queryString(Object queryStringObject) {
            _queryString = OpenApiUtil.getOpenApiUrl("", queryStringObject);
            _uri = _uriWithoutQueryString + _queryString;

            try {
                makeSignature.getNCloudHttpHeaders(_httpMethod, _uri)
                    .forEach((key, values) -> _headers.addAll(key, values));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return new TestRequestHeader();
        }
    }

    private class TestRequestHeader implements RequestHeadersSpec {
        @Override
        public RequestHeadersSpec accept(MediaType... acceptableMediaTypes) {
            getHeaders().setAccept(Arrays.asList(acceptableMediaTypes));
            return this;
        }

        @Override
        public RequestHeadersSpec acceptCharset(Charset... acceptableCharsets) {
            getHeaders().setAcceptCharset(Arrays.asList(acceptableCharsets));
            return this;
        }

        @Override
        public RequestHeadersSpec header(String key, String value) {
            getHeaders().add(key, value);
            return this;
        }

        @Override
        public RequestHeadersSpec headers(HttpHeaders headers) {
            _headers = headers;
            return this;
        }

        @Override
        public RequestHeadersSpec cookie(String name, String value) {
            getCookies().add(name, value);
            return this;
        }

        @Override
        public RequestHeadersSpec cookie(Cookie... cookies) {
            for (Cookie cookie : cookies) {
                getCookies().add(cookie.getName(), cookie.getValue());
            }
            return this;
        }

        @Override
        public RequestHeadersSpec cookies(Consumer<MultiValueMap<String, String>> cookiesConsumer) {
            cookiesConsumer.accept(getCookies());
            return this;
        }

        private MultiValueMap<String, String> getCookies() {
            if (_cookies == null) {
                _cookies = new LinkedMultiValueMap<>(3);
            }
            return _cookies;
        }

        @Override
        public ResponseHandler retrieve() {
            return new TestResponseHandler();
        }

        @Override
        public <V> Mono<V> exchangeToMono(Function<ClientResponse, ? extends Mono<V>> responseHandler) {
            WebClient webClient = setBaseUrl(BaseNCloudClient.this._baseUrl);
            return getRequestSpec(webClient, _httpMethod).exchangeToMono(responseHandler);
        }

        @Override
        public <V> Flux<V> exchangeToFlux(Function<ClientResponse, ? extends Flux<V>> responseHandler) {
            WebClient webClient = setBaseUrl(BaseNCloudClient.this._baseUrl);
            return getRequestSpec(webClient, _httpMethod).exchangeToFlux(responseHandler);
        }

//        private WebClient.RequestHeadersSpec<?> getExchange(WebClient webClient, HttpMethod httpMethod) {
//            WebClient.RequestHeadersUriSpec<?> uriSpec = null;
//            WebClient.RequestBodyUriSpec bodyUriSpec = null;
//
//            switch (httpMethod) {
//                case GET:
//                    uriSpec = webClient.get();
//                    break;
//                case HEAD:
//                    uriSpec = webClient.head();
//                    break;
//                case POST:
//                    bodyUriSpec = webClient.post();
//                    break;
//                case PATCH:
//                    bodyUriSpec = webClient.patch();
//                    break;
//                case PUT:
//                    bodyUriSpec = webClient.put();
//                    break;
//                case OPTIONS:
//                    uriSpec = webClient.options();
//                    break;
//                case DELETE:
//                    uriSpec = webClient.delete();
//                    break;
//            }
//
//            if (Objects.nonNull(uriSpec)) {
//                return getResponseSpec(uriSpec);
//            } else if (Objects.nonNull(bodyUriSpec)) {
//                return getResponseBodySpec(bodyUriSpec);
//            }
//
//            return null;
//        }

    }

    private WebClient setBaseUrl(String baseUrl) {
        return BaseNCloudClient.this.webClient
            .mutate()
            .baseUrl(baseUrl)
            .build();
    }

    private class TestResponseHandler implements ResponseHandler {
        @Override
        public ResponseHandler onError(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
            _onStatus.put(HttpStatus::isError, exceptionFunction);
            return this;
        }

        @Override
        public ResponseHandler on2xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
            _onStatus.put(HttpStatus::is2xxSuccessful, exceptionFunction);
            return this;
        }

        @Override
        public ResponseHandler on3xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
            _onStatus.put(HttpStatus::is3xxRedirection, exceptionFunction);
            return this;
        }

        @Override
        public ResponseHandler on4xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
            _onStatus.put(HttpStatus::is4xxClientError, exceptionFunction);
            return this;
        }

        @Override
        public ResponseHandler on5xxStatus(Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
            _onStatus.put(HttpStatus::is5xxServerError, exceptionFunction);
            return this;
        }

        @Override
        public <T> Mono<ResponseEntity<T>> request(Class<T> cls) {

            WebClient webClient = setBaseUrl(BaseNCloudClient.this._baseUrl);

            WebClient.ResponseSpec retrieve = getRequestSpec(webClient, _httpMethod).retrieve();
            _onStatus.forEach((key, value) -> {
                retrieve.onStatus(key, value);
            });

            return retrieve
                .toEntity(cls)
                .log(_logName);
        }


//        private WebClient.ResponseSpec getRetrieve(WebClient webClient, HttpMethod httpMethod) {
//            WebClient.RequestHeadersUriSpec<?> uriSpec = null;
//            WebClient.RequestBodyUriSpec bodyUriSpec = null;
//
//            switch (httpMethod) {
//                case GET:
//                    uriSpec = webClient.get();
//                    break;
//                case HEAD:
//                    uriSpec = webClient.head();
//                    break;
//                case POST:
//                    bodyUriSpec = webClient.post();
//                    break;
//                case PATCH:
//                    bodyUriSpec = webClient.patch();
//                    break;
//                case PUT:
//                    bodyUriSpec = webClient.put();
//                    break;
//                case OPTIONS:
//                    uriSpec = webClient.options();
//                    break;
//                case DELETE:
//                    uriSpec = webClient.delete();
//                    break;
//            }
//
//            WebClient.ResponseSpec retrieve = null;
//            if (Objects.nonNull(uriSpec)) {
//                retrieve = getResponseSpec(uriSpec).retrieve();
//            } else if (Objects.nonNull(bodyUriSpec)) {
//                retrieve = getResponseBodySpec(bodyUriSpec).retrieve();
//            }
//
//            return retrieve;
//        }
    }

    private WebClient.RequestHeadersSpec<?> getRequestSpec(WebClient webClient, HttpMethod httpMethod) {
        WebClient.RequestHeadersUriSpec<?> uriSpec = null;
        WebClient.RequestBodyUriSpec bodyUriSpec = null;

        switch (httpMethod) {
            case GET:
                uriSpec = webClient.get();
                break;
            case HEAD:
                uriSpec = webClient.head();
                break;
            case POST:
                bodyUriSpec = webClient.post();
                break;
            case PATCH:
                bodyUriSpec = webClient.patch();
                 break;
            case PUT:
                bodyUriSpec = webClient.put();
                break;
            case OPTIONS:
                uriSpec = webClient.options();
                break;
            case DELETE:
                uriSpec = webClient.delete();
                break;
        }


        if (Objects.nonNull(uriSpec)) {
            return getResponseSpec(uriSpec);
        } else if (Objects.nonNull(bodyUriSpec)) {
            return getResponseBodySpec(bodyUriSpec);
        }
        return null;
    }

    private WebClient.RequestHeadersSpec<?> getResponseBodySpec(WebClient.RequestBodyUriSpec bodyUriSpec) {
        return bodyUriSpec
            .uri(_uri)
            .cookies(cookies -> cookies.addAll(_cookies))
            .headers(httpHeaders ->
                _headers.forEach((key, values) -> httpHeaders.addAll(key, values))
            )
            .bodyValue(_body);
    }

    private WebClient.RequestHeadersSpec<?> getResponseSpec(WebClient.RequestHeadersUriSpec<?> uriSpec) {
        return uriSpec
            .uri(_uri)
            .cookies(c -> c.addAll(_cookies))
            .headers(httpHeaders ->
                _headers.forEach((key, values) -> httpHeaders.addAll(key, values))
            );
    }

}
