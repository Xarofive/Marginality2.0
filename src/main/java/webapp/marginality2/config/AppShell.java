package webapp.marginality2.config;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.component.page.Push;
import org.springframework.stereotype.Component;

@Component
@Push(PushMode.AUTOMATIC)
public class AppShell implements AppShellConfigurator {
}