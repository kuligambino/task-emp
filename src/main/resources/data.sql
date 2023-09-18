DROP TABLE IF EXISTS api_request_stats;

CREATE TABLE api_request_stats (
    login VARCHAR(255) PRIMARY KEY NOT NULL,
    request_count NUMERIC
);