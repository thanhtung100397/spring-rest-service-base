package com.spring.baseproject.events_handle.base;

import com.spring.baseproject.annotations.event.EventHandler;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.events_handle.ApplicationEvent;
import com.spring.baseproject.events_handle.ApplicationEventHandle;
import com.spring.baseproject.utils.base.ClassScannerUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.instrument.IllegalClassFormatException;
import java.util.HashSet;
import java.util.Set;

@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP)
public class ControllerValidationService implements ApplicationEventHandle {

    @Override
    public String startMessage() {
        return "Start validating controller...";
    }

    @Override
    public String successMessage() {
        return "Controller validation...OK";
    }

    @Override
    public void handleEvent() throws Exception {
        Set<Class<? extends Annotation>> includedAnnotations = new HashSet<>();
        includedAnnotations.add(RestController.class);
        Class<?> invalidRestController = ClassScannerUtils
                .findAnnotatedClass(ApplicationConstants.BASE_PACKAGE_NAME, includedAnnotations, null,
                        clazz -> !BaseRESTController.class.isAssignableFrom(clazz));
        if (invalidRestController != null) {
            throw new IllegalClassFormatException(invalidRestController.getName() +
                    " is annotated with @RestController must be annotated with subclass of " + BaseRESTController.class.getName());
        }
    }
}
