package com.SprCustomers.Mapper;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModelMapperExpTest {

    @Test
    void testModelMapperBean() {
        ModelMapperExp config = new ModelMapperExp();
        ModelMapper mapper = config.modelMapper();
        assertNotNull(mapper);
    }
}
