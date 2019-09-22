package welley.capstone.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import welley.capstone.entities.Product;
import welley.capstone.repos.ProductRepository;

import java.util.List;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
public class ProductRestController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("batch-status")
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameters = new JobParameters();
        JobExecution jobExecution = jobLauncher.run(job, parameters);

        System.out.println("Status: " + jobExecution.getStatus());
        System.out.println("Spring Batch is running...");

        return jobExecution.getStatus();
    }

    @GetMapping("/dashboard")
    public List<Product> getDashboard() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @GetMapping("/category")
    public List<Product> sortByCategory(@RequestParam(required=true, defaultValue = "") String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products;
    }

    @GetMapping("/categorystock")
    public List<Product> sortByCategoryAvailability(@RequestParam(required=true, defaultValue = "") String category,
                                                    @RequestParam(required=true, defaultValue = "") String availability) {
        List<Product> products = productRepository.findByCategoryAndAvailability(category, availability);
        return products;
    }

}
