package com.adwork.microservices.ratings;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.adwork.microservices.ratings.functional.RatingsReactiveRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AdworkRatingsWebfluxApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RatingsSpringFunctionalIntegrationTest {

    @MockBean
    private RatingsReactiveRepository ratingsRepository;

    /*
    @Test
	public void inbox() {
		List<Message> inbox = this.messages.findByTo("1").collectList().block();
		assertThat(inbox).hasSize(2);
	}
    
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenCorrectEmployee() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(config.getEmployeeByIdRoute())
            .build();

        Employee employee = new Employee("1", "Employee 1");

        given(employeeRepository.findEmployeeById("1")).willReturn(Mono.just(employee));

        client.get()
            .uri("/employees/1")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Employee.class)
            .isEqualTo(employee);
    }

    @Test
    public void whenGetAllEmployees_thenCorrectEmployees() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(config.getAllEmployeesRoute())
            .build();

        List<Employee> employeeList = new ArrayList<>();

        Employee employee1 = new Employee("1", "Employee 1");
        Employee employee2 = new Employee("2", "Employee 2");

        employeeList.add(employee1);
        employeeList.add(employee2);

        Flux<Employee> employeeFlux = Flux.fromIterable(employeeList);
        given(employeeRepository.findAllEmployees()).willReturn(employeeFlux);

        client.get()
            .uri("/employees")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(Employee.class)
            .isEqualTo(employeeList);
    }

    @Test
    public void whenUpdateEmployee_thenEmployeeUpdated() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(config.updateEmployeeRoute())
            .build();

        Employee employee = new Employee("1", "Employee 1 Updated");

        client.post()
            .uri("/employees/update")
            .body(Mono.just(employee), Employee.class)
            .exchange()
            .expectStatus()
            .isOk();

        verify(employeeRepository).updateEmployee(employee);
    }*/
}
