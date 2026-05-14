package com.shubham.event_manager.repository;

import com.shubham.event_manager.document.EventDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EventSearchRepository
        extends ElasticsearchRepository<EventDocument, String> {

    // Text-only fuzzy search — no category filter
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

    // Text + category filter combined
    @Query("""
            {
              "bool": {
                "must": [
                  {
                    "multi_match": {
                      "query": "?0",
                      "fields": ["title^3", "description", "location"],
                      "fuzziness": "AUTO"
                    }
                  }
                ],
                "filter": [
                  {
                    "term": {
                      "categories": "?1"
                    }
                  }
                ]
              }
            }
            """)
    List<EventDocument> fuzzySearchWithCategory(
            String query, String category);

    // Category-only filter — no text search
    List<EventDocument> findByCategories(String category);
}