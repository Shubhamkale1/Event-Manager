   CREATE TABLE events (
       id          BIGINT          NOT NULL AUTO_INCREMENT,
       title       VARCHAR(255)    NOT NULL,
       description VARCHAR(1000),
       event_date  DATETIME(6)     NOT NULL,
       location    VARCHAR(255),
       capacity    INT,
       PRIMARY KEY (id)
   );