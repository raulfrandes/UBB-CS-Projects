package triathlon.rest;

import org.springframework.web.client.RestClientException;
import triathlon.model.Referee;
import triathlon.model.Trial;
import triathlon.rest.client.TrialsClient;
import triathlon.rest.controller.ServiceException;

public class StartRestClient {
    private final static TrialsClient trialsClient = new TrialsClient();

    public static void main(String[] args) {
        Referee referee = new Referee();
        referee.setId(5L);
        Trial trialT = new Trial("test-trial", "This is a test trial", referee);
        try {
            System.out.println("Adding a new trial " + trialT);
            show(() -> System.out.println(trialsClient.save(trialT)));
            System.out.println("\nPrinting all trials ...");
            show(() -> {
                Trial[] trials = trialsClient.findAll();
                trialT.setId(trials[trials.length - 1].getId());
                trialT.setReferee(trials[trials.length - 1].getReferee());
                for (Trial trial:trials) {
                    System.out.println(trial.getId() + ": " + trial.getName());
                }
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }

        System.out.println("\nInfo for trial with id=1");
        show(() -> System.out.println(trialsClient.findById(1L)));

        System.out.println("\nUpdating trial with id=" + trialT.getId());
        trialT.setName("updated-test-trial");
        show(() -> System.out.println(trialsClient.update(trialT)));

        System.out.println("\nDeleting trial with id=" + trialT.getId());
        show(() -> trialsClient.delete(trialT.getId()));
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception " + e);
        }
    }
}
