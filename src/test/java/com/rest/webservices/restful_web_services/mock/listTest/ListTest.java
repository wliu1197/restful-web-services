package com.rest.webservices.restful_web_services.mock.listTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

@ExtendWith(MockitoExtension.class)
class ListTest {

	@Mock
	List <String> mockList;
	
	@Test
	void test() {
		when(mockList.size()).thenReturn(3).thenReturn(2);
		assertEquals(3, mockList.size());
		assertEquals(2, mockList.size());
				
	}
	@Test
	void test2() {
		when(mockList.get(Mockito.anyInt())).thenReturn("test1").thenReturn("test2");
		assertEquals("test1", mockList.get(100));
		assertEquals("test2", mockList.get(3));
				
	}
}
