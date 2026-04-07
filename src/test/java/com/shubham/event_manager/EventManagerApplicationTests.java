package com.shubham.event_manager;

import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventManagerApplicationTests {

	@Autowired
	private EventRepository eventRepository;

	@Test
	void testCreateAndFindEvent() {
		Event event = new Event();
		event.setTitle("LeetCode Challenge");
		event.setDescription("college events");
		event.setLocation("pune");
		event.setEventDate(LocalDateTime.now());

		eventRepository.save(event);

		List<Event> events = eventRepository.findAll();
		assertThat(events).isNotEmpty();
		assertThat(events.get(0).getTitle()).isEqualToIgnoringCase("Leetcode Challenge");

		System.out.println("test passed");
	}

}
