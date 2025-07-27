package triathlon.rest.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import triathlon.model.Trial;
import triathlon.rest.controller.ServiceException;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class TrialsClient {
    RestClient restClient = RestClient.builder()
            .requestInterceptor(new CustomRestClientInterceptor())
            .build();

    public static final String URL = "http://localhost:8080/triathlon/trials";

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Trial[] findAll() {
        return execute(() -> restClient.get()
                .uri(URL)
                .retrieve()
                .body(Trial[].class));
    }

    public Trial findById(Long id) {
        return execute(() -> restClient.get()
                .uri(String.format("%s/%s", URL, id.toString()))
                .retrieve()
                .body(Trial.class));
    }

    public Trial save(Trial trial) {
        return execute(() -> restClient.post()
                .uri(URL)
                .contentType(APPLICATION_JSON).body(trial)
                .retrieve()
                .body(Trial.class));
    }

    public Trial update(Trial trial) {
        return execute(() -> restClient.put()
                .uri(URL)
                .contentType(APPLICATION_JSON).body(trial)
                .retrieve()
                .body(Trial.class));
    }

    public void delete(Long id) {
        execute(() -> restClient.delete()
                .uri(String.format("%s/%s", URL, id.toString()))
                .retrieve()
                .toBodilessEntity());
    }

    public class CustomRestClientInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sending a " + request.getMethod() + " request to " + request.getURI() + " and body [" +
                    new String(body) + "]");
            ClientHttpResponse response = null;
            try {
                response = execution.execute(request, body);
                System.out.println("Got response code " + response.getStatusCode());
            } catch (IOException ex) {
                System.err.println("Eroare executie " + ex);
            }
            return response;
        }
    }
}
