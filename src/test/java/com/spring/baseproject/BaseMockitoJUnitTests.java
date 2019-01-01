package com.spring.baseproject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseMockitoJUnitTests {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
