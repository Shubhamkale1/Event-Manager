package com.shubham.event_manager.repository;

import com.shubham.event_manager.document.EventDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EventSearchRepository
        extends ElasticsearchRepository<EventDocument, String> {

    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["title^3", "description", "location"],
                "fuzziness": "AUTO"
              }
            }
            """)
    List<EventDocument> fuzzySearch(String query);
}
