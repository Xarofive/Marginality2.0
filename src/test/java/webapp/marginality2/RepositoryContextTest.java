package webapp.marginality2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import webapp.marginality2.repository.ChickenReactiveRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RepositoryContextTest {

    @Autowired
    private ChickenReactiveRepository chickenReactiveRepository;

    @Test
    void contextLoads() {
        assertThat(chickenReactiveRepository).isNotNull();
    }
}
