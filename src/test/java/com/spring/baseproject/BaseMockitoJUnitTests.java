package com.spring.baseproject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseMockitoJUnitTests {

    @BeforeAll
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        init();
    }

    @AfterAll
    public abstract void clear();

    public abstract void init();
}
